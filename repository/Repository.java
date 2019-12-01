package repository;

import java.util.List;

public interface Repository<O> {

    void add(O o);

    List<O> findAll();
}
