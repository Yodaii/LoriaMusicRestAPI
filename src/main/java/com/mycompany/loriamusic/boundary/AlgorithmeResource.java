package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.entity.Algorithme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AlgorithmeResource extends JpaRepository<Algorithme, Long>{
    // GET
    // POST
    // PATCH
    // DELETE
}