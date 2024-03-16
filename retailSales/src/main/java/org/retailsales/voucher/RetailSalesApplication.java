package org.retailsales.voucher;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//@SpringBootApplication
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@MapperScan(basePackages = "org.retailsales.voucher.dao")
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class RetailSalesApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetailSalesApplication.class, args);
    }

}
