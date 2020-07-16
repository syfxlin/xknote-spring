package me.ixk.xknote;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("me.ixk.xknote")
public class XknoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(XknoteApplication.class, args);
    }
}
