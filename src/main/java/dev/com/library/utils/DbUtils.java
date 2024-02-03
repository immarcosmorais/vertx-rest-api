package dev.com.library.utils;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.Properties;


public class DbUtils {

  private static final String HOST_CONFIG = "datasource.host";
  private static final String PORT_CONFIG = "datasource.port";
  private static final String DATABASE_CONFIG = "datasource.database";
  private static final String USERNAME_CONFIG = "datasource.username";
  private static final String PASSWORD_CONFIG = "datasource.password";
  private static final String AUTH_SOURCE_CONFIG = "datasource.authSource";

  private DbUtils() {
  }


  public static MongoClient createMongoClient(final Vertx vertx) {
    final Properties properties = ConfigUtils.getInstance().getProperties();
    final JsonObject config = new JsonObject()
      .put("host", properties.getProperty(HOST_CONFIG))
      .put("username", properties.getProperty(USERNAME_CONFIG))
      .put("password", properties.getProperty(PASSWORD_CONFIG))
      .put("db_name", properties.getProperty(DATABASE_CONFIG))
      .put("authSource", properties.getProperty(AUTH_SOURCE_CONFIG))
      .put("useObjectId", true);
    return MongoClient.createShared(vertx, config);
  }

}
