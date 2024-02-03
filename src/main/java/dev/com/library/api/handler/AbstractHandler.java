package dev.com.library.api.handler;


import dev.com.library.api.service.AbstractService;
import dev.com.library.utils.LogUtils;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.NoSuchElementException;

public abstract class AbstractHandler<Entity, Service extends AbstractService> {

  private static final String ID_PARAMETER = "id";
  private static final String PAGE_PARAMETER = "page";
  private static final String LIMIT_PARAMETER = "limit";
  private static final String CONTENT_TYPE_HEADER = "Content-Type";
  private static final String APPLICATION_JSON = "application/json";

  private final Service service;
  private final Class<Entity> entityClass;

  @SuppressWarnings("unchecked")
  public AbstractHandler(Service service) {
    this.service = service;
    this.entityClass = (Class<Entity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  public void findAll(RoutingContext rc) {
    final int page = rc.request().getParam(PAGE_PARAMETER) == null ? 0 : Integer.parseInt(rc.request().getParam(PAGE_PARAMETER));
    final int limit = rc.request().getParam(LIMIT_PARAMETER) == null ? 999 : Integer.parseInt(rc.request().getParam(LIMIT_PARAMETER));
    service.findAll(page, limit).subscribe(result -> onSuccessResponse(rc, 200, result), error -> onErrorResponse(rc, 400, (Throwable) error));
  }

  public void findById(RoutingContext rc) {
    final String id = rc.pathParam(ID_PARAMETER);
    service.findById(id).subscribe(result -> onSuccessResponse(rc, 200, result), error -> onErrorResponse(rc, 400, (Throwable) error), () -> onErrorResponse(rc, 400, new NoSuchElementException(LogUtils.ERROR_NOT_FOUND_DOCUMENT.buildMessage(id))));
  }

  public void create(RoutingContext rc) {
    final Entity entity = mapRequestBodyToBook(rc);
    service.create(entity).subscribe(result -> onSuccessResponse(rc, 201, result), error -> onErrorResponse(rc, 400, (Throwable) error));
  }

  public void update(RoutingContext rc) {
    final String id = rc.pathParam(ID_PARAMETER);
    final Entity entity = mapRequestBodyToBook(rc);
    service.update(entity, id).subscribe(result -> onSuccessResponse(rc, 200, result), error -> onErrorResponse(rc, 400, (Throwable) error));
  }

  public void patch(RoutingContext rc) {
    final String id = rc.pathParam(ID_PARAMETER);
    final Entity entity = mapRequestBodyToBook(rc);
    service.patch(entity, id).subscribe(result -> onSuccessResponse(rc, 200, result), error -> onErrorResponse(rc, 400, (Throwable) error));
  }

  public void delete(RoutingContext rc) {
    final String id = rc.pathParam(ID_PARAMETER);
    service.delete(id).subscribe(() -> onSuccessResponse(rc, 204, null), error -> onErrorResponse(rc, 400, new NoSuchElementException(LogUtils.ERROR_NOT_FOUND_DOCUMENT.buildMessage(id))));
  }

  private Entity mapRequestBodyToBook(RoutingContext rc) {
    Entity entity = null;
    try {
      entity = rc.body().asJsonObject().mapTo(entityClass);
    } catch (IllegalArgumentException ex) {
      onErrorResponse(rc, 400, ex);
    }
    return entity;
  }

  public void onSuccessResponse(RoutingContext rc, int status, Object object) {
    rc.response().setStatusCode(status).putHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON).end(Json.encodePrettily(object));
  }

  public void onErrorResponse(RoutingContext rc, int status, Throwable throwable) {
    final JsonObject error = new JsonObject().put("error", throwable.getMessage());
    rc.response().setStatusCode(status).putHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON).end(Json.encodePrettily(error));
  }

  public void validateStringFields(RoutingContext rc, String... attributes) {
    Arrays.asList(attributes).forEach(attribute -> {
      if (rc.body().asJsonObject().getString(attribute) == null || rc.body().asJsonObject().getString(attribute).isEmpty()) {
        onErrorResponse(rc, 400, new IllegalArgumentException("Field " + attribute + " is required"));
      }
    });
  }

  private void validadeStringField(RoutingContext rc, String field) {
    if (rc.body().asJsonObject().getString(field) == null || rc.body().asJsonObject().getString(field).isEmpty()) {
      onErrorResponse(rc, 400, new IllegalArgumentException("Field " + field + " is required"));
    }
  }

  private void validadeIntegerField(JsonObject body, String field) {
    if (body.getInteger(field) == null) {
      throw new IllegalArgumentException("Field " + field + " is required");
    }
  }

  private void validadeBooleanField(JsonObject body, String field) {
    if (body.getBoolean(field) == null) {
      throw new IllegalArgumentException("Field " + field + " is required");
    }
  }

  private void validadeDoubleField(JsonObject body, String field) {
    if (body.getDouble(field) == null) {
      throw new IllegalArgumentException("Field " + field + " is required");
    }
  }

  private void validadeLongField(JsonObject body, String field) {
    if (body.getLong(field) == null) {
      throw new IllegalArgumentException("Field " + field + " is required");
    }
  }

  private void validadeFloatField(JsonObject body, String field) {
    if (body.getFloat(field) == null) {
      throw new IllegalArgumentException("Field " + field + " is required");
    }
  }

  private void validadeArrayField(JsonObject body, String field) {
    if (body.getJsonArray(field) == null || body.getJsonArray(field).isEmpty()) {
      throw new IllegalArgumentException("Field " + field + " is required");
    }
  }

  private void validadeObjectField(JsonObject body, String field) {
    if (body.getJsonObject(field) == null || body.getJsonObject(field).isEmpty()) {
      throw new IllegalArgumentException("Field " + field + " is required");
    }
  }


}
