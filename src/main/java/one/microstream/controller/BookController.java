package one.microstream.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import one.microstream.domain.Author;
import one.microstream.domain.Book;
import one.microstream.reference.Lazy;
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
				k -> Lazy.Reference(new ArrayList<>())).get().add(b);
			
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
			k -> Lazy.Reference(new ArrayList<>())).get().add(book);
		
		DB.root.getAuthors().computeIfAbsent(
			book.getAuthor().getId() + ":" + book.getAuthor().getEmail() + ":" + book.getAuthor().getLastname(),
			k -> Lazy.Reference(book.getAuthor()));
		
		DB.storageManager.storeAll(DB.root.getBooks(), DB.root.getAuthors());
		
		return HttpResponse.ok("Book successfully created!");
	}
	
	@Get
	public List<Book> getBooks()
	{
		// Enter your code here
	}
	
	@Get("/startsWith_A")
	public List<Book> getBooksStartsWithA()
	{
		// Enter your code here
	}
	
	@Get("/clear")
	public HttpResponse<?> clearBooks()
	{
		// Enter your code here
		
		return HttpResponse.ok("Books successfully cleared!");
	}
	
	@Get("/updateSingle")
	public HttpResponse<?> updateSingleBook()
	{
		// Enter your code here
		
		return HttpResponse.ok("Book successfully updated!");
	}
	
	@Get("/updateMulti")
	public HttpResponse<?> updateMultiBooks()
	{
		// Enter your code here
		
		return HttpResponse.ok("Bookss successfully updated!");
	}
}
