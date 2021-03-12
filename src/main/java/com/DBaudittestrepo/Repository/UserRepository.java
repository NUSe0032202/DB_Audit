package com.DBaudittestrepo.Repository;

import com.DBaudittestrepo.entity.Student;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Student,Integer> {
}
