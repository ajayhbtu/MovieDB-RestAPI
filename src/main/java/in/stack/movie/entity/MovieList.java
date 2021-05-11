package in.stack.movie.entity;

import java.util.List;

public class MovieList {

	private int page;
	private List<MovieSummary> results;
	private int total_pages;
	private int total_results;
	
	
	public MovieList() {
		super();
	}


	public MovieList(int page, List<MovieSummary> results, int total_pages, int total_results) {
		super();
		this.page = page;
		this.results = results;
		this.total_pages = total_pages;
		this.total_results = total_results;
	}


	public int getPage() {
		return page;
	}


	public void setPage(int page) {
		this.page = page;
	}


	public List<MovieSummary> getResults() {
		return results;
	}


	public void setResults(List<MovieSummary> results) {
		this.results = results;
	}


	public int getTotal_pages() {
		return total_pages;
	}


	public void setTotal_pages(int total_pages) {
		this.total_pages = total_pages;
	}


	public int getTotal_results() {
		return total_results;
	}


	public void setTotal_results(int total_results) {
		this.total_results = total_results;
	}


	@Override
	public String toString() {
		return "ApiResult [page=" + page + ", results=" + results + ", total_pages=" + total_pages + ", total_results="
				+ total_results + "]";
	}
}
