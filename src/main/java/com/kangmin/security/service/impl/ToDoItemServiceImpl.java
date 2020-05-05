package com.kangmin.security.service.impl;

import com.kangmin.security.dao.ToDoItemDao;
import com.kangmin.security.model.ToDoItem;
import com.kangmin.security.service.ToDoItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToDoItemServiceImpl implements ToDoItemService {

    private final ToDoItemDao toDoItemDao;

    public ToDoItemServiceImpl(final ToDoItemDao toDoItemDao) {
        this.toDoItemDao = toDoItemDao;
    }

    @Override
    public Optional<List<ToDoItem>> findAll() {
        return Optional.of(toDoItemDao.findAll());
    }

    @Override
    public Optional<ToDoItem> findById(final long id) {
        return toDoItemDao.findById(id);
    }

    @Override
    public Optional<List<ToDoItem>> findByAccountId(final String accountId) {
        return toDoItemDao.findByAccountId(accountId);
    }

    @Override
    public void create(final ToDoItem toDoItem) {
        toDoItemDao.save(toDoItem);
    }
}
