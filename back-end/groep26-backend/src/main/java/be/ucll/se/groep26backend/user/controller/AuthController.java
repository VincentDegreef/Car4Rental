package be.ucll.se.groep26backend.user.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import be.ucll.se.groep26backend.Auth.JwtUtil;
import be.ucll.se.groep26backend.request.model.LoginReq;
import be.ucll.se.groep26backend.response.model.ErrorRes;
import be.ucll.se.groep26backend.response.model.LoginRes;
import be.ucll.se.groep26backend.role.model.Role;
import be.ucll.se.groep26backend.role.service.RoleService;
import be.ucll.se.groep26backend.role.service.RoleServiceException;
import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.model.UserRegistrationRequest;
import be.ucll.se.groep26backend.user.service.UserService;
import be.ucll.se.groep26backend.user.service.UserServiceException;
import jakarta.validation.Valid;

@CrossOrigin(origins = {"http://localhost:3000", "https://group26-frontend.azurewebsites.net", "https://group26-ac-frontend.azurewebsites.net", "https://groep26-frontend.azurewebsites.net"})
// @CrossOrigin(origins = "*")
@RestController
@RequestMapping("/rest/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private final AuthenticationManager authenticationManager;


    private JwtUtil jwtUtil;
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST )
    @ExceptionHandler({
    MethodArgumentNotValidException.class})
    public Map<String, String>
    handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST )
    @ExceptionHandler({ UserServiceException.class})
    public Map<String, String>
    handleServiceExceptions(UserServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), ex.getMessage());
        return errors;
    }


    @SuppressWarnings("rawtypes")
    @ResponseBody
    @PostMapping("/login")    
    public ResponseEntity login(@RequestBody LoginReq loginReq) {
    try {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
        
        // Retrieve user details
        String email = authentication.getName();
        User user2 = userService.getUserByEmail(email);
        String token = jwtUtil.createToken(user2);
        long id = user2.getId();
        double balance = user2.getWallet().getBalance();
        

        LoginRes loginRes = new LoginRes(id, email,token, user2.getUsername(), user2.getPhoneNumber(), user2.getRole(), balance);

        return ResponseEntity.ok(loginRes);
    } catch (BadCredentialsException e) {
        // Handle invalid credentials
        ErrorRes errorResponse = new ErrorRes(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    } catch (Exception e) {
        // Handle other exceptions
        ErrorRes errorResponse = new ErrorRes(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
     

   @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRegistrationRequest request) throws UserServiceException, RoleServiceException {
        
        Role role = new Role();

        try {
            role = roleService.getRoleByName(request.getRoleName());
        } catch (RoleServiceException e) {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            //return ResponseEntity.badRequest().body("Role not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        
        
        User newUser = request.getUser();
        newUser.setRole(role);
        String hashedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword( hashedPassword);

        User user = userService.createUser(newUser);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/sendVerificationCode/{email}")
    public int sendVerificationCode(@PathVariable String email) throws UserServiceException {
        return userService.sendVerificationCode(email);
    }
    
    @GetMapping("/verifyCode/{email}/{code}")
    public User verifyCode(@PathVariable String email, @PathVariable int code) throws UserServiceException {
        return userService.verifyCode(email, code);
    }

    @GetMapping("/checkUserVerified/{email}")
    public boolean checkUserVerified(@PathVariable String email) throws UserServiceException {
        return userService.checkIfUserIsVerified(email);
    }

}