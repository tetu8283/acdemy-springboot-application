package com.spring.springbootapplication;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// MyBatisのマッパーパッケージをスキャンして、マッパーインターフェースを検出
@MapperScan("com.spring.springbootapplication.mapper")
public class SpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}
}
