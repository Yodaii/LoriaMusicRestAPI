(function () {
  'use strict';



  angular
  .module('app')
  .controller('NavController', NavController);


  NavController.$inject = ['UserService', '$rootScope', '$http','$scope','autoComplete','$location'];
  function NavController(UserService, $rootScope, $http,$scope,autoComplete,$location) {

      var player;
      var onYouTubeIframeAPIReady = onYouTubeIframeAPIReady;
      var changeVid = changeVid;
      var vm = this;
      $scope.searchText = "";
      $scope.Person = [];
      $scope.selectedItem = [];
      $scope.isDisabled = false;
      $scope.noCache = false;
      $scope.vids= [];
      $scope.selectedItemChange = function (item) {
          //alert("Item Changed");
      }
      $scope.searchTextChange = function (str) {
      return autoComplete.SearchBarAutoComplete(str);
      }
    // A l'arrivée sur la page
    if( $rootScope.form_data && $rootScope.form_data.name!=undefined )
    {
      vm.dataLoading = true;
      var name = $rootScope.form_data.name ;
      var artist = $rootScope.form_data.artist;
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
            
            response.data._embedded.tracks.forEach(function(data) {
            var add = {name: data.titre , artist : 'Bruno Mars', id_track : data.id_track};
            $scope.vids.push(add);
          });
            vm.recoLoading=false;
            response = { success: true, titre: response.data.titre , videoId: response.data.id_track };
            
        }, function(reason) {
            response = { success: false, message: 'Impossible de charger la vidéo' };
            
        });
          
        if(player){
          changeVid(response.videoId);
          
        }
        else{
          vm.dataLoading = false;
          vm.reco=true;
          onYouTubeIframeAPIReady(response.videoId);

        }
                } else {
                  vm.dataLoading = false;
                  vm.reco=true;
                }
      });
    }
    // Suite au click sur bouton Rechercher
    $(function() {
      $("form").on("submit", function(e) {
       e.preventDefault();
       $rootScope.form_data.name = $scope.selectedItem.name;
       $rootScope.form_data.artist =$scope.selectedItem.artist;
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
            
            response.data._embedded.tracks.forEach(function(data) {
            var add = {name: data.titre , artist : 'Bruno Mars', id_track : data.id_track};
            $scope.vids.push(add);
          });
            vm.recoLoading=false;
            response = { success: true, titre: response.data.titre , videoId: response.data.id_track };
            
        }, function(reason) {
            response = { success: false, message: 'Impossible de charger la vidéo' };
            
        });
          
        if(player){
          changeVid(response.videoId);
          
        }
        else{
          onYouTubeIframeAPIReady(response.videoId);

        }
        
        } else {
                  alert("Erreur de communication avec youtube, faites une nouvelle recherche");
                }
      });
       
     });
      $(window).on("resize", resetVideoHeight);
    }); 

    // Fais appel à notre API, afin de chercher id et informations vidéo en BDD, si non présent, insertion BDD et retour informations
    function getYoutubeId(name,artist,callback){
      var data = $rootScope.form_data;
             var response;
            return $http({
              method: 'GET',
              url: 'http://localhost:8080/track/'+$rootScope.globals.currentUser.username+'/'+artist+'/'+name
          }).then(function(response) {

            response = { success: true, titre: response.data.titre , videoId: response.data.id_track };
            callback(response);
        }, function(reason) {
            response = { success: false, message: 'Impossible de charger la vidéo' };
            callback(response);
        });
    } 
  // Click sur l'une des proposition des algorithmes, on envoi alors à l'API ce changement (nom user, vidéo en cours, vidéo choisie, id algo)
   $scope.modify = function(i,col) {
    var x = i + col;
    $rootScope.form_data.name = $scope.vids[x].name;
    $rootScope.form_data.artist =$scope.vids[x].artist;
    $rootScope.form_data.id_track =$scope.vids[x].id_track;
    $("#results").html("");
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
            
            response.data._embedded.tracks.forEach(function(data) {
            var add = {name: data.titre , artist : 'Bruno Mars', id_track : data.id_track};
            $scope.vids.push(add);
          });
           
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
      // Fonction permettant d'initialiser le player youtube grâce à API youtube
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

  }
})();



// Amené à disparaitre 
function tplawesome(e,t){res=e;for(var n=0;n<t.length;n++){res=res.replace(/\{\{(.*?)\}\}/g,function(e,r){return t[n][r]})}return res}
// Aussi
function resetVideoHeight() {
      $(".video").css("height", $("#results").width() * 9/16);
    }
// Initialisation de notre clé API youtube
function init() {
      gapi.client.setApiKey("AIzaSyD5ImYyyM8eVEnAkvnI06AxBZX8KSB-3p4");
      gapi.client.load("youtube", "v3", function() {
        // yt api is ready
      });
    }


     
    
      // Lorsque le player est pret, on play la vidéo automatiquement
      function onPlayerReady(event) {
        event.target.playVideo();
      }

      // Detection de mise en pause de la vidéo
      var done = false;
      function onPlayerStateChange(event) {
        if (event.data == YT.PlayerState.PAUSED && !done) {
          alert("Prévenir API de mise en pause de vidéo");
        }
      }
      // Arret de la vidéo
      function stopVideo() {
        player.stopVideo();
      }