package in.stack.movie.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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
import in.stack.movie.entity.Movie;
import in.stack.movie.entity.User;
import in.stack.movie.repository.UserRepository;
import in.stack.movie.service.MovieUserDetailsService;
import in.stack.movie.service.UserMovieService;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class UserMovieControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@MockBean
	private UserMovieService userMovieService;

	@MockBean
	private MovieUserDetailsService movieUserDetailsService;

	@MockBean
	private AuthenticationRequest authenticationRequest;

	@MockBean
	private UserRepository userRepository;

//	@MockBean
//	private HttpSession session;

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
	@Order(1)
	public void positiveTestAddFavoriteMovie() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");

		String json_content = mapper.writeValueAsString(authenticationRequest);
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				"user", passwordEncoder.encode("pass"), new ArrayList<>());

		Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
		Mockito.when(movieUserDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).thenReturn(userDetails);

		MvcResult result = mockMvc
				.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
						.content(json_content).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		List<Movie> movies = new ArrayList<Movie>();
		Movie movie = new Movie("2", "Ariel",
				"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...");
		movies.add(movie);
		user.setFavoriteMovies(movies);

//		Mockito.when(session.getAttribute("user")).thenReturn(user);
		Mockito.when(userMovieService.getUser(ArgumentMatchers.anyInt())).thenReturn(user);
		Mockito.when(userMovieService.addMovie(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
				.thenReturn(user);

		mockMvc.perform(MockMvcRequestBuilders.post("/favorites/" + movie.getMovieId())
				.header("Authorization", "Bearer " + jwt).sessionAttr("user", user)).andExpect(status().isOk())
				.andExpect(jsonPath("$.favoriteMovies[0].name", Matchers.equalTo("Ariel")));

	}

	@Test
	@Order(2)
	public void negativeTest1AddFavoriteMovie() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");

		String json_content = mapper.writeValueAsString(authenticationRequest);
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				"user", passwordEncoder.encode("pass"), new ArrayList<>());

		Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
		Mockito.when(movieUserDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).thenReturn(userDetails);

		MvcResult result = mockMvc
				.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
						.content(json_content).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		List<Movie> movies = new ArrayList<Movie>();
		Movie movie = new Movie("2", "Ariel",
				"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...");
		movies.add(movie);
		user.setFavoriteMovies(movies);

		Mockito.when(userMovieService.getUser(ArgumentMatchers.anyInt())).thenReturn(user);
		Mockito.when(userMovieService.addMovie(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
				.thenReturn(user);

		mockMvc.perform(MockMvcRequestBuilders.post("/favorites/" + movie.getMovieId())
				.header("Authorization", "Bearers " + jwt).sessionAttr("user", user)).andExpect(status().isForbidden());

	}

	@Test
	@Order(3)
	public void negativeTest2AddFavoriteMovie() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");

		String json_content = mapper.writeValueAsString(authenticationRequest);
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				"user", passwordEncoder.encode("pass"), new ArrayList<>());

		Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
		Mockito.when(movieUserDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).thenReturn(userDetails);

		MvcResult result = mockMvc
				.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
						.content(json_content).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		List<Movie> movies = new ArrayList<Movie>();
		Movie movie = new Movie("2", "Ariel",
				"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...");
		movies.add(movie);
		user.setFavoriteMovies(movies);

		Mockito.when(userMovieService.getUser(ArgumentMatchers.anyInt())).thenReturn(user);
		Mockito.when(userMovieService.addMovie(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
				.thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.post("/favorites/" + movie.getMovieId())
				.header("Authorization", "Bearer " + jwt).sessionAttr("user", user)).andExpect(status().isConflict())
				.andReturn();

	}

	@Test
	@Order(4)
	public void positiveTestGetfavoriteMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");

		String json_content = mapper.writeValueAsString(authenticationRequest);
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				"user", passwordEncoder.encode("pass"), new ArrayList<>());

		Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
		Mockito.when(movieUserDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).thenReturn(userDetails);

		MvcResult result = mockMvc
				.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
						.content(json_content).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		List<Movie> movies = new ArrayList<Movie>();
		Movie movie = new Movie("2", "Ariel",
				"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...");
		movies.add(movie);
		user.setFavoriteMovies(movies);

		Mockito.when(userMovieService.getMoviesbyUserId(ArgumentMatchers.anyInt()))
				.thenReturn(user.getFavoriteMovies());

		mockMvc.perform(get("/favorites/all").header("Authorization", "Bearer " + jwt).sessionAttr("user", user))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].name", Matchers.equalTo("Ariel")));

	}

	@Test
	@Order(5)
	public void negativeTest1GetfavoriteMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");

		String json_content = mapper.writeValueAsString(authenticationRequest);
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				"user", passwordEncoder.encode("pass"), new ArrayList<>());

		Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
		Mockito.when(movieUserDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).thenReturn(userDetails);

		MvcResult result = mockMvc
				.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
						.content(json_content).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		List<Movie> movies = new ArrayList<Movie>();
		Movie movie = new Movie("2", "Ariel",
				"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...");
		movies.add(movie);
		user.setFavoriteMovies(movies);

		Mockito.when(userMovieService.getMoviesbyUserId(ArgumentMatchers.anyInt())).thenReturn(null);

		mockMvc.perform(get("/favorites/all").header("Authorization", "Bearer " + jwt).sessionAttr("user", user))
				.andExpect(status().isConflict()).andReturn();

	}

	@Test
	@Order(6)
	public void negativeTest2GetfavoriteMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");

		String json_content = mapper.writeValueAsString(authenticationRequest);
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				"user", passwordEncoder.encode("pass"), new ArrayList<>());

		Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
		Mockito.when(movieUserDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).thenReturn(userDetails);

		MvcResult result = mockMvc
				.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
						.content(json_content).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		List<Movie> movies = new ArrayList<Movie>();
		Movie movie = new Movie("2", "Ariel",
				"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...");
		movies.add(movie);
		user.setFavoriteMovies(movies);

		Mockito.when(userMovieService.getMoviesbyUserId(ArgumentMatchers.anyInt())).thenReturn(null);

		mockMvc.perform(get("/favorites/all").header("Authorization", "Bearers " + jwt).sessionAttr("user", user))
				.andExpect(status().isForbidden());

	}

	@Test
	@Order(7)
	public void positiveTestDeleteMovie() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");

		String json_content = mapper.writeValueAsString(authenticationRequest);
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				"user", passwordEncoder.encode("pass"), new ArrayList<>());

		Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
		Mockito.when(movieUserDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).thenReturn(userDetails);

		MvcResult result = mockMvc
				.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
						.content(json_content).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		Movie movie = new Movie("2", "Ariel",
				"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...");

		Mockito.when(userMovieService.deleteByMovieId(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
				.thenReturn(movie);

		mockMvc.perform(MockMvcRequestBuilders.delete("/favorites/" + movie.getMovieId())
				.header("Authorization", "Bearer " + jwt).sessionAttr("user", user)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", Matchers.equalTo("Ariel")));

	}

	@Test
	@Order(8)
	public void negativeTest1DeleteMovie() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");

		String json_content = mapper.writeValueAsString(authenticationRequest);
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				"user", passwordEncoder.encode("pass"), new ArrayList<>());

		Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
		Mockito.when(movieUserDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).thenReturn(userDetails);

		MvcResult result = mockMvc
				.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
						.content(json_content).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		Movie movie = new Movie("2", "Ariel",
				"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...");

		Mockito.when(userMovieService.deleteByMovieId(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
				.thenReturn(movie);

		mockMvc.perform(MockMvcRequestBuilders.delete("/favorites/" + movie.getMovieId())
				.header("Authorization", "Bearers " + jwt).sessionAttr("user", user)).andExpect(status().isForbidden());

	}

	@Test
	@Order(9)
	public void negativeTest2DeleteMovie() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");

		String json_content = mapper.writeValueAsString(authenticationRequest);
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				"user", passwordEncoder.encode("pass"), new ArrayList<>());

		Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
		Mockito.when(movieUserDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).thenReturn(userDetails);

		MvcResult result = mockMvc
				.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
						.content(json_content).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		Movie movie = new Movie("2", "Ariel",
				"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...");

		Mockito.when(userMovieService.deleteByMovieId(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
				.thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.delete("/favorites/" + movie.getMovieId())
				.header("Authorization", "Bearer " + jwt).sessionAttr("user", user)).andExpect(status().isConflict())
				.andReturn();

	}

}
