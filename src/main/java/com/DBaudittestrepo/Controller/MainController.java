package com.DBaudittestrepo.Controller;

import com.DBaudittestrepo.Repository.UserRepository;
import com.DBaudittestrepo.entity.Student;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.IOException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class MainController {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @ModelAttribute("student")
    public Student getNewStudent(){
        return  new Student();
    }

    @GetMapping(path = "/")
    public String index() {
        return "external";
    }

    @GetMapping(path = "/landing")
    public String customers(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "landing";
    }

    @GetMapping(path="/retrieve")
    public ResponseEntity<List<Student>> getAllStudents() {
        System.out.println("Attempting to get all students");
        List<Student> students = new ArrayList<>();
        repository.findAll().forEach(students::add);
        System.out.println("Size of list: " + students.size());
        return new ResponseEntity<List<Student>>(students, HttpStatus.OK);
    }

    @PostMapping("/insert")
    //public ResponseEntity<String> createStudent(@RequestBody Student newStudent) {
    public String createStudent(@ModelAttribute("student") Student student,Model model) {
        System.out.print("Form Data: ");
        System.out.println(student.toString());
        model.addAttribute("student", new Student());
        System.out.println("Attempting to insert new student into the DB");
        repository.save(student);
        return "landing";
        //System.out.println("Attempting to save student");
        //try{
        //    repository.save(newStudent);
        //} catch (Exception e) {
        //    e.printStackTrace();
            //return new ResponseEntity<String>("Error while saving!", HttpStatus.BAD_REQUEST);
        //}
        //return new ResponseEntity<String>("Saved!", HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateStudent(@RequestBody Student updateStudent) {
        System.out.println("Attempting to update student");
        Student student = repository.findById(updateStudent.getId()).get();
        student.setFirstName(updateStudent.getFirstName());
        student.setLastName(updateStudent.getLastName());
        student.setEmail(updateStudent.getEmail());
        repository.save(student);
        return new ResponseEntity<String>("Updated Student!", HttpStatus.OK);
    }

    @PostMapping("/audit")
    public void printAuditTrail() {
        System.out.println("Trying to print DB audit messages");
        AuditReader reader = AuditReaderFactory.get(entityManager);
        //getRevisions() returns a list of revision numbers at which an entity is modified
        List<Number>revisions = reader.getRevisions(Student.class,2);
        System.out.println("Revisions: ");
        for(Number rev: revisions) {
            Student tempStudent = reader.find(Student.class,2,rev);
            System.out.println(tempStudent.toString());
            Date revisionDate = reader.getRevisionDate(rev);
            System.out.println(rev + " " + revisionDate);
            System.out.println("________________________");
        }
    }
}
