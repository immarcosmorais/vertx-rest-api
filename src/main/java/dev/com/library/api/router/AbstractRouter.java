package dev.com.library.api.router;

import dev.com.library.api.handler.AbstractHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class AbstractRouter<Handler extends AbstractHandler> {

  private final Vertx vertx;
  private final Handler handler;
  private final String path;

  public Router getRouter() {
    final Router router = Router.router(vertx);
    router.route("/" + path + "*").handler(BodyHandler.create());
    router.get("/" + path).handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(handler::findAll);
    router.get("/" + path + "/:id").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(handler::findById);
    router.post("/" + path).handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(handler::create);
    router.put("/" + path + "/:id").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(handler::update);
    router.patch("/" + path + "/:id").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(handler::patch);
    router.delete("/" + path + "/:id").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(handler::delete);
    return router;
  }

}
