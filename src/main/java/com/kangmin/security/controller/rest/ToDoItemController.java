package com.kangmin.security.controller.rest;

import com.kangmin.security.model.ToDoItem;
import com.kangmin.security.service.ToDoItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/todolists")
public class ToDoItemController {

    private final ToDoItemService toDoItemService;

    public ToDoItemController(final ToDoItemService toDoItemService) {
        this.toDoItemService = toDoItemService;
    }

    @GetMapping("")
    public List<ToDoItem> getAllItems() {
        return toDoItemService.findAll().orElse(new ArrayList<>());
    }

    @GetMapping(path = "/{id}")
    public ToDoItem getItemById(final @PathVariable("id") long id) {
        return toDoItemService.findById(id).orElse(new ToDoItem());
    }

    @GetMapping(path = "/{accountId}")
    public List<ToDoItem> getItemsByAccountId(final @PathVariable("accountId") String accountId) {
        return toDoItemService.findByAccountId(accountId).orElse(new ArrayList<>());
    }
}
