package in.stack.movie.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.stack.movie.entity.AuthenticationRequest;
import in.stack.movie.entity.AuthenticationResponse;
import in.stack.movie.entity.MovieUserDetails;
import in.stack.movie.entity.User;
import in.stack.movie.repository.UserRepository;
import in.stack.movie.service.MovieUserDetailsService;
import in.stack.movie.service.UserMovieService;
import in.stack.movie.util.FileUploadUtil;
import in.stack.movie.util.JwtUtil;
import io.swagger.annotations.Api;

@RestController
@Api(value="Authentication Service", description="Authentication micro service")
public class AuthenticationController {

	@Autowired
	private MovieUserDetailsService movieUserDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserMovieService userMovieService;
	
	@Value("${file.upload-dir}")
	private String uploadDir;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody MovieUserDetails user) throws Exception {
		return ResponseEntity.ok(movieUserDetailsService.saveUser(user));
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
			HttpSession session) throws Exception {

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

	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable("userId") int userId) {
		userRepository.deleteById(userId);
		String result = "User Deleted with id = " + userId;
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@PostMapping("/profile-image")
    public ResponseEntity<?> uploadProfileImage(
            @RequestParam("image") MultipartFile multipartFile, HttpSession session) throws IOException {
         
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        
        User sessionUser = (User) session.getAttribute("user");
		User user = userMovieService.getUser(sessionUser.getId());
		
        user.setProfilePhoto(fileName);
        
        User savedUser = userRepository.save(user);
 
        String userUploadDir = uploadDir + savedUser.getId();
 
        FileUploadUtil.saveFile(userUploadDir, fileName, multipartFile);
         
        return new ResponseEntity<String> ("Profile Image Uploaded Successfully!", HttpStatus.OK);
    }

}
