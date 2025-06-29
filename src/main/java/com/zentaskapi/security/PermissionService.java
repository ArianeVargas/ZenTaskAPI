package com.zentaskapi.security;

import com.zentaskapi.entity.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class PermissionService {

    public void verifyUserPermission(User user, String currentUsername, boolean isAdmin) {
        if (!isAdmin && !user.getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");
        }
    }
}

