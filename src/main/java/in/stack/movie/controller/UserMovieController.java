package in.stack.movie.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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
	public User addMovieToFavs(@PathVariable("movieId") String movieId, HttpSession session) {
		MovieSummary movieSummary = restTemplate.getForObject(
				"https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey, MovieSummary.class);
		Movie movie = new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview());
		movieService.addmovie(movie);
		User u = (User) session.getAttribute("user");
		User user = userMovieService.getUser(u.getId());
		List<Movie> favMovies = user.getFavoriteMovies();
		favMovies.add(movie);
		user.setFavoriteMovies(favMovies);
		return userMovieService.addMovie(user.getId(), movieId);
	}

	// getting favorite movie list by userId
	@GetMapping("/all")
	public List<Movie> getMovies(HttpSession session) {
		User user = (User) session.getAttribute("user");
		return userMovieService.getMoviesbyUserId(user.getId());
	}

	// deleting particular movie by movieId of particular user favorite list
	@DeleteMapping("/{movieId}")
	public Movie deleteByMovieIds(@PathVariable("movieId") String movieId, HttpSession session) {
		User user1 = (User) session.getAttribute("user");
		return userMovieService.deleteByMovieId(user1.getId(), movieId);
	}

}
