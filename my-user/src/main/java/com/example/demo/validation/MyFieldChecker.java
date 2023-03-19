package com.example.demo.validation;

import com.example.demo.ann.AllFieldsNullChecker;
import com.example.demo.dto.Debtor;
import com.example.demo.dto.Student;
import org.springframework.stereotype.Service;

@AllFieldsNullChecker(sources = {
        com.example.demo.dto.Debtor.class,
        com.example.demo.dto.Student.class
})
@Service
public interface MyFieldChecker {
    Boolean isEveryDebtorFieldNull(Debtor debtor);
    Boolean isEveryStudentFieldNull(Student student);
}
