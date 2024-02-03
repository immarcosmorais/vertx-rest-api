package dev.com.library.api.router;

import dev.com.library.api.handler.BookHandler;
import io.vertx.core.Vertx;

public class BookRouter extends AbstractRouter<BookHandler> {
  public BookRouter(Vertx vertx, BookHandler handler, String path) {
    super(vertx, handler, path);
  }


}
