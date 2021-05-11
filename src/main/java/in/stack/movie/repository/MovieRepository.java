package in.stack.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.stack.movie.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, String> {

}
