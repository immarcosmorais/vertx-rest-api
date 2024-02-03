package dev.com.library.api.service;


import dev.com.library.api.repository.AbstractMongoDBRepository;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public abstract class AbstractService<Entity, Repository extends AbstractMongoDBRepository<Entity>> {

  private final Repository repository;

  public Single<List<Entity>> findAll(int page, int size) {
    return repository.findAll(page, size);
  }

  public Maybe<Entity> findById(String id) {
    return repository.findById(id);
  }

  public Single<Entity> create(Entity entity) {
    return repository.create(entity);
  }

  public Single<Entity> update(Entity entity, String id) {
    return repository.update(entity, id);
  }

  public Single<Entity> patch(Entity entity, String id) {
    return repository.patch(entity, id);
  }

  public Completable delete(String id) {
    return repository.delete(id);
  }

}
