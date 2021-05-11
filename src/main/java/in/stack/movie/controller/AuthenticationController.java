package in.stack.movie.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import in.stack.movie.entity.AuthenticationRequest;
import in.stack.movie.entity.AuthenticationResponse;
import in.stack.movie.entity.MovieUserDetails;
import in.stack.movie.entity.User;
import in.stack.movie.repository.UserRepository;
import in.stack.movie.service.MovieUserDetailsService;
import in.stack.movie.util.JwtUtil;

@RestController
public class AuthenticationController {

	@Autowired
	private MovieUserDetailsService movieUserDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody MovieUserDetails user) throws Exception {
		return ResponseEntity.ok(movieUserDetailsService.saveUser(user));
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(
			@RequestBody AuthenticationRequest authenticationRequest, HttpSession session) throws Exception {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));

		} catch (DisabledException e) {
			throw new Exception("User Disabled!", e);
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password!", e);
		}

		final UserDetails userDetails = movieUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		User user = userRepository.findByUsername(authenticationRequest.getUsername());

		session.setAttribute("user", user);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
