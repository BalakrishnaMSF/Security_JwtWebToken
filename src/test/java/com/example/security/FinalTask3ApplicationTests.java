package com.example.security;

import com.example.security.controller.UserController;
import com.example.security.model.User;
import com.example.security.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
class FinalTask3ApplicationTests {

	@InjectMocks
	private UserController userController;

	@Mock
	private UserService userService;


	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testAddUser() {
		setUp();

		User userInfo = new User();
		userInfo.setUserName("Bala");
		userInfo.setEmail("Bala@example.com");
		userInfo.setRole("ROLE_USER");
		userInfo.setPassword("123");
		userInfo.setPhNo("8745634563");

		when(userService.addUserDetails(any(User.class))).thenReturn("USER_DETAILS_ADDED_SUCCESSFULLY");

		ResponseEntity<String> result = userController.addUser(userInfo);

		verify(userService).addUserDetails(any(User.class));
	}

	@Test
	public void testAllUsers() {
		List<User> userList = new ArrayList<>();

		User user1 = new User();
		user1.setId(1);
		user1.setUserName("User1");
		user1.setEmail("user1@example.com");
		user1.setPhNo("7890123456");
		user1.setPassword("123");
		user1.setRole("ROLE_USER");
		userList.add(user1);

		User user2 = new User();
		user2.setId(2);
		user2.setUserName("User2");
		user2.setEmail("user2@example.com");
		user2.setPhNo("9876543210");
		user2.setPassword("123");
		user2.setRole("ROLE_ADMIN");
		userList.add(user2);


		when(userService.getAllUsers()).thenReturn(userList);

		List<User> response = userController.getAllUsers();

		assertEquals(userList, response);
	}

}
