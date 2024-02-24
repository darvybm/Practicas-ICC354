package com.pucmm.eict.mockupapi;

import com.pucmm.eict.mockupapi.enums.UserRole;
import com.pucmm.eict.mockupapi.models.User;
import com.pucmm.eict.mockupapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class MockupApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockupApiApplication.class, args);
	}

	@Component
	public static class DbInitializer implements CommandLineRunner {

		private final UserRepository userRepository;
		private final PasswordEncoder passwordEncoder;

		@Autowired
		public DbInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
			this.userRepository = userRepository;
			this.passwordEncoder = passwordEncoder;
		}

		@Override
		public void run(String... args) throws Exception {
			if (userRepository.findByRole(UserRole.ADMINISTRADOR).isEmpty()) {
				User admin = new User();
				admin.setName("Administrador");
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("admin"));
				admin.setRole(UserRole.ADMINISTRADOR);

				userRepository.save(admin);
			}
		}
	}
}
