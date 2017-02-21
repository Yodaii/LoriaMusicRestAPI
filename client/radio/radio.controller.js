(function () {
  'use strict';

var key = '2776b29967e1bfd49cfc90686cd48b6c';
var lastfm = 'http://ws.audioscrobbler.com/2.0/?api_key=' + key + '&format=json';

  angular
  .module('app')
  .controller('RadioController', RadioController);


  RadioController.$inject = ['LastFMAPI','UserService', '$rootScope','$http','$scope','autoComplete','$location'];
  function RadioController(LastFMAPI,UserService, $rootScope,$http,$scope,autoComplete,$location) {
    console.log("rad controller loeaded once")
      var player;
      var onYouTubeIframeAPIReady = onYouTubeIframeAPIReady;
      var changeVid = changeVid;
      var algoChoisi;
      var vm = this;
      var done = false;
      $scope.searchText = "";
      $scope.Person = [];
      $scope.selectedItem = [];
      $scope.isDisabled = false;
      $scope.noCache = false;
      var vids= [];
      var nomAlgoChoisi = "";

      // Catch the item selected by the user
      // For further information check : https://material.angularjs.org/latest/api/directive/mdAutocomplete
      $scope.selectedItemChange = function (item) {
          //alert("Item Changed");
      }
      $scope.searchTextChange = function (str) {

      return autoComplete.SearchBarAutoComplete(str);
      }
      // API call when the user hit the like button
      $scope.like=function(){
        vm.showLike = false;
         $http({
           
              method: 'PUT',
              url: 'http://localhost:8080/listening/'+$rootScope.globals.currentUser.username+'/'+$rootScope.form_data.id_track+'/'+'true'
          }).then(function(response){
          },function(error){
            console.log("Erreur dans sauvegarde du choix like");
          });
      }
      // API call when the user hit the dislike button
      $scope.unlike=function(){
        vm.showLike = false;
        $http({
              method: 'PUT',
              url: 'http://localhost:8080/listening/'+$rootScope.globals.currentUser.username+'/'+$rootScope.form_data.id_track+'/'+'false'
          }).then(function(response){
          },function(error){
            console.log("Erreur dans sauvegarde du choix de ban");
          });
      }
      // API call when the user hit the next button, then load the next vid
      $scope.next = function(){
        console.log(player);
        var time = player.getCurrentTime();
        $http({
              method: 'GET',
              url: 'http://localhost:8080/listening/'+$rootScope.globals.currentUser.username+'/'+$rootScope.form_data.id_track+'/'+time
          }).then(function(response){
            console.log("Sauvegarde du temps d'écoute de la musique ok");
          },function(error){
            console.log("Erreur dans sauvegarde du temps d'écoute de la musique");
          });
      console.log(algoChoisi);
      if(algoChoisi === 1)
      {
        loadNextByAlgoIg(3);
      } else if (algoChoisi === 2){
        loadNextByAlgoIg(6);
      } else {
        loadNextByAlgoIg(0);
      }

        console.log(player.getCurrentTime());
      }
      // When the page is loaded for the first time, check if a search has been set
    if( $rootScope.form_data && $rootScope.form_data.name!=undefined )
    {
      $rootScope.globals.type = "radio";
      // Get the algo used for the user in radio mode
      algoChoisi = $rootScope.idAlgoRadioUser;
      vm.dataLoading = true;

      var name = $rootScope.form_data.name ;
      var artist = $rootScope.form_data.artist;
      // get the video from our API with the text search
      getYoutubeId(name,artist,function (response){
        if (response.success) {
          if(vids)
          {
            vids = [];
          }
          
          $http({
              method: 'GET',
              url: 'http://localhost:8080/reco/'+$rootScope.globals.currentUser.username+'/'+response.videoId
          }).then(function(response) {
            
            response.data.forEach(function(data) {
            var add = {name: data.track.title , artist : data.track.artist.name, id_track : data.track.id_track ,nomAlgo : data.nameAlgorithm};
            vids.push(add);
            

          });
            if(algoChoisi == 1){
              nomAlgoChoisi = vids[0].nomAlgo;
            } else if(nomAlgoChoisi == 2){
              nomAlgoChoisi = vids[2].nomAlgo;
            } else {
              nomAlgoChoisi = vids[5].nomAlgo;
            }
            vm.recoLoading=false;
            response = { success: true, titre: response.data.titre , videoId: response.data.id_track };
            
        }, function(reason) {
            response = { success: false, message: 'Impossible de charger la vidéo' };
            
        });
        // if the player already exist, reload with the new video id
        if(player){
          console.log("player exist, modify");
          changeVid(response.videoId);
          $rootScope.form_data.id_track = response.videoId;
          
        }
        // if the player don't exist, create it with the video id
        else{
          vm.showLike = true;
          vm.dataLoading = false;
          vm.reco=true;
          onYouTubeIframeAPIReady(response.videoId);
          $rootScope.form_data.id_track = response.videoId;

        }
                } else {
                  vm.showLike = true;
                  vm.dataLoading = false;
                  vm.reco=true;
                }
      });
    }
    // When the user search from this page
    $(function() {    
      $("form").on("submit", function(e) {
        $rootScope.globals.type = "radio";
       e.preventDefault();
       // Check if item selected or not
        if($scope.selectedItem)
      {
        $rootScope.form_data.name = $scope.selectedItem.name;
        $rootScope.form_data.artist = $scope.selectedItem.artist;
        loadVid();
      }
      //if not, call lastfm API to correct it
      else
      {
        LastFMAPI.search($scope.searchText, function(response){
          if(response.success)
          {
            $rootScope.form_data.name =response.name;
            $rootScope.form_data.artist = response.artist;
            loadVid();

          }
          else
          {
            $rootScope.form_data.name =null;
            $rootScope.form_data.artist = null;
            alert("votre recherche n'a renvoyer aucun résultat");
          }
        });
      }
        
       
     });
    }); 
    // function called to load video only if the title is found on the API, to be able to catch if the user selected an item and wrote something else, leading to an inconclusive result
    function loadVid(){
      getYoutubeId($rootScope.form_data.name,$rootScope.form_data.artist,function (response){
        if (response.success) {
          vm.reco=true;
          vm.recoLoading=true;
          if(vids)
          {
            vids = [];
          }
          $http({
              method: 'GET',
              url: 'http://localhost:8080/reco/'+$rootScope.globals.currentUser.username+'/'+response.videoId
          }).then(function(response) {
            
            response.data.forEach(function(data) {
            var add = {name: data.track.title , artist : data.track.artist.name, id_track : data.track.id_track ,nomAlgo : data.nameAlgorithm};
            vids.push(add);

          });
            // Set name of the choosen algo to send it to our API calls
            if(algoChoisi == 1){
              nomAlgoChoisi = vids[0].nomAlgo;
            } else if(nomAlgoChoisi == 2){
              nomAlgoChoisi = vids[2].nomAlgo;
            } else {
              nomAlgoChoisi = vids[5].nomAlgo;
            }

            vm.recoLoading=false;
            vm.showLike = true;
            response = { success: true, titre: response.data.titre , videoId: response.data.id_track };
            
        }, function(reason) {
            response = { success: false, message: 'Impossible de charger la vidéo' };
            
        });
          
        if(player){
          console.log("player exist, modify");
          changeVid(response.videoId);
          $rootScope.form_data.id_track = response.videoId;
          
        }
        else{
          console.log("player don't exist, create");
          onYouTubeIframeAPIReady(response.videoId);
          $rootScope.form_data.id_track = response.videoId;

        }
        
        } else {
                  console.log("Erreur de communication avec youtube, faites une nouvelle recherche");
                }
      });
    }
    // Call our API to get the id corresponding with the title and the artist in parameters, if not exist, add in our dataBase
    function getYoutubeId(name,artist,callback){
      var data = $rootScope.form_data;
             var response;
            return $http({
              method: 'GET',
              url: 'http://localhost:8080/track/'+$rootScope.globals.currentUser.username+'/'+artist+'/'+name+'/'+$rootScope.globals.type
          }).then(function(response) {

            response = { success: true, titre: response.data.titre , videoId: response.data.id_track };
            callback(response);
        }, function(reason) {
            response = { success: false, message: 'Impossible de charger la vidéo' };
            callback(response);
        });
    }
    // Create the youtube iFrame
    function onYouTubeIframeAPIReady(str) {

        player = new YT.Player('player', {
        height: '360',
        width: '640',
        videoId: str,
        events: {
          'onReady': onPlayerReady,
          'onStateChange': onPlayerStateChange
            }
          });
        }
        // Change the video id of the iframe
    function changeVid(videoId){
      player.loadVideoById(videoId, 0, "default");
    }

    // Detection different events

  function onPlayerStateChange(event) {
   
    if (event.data == YT.PlayerState.ENDED && !done) {
      
      
      if(algoChoisi === 3)
      {
        loadNextByAlgoIg(3);
      } else if (algoChoisi === 6){
        loadNextByAlgoIg(6);
      } else {
        loadNextByAlgoIg(0);
      }
    }

  }
  // Load next video with the selected algorithm
  function loadNextByAlgoIg(idAlgo){
      $rootScope.form_data.name = vids[idAlgo].name;
      $rootScope.form_data.artist =vids[idAlgo].artist;
      $rootScope.form_data.id_track =vids[idAlgo].id_track;
    
      getYoutubeId($rootScope.form_data.name,$rootScope.form_data.artist,function (response){
        if(response.success){
           changeVid($rootScope.form_data.id_track);
            if(vids)
            {
              vids = [];
            }
            
            $http({
                method: 'GET',
                url: 'http://localhost:8080/reco/'+$rootScope.globals.currentUser.username+'/'+$rootScope.form_data.id_track
            }).then(function(response) {
              
              response.data.forEach(function(data) {
              var add = {name: data.track.title , artist : data.track.artist.name , id_track : data.track.id_track,nomAlgo : data.nameAlgorithm};
              vids.push(add);
            });
             vm.showLike=true;
              response = { success: true, titre: response.data.titre , videoId: response.data.id_track };
              
          }, function(reason) {
              response = { success: false, message: 'Impossible de charger la vidéo' };
              
          });
        }
        else{
          alert("Erreur de communication avec youtube, faites une nouvelle recherche");
        }
                    
          });
  }  
  }
})();

   // initialisation des information API youtube
function init() {
      gapi.client.setApiKey("AIzaSyD5ImYyyM8eVEnAkvnI06AxBZX8KSB-3p4");
      gapi.client.load("youtube", "v3", function() {
        // yt api is ready
      });
    }
// Play video automatically when player is ready
function onPlayerReady(event) {
  event.target.playVideo();
}


// Stop of the video
function stopVideo() {
  player.stopVideo();
}

 // fin vidéo --> prendre la première vidéo de l'algo choisis précédemment par user, défaut 1 -- OK
    // Nom des algos dans le json -- OK
    // Likes ok
    // Skin nn
    // vérification information inscription 
    // deconnexion de l'autre coté et dans fermeture de la fenêtre 
    // Base de données en anglais -- OK


    // Importanter !
    // noms algos / j'aime - bannir / stockerModeNav ou radio / click ou Next
    // AJouter TimeStamp dans consultation d'une vidéo
    // + timestamp sur bouton JM / JMpas
    // Ajouter table like/ban avec idUser,idTrack,timeStamp



     // cancel search if typed -- 1
          // see mdp change --0
          // désinscription ? --0
          // En mode radio toujours le même algo pour 1 user fixe --0
          // englais --1
          // commentaires --1
          // rapport vendredi soir
          // previous --> alert (attention vous allez désactiver l'écoute en cours) OU désactiver l'action du bouton précédent --1
          // Nom music + Nom artist au dessus du player --1
          // type email pour inscription
          // CORS 