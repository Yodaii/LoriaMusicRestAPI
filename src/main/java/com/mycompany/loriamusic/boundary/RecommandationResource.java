package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.entity.Recommandation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RecommandationResource extends JpaRepository<Recommandation, Long>{
    // GET
    // POST
    // PATCH
    // DELETE
}