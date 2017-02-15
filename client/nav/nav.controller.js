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
      $scope.selectedItemChange = function (item) {
          //alert("Item Changed");
      }
      $scope.searchTextChange = function (str) {
      return autoComplete.SearchBarAutoComplete(str);
      }
      $scope.like=function(){
        vm.showLike = false;
         $http({
            // TODO modifier appel API pour passer les paramtres nécessaires pour insertion écoute et choix algo en bdd
              method: 'PUT',
              url: 'http://localhost:8080/ecoute/'+$rootScope.globals.currentUser.username+'/'+$rootScope.form_data.id_track+'/'+'true'
          }).then(function(response){
            //console.log("Sauvegarde du choix recommendations ok");
          },function(error){
            console.log("Erreur dans sauvegarde du choix like");
          });
      }
      $scope.unlike=function(){
        vm.showLike = false;
        $http({
            // TODO modifier appel API pour passer les paramtres nécessaires pour insertion écoute et choix algo en bdd
              method: 'PUT',
              url: 'http://localhost:8080/ecoute/'+$rootScope.globals.currentUser.username+'/'+$rootScope.form_data.id_track+'/'+'false'
          }).then(function(response){
            //console.log("Sauvegarde du choix recommendations ok");
          },function(error){
            console.log("Erreur dans sauvegarde du choix de ban");
          });
      }
      $scope.next = function(){
         console.log(player.getCurrentTime());
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
            
            response.data._embedded.recommendations.forEach(function(data) {
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
          
        if(player){
          changeVid(response.videoId);
          $rootScope.form_data.id_track = response.videoId;
          
        }
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
            
            response.data._embedded.recommendations.forEach(function(data) {
            var add = {name: data.track.title , artist : data.track.artist.name, id_track : data.track.id_track ,nomAlgo : data.nameAlgorithm};
            $scope.vids.push(add);

          });
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
    var videoEnCours = $rootScope.form_data.id_track;
    algoChoisi = col;
    $rootScope.form_data.name = $scope.vids[x].name;
    $rootScope.form_data.artist =$scope.vids[x].artist;
    $rootScope.form_data.id_track =$scope.vids[x].id_track;
    var nomAlgoChoisis = $scope.vids[x].nomAlgo;
    $http({
            // TODO modifier appel API pour passer les paramtres nécessaires pour insertion écoute et choix algo en bdd
              method: 'GET',
              url: 'http://localhost:8080/reco/'+$rootScope.globals.currentUser.username+'/'+videoEnCours+'/'+$rootScope.form_data.id_track+'/'+nomAlgoChoisis
          }).then(function(response){
            //console.log("Sauvegarde du choix recommendations ok");
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
            // TODO modifier appel API pour passer les paramtres nécessaires pour insertion écoute et choix algo en bdd
              method: 'GET',
              url: 'http://localhost:8080/reco/'+$rootScope.globals.currentUser.username+'/'+$rootScope.form_data.id_track
          }).then(function(response) {
            
            response.data._embedded.recommendations.forEach(function(data) {
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

    // Detection de mise en pause de la vidéo
  
  function onPlayerStateChange(event) {
    if (event.data == YT.PlayerState.PAUSED && !done) {
    
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
    }
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
              
              response.data._embedded.recommendations.forEach(function(data) {
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
/*// Amené à disparaitre 
function tplawesome(e,t){res=e;for(var n=0;n<t.length;n++){res=res.replace(/\{\{(.*?)\}\}/g,function(e,r){return t[n][r]})}return res}
// Aussi
function resetVideoHeight() {
      $(".video").css("height", $("#results").width() * 9/16);
    }*/
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


// Arret de la vidéo
function stopVideo() {
  player.stopVideo();
}