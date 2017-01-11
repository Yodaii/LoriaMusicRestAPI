package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TrackResource extends JpaRepository<Track, String>{
    // GET
    // POST
    // PATCH
    // DELETE
}