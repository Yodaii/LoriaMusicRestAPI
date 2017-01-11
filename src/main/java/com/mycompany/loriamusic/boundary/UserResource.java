package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserResource extends JpaRepository<User, Long>{
    // GET
    // POST
    // PATCH
    // DELETE
}