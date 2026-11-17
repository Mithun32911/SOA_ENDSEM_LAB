package soa.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import soa.model.Book;
import soa.repo.BookRepo;
@Service
public class BookService {
	BookRepo BR;
	public BookService(BookRepo BR) {
		this.BR=BR;
	}
	public Object addBook(Book B) {
		Map<String, Object> response=new HashMap<>();
		try {
			BR.save(B);
			response.put("status", "response");
			response.put("message", "New book has been added");
		}catch(Exception e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
		}
		return response;
	}
	public Object getAllBooks() {
		Map<String, Object> response=new HashMap<>();
		try {
			List<Book>books=BR.findAll();
			response.put("status", "success");
			response.put("books", books);
			
		}catch(Exception e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
		}
		return response;
	}
	public Object getBookById(Long id) {
		Map<String, Object> response=new HashMap<>();
		try {
			Book B=BR.findById(id).get();
			response.put("status", "success");
			response.put("book", B);
		}catch(Exception e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
		}
		return response;
	}
	public Object updateBook(Long id, Book B) {
		Map<String, Object> response=new HashMap<>();
		try {
			Book temp = BR.findById(id).get();
			temp.setTitle(B.getTitle());
			temp.setAuthor(B.getAuthor());
			temp.setIsbn(B.getIsbn());
			BR.save(temp); 
		    response.put("status", "success");
			response.put("message", "Updated successfully");
			
			
		}catch(Exception e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
		}
		return response;
	}
	public Object deleteBook(Long id) {
		Map<String, Object> response=new HashMap<>();
		try {
			BR.deleteById(id);
			response.put("status","success");
			response.put("message", "Deleted successfully");
			
		}catch(Exception e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
		}
		return response;
	}
	

}
