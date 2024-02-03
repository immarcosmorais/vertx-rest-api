package dev.com.library.api.handler;


import dev.com.library.api.model.entity.Book;
import dev.com.library.api.service.BookService;

public class BookHandler extends AbstractHandler<Book, BookService> {
  public BookHandler(BookService service) {
    super(service);
  }

}
