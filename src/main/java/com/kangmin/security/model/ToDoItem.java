package com.kangmin.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "to_do_items")
@NoArgsConstructor
@AllArgsConstructor
public class ToDoItem {

    @Id
    private long id;

    private String accountId;

    private String description;

    private Date createDate;
}
