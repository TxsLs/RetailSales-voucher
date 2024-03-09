package org.retailsales.voucher;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

//@SpringBootApplication
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@MapperScan(basePackages = "org.boot.hf.admin.dao")
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class RetailSalesApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetailSalesApplication.class, args);
    }

}
