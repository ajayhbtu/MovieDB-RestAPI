package in.stack.movie.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.stack.movie.entity.Movie;
import in.stack.movie.entity.User;
import in.stack.movie.repository.MovieRepository;
import in.stack.movie.repository.UserRepository;

@Service
public class UserMovieService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MovieRepository movieRepository;

	public User getUser(int userId) {
		return userRepository.findById(userId);
	}

	public User addMovie(int userId, String movieId) {
		User user = userRepository.findById(userId);
		return userRepository.save(user);
	}

	public List<Movie> getMoviesbyUserId(int userId) {
		User user = userRepository.findById(userId);
		return user.getFavoriteMovies();
	}

	public Movie deleteByMovieId(int userId, String movieId) {

		User user = userRepository.findById(userId);
		List<Movie> movies = user.getFavoriteMovies();
		Movie movie = movieRepository.findById(movieId).orElse(null);
		movies.remove(movie);
		user.setFavoriteMovies(movies);
		userRepository.save(user);
		return movie;
	}
}
