package com.post_hub.iam_service.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IamServiceUserRole {
    USER("USER"),
    ADMIN("ADMIN"),
    SUPER_ADMIN("SUPERADMIN");

    private final String role;

    public static IamServiceUserRole fromName(String role){
        return IamServiceUserRole.valueOf(role.toUpperCase());
    }
}
