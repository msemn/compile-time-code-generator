package com.example.generated;

import com.example.demo.dto.Debtor;
import com.example.demo.dto.Student;
import com.example.demo.validation.MyFieldChecker;
import java.lang.Boolean;

public final class MyFieldCheckerImpl implements MyFieldChecker {
  public Boolean isEveryDebtorFieldNull(Debtor debtor) {
    return debtor.getName() == null &&
        debtor.getCheckOuts() == null;
  }

  public Boolean isEveryStudentFieldNull(Student student) {
    return student.getName() == null;
  }
}
