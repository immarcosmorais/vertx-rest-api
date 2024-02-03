package dev.com.library.api.repository;

import dev.com.library.api.model.entity.Book;
import io.vertx.ext.mongo.MongoClient;


public class BookRepository extends AbstractMongoDBRepository<Book> {

  public static final String COLLECTION_NAME = "books";

  public BookRepository(MongoClient client) {
    super(COLLECTION_NAME, client);
  }

}
