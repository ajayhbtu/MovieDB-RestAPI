package in.stack.movie.restassured;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import in.stack.movie.entity.AuthenticationRequest;
import in.stack.movie.entity.User;

import io.restassured.response.Response;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class RestAssuredTest {

	private User user;
	private AuthenticationRequest authenticationRequest;
	public static int userId;
	private String baseUrl = "http://localhost:8080/";
	private String endpoint;

	@Test
	@Order(1)
	public void positiveTestRegisterUser() throws Exception {

		endpoint = "api/user/register";
		user = new User();
		user.setUsername("user");
		user.setPassword("pass");
		user.setEmail("email@gmail.com");

		Response response = given().contentType("application/json").body(user).when().post(baseUrl + endpoint).then()
				.extract().response();

		assertEquals(200, response.statusCode());
		assertEquals("user", response.jsonPath().getString("username"));
		userId = response.jsonPath().getInt("id");

	}

	@Test
	@Order(2)
	public void negativeTestRegisterUser() throws Exception {

		endpoint = "api/user/register";
		user = new User();
		user.setUsername("user");
		user.setPassword("pass");
		user.setEmail("email@gamil.com");

		Response response = given().contentType("application/json").body(user).when().post(baseUrl + endpoint).then()
				.extract().response();
		assertEquals(500, response.statusCode());

	}

	@Test
	@Order(3)
	public void positiveTestAuthenticateUser() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		endpoint = "api/user/authenticate";

		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response.statusCode());
		assertNotNull(response.jsonPath().getString("jwt"));

	}

	@Test
	@Order(4)
	public void negativeTestAuthenticateUser() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass1");
		endpoint = "api/user/authenticate";

		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(403, response.statusCode());

	}

	@Test
	@Order(5)
	public void positiveTestGetMovieById() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		endpoint = "api/user/authenticate";
		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response.statusCode());

		String jwt = response.jsonPath().getString("jwt");
		String movieId = "2";
		endpoint = "api/movies/" + movieId;

		Response response1 = given().header("Authorization", "Bearer " + jwt).when().get(baseUrl + endpoint).then()
				.extract().response();
		assertEquals(200, response1.statusCode());
		assertEquals("Ariel", response1.jsonPath().getString("name"));

	}

	@Test
	@Order(6)
	public void negativeTestGetMovieById() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		endpoint = "api/user/authenticate";
		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response.statusCode());

		String jwt = response.jsonPath().getString("jwt");
		String movieId = "1";
		endpoint = "api/movies/" + movieId;

		Response response1 = given().header("Authorization", "Bearer " + jwt).when().get(baseUrl + endpoint).then()
				.extract().response();
		assertEquals(500, response1.statusCode());

	}

	@Test
	@Order(7)
	public void positiveTestGetTopRatedMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		endpoint = "api/user/authenticate";

		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response.statusCode());

		String jwt = response.jsonPath().getString("jwt");
		endpoint = "api/movies/toprated/1";

		Response response1 = given().header("Authorization", "Bearer " + jwt).when().get(baseUrl + endpoint).then()
				.extract().response();
		assertEquals(200, response1.statusCode());
		assertTrue(response1.jsonPath().getList("results").size() > 1);
	}

	@Test
	@Order(8)
	public void negativeTestGetTopRatedMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		endpoint = "api/user/authenticate";

		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response.statusCode());

		String jwt = response.jsonPath().getString("jwt");
		endpoint = "api/movies/toprated/1000";

		Response response1 = given().header("Authorization", "Bearer " + jwt).when().get(baseUrl + endpoint).then()
				.extract().response();
		assertEquals(500, response1.statusCode());

	}

	@Test
	@Order(9)
	public void positiveTestGetPopularMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		endpoint = "api/user/authenticate";

		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response.statusCode());

		String jwt = response.jsonPath().getString("jwt");
		endpoint = "api/movies/popular/1";

		Response response1 = given().header("Authorization", "Bearer " + jwt).when().get(baseUrl + endpoint).then()
				.extract().response();
		assertEquals(200, response1.statusCode());
		assertTrue(response1.jsonPath().getList("results").size() > 1);
	}

	@Test
	@Order(10)
	public void negativeTestGetPopularMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		endpoint = "api/user/authenticate";

		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response.statusCode());

		String jwt = response.jsonPath().getString("jwt");
		endpoint = "api/movies/popular/1000";

		Response response1 = given().header("Authorization", "Bearer " + jwt).when().get(baseUrl + endpoint).then()
				.extract().response();
		assertEquals(500, response1.statusCode());

	}

	@Test
	@Order(11)
	public void positiveTestGetLatestMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		endpoint = "api/user/authenticate";

		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response.statusCode());

		String jwt = response.jsonPath().getString("jwt");
		endpoint = "api/movies/latest";

		Response response1 = given().header("Authorization", "Bearer " + jwt).when().get(baseUrl + endpoint).then()
				.extract().response();
		assertEquals(200, response1.statusCode());
		assertNotNull(response1.jsonPath().getString("movieId"));
		assertNotNull(response1.jsonPath().getString("name"));
		assertNotNull(response1.jsonPath().getString("description"));

	}

	@Test
	@Order(12)
	public void negativeTestGetLatestMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		endpoint = "api/user/authenticate";

		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response.statusCode());

		String jwt = response.jsonPath().getString("jwt");
		endpoint = "api/movies/latests";

		Response response1 = given().header("Authorization", "Bearer " + jwt).when().get(baseUrl + endpoint).then()
				.extract().response();
		assertEquals(500, response1.statusCode());

	}

	@Test
	@Order(13)
	public void positiveTestAddFavoriteMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		endpoint = "api/user/authenticate";

		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response.statusCode());

		String jwt = response.jsonPath().getString("jwt");
		String sessionId = response.sessionId();
		String movieId = "2";
		endpoint = "api/favorites/" + movieId;

		Response response1 = given().header("Authorization", "Bearer " + jwt).contentType("application/json")
				.sessionId(sessionId).when().post(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response1.statusCode());
		assertEquals("user", response1.jsonPath().getString("username"));
		assertNotNull(response1.jsonPath().getString("username"));
		assertTrue(response1.jsonPath().getList("favoriteMovies").size() == 1);

	}

	@Test
	@Order(14)
	public void negativeTestAddFavoriteMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		endpoint = "api/user/authenticate";

		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response.statusCode());

		String jwt = response.jsonPath().getString("jwt");
		String movieId = "2";
		endpoint = "api/favorites/" + movieId;

		Response response1 = given().header("Authorization", "Bearer " + jwt).contentType("application/json").when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(500, response1.statusCode());

	}

	@Test
	@Order(15)
	public void positiveTestGetFavoriteMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		endpoint = "api/user/authenticate";

		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response.statusCode());

		String jwt = response.jsonPath().getString("jwt");
		String sessionId = response.sessionId();
		endpoint = "api/favorites/all";

		Response response1 = given().header("Authorization", "Bearer " + jwt).sessionId(sessionId).when()
				.get(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response1.statusCode());
		assertTrue(response1.jsonPath().getList("movies").size() == 1);

	}

	@Test
	@Order(16)
	public void negativeTestGetFavoriteMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		endpoint = "api/user/authenticate";

		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response.statusCode());

		String jwt = response.jsonPath().getString("jwt");
		String sessionId = response.sessionId();
		endpoint = "api/favourite/alls";

		Response response1 = given().header("Authorization", "Bearer " + jwt).sessionId(sessionId).when()
				.get(baseUrl + endpoint).then().extract().response();
		assertEquals(404, response1.statusCode());

	}

	@Test
	@Order(17)
	public void positiveTestDeleteMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		endpoint = "api/user/authenticate";

		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response.statusCode());

		String jwt = response.jsonPath().getString("jwt");
		String sessionId = response.sessionId();
		String movieId = "2";
		endpoint = "api/favorites/" + movieId;

		Response response1 = given().header("Authorization", "Bearer " + jwt).sessionId(sessionId).when()
				.delete(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response1.statusCode());
		assertEquals("Ariel", response1.jsonPath().getString("name"));

	}

	@Test
	@Order(18)
	public void negativeTestDeleteMovies() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		endpoint = "api/user/authenticate";

		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response.statusCode());

		String jwt = response.jsonPath().getString("jwt");
		String sessionId = response.sessionId();
		String movieId = "2";
		endpoint = "api/favorites/" + movieId;

		Response response1 = given().header("Authorization", "Bearer " + jwt).sessionId(sessionId).when()
				.delete(baseUrl + endpoint).then().extract().response();
		assertEquals(409, response1.statusCode());

	}

	@Test
	@Order(19)
	public void positiveTestDeleteUser() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		endpoint = "api/user/authenticate";

		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(200, response.statusCode());

		String jwt = response.jsonPath().getString("jwt");
		endpoint = "api/user/delete/" + userId;

		Response response1 = given().header("Authorization", "Bearer " + jwt).when().delete(baseUrl + endpoint).then()
				.extract().response();

		assertEquals(200, response1.statusCode());

	}

	@Test
	@Order(20)
	public void negativeTestDeleteUser() throws Exception {

		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("user");
		authenticationRequest.setPassword("pass");
		endpoint = "api/user/authenticate";

		Response response = given().contentType("application/json").body(authenticationRequest).when()
				.post(baseUrl + endpoint).then().extract().response();
		assertEquals(403, response.statusCode());

	}

}
