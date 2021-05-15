package in.stack.movie.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import in.stack.movie.entity.AuthenticationRequest;
import in.stack.movie.entity.User;
import in.stack.movie.repository.UserRepository;
import in.stack.movie.service.MovieUserDetailsService;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerTest {

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
	String jwt = null;

	@BeforeAll
	public static void setUp() {

		user = new User();
		user.setId(1);
		user.setUsername("user");
		user.setPassword("pass");
		user.setEmail("email@gmail.com");
		user.setCreatedAt(new Date());

	}

	@ParameterizedTest
	@ValueSource(strings = { "2", "3" })
	public void positiveTestGetMovieById(String movieId) throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");

		String json_content = mapper.writeValueAsString(authenticationRequest);
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				"user", passwordEncoder.encode("pass"), new ArrayList<>());

		Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
		Mockito.when(movieUserDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).thenReturn(userDetails);

		MvcResult result = mockMvc
				.perform(post("/api/user/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
						.content(json_content).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/" + movieId).header("Authorization", "Bearer " + jwt))
				.andExpect(status().isOk()).andDo(mvcResult -> {
					String jsonString = (mvcResult.getResponse().getContentAsString());
					Assertions.assertTrue(jsonString.contains("movieId"));
				});

	}

	@Test
	public void negativeTestGetMovieById() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");

		String json_content = mapper.writeValueAsString(authenticationRequest);
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				"user", passwordEncoder.encode("pass"), new ArrayList<>());

		Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
		Mockito.when(movieUserDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).thenReturn(userDetails);

		MvcResult result = mockMvc
				.perform(post("/api/user/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
						.content(json_content).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");
		String movieId = "2";

		mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/" + movieId).header("Authorization", "Bearers " + jwt))
				.andExpect(status().isForbidden());

	}

	@Test
	public void positiveTestGetTopRatedMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");

		String json_content = mapper.writeValueAsString(authenticationRequest);
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				"user", passwordEncoder.encode("pass"), new ArrayList<>());

		Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
		Mockito.when(movieUserDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).thenReturn(userDetails);

		MvcResult result = mockMvc
				.perform(post("/api/user/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
						.content(json_content).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/toprated/1").header("Authorization", "Bearer " + jwt))
				.andExpect(status().isOk()).andDo(mvcResult -> {
					String jsonString = (mvcResult.getResponse().getContentAsString());
					int length = JsonPath.parse(jsonString).read("$.length()");
					Assertions.assertTrue(length > 1);
				});

	}

	@Test
	public void positiveTestGetPopularMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");

		String json_content = mapper.writeValueAsString(authenticationRequest);
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				"user", passwordEncoder.encode("pass"), new ArrayList<>());

		Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
		Mockito.when(movieUserDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).thenReturn(userDetails);

		MvcResult result = mockMvc
				.perform(post("/api/user/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
						.content(json_content).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/popular/1").header("Authorization", "Bearer " + jwt))
				.andExpect(status().isOk()).andDo(mvcResult -> {
					String jsonString = (mvcResult.getResponse().getContentAsString());
					int length = JsonPath.parse(jsonString).read("$.length()");
					Assertions.assertTrue(length > 1);
				});

	}

	@Test
	public void positiveTestGetLatestMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");

		String json_content = mapper.writeValueAsString(authenticationRequest);
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				"user", passwordEncoder.encode("pass"), new ArrayList<>());

		Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
		Mockito.when(movieUserDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).thenReturn(userDetails);

		MvcResult result = mockMvc
				.perform(post("/api/user/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
						.content(json_content).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/latest").header("Authorization", "Bearer " + jwt))
				.andExpect(status().isOk()).andExpect(jsonPath("$.movieId", Matchers.notNullValue()))
				.andExpect(jsonPath("$.name", Matchers.notNullValue()));

	}

}
