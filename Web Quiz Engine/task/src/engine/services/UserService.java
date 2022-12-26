package engine.services;

import engine.exeptions.UserWasRegister;
import engine.models.User;
import engine.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            return userRepository.findByEmail(username);
        } catch (Exception e){
            throw new UsernameNotFoundException(
                    String.format("No user %s found", username));
        }
    }

    public Long registerNewUser(String username, String password) {
        try {
            var encodedPassword = encoder.encode(password);
            var user = new User(username, encodedPassword);
            return userRepository.save(user).getId();
        } catch (Exception e) {
            throw new UserWasRegister();
        }
    }
}
