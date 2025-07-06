package tn.besoftilys.user.controller;

//import com.oracle.svm.core.annotate.Delete;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.besoftilys.user.entity.User;
import tn.besoftilys.user.payload.request.LoginRequest;
import tn.besoftilys.user.payload.request.SignupRequest;
import tn.besoftilys.user.repository.UserRepository;
import tn.besoftilys.user.service.IUser;

import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    IUser iUser;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        return iUser.authenticateUser(loginRequest);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest){
        return iUser.registerUser(signUpRequest);
    }
    @GetMapping("/getAllUsers")
    ResponseEntity<List<User>>getAllMessages(){
        return iUser.getAllUsers();
    }

   /* @RolesAllowed("ROLE_ADMIN")
    @Delete("/deleteUser")
    ResponseEntity<HttpStatus>deleteUser(@PathVariable Long id){
        return iUser.deleteUser(id);
    }*/

    @GetMapping("/currentUser")
    public Optional<User> getUser() {
        return iUser.getCurrentUser();
    }

}
