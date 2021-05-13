package in.stack.movie.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ModelTest {

	// test case to check movie object
	@Test
	public void testMovie() {
		Movie movie = new Movie("2", "Ariel",
				"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...");
		assertEquals("Ariel", movie.getName());
	}

	// test case to check user object
	@Test
	public void testUser() {
		User user = new User();
		user.setId(1);
		user.setUsername("user");
		user.setPassword("pass");
		user.setEmail("email@gmail.com");
		user.setCreatedAt(new Date());
		assertEquals("user", user.getUsername());
	}

}
