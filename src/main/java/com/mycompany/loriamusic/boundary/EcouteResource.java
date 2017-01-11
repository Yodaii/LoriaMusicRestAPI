package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.entity.Ecoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EcouteResource extends JpaRepository<Ecoute, Long>{
    // GET
    // POST
    // PATCH
    // DELETE
}