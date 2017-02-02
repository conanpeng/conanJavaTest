package com.conan.javaTest;

import jdk.Exported;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

import javax.swing.*;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Import(value = SpringConfig.class)
public class Test1Application {

	public static void main(String[] args) {
		SpringApplication.run(Test1Application.class, args);
	}
}
