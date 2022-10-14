package br.uniamerica.pwmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.rest.core.annotation.RestResource;

@NoRepositoryBean
public interface SoftDeleteRepository<T, ID> extends JpaRepository<T, ID> {
    @Override
    @Query("SELECT e FROM #{#entityName} e WHERE e.active = TRUE")
    Page<T> findAll(Pageable pageable);

    @RestResource(path = "inactive", rel = "inactive")
    Page<T> findByActiveIsFalse(Pageable pageable);

    @RestResource(path = "all", rel = "all")
    @Query("select e from #{#entityName} e")
    Page<T> findAllIncludingInactive(Pageable pageable);
}
