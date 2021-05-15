package in.stack.movie.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.stack.movie.entity.AuthenticationRequest;
import in.stack.movie.entity.User;
import in.stack.movie.repository.UserRepository;
import in.stack.movie.service.MovieUserDetailsService;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@MockBean
	private MovieUserDetailsService movieUserDetailsService;

	@MockBean
	private AuthenticationRequest authenticationRequest;

	@MockBean
	private UserRepository userRepository;

	private static User user;

	private ObjectMapper mapper = new ObjectMapper();

	@BeforeAll
	public static void setUp() {

		user = new User();
		user.setId(1);
		user.setUsername("user");
		user.setPassword("pass");
		user.setEmail("email@gmail.com");
		user.setCreatedAt(new Date());

	}

	@Test
	public void positiveTestRegisterUser() throws Exception {

		Mockito.when(movieUserDetailsService.saveUser(ArgumentMatchers.any())).thenReturn(user);
		String json_content = mapper.writeValueAsString(user);
		mockMvc.perform(post("/api/user/register").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(json_content).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.username", Matchers.equalTo("user")));

	}

	@Test
	public void negativeTestRegisterUser() throws Exception {

		Mockito.when(movieUserDetailsService.saveUser(ArgumentMatchers.any())).thenReturn(user);
		mockMvc.perform(post("/api/user/register").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());

	}

	@Test
	public void positiveTestAuthenticateUser() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		
		String json_content = mapper.writeValueAsString(authenticationRequest);
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				"user", passwordEncoder.encode("pass"), new ArrayList<>());

		Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
		Mockito.when(movieUserDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).thenReturn(userDetails);
		
		mockMvc.perform(post("/api/user/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(json_content).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue()))
				.andExpect(status().isOk());

	}

	@Test
	public void negativeTestAuthenticateUser() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("12345");
		
		String json_content = mapper.writeValueAsString(authenticationRequest);
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				"user", passwordEncoder.encode("pass"), new ArrayList<>());

		Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
		Mockito.when(movieUserDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).thenReturn(userDetails);

		mockMvc.perform(post("/api/user/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(json_content).accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());

	}

}
