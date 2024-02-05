package dev.com.library.verticle;

import dev.com.library.api.handler.BookHandler;
import dev.com.library.api.repository.BookRepository;
import dev.com.library.api.router.BookRouter;
import dev.com.library.api.service.BookService;
import dev.com.library.utils.ConfigUtils;
import dev.com.library.utils.DbUtils;
import dev.com.library.utils.LogUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;

public class ApiVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiVerticle.class);
  private static final String API_V1 = "/api/v1";

  @Override
  public void start(Promise<Void> promise) {
    final MongoClient client = DbUtils.createMongoDBClient(vertx);

    final BookRepository bookRepository = new BookRepository(client);
    final BookService bookService = new BookService(bookRepository);
    final BookHandler bookHandler = new BookHandler(bookService);
    final BookRouter bookRouter = new BookRouter(vertx, bookHandler, BookRepository.COLLECTION_NAME);

    Router mainRouter = Router.router(vertx);
    mainRouter.mountSubRouter(API_V1, bookRouter.getRouter());
    showEndpoints(API_V1, bookRouter.getRouter());
    buildHttpServer(vertx, promise, mainRouter);
  }

  private void showEndpoints(String mainPath, Router... routers) {
    StringBuilder sb = new StringBuilder("Registered routes:\n");
    for (Router router : routers) {
      router.getRoutes().stream().filter(f -> f.getPath() != null && f.methods() != null).forEach(f -> {
        sb.append(String.format("path=%s, method=%s\n", mainPath + f.getPath(), f.methods()));
      });
    }
    LOGGER.info(sb);
  }

  private void buildHttpServer(Vertx vertx, Promise<Void> promise, Router router) {
    int port = ConfigUtils.getInstance().getApplicationUtils().getServerPort();
    vertx.createHttpServer().requestHandler(router).listen(port, http -> {
      if (http.succeeded()) {
        promise.complete();
        LOGGER.info(LogUtils.RUN_HTTP_SERVER_SUCCESS_MESSAGE.buildMessage(port));
      } else {
        promise.fail(http.cause());
        LOGGER.info(LogUtils.RUN_HTTP_SERVER_ERROR_MESSAGE.buildMessage());
      }
    });
  }
}
