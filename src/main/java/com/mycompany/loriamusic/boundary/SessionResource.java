package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SessionResource extends JpaRepository<Session, Long>{
    // GET
    // POST
    // PATCH
    // DELETE
}
