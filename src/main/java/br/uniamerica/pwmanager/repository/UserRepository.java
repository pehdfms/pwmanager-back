package br.uniamerica.pwmanager.repository;

import br.uniamerica.pwmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
@CrossOrigin
public interface UserRepository extends SoftDeleteRepository<User, Long> {
    User findFirstByUsername(String username);
}
