/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.loriamusic.apicall;

import com.mycompany.loriamusic.DAO.ArtistDAO;
import com.mycompany.loriamusic.DAO.GenreDAO;
import com.mycompany.loriamusic.DAO.TrackDAO;
import com.mycompany.loriamusic.entity.Artist;
import com.mycompany.loriamusic.entity.Genre;
import com.mycompany.loriamusic.entity.Track;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ImportData {
//    @Autowired
//    TrackDAO trackDao;
//
//    @Autowired
//    ArtistDAO artistDao;
//
//    @Autowired
//    GenreDAO genreDao;
//    
//    @PostConstruct
//    public void importData(){
//        List<String> result = new ArrayList<>();
//        try {
//            String FILE_NAME = "./src/main/resources/data/musiques.csv";
//            final File f = new File(""); 
//            String dossierPath = f.getAbsolutePath() + File.separator + FILE_NAME;
//            File file = new File(dossierPath);
//
//            FileReader fr = new FileReader(file);
//            BufferedReader br = new BufferedReader(fr);
//
//            for (String line = br.readLine(); line != null; line = br.readLine()) {
//                result.add(line);
//            }
//
//            br.close();
//            fr.close();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ImportData.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(ImportData.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        for (String line : result) {
//            Thread t = new Thread();
//            try {
//                double random = Math.random()*(3600 - 1000) + 1000;
//                t.sleep((long) random);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(ImportData.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            
//            String[] entries = line.split(";");
//            String titleTrack = entries[0].replace("\"", "");
//            String nameArtist = entries[1].replace("\"", "");
//
//            Spotify spotify = new Spotify();
//            
//            Artist artist = artistDao.getById(nameArtist); 
//
//            if (artist == null) {
//                artist = new Artist();
//                artist.setNom(nameArtist);
//                ArrayList data = spotify.getArtistMetadata(artist, nameArtist, titleTrack);
//                artist = (Artist) data.get(1);
//
//                Set<Genre> genresArtist = new HashSet<Genre>();
//                ArrayList<Genre> genres = (ArrayList<Genre>) data.get(0);
//                if (genres != null) {
//                    for (Genre g : genres) {
//                        Genre exist = genreDao.getById(g.getNom()); 
//
//                        if (exist == null) {
//                            genreDao.create(g); 
//                        }
//
//                        genresArtist.add(g);
//                    }
//                }
//                artist.setGenres(genresArtist);
//                artistDao.create(artist); 
//            }
//
//            Track track = trackDao.searchTrack(titleTrack, nameArtist); 
//
//            if (track == null) {
//                if (Youtube.search(nameArtist + " " + titleTrack) != null) {
//                    track = new Track();
//                    track.setTitre(titleTrack);
//                    track.setArtist(artist);
//
//                    // id youtube
//                    track.setId_track(Youtube.search(nameArtist + " " + titleTrack));
//
//                    // info spotify
//                    track = spotify.getTrackMetadata(track, nameArtist, titleTrack);
//
//                    trackDao.create(track); 
//                }
//            } else if (track.getId_track() != Youtube.search(track.getId_track())) {
//                track.setId_track(Youtube.search(nameArtist + " " + titleTrack));
//                trackDao.update(track); 
//            }
//        }
//    }
}
