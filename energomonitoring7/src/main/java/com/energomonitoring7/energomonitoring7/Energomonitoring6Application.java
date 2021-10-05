package com.energomonitoring7.energomonitoring7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@SpringBootApplication
public class Energomonitoring6Application {

	public static void main(String[] args) {
		SpringApplication.run(Energomonitoring6Application.class, args);
	}
}
