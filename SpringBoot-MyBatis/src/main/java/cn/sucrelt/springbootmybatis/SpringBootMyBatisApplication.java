package cn.sucrelt.springbootmybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("cn.sucrelt.springbootmybatis.dao")
@EnableTransactionManagement
public class SpringBootMyBatisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMyBatisApplication.class, args);
	}

}
