package dev.com.library.api.service;


import dev.com.library.api.model.entity.Book;
import dev.com.library.api.repository.BookRepository;


public class BookService extends AbstractService<Book, BookRepository> {
  public BookService(BookRepository repository) {
    super(repository);
  }
}
