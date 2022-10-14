package br.uniamerica.pwmanager.service;

import br.uniamerica.pwmanager.entity.User;
import br.uniamerica.pwmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthenticationService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = this.userRepository
                .findFirstByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " couldn't be located.");
        }

        return User.build(user);
    }
}
