package com.kangmin.security.model.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.kangmin.security.model.security.WebUserPermission.ACCOUNT_READ;
import static com.kangmin.security.model.security.WebUserPermission.ACCOUNT_WRITE;
import static com.kangmin.security.model.security.WebUserPermission.ADMIN_READ;
import static com.kangmin.security.model.security.WebUserPermission.ADMIN_WRITE;
import static com.kangmin.security.model.security.WebUserPermission.TASK_READ;
import static com.kangmin.security.model.security.WebUserPermission.TASK_WRITE;

public enum WebUserRole {

    UNKNOWN(Sets.newHashSet()),
    NORMAL(Sets.newHashSet(TASK_READ, ACCOUNT_READ)),
    ADMIN(Sets.newHashSet(TASK_READ, TASK_WRITE, ACCOUNT_READ, ACCOUNT_WRITE)),
    SUPER_ADMIN(Sets.newHashSet(TASK_READ, TASK_WRITE, ACCOUNT_READ, ACCOUNT_WRITE, ADMIN_READ, ADMIN_WRITE));

    private final Set<WebUserPermission> permissions;

    WebUserRole(final Set<WebUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<WebUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        final Set<SimpleGrantedAuthority> permissions = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
