/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.loriamusic.apicall;

import com.mycompany.loriamusic.entity.Artist;
import com.mycompany.loriamusic.entity.Genre;
import com.mycompany.loriamusic.entity.Track;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Amaury
 */
public class Spotify {


    /*
    Id LORIA musicn ne pas utiliser ces id avant le 13/01/2017 Merci
     */
    String clientId = "f92e057245f242a9bb188b02aecf121a";
    String secretId = "e24901b28cb442db86c6265406f265f6";

    //on passe un vieux token, qui est expir� et comme �a on en apppel un nouveau apr�s
    String token;

    public Spotify() {
        getToken();
    }

    public ArrayList<String> getIdtrack(String artistName, String trackName) {
        String trackId = "";
        String artistId = "";
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String url = "https://api.spotify.com/v1/search?q=" + URLEncoder.encode((artistName + " " + trackName), "UTF-8") + "&type=track";
            String USER_AGENT = "Mozilla/5.0";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // optional default is GET
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            System.out.println("code api " + responseCode);
            String line;
            String json = "";
            int test = 0;
            while ((line = br.readLine()) != null) {
                test++;
                json += line;
                //System.out.println(line);
            }
            br.close();
            if (responseCode == 200 && test > 11) {
                JSONObject jsonObj = new JSONObject(json);
                JSONObject jsonTracks = jsonObj.getJSONObject("tracks");
                //System.out.println("taille du Json " + jsonTracks.length());
                JSONArray jsonItems = jsonTracks.getJSONArray("items");
                //System.out.println("nb d'item trouv�s " + jsonItems.length());
                JSONObject firstTrack = (JSONObject) jsonItems.get(0);
                //System.out.println(jsonItems.get(0));
                JSONArray artistsFoundArray = firstTrack.getJSONArray("artists");
                JSONObject firstArtist = (JSONObject) artistsFoundArray.get(0);
                //System.out.println("artist id " + firstArtist.get("id"));
                //System.out.println("track id " + firstTrack.get("id"));
                trackId = firstTrack.get("id").toString();
                artistId = firstArtist.get("id").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<String> toReturn = new ArrayList<>();
        toReturn.add(artistId);
        toReturn.add(trackId);
        return toReturn;
    }

    public Track getTrackMetadata(Track trackToUpdate, String artistName, String trackName) {
        String idTrack = getIdtrack(artistName, trackName).get(1);
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String url = "https://api.spotify.com/v1/audio-features/" + idTrack;
            String USER_AGENT = "Mozilla/5.0";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // optional default is GET
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Authorization", "Bearer " + this.token);
            con.setRequestProperty("Accept", "application/json");
            int responseCode = con.getResponseCode();
            System.out.println("code api " + responseCode);
            if (responseCode == 401) {
                System.out.println("le token est invalid, on en prend un nouveau");
                getToken();
                con = (HttpURLConnection) obj.openConnection();
                // optional default is GET
                con.setRequestMethod("GET");
                //add request header
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Authorization", "Bearer " + this.token);
                con.setRequestProperty("Accept", "application/json");
                responseCode = con.getResponseCode();
                System.out.println("code api " + responseCode);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            String json = "";
            int test = 0;
            while ((line = br.readLine()) != null) {
                test++;
                json += line;
            }
            br.close();
            JSONObject jsonObj = new JSONObject(json);
            trackToUpdate.setAcousticness(jsonObj.getDouble("acousticness"));
            trackToUpdate.setDanceability(jsonObj.getDouble("danceability"));
            trackToUpdate.setDuration_ms(jsonObj.getInt("duration_ms"));
            trackToUpdate.setEnergy(jsonObj.getDouble("energy"));
            trackToUpdate.setInstrumentalness(jsonObj.getDouble("instrumentalness"));
            trackToUpdate.setKey(jsonObj.getInt("key"));
            trackToUpdate.setLiveness(jsonObj.getDouble("liveness"));
            trackToUpdate.setLoudness(jsonObj.getDouble("loudness"));
            trackToUpdate.setMode(jsonObj.getInt("mode"));
            trackToUpdate.setSpeechiness(jsonObj.getDouble("speechiness"));
            trackToUpdate.setTempo(jsonObj.getDouble("tempo"));
            trackToUpdate.setTime_signature(jsonObj.getInt("time_signature"));
            trackToUpdate.setValence(jsonObj.getDouble("valence"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trackToUpdate;
    }

//    public void getTrackInfo(String trackId) {
//        String trackName = "";
//        String artistName = "";
//        try {
//            Thread.sleep(200);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            String url = "https://api.spotify.com/v1/tracks/" + trackId;
//            String USER_AGENT = "Mozilla/5.0";
//            URL obj = new URL(url);
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//            // optional default is GET
//            con.setRequestMethod("GET");
//            //add request header
//            con.setRequestProperty("User-Agent", USER_AGENT);
//            con.setRequestProperty("Authorization", "Bearer " + this.token);
//            con.setRequestProperty("Accept", "application/json");
//            int responseCode = con.getResponseCode();
//            System.out.println("code api " + responseCode);
//            if (responseCode == 401) {
//                System.out.println("le token est invalid, on en prend un nouveau");
//                getToken();
//                con = (HttpURLConnection) obj.openConnection();
//                // optional default is GET
//                con.setRequestMethod("GET");
//                //add request header
//                con.setRequestProperty("User-Agent", USER_AGENT);
//                con.setRequestProperty("Authorization", "Bearer " + this.token);
//                con.setRequestProperty("Accept", "application/json");
//                responseCode = con.getResponseCode();
//                System.out.println("code api " + responseCode);
//            }
//            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String line;
//            String json = "";
//            while ((line = br.readLine()) != null) {
//                //System.out.println(line);
//                json += line;
//            }
//            br.close();
//            JSONObject response = new JSONObject(json);
//            JSONArray artistArray = response.getJSONArray("artists");
//            JSONObject artist = artistArray.getJSONObject(0);
//            artistName = artist.get("name").toString();
//            trackName = response.getString("name").toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        musicToYoutube(artistName, trackName);
//    }

    public ArrayList getArtistMetadata(Artist artistToUpdate, String artistName, String trackName) {
        String idArtist = getIdtrack(artistName, trackName).get(0);
        ArrayList genresArtist = new ArrayList();
        
        try {
            String url = "https://api.spotify.com/v1/artists/" + idArtist;
            String USER_AGENT = "Mozilla/5.0";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // optional default is GET
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            System.out.println("code api " + responseCode);
            String line;
            String json = "";
            while ((line = br.readLine()) != null) {
                json += line;
                //System.out.println(line);
            }
            br.close();
            JSONObject results = new JSONObject(json);
            JSONArray genres = results.getJSONArray("genres");
            for (int i = 0; i < genres.length(); i++) {
                Genre g = new Genre();
                g.setName(genres.getString(i));
                genresArtist.add(g);
            }
            artistToUpdate.setPopularity(results.getInt("popularity"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        ArrayList res = new ArrayList();
        res.add(genresArtist);
        res.add(artistToUpdate);
        return res;
    }

//    public Artist getRelatedArtist(Artist artistToUpdate) {
//        try {
//            Thread.sleep(200);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            String url = "https://api.spotify.com/v1/artists/" + artistToUpdate.getIdSpotify() + "/related-artists";
//            String USER_AGENT = "Mozilla/5.0";
//            URL obj = new URL(url);
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//            // optional default is GET
//            con.setRequestMethod("GET");
//            //add request header
//            con.setRequestProperty("User-Agent", USER_AGENT);
//            con.setRequestProperty("Authorization", "Bearer " + this.token);
//            con.setRequestProperty("Accept", "application/json");
//            int responseCode = con.getResponseCode();
//            System.out.println("code api " + responseCode);
//            if (responseCode == 401) {
//                System.out.println("le token est invalid, on en prend un nouveau");
//                getToken();
//                con = (HttpURLConnection) obj.openConnection();
//                // optional default is GET
//                con.setRequestMethod("GET");
//                //add request header
//                con.setRequestProperty("User-Agent", USER_AGENT);
//                con.setRequestProperty("Authorization", "Bearer " + this.token);
//                con.setRequestProperty("Accept", "application/json");
//                responseCode = con.getResponseCode();
//                System.out.println("code api " + responseCode);
//            }
//            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String line;
//            String json = "";
//            int test = 0;
//            while ((line = br.readLine()) != null) {
//                test++;
//                json += line;
//                //System.out.println(line);
//            }
//            br.close();
//            JSONObject jsonObj = new JSONObject(json);
//            JSONArray artists = jsonObj.getJSONArray("artists");
//            JSONObject relatedArtist;
//            for (int i = 0; i < artists.length(); i++) {
//                relatedArtist = artists.getJSONObject(i);
//                artistToUpdate.getRelatedArtistsSpotifyIdSpotify().add(relatedArtist.getString("id"));
//                artistToUpdate.getRelatedArtistsSpotifyName().add(relatedArtist.getString("name"));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return artistToUpdate;
//    }
//
//    public void getRecommendationFromSeed(SpotifyRecommendationParameter param) {
//        try {
//            Thread.sleep(200);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            String url = "https://api.spotify.com/v1/recommendations?" + param.getParamUrl();
//            String USER_AGENT = "Mozilla/5.0";
//            URL obj = new URL(url);
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//            // optional default is GET
//            con.setRequestMethod("GET");
//            //add request header
//            con.setRequestProperty("User-Agent", USER_AGENT);
//            con.setRequestProperty("Authorization", "Bearer " + this.token);
//            con.setRequestProperty("Accept", "application/json");
//            int responseCode = con.getResponseCode();
//            System.out.println("code api " + responseCode);
//            if (responseCode == 401) {
//                System.out.println("le token est invalid, on en prend un nouveau");
//                getToken();
//                con = (HttpURLConnection) obj.openConnection();
//                // optional default is GET
//                con.setRequestMethod("GET");
//                //add request header
//                con.setRequestProperty("User-Agent", USER_AGENT);
//                con.setRequestProperty("Authorization", "Bearer " + this.token);
//                con.setRequestProperty("Accept", "application/json");
//                responseCode = con.getResponseCode();
//                System.out.println("code api " + responseCode);
//            }
//            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String line;
//            String json = "";
//            int test = 0;
//            while ((line = br.readLine()) != null) {
//                test++;
//                json += line;
//            }
//            br.close();
//            JSONObject response = new JSONObject(json);
//            System.out.println("taille json " + response.length());
//            JSONArray trackList = response.getJSONArray("tracks");
//            System.out.println("nb reco " + trackList.length());
//            JSONObject reco;
//            for (int i = 0; i < trackList.length(); i++) {
//                reco = trackList.getJSONObject(i);
//            }
//            getTrackInfo(trackList.getJSONObject(0).getString("id"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void getToken() {
        System.out.println("obtention d'un token");
        try {
            String codeBase64 = "OWRlNmExMmQzODEwNDg4OTgwNDNmZjU3MzJjNzE5ZmY6NjY0Mzk1YTA1OTY2NDlkZDk2MWU3NDMyYWY1Zjk3M2Y=";
            String url = "https://accounts.spotify.com/api/token";
            String USER_AGENT = "Mozilla/5.0";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // optional default is GET
            con.setRequestMethod("POST");
            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Authorization", "Basic " + codeBase64);
            con.setRequestProperty("Accept", "application/json");
            String urlParameters = "grant_type=client_credentials";
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            System.out.println("code api " + responseCode);

            if (responseCode == 200) {
                System.out.println("obtention token r�ussie");
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                String json = "";
                while ((line = br.readLine()) != null) {
                    //System.out.println(line);
                    json += line;
                }
                br.close();
                JSONObject response = new JSONObject(json);
                this.token = response.getString("access_token");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
