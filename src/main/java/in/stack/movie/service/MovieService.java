package in.stack.movie.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.stack.movie.entity.Movie;
import in.stack.movie.repository.MovieRepository;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;

	public Movie addmovie(Movie movie) {
		return movieRepository.save(movie);
	}

	public List<Movie> getAllFavouriteMovies() {
		return movieRepository.findAll();
	}
}
