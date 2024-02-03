package dev.com.library.api.repository;


import dev.com.library.api.model.entity.AbstractEntity;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class AbstractMongoDBRepository<Entity> {

  private final String collectionName;
  private final MongoClient client;
  private final Class<Entity> entityClass;

  @SuppressWarnings("unchecked")
  public AbstractMongoDBRepository(String collectionName, MongoClient client) {
    this.collectionName = collectionName;
    this.client = client;
    this.entityClass = (Class<Entity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  public Single<Entity> create(Entity doc) {
    return Single.create(emitter -> {
      JsonObject docJson = beforeCreate(doc);
      client.insert(collectionName, docJson, res -> {
        if (res.succeeded()) {
          emitter.onSuccess(afterUpdateOrCreate(docJson, res.result()));
        } else {
          emitter.onError(res.cause());
        }
      });
    });
  }

  public Single<List<Entity>> findAll(int page, int size) {
    return Single.create(emitter -> {
      FindOptions findOptions = new FindOptions().setSkip(page * size).setLimit(size).setSort(new JsonObject().put(AbstractEntity.CREATED_DATE, -1));
      client.findWithOptions(collectionName, new JsonObject(), findOptions, res -> {
        if (res.succeeded()) {
          List<Entity> documents = new ArrayList<>();
          res.result().forEach(json -> documents.add(JsonObject.mapFrom(json).mapTo(entityClass)));
          emitter.onSuccess(documents);
        } else {
          emitter.onError(res.cause());
        }
      });
    });
  }

  public Single<List<Entity>> findWithQuery(FindOptions options) {
    return Single.create(emitter -> {
      client.findWithOptions(collectionName, new JsonObject(), options, res -> {
        if (res.succeeded()) {
          List<Entity> documents = new ArrayList<>();
          res.result().forEach(json -> documents.add(JsonObject.mapFrom(json).mapTo(entityClass)));
          emitter.onSuccess(documents);
        } else {
          emitter.onError(res.cause());
        }
      });
    });
  }

  public Maybe<Entity> findById(String id) {
    return Maybe.create(emitter -> {
      JsonObject query = new JsonObject().put(AbstractEntity.ID, id);
      client.findOne(collectionName, query, null, res -> {
        if (res.succeeded()) {
          if (res.result() != null) {
            Entity entity = res.result().mapTo(entityClass);
            emitter.onSuccess(entity);
          } else {
            emitter.onComplete();
          }
        } else {
          emitter.onError(res.cause());
        }
      });
    });
  }

  public Completable delete(String id) {
    return Completable.create(emitter -> {
      JsonObject query = new JsonObject().put(AbstractEntity.ID, id);
      client.removeDocument(collectionName, query, res -> {
        if (res.succeeded()) {
          if (res.result().getRemovedCount() == 0) {
            emitter.onError(new NoSuchElementException("Model not found"));
          } else {
            emitter.onComplete();
          }
        } else {
          emitter.onError(res.cause());
        }
      });
    });
  }

  public Single<Entity> update(Entity doc, String id) {
    return Single.create(emitter -> {
      JsonObject query = new JsonObject().put(AbstractEntity.ID, id);
      JsonObject docJson = beforeUpdate(doc);
      client.replaceDocuments(collectionName, query, docJson, res -> {
        if (res.succeeded()) {
          if (res.result().getDocMatched() == 0) {
            emitter.onError(new NoSuchElementException("Model not found"));
          } else {
            emitter.onSuccess(afterUpdateOrCreate(res.result().toJson(), id));
          }
        } else {
          emitter.onError(res.cause());
        }
      });
    });
  }


  public Single<Entity> patch(Entity doc, String id) {
    return Single.create(emitter -> {
      JsonObject query = new JsonObject().put(AbstractEntity.ID, id);
      JsonObject docJson = beforeUpdate(doc);
      JsonObject update = new JsonObject().put("$set", docJson);
      client.findOneAndUpdate(collectionName, query, update, res -> {
        if (res.succeeded()) {
          emitter.onSuccess(afterUpdateOrCreate(res.result(), id));
        } else {
          emitter.onError(res.cause());
        }
      });
    });
  }

  public Entity afterUpdateOrCreate(JsonObject docJson, String id) {
    if (docJson.containsKey(AbstractEntity.ID)) docJson.remove(AbstractEntity.ID);
    JsonObject json = new JsonObject().put(AbstractEntity.ID, id).mergeIn(docJson);
    return json.mapTo(entityClass);
  }

  public JsonObject beforeUpdate(Entity doc) {
    JsonObject docJson = JsonObject.mapFrom(doc);
    if (docJson.containsKey(AbstractEntity.LAST_MODIFIED_DATE)) docJson.remove(AbstractEntity.LAST_MODIFIED_DATE);
    if (docJson.containsKey(AbstractEntity.ID)) docJson.remove(AbstractEntity.ID);
    docJson.put(AbstractEntity.LAST_MODIFIED_DATE, new Date().getTime());
    return docJson;
  }

  public JsonObject beforeCreate(Entity doc) {
    JsonObject docJson = JsonObject.mapFrom(doc);
    if (docJson.containsKey(AbstractEntity.LAST_MODIFIED_DATE)) docJson.remove(AbstractEntity.LAST_MODIFIED_DATE);
    if (docJson.containsKey(AbstractEntity.ID)) docJson.remove(AbstractEntity.ID);
    docJson.put(AbstractEntity.CREATED_DATE, new Date().getTime());
    docJson.put(AbstractEntity.LAST_MODIFIED_DATE, new Date().getTime());
    return docJson;
  }

}
