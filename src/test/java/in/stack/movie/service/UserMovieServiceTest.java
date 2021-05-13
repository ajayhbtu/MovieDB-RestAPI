package in.stack.movie.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import in.stack.movie.entity.Movie;
import in.stack.movie.entity.User;
import in.stack.movie.repository.MovieRepository;
import in.stack.movie.repository.UserRepository;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class UserMovieServiceTest {

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private MovieRepository movieRepository;

	@Autowired
	private UserMovieService userMovieService;

	// test case to check UserMovieService class object
	@Test
	@Order(1)
	public void testUserMovieService() {

		UserMovieService service = Mockito.mock(UserMovieService.class);
		assertNotNull(service);
		assertTrue(service instanceof UserMovieService);

	}

	// test case to check user is getting or not
	@Test
	@Order(2)
	public void positiveTestGetUser() {

		User user1 = new User();
		user1.setId(1);
		user1.setUsername("user");
		user1.setPassword("pass");
		user1.setEmail("email@gmail.com");
		user1.setCreatedAt(new Date());
		when(userRepository.findById(ArgumentMatchers.anyInt())).thenReturn(user1);

		User user2 = userMovieService.getUser(1);
		assertNotNull(user2);
		assertEquals(user1.getUsername(), user2.getUsername());

	}

	// negative test case to check user is getting or not
	@Test
	@Order(3)
	public void negativeTestGetUser() {

		when(userRepository.findById(ArgumentMatchers.anyInt())).thenReturn(null);
		User user2 = userMovieService.getUser(1);
		assertNull(user2);

	}

	// test case to check movie is added in user fav list or not
	@Test
	@Order(4)
	public void positiveTestAddMovie() {

		User user1 = new User();
		user1.setId(1);
		user1.setUsername("user");
		user1.setPassword("pass");
		user1.setEmail("email@gmail.com");
		user1.setCreatedAt(new Date());
		when(userRepository.save(ArgumentMatchers.any())).thenReturn(user1);

		User user2 = userMovieService.addMovie(1, "1");
		assertNotNull(user2);
		assertEquals(user1.getUsername(), user2.getUsername());

	}

	// negative test case to check movie is added in user fav list or not
	@Test
	@Order(5)
	public void negativeTestAddMovie() {

		when(userRepository.save(ArgumentMatchers.any())).thenReturn(null);

		User user2 = userMovieService.addMovie(1, "1");
		assertNull(user2);

	}

	// test case to check movies of particular user are getting or not
	@Test
	@Order(6)
	public void positiveTestGetMoviesbyUserId() {

		User user1 = new User();
		user1.setId(1);
		user1.setUsername("user");
		user1.setPassword("pass");
		user1.setEmail("email@gmail.com");
		user1.setCreatedAt(new Date());

		List<Movie> m = new ArrayList<Movie>();
		Movie m1 = new Movie("2", "Ariel",
				"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...");
		m.add(m1);
		user1.setFavoriteMovies(m);

		when(userRepository.findById(ArgumentMatchers.anyInt())).thenReturn(user1);
		List<Movie> movies = userMovieService.getMoviesbyUserId(1);

		assertEquals(m.contains(m1), movies.contains(m1));

	}

	// negative test case to check movies of particular user are getting or not
	@Test
	@Order(7)
	public void negativeTestGetMoviesbyUserId() {

		User user1 = new User();
		user1.setId(1);
		user1.setUsername("user");
		user1.setPassword("pass");
		user1.setEmail("email@gmail.com");
		user1.setCreatedAt(new Date());

		List<Movie> m = new ArrayList<Movie>();
		user1.setFavoriteMovies(m);

		when(userRepository.findById(ArgumentMatchers.anyInt())).thenReturn(user1);

		List<Movie> movies = userMovieService.getMoviesbyUserId(1);
		assertEquals(0, movies.size());

	}

	// test case to check user is able to delete particular movie from his fav list
	// or not
	@Test
	@Order(8)
	public void positiveTestDeleteByMovieId() {

		User user1 = new User();
		user1.setId(1);
		user1.setUsername("user");
		user1.setPassword("pass");
		user1.setEmail("email@gmail.com");
		user1.setCreatedAt(new Date());

		List<Movie> m = new ArrayList<Movie>();
		Movie m1 = new Movie("2", "Ariel",
				"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...");
		Movie m2 = new Movie("3", "Shadows in Paradise",
				"An episode in the life of Nikander, a garbage man, involving the death of a co-worker, an affair and much more.");

		m.add(m1);
		m.add(m2);
		user1.setFavoriteMovies(m);

		when(userRepository.findById(ArgumentMatchers.anyInt())).thenReturn(user1);
		Movie movie = userMovieService.deleteByMovieId(10100, "100");
		assertNull(movie);
	}

	// negative test case to check user is able to delete particular movie from his
	// favorite list or not
	@Test
	@Order(9)
	public void negativeTestDeleteByMovieId() {

		User user1 = new User();
		user1.setId(1);
		user1.setUsername("user");
		user1.setPassword("pass");
		user1.setEmail("email@gmail.com");
		user1.setCreatedAt(new Date());

		List<Movie> m = new ArrayList<Movie>();
		Movie m1 = new Movie("2", "Ariel",
				"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...");
		Movie m2 = new Movie("3", "Shadows in Paradise",
				"An episode in the life of Nikander, a garbage man, involving the death of a co-worker, an affair and much more.");

		m.add(m1);
		m.add(m2);
		user1.setFavoriteMovies(m);

		when(userRepository.findById(ArgumentMatchers.anyInt())).thenReturn(user1);
		Movie movie = userMovieService.deleteByMovieId(1, "12345");
		assertNull(movie);
	}

}
