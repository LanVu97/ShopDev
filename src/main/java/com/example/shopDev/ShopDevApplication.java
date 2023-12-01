package com.example.shopDev;

//import com.example.shopDev.Filter.APIKeyValidatorFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@SpringBootApplication
@EnableMongoAuditing
public class ShopDevApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {


		SpringApplication.run(ShopDevApplication.class, args);

//		new SpringApplicationBuilder(ShopDevApplication.class).profiles("dev", "prod").run(args);
	}




}
