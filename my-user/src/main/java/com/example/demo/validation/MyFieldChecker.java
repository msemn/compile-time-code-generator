package com.example.demo.validation;

import com.example.demo.ann.AllFieldsNullChecker;

@AllFieldsNullChecker(sources = {
        com.example.demo.dto.Debtor.class,
        com.example.demo.dto.Student.class
})
public interface MyFieldChecker {
}
