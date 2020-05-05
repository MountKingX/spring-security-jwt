package com.kangmin.security.model.security;

public enum WebUserPermission {

    TASK_READ("task:read"),
    TASK_WRITE("task:write"),

    ACCOUNT_READ("account:read"),
    ACCOUNT_WRITE("account:write"),

    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write");

    private final String name;

    WebUserPermission(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
