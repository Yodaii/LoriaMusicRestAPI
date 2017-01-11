package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArtistResource extends JpaRepository<Artist, String>{
    // GET
    // POST
    // PATCH
    // DELETE
}