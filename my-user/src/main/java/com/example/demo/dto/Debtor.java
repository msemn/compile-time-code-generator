package com.example.demo.dto;

import java.util.List;

public class Debtor {
    private String name;
    private int age;
    private List<String> checkOuts;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public List<String> getCheckOuts() {
        return checkOuts;
    }
}
