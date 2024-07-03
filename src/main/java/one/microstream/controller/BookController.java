package one.microstream.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.serializer.persistence.types.Storer;
import org.eclipse.serializer.persistence.util.Reloader;
import org.eclipse.serializer.reference.Lazy;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import one.microstream.domain.Author;
import one.microstream.domain.Book;
import one.microstream.storage.DB;
import one.microstream.utils.MockupUtils;


@Controller("/books")
public class BookController
{
	@Get("/setupData")
	public HttpResponse<?> setupData()
	{
		List<Book> allCreatedBooks = MockupUtils.loadMockupData();
		
		allCreatedBooks.forEach(b ->
		{
			DB.root.getBooks().computeIfAbsent(
				b.getIsbn().substring(0, 2),
				k -> Lazy.Reference(new ArrayList<>()))
			.get().add(b);
			
			DB.root.getAuthors().computeIfAbsent(
				b.getAuthor().getId() + ":" + b.getAuthor().getEmail() + ":" + b.getAuthor().getLastname(),
				k -> Lazy.Reference(b.getAuthor()));
		});
		
		DB.storageManager.storeAll(DB.root.getBooks(), DB.root.getAuthors());
		
		return HttpResponse.ok("Books successfully created!");
	}
	
	@Get("/createSingle")
	public HttpResponse<?> createSingleBook()
	{
		Author author = new Author("100", "John", "Doe", "j.doe@example.com", "Male");
		Book book = new Book("123456789", "Single Book", LocalDate.now(), new BigDecimal(13.32), author);
		
		DB.root.getBooks().computeIfAbsent(
			book.getIsbn().substring(0, 2),
			k -> Lazy.Reference(new ArrayList<>()))
		.get().add(book);
		
		DB.root.getAuthors().computeIfAbsent(
			book.getAuthor().getId() + ":" + book.getAuthor().getEmail() + ":" + book.getAuthor().getLastname(),
			k -> Lazy.Reference(book.getAuthor()));
		
		DB.storageManager.storeAll(DB.root.getBooks(), DB.root.getAuthors());
		
		return HttpResponse.ok("Book successfully created!");
	}

	@Get("/ISBNstartsWith_1")
	public List<Book> ISBNstartsWith1()
	{
		return DB.root.getBooks().entrySet().stream()
			.filter(s -> s.getKey().startsWith("1"))
			.flatMap(e -> e.getValue().get().stream())
			.collect(Collectors.toList());
	}
	
	@Get("/updateMulti")
	public HttpResponse<?> updateMultiBooks()
	{
		Storer ls = DB.storageManager.createLazyStorer();
		
		List<Book> isbNstartsWith1 = ISBNstartsWith1();
		
		try
		{
			isbNstartsWith1.forEach(b ->
			{
				b.setPrice(new BigDecimal(50.00));
				ls.store(b);
			});
			
			ls.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			Reloader reloader = Reloader.New(DB.storageManager.persistenceManager());
			
			isbNstartsWith1.forEach(b -> reloader.reloadFlat(b));
			return HttpResponse.serverError("Update books failed. All changes are rollbacked" + e.getMessage());
		}
		
		return HttpResponse.ok("Bookss successfully updated!");
	}
}
