package com.conductor.apni.littleleappwa.dbmanager;

import java.util.Collection;
import java.util.List;

/**
 * Created by Saipro on 28-04-2017.
 */

public interface Manager<T> {

    void create(T object);

    void createOrUpdate(T object);

    void createOrUpdate(T object, boolean notify);

    void createOrUpdateAll(Collection<T> objectsCollection);

    T get(long id);

    List<T> getAll();

    List<T> getAllSorted(String sortedColumn, boolean ascending);

    void update(T object);

    void update(T object, boolean notify);

    void updateAll(Collection<T> objectsCollection);

    void delete(T object);

    void deleteById(long id);

    boolean exists(long id);
}
