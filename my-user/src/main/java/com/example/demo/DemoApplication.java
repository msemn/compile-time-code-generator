package com.example.demo;

import com.example.demo.dto.Debtor;
import com.example.demo.validation.MyFieldChecker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@SpringBootApplication
@ComponentScan({"com.example"})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

@Service
class TestAnnotation implements ApplicationListener<ContextRefreshedEvent> {
	private final MyFieldChecker fieldChecker;

	public TestAnnotation(MyFieldChecker fieldChecker) {
		this.fieldChecker = fieldChecker;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println("=================================");
		Boolean everyDebtorFieldNull = fieldChecker.isEveryDebtorFieldNull(new Debtor());
		System.out.println(everyDebtorFieldNull);
		Debtor debtor = new Debtor();
		debtor.setName("Gholi");
		System.out.println(fieldChecker.isEveryDebtorFieldNull(debtor));
		System.out.println("=================================");
	}
}
