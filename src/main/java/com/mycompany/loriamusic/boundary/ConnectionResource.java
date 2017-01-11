package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.entity.Algorithme;
import com.mycompany.loriamusic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ConnectionResource extends JpaRepository<User, Long>{
    // POST
}
