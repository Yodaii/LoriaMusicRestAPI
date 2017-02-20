(function () {
  'use strict';



  angular
  .module('app')
  .controller('NavController', NavController);


  NavController.$inject = ['LastFMAPI','UserService', '$rootScope', '$http','$scope','autoComplete','$location'];
  function NavController(LastFMAPI,UserService, $rootScope, $http,$scope,autoComplete,$location) {

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
      $scope.vids= [];
      $scope.algo1 = "";
      $scope.algo2 = "";
      $scope.algo3 = "";
       // Catch the item selected by the user
      // For further information check : https://material.angularjs.org/latest/api/directive/mdAutocomplete
      $scope.selectedItemChange = function (item) {
       // alert("Item Changed");
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
        $http({
              method: 'GET',
              url: 'http://localhost:8080/listening/'+$rootScope.globals.currentUser.username+'/'+$rootScope.form_data.id_track+'/'+player.getCurrentTime()
          }).then(function(response){
            console.log("Sauvegarde du temps d'écoute de la musique ok");
          },function(error){
            console.log("Erreur dans sauvegarde du temps d'écoute de la musique");
          });
      if(algoChoisi === 3)
      {
        loadNextByAlgoIg(3);
      } else if (algoChoisi === 6){
        loadNextByAlgoIg(6);
      } else {
        loadNextByAlgoIg(0);
      }
        console.log(player.getCurrentTime());
      }
    // When the page is loaded for the first time, check if a search has been set
    if( $rootScope.form_data && $rootScope.form_data.name!=undefined )
    {
      console.log("nav loeaded once");
      $rootScope.globals.type = "nav";
      vm.dataLoading = true;
      var name = $rootScope.form_data.name ;
      var artist = $rootScope.form_data.artist;
      // get the video from our API with the text search
      getYoutubeId(name,artist,function (response){
        if (response.success) {
          if($scope.vids)
          {
            $scope.vids = [];
          }
          
          $http({
              method: 'GET',
              url: 'http://localhost:8080/reco/'+$rootScope.globals.currentUser.username+'/'+response.videoId
          }).then(function(response) {
            
            response.data.forEach(function(data) {
            var add = {name: data.track.title , artist : data.track.artist.name, id_track : data.track.id_track ,nomAlgo : data.nameAlgorithm};
            $scope.vids.push(add);
            

          });
            $scope.algo1 = $scope.vids[0].nomAlgo;
            $scope.algo2 = $scope.vids[2].nomAlgo;
            $scope.algo3 = $scope.vids[5].nomAlgo;
            vm.recoLoading=false;
            response = { success: true, titre: response.data.titre , videoId: response.data.id_track };
            
        }, function(reason) {
            response = { success: false, message: 'Impossible de charger la vidéo' };
            
        });
        // if the player already exist, reload with the new video id
        if(player){
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
      $rootScope.globals.type = "nav";
      e.preventDefault();
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
          if($scope.vids)
          {
            $scope.vids = [];
          }
          $http({
              method: 'GET',
              url: 'http://localhost:8080/reco/'+$rootScope.globals.currentUser.username+'/'+response.videoId
          }).then(function(response) {
            
            response.data.forEach(function(data) {
            var add = {name: data.track.title , artist : data.track.artist.name, id_track : data.track.id_track ,nomAlgo : data.nameAlgorithm};
            $scope.vids.push(add);

          });
            // Set name of the choosen algo to send it to our API calls
            $scope.algo1 = $scope.vids[0].nomAlgo;
            $scope.algo2 = $scope.vids[2].nomAlgo;
            $scope.algo3 = $scope.vids[5].nomAlgo;
            vm.recoLoading=false;
            vm.showLike = true;
            response = { success: true, titre: response.data.titre , videoId: response.data.id_track };
            
        }, function(reason) {
            response = { success: false, message: 'Impossible de charger la vidéo' };
            
        });
          
        if(player){
          changeVid(response.videoId);
          $rootScope.form_data.id_track = response.videoId;
          
        }
        else{
          onYouTubeIframeAPIReady(response.videoId);
          $rootScope.form_data.id_track = response.videoId;

        }
        
        } else {
                  alert("Erreur de communication avec youtube, faites une nouvelle recherche");
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
  // Function called when user click on one recommendation from algorithms, it load the video on iframe, reload list of recommendation, and stock the selected algo
   $scope.modify = function(i,col) {
     $http({
              method: 'GET',
              url: 'http://localhost:8080/listening/'+$rootScope.globals.currentUser.username+'/'+$rootScope.form_data.id_track+'/'+player.getCurrentTime()
          }).then(function(response){
            console.log("Sauvegarde du temps d'écoute de la musique ok");
          },function(error){
            console.log("Erreur dans sauvegarde du temps d'écoute de la musique");
    });
    var x = i + col;
    var videoEnCours = $rootScope.form_data.id_track;
    algoChoisi = col;
    $rootScope.form_data.name = $scope.vids[x].name;
    $rootScope.form_data.artist =$scope.vids[x].artist;
    $rootScope.form_data.id_track =$scope.vids[x].id_track;
    var nomAlgoChoisis = $scope.vids[x].nomAlgo;
    $http({
              method: 'GET',
              url: 'http://localhost:8080/reco/'+$rootScope.globals.currentUser.username+'/'+videoEnCours+'/'+$rootScope.form_data.id_track+'/'+nomAlgoChoisis
          }).then(function(response){
          },function(error){
            console.log("Erreur dans sauvegarde du choix recommendations");
          });

    
    getYoutubeId($rootScope.form_data.name,$rootScope.form_data.artist,function (response){
      if(response.success){
         console.log("ChangeVid");
         changeVid($rootScope.form_data.id_track);
          if($scope.vids)
          {
            $scope.vids = [];
          }
          
          $http({
              method: 'GET',
              url: 'http://localhost:8080/reco/'+$rootScope.globals.currentUser.username+'/'+$rootScope.form_data.id_track
          }).then(function(response) {
            
            response.data.forEach(function(data) {
            var add = {name: data.track.title , artist : data.track.artist.name, id_track : data.track.id_track ,nomAlgo : data.nameAlgorithm};
            $scope.vids.push(add);
          });
           vm.showLike = true;
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
    function changeVid(videoId){
      player.loadVideoById(videoId, 0, "default");
    }

  // Envent that happens on the player
  function onPlayerStateChange(event) {
    // load next video
    if (event.data == YT.PlayerState.ENDED && !done) {
      
      console.log(algoChoisi);
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
      $rootScope.form_data.name = $scope.vids[idAlgo].name;
      console.log($rootScope.form_data.name);
      $rootScope.form_data.artist =$scope.vids[idAlgo].artist;
      $rootScope.form_data.id_track =$scope.vids[idAlgo].id_track;
      getYoutubeId($rootScope.form_data.name,$rootScope.form_data.artist,function (response){
        if(response.success){
           changeVid($rootScope.form_data.id_track);
            if($scope.vids)
            {
              $scope.vids = [];
            }
            
            $http({
              // TODO modifier appel API pour passer les paramtres nécessaires pour insertion écoute et choix algo en bdd
                method: 'GET',
                url: 'http://localhost:8080/reco/'+$rootScope.globals.currentUser.username+'/'+$rootScope.form_data.id_track
            }).then(function(response) {
              
              response.data.forEach(function(data) {
              var add = {name: data.track.title , artist : data.track.artist.name , id_track : data.track.id_track,nomAlgo : data.nameAlgorithm};
              $scope.vids.push(add);
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

// Init Youtube API key
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