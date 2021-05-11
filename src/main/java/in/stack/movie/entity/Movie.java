package in.stack.movie.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import javax.persistence.Table;

@Entity
@Table(name = "movies")
public class Movie {

	@Id
	private String movieId;
	private String name;

	@Column(length = 65535, columnDefinition = "text")
	private String description;

	public Movie() {
		super();
	}

	public Movie(String movieId, String name, String description) {
		super();
		this.movieId = movieId;
		this.name = name;
		this.description = description;
	}

	public Movie(String movieId) {
		super();
		this.movieId = movieId;
	}

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
