package com.DBaudittestrepo.EntityListener;

import com.DBaudittestrepo.entity.CustomRevisionEntity;
import org.hibernate.envers.RevisionListener;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

public class CustomRevisionEntityListener implements RevisionListener {

    @Override
    public void newRevision(Object o) {
        try {
            KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken)
                    SecurityContextHolder.getContext().getAuthentication();
            Principal principal = (Principal) authentication.getPrincipal();
            System.out.println("Username: " + principal.getName());

            CustomRevisionEntity customRevisionEntity = (CustomRevisionEntity) o;
            customRevisionEntity.setUsername(principal.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
