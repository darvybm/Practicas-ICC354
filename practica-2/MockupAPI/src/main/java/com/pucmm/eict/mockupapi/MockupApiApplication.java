package com.pucmm.eict.mockupapi;

import com.pucmm.eict.mockupapi.enums.UserRole;
import com.pucmm.eict.mockupapi.models.User;
import com.pucmm.eict.mockupapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class MockupApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockupApiApplication.class, args);
	}

	@Component
	public static class DbInitializer implements CommandLineRunner {

		private final UserRepository userRepository;

		@Autowired
		public DbInitializer(UserRepository userRepository) {
			this.userRepository = userRepository;
		}

		@Override
		public void run(String... args) throws Exception {
			if (userRepository.findByRole(UserRole.ADMINISTRADOR).isEmpty()) {
				User admin = new User();
				admin.setName("Administrador");
				admin.setUsername("admin");
				admin.setPassword("admin");
				admin.setRole(UserRole.ADMINISTRADOR);

				userRepository.save(admin);
			}
		}
	}
}