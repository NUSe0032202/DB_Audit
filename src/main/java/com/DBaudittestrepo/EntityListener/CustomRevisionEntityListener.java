package com.DBaudittestrepo.EntityListener;

import com.DBaudittestrepo.entity.CustomRevisionEntity;
import org.hibernate.envers.RevisionListener;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

public class CustomRevisionEntityListener implements RevisionListener {

    @Autowired
    private DataSource dataSource;

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
