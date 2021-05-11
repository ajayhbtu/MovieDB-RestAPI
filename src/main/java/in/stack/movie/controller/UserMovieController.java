package in.stack.movie.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import in.stack.movie.entity.Movie;
import in.stack.movie.entity.MovieSummary;
import in.stack.movie.entity.User;
import in.stack.movie.service.MovieService;
import in.stack.movie.service.UserMovieService;

@RestController
@RequestMapping("/favorites")
public class UserMovieController {

	@Value("${api.key}")
	private String apiKey;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private UserMovieService userMovieService;

	@Autowired
	private MovieService movieService;

	// saving movie in User favorite list by movieId
	@PostMapping("/{movieId}")
	public ResponseEntity<?> addMovieToFavs(@PathVariable("movieId") String movieId, HttpSession session) {

		ResponseEntity<?> responseEntity = null;

		try {

			MovieSummary movieSummary = restTemplate.getForObject(
					"https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey, MovieSummary.class);
			Movie movie = new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview());
			movieService.addmovie(movie);
			User sessionUser = (User) session.getAttribute("user");
			User user = userMovieService.getUser(sessionUser.getId());
			List<Movie> favMovies = user.getFavoriteMovies();
			favMovies.add(movie);
			user.setFavoriteMovies(favMovies);
			User userMovie = userMovieService.addMovie(user.getId(), movieId);
			if (userMovie != null) {
				responseEntity = new ResponseEntity<User>(userMovie, HttpStatus.OK);
			} else {
				responseEntity = new ResponseEntity<String>("Conflict Data", HttpStatus.CONFLICT);
			}

		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return responseEntity;

	}

	// getting favorite movie list by userId
	@GetMapping("/all")
	public ResponseEntity<?> getMovies(HttpSession session) {

		ResponseEntity<?> responseEntity = null;

		try {

			User user = (User) session.getAttribute("user");
			List<Movie> movieList = userMovieService.getMoviesbyUserId(user.getId());
			if (movieList != null) {
				responseEntity = new ResponseEntity<List<Movie>>(movieList, HttpStatus.OK);
			} else {
				responseEntity = new ResponseEntity<String>("Conflict Data", HttpStatus.CONFLICT);
			}

		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return responseEntity;

	}

	// deleting particular movie by movieId of particular user favorite list
	@DeleteMapping("/{movieId}")
	public ResponseEntity<?> deleteByMovieIds(@PathVariable("movieId") String movieId, HttpSession session) {

		ResponseEntity<?> responseEntity = null;

		try {

			User user = (User) session.getAttribute("user");
			Movie movie = userMovieService.deleteByMovieId(user.getId(), movieId);
			if (movie != null) {
				responseEntity = new ResponseEntity<Movie>(movie, HttpStatus.OK);
			} else {
				responseEntity = new ResponseEntity<String>("Conflict Data", HttpStatus.CONFLICT);
			}

		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return responseEntity;

	}

}
