
package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GenreResource extends JpaRepository<Genre, String>{
    // GET
    // POST
    // PATCH
    // DELETE
}