package dev.com.library;

import dev.com.library.verticle.MainVerticle;
import io.vertx.core.Vertx;

public class Main {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(MainVerticle.class.getName())
      .onFailure(throwable -> System.exit(-1))
      .onSuccess(res -> System.out.println("Success"));
  }

}
