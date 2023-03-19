package com.example.demo.validation;

import com.example.demo.ann.ToStringImplementer;
import org.springframework.stereotype.Component;

@Component
@ToStringImplementer(com.example.demo.dto.Debtor.class)
public interface MyService {}
