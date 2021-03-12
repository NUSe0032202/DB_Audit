package com.DBaudittestrepo.EntityListener;

import com.DBaudittestrepo.entity.CustomRevisionEntity;
import org.hibernate.envers.RevisionListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

public class CustomRevisionEntityListener implements RevisionListener {

    @Autowired
    private DataSource dataSource;

    @Override
    public void newRevision(Object o) {
        try {
            CustomRevisionEntity customRevisionEntity = (CustomRevisionEntity) o;
            Connection connection = dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            customRevisionEntity.setUsername(metaData.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
