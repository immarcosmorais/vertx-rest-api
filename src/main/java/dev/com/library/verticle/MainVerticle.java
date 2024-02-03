package dev.com.library.verticle;

import dev.com.library.utils.ApplicationUtils;
import dev.com.library.utils.LogUtils;
import io.vertx.core.*;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) {
    final long start = System.currentTimeMillis();
    deployApiVerticle(vertx).onSuccess(success -> LOGGER.info(LogUtils.RUN_APP_SUCCESSFULLY_MESSAGE.buildMessage(System.currentTimeMillis() - start))).onFailure(throwable -> LOGGER.error(throwable.getMessage()));
  }

  private Future<String> deployApiVerticle(Vertx vertx) {
    return vertx.deployVerticle(ApiVerticle.class.getName(), new DeploymentOptions().setInstances(ApplicationUtils.numberOfAvailableCores()));
  }

}
