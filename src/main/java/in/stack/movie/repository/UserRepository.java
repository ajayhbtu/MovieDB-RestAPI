package in.stack.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.stack.movie.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUsername(String username);
	User findById(int id);
}
