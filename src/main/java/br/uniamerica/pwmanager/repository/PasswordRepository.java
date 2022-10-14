package br.uniamerica.pwmanager.repository;

import br.uniamerica.pwmanager.entity.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "passwords", path = "passwords")
@CrossOrigin
public interface PasswordRepository extends SoftDeleteRepository<Password, Long> {
}
