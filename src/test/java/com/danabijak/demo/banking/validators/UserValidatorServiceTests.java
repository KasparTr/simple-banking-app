package com.danabijak.demo.banking.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.danabijak.demo.banking.core.ValidationReport;
import com.danabijak.demo.banking.domain.users.entity.User;
import com.danabijak.demo.banking.domain.users.repositories.UserRepository;
import com.danabijak.demo.banking.domain.users.validators.UserValidatorServiceImpl;
import com.danabijak.demo.banking.domain.users.valueobjects.UserRequest;

public class UserValidatorServiceTests {
	
	// Not using BeforeClass here because static methods don't work with @Autowired
	private static boolean setUpIsDone = false;
	private static String EXISTING_USERNAME = "existing@gmail.com";
	private static String NOT_FOUND_USERNAME = "notFoundUser@gmail.com";
	
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	@Resource
	private UserValidatorServiceImpl uvs = new UserValidatorServiceImpl();
	
	@org.junit.Before
	public void setUp() throws Exception {
		// Initialize mocks created above
	    MockitoAnnotations.initMocks(this);
	    
	    
	    // Change Mocks behavior for user queries
	    User nUser = new User("existing@gmail.com", "exit342User@Psw");
	    when(userRepository.findByUsername(EXISTING_USERNAME)).thenReturn(Optional.of(nUser));
	    
	    Optional<User> emptyUser = Optional.empty();
	    when(userRepository.findByUsername(NOT_FOUND_USERNAME)).thenReturn(emptyUser);
	    this.setUpIsDone = true;
	}
	
	@Test
	public void testGetPasswordFaults_fauly_password(){
		User user = new User("username@email.com", "fpass");
		List<String> faults= uvs.getPasswordFaults(user.getPassword());
		assertTrue(faults.size() > 0);
	}
	
	@Test
	public void testGetPasswordFaults_correct_password(){
		User user = new User("username@email.com", "fpa23Fdss@sdSF3");
		List<String> faults= uvs.getPasswordFaults(user.getPassword());
		assertTrue(faults == null);
	}
	
	@Test
	public void testIsUsernameValid_fauly_username(){
		User user = new User("notcorrectusername", "fpa23Fdss@sdSF3");
		assertFalse(uvs.isUsernameValid(user.getUsername()));
	}
	
	@Test
	public void testIsUsernameValid_correct_username(){
		User user = new User("correctusername@gmail.com", "fpa23Fdss@sdSF3");
		assertTrue(uvs.isUsernameValid(user.getUsername()));
	}

	@Test
	public void testIsUserTaken_taken_username(){
		UserRequest user = new UserRequest(EXISTING_USERNAME, "fpa23Fdss@sdSF3");
		assertTrue( uvs.isUserTaken(user));
	}
	
	@Test
	public void testIsUserTaken_available_username(){
		UserRequest user = new UserRequest(NOT_FOUND_USERNAME, "fpa23Fdss@sdSF3");
		assertFalse( uvs.isUserTaken(user));
	}

}
