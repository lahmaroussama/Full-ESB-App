package tn.besoftilys.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tn.besoftilys.user.entity.User;
import tn.besoftilys.user.payload.request.LoginRequest;
import tn.besoftilys.user.payload.request.SignupRequest;

import java.util.List;
import java.util.Optional;

public interface IUser {
    ResponseEntity<?> authenticateUser(LoginRequest loginRequest);
    ResponseEntity<?> registerUser(SignupRequest signUpRequest);
    ResponseEntity<List<User>>getAllUsers();
    ResponseEntity<HttpStatus>deleteUser(Long id);
    Optional<User> getCurrentUser();


}
