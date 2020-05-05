package com.kangmin.security.service;

import com.kangmin.security.model.ToDoItem;

import java.util.List;
import java.util.Optional;

public interface ToDoItemService {

    Optional<List<ToDoItem>> findAll();

    Optional<ToDoItem> findById(final long id);

    Optional<List<ToDoItem>> findByAccountId(final String accountId);

    void create(final ToDoItem toDoItem);
}
