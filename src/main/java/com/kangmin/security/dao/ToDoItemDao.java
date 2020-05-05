package com.kangmin.security.dao;

import com.kangmin.security.model.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToDoItemDao extends JpaRepository<ToDoItem, Long> {

    Optional<ToDoItem> findById(final long id);

    Optional<List<ToDoItem>> findByAccountId(final String accountId);
}