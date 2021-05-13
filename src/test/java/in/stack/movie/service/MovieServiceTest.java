package in.stack.movie.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import in.stack.movie.repository.MovieRepository;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class MovieServiceTest {

	@MockBean
	private MovieRepository movieRepository;

	@Autowired
	private MovieService movieService;

	// test case to check movieService class object
	@Test
	@Order(1)
	public void testService() {

		MovieService service = Mockito.mock(MovieService.class);
		assertNotNull(service);
		assertTrue(service instanceof MovieService);

	}

	// test to check movie is saved or not
	@Test
	@Order(2)
	public void positiveTestAddMovie() {

		Movie movie = new Movie("2", "Ariel",
				"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...");

		when(movieRepository.save(ArgumentMatchers.any())).thenReturn(movie);
		Movie b = movieService.addmovie(movie);

		assertEquals(movie.getName(), b.getName());
		assertEquals(movie.getDescription(), b.getDescription());

	}

	// negative test to check movie is saved or not
	@Test
	@Order(3)
	public void negativeTestAddMovie() {

		Movie movie = new Movie("2", "Ariel",
				"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...");

		when(movieRepository.save(ArgumentMatchers.any())).thenReturn(null);
		Movie m = movieService.addmovie(movie);

		assertNull(m);

	}

	// test to check it is returning list of movies or not
	@Test
	@Order(4)
	public void testGetAllFavouriteMovies() {

		List<Movie> m = new ArrayList<Movie>();
		Movie movie = new Movie("2", "Ariel",
				"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...");
		m.add(movie);

		when(movieRepository.findAll()).thenReturn(m);
		List<Movie> result = movieService.getAllFavouriteMovies();

		assertTrue(result.size() == 1);
		assertTrue(result.contains(movie));

	}

	// negative test to check it is returning list of movies or not
	@Test
	@Order(5)
	public void negativetestGetAllFavouriteMovies() {

		List<Movie> m = new ArrayList<Movie>();
		Movie movie = new Movie("2", "Ariel",
				"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...");
		m.add(movie);

		when(movieRepository.findAll()).thenReturn(null);
		List<Movie> result = movieService.getAllFavouriteMovies();
		assertNull(result);

	}

}
