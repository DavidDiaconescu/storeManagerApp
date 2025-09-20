package Repo;

import java.util.List;
import java.util.Optional;

public interface IRepo<T> {
    void create(T entity);
    List<T> readAll();
    Optional<T> readById(int id);
    void update(T entity);
    void delete(int id);
}
