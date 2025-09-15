package vm.erik.sectors.repository;

import vm.erik.sectors.model.User;

import java.util.List;
import java.util.Optional;


public interface UserRepository {




    void saveUser(User user);


    List<User> findAll();


    boolean isUsernameTaken(String username);

    boolean isEmailTaken(String email);


    User findById(Long userId);

    User findByEmail(String email);
    User findByUsername(String username);
}
