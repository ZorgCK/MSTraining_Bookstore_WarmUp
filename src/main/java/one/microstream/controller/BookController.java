package one.microstream.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import one.microstream.domain.Author;
import one.microstream.domain.Book;
import one.microstream.utils.MockupUtils;


@Controller("/books")
public class BookController
{
	@Get("/create")
	public HttpResponse<?> createBooks()
	{
		List<Book> allCreatedBooks = MockupUtils.loadMockupData();
		
		// Enter your code here
		
		return HttpResponse.ok("Books successfully created!");
	}
	
	@Get("/createSingle")
	public HttpResponse<?> createSingleBook()
	{
		Author author = new Author("100", "John", "Doe", "j.doe@example.com", "Male");
		Book book = new Book("123456789", "Single Book", LocalDate.now(), new BigDecimal(13.32), author);
		
		// Enter your code here
				
		return HttpResponse.ok("Book successfully created!");
	}
	
	@Get("/ISBNstartsWith_1")
	public List<Book> ISBNstartsWith1()
	{
		// Enter your code here
	}
	
	@Get("/updateMulti")
	public HttpResponse<?> updateMultiBooks()
	{
		// Enter your code here
		
		return HttpResponse.ok("Bookss successfully updated!");
	}
}
