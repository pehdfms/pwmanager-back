package br.uniamerica.pwmanager.service;

import br.uniamerica.pwmanager.entity.User;
import br.uniamerica.pwmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> findById(Long id) {
        return this.userRepository.findById(id);
    }

    public Page<User> listAll(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }

    public Optional<User> insert(User user) {
        return Optional.ofNullable(this.saveTransactional(user));
    }

    @Transactional
    protected User saveTransactional(User user) {
        return this.userRepository.save(user);
    }
}
