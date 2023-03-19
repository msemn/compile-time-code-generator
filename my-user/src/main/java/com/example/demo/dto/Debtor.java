package com.example.demo.dto;

import java.util.List;

public class Debtor {
    private String name;
    private int age;
    private List<String> checkOuts;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCheckOuts(List<String> checkOuts) {
        this.checkOuts = checkOuts;
    }

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
