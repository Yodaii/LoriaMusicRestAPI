(function () {
  'use strict';

var key = '2776b29967e1bfd49cfc90686cd48b6c';
var lastfm = 'http://ws.audioscrobbler.com/2.0/?api_key=' + key + '&format=json';

  angular
  .module('app')
  .controller('RadioController', RadioController);


  RadioController.$inject = ['LastFMAPI','UserService', '$rootScope','$http','$scope','autoComplete'];
  function RadioController(LastFMAPI,UserService, $rootScope,$http,$scope,autoComplete) {
    
      var vm = this;
      $scope.searchText = "";
      $scope.Person = [];
      $scope.selectedItem = [];
      $scope.isDisabled = false;
      $scope.noCache = false;

      $scope.selectedItemChange = function (item) {
          //alert("Item Changed");
      }
      $scope.searchTextChange = function (str) {

      return autoComplete.SearchBarAutoComplete(str);
      }

    if( $rootScope.form_data && $rootScope.form_data.name!=undefined )
    {
      var data = $rootScope.form_data;
      var name = $rootScope.form_data.name ;
      var artist = $rootScope.form_data.artist;
      getYoutubeId(name,artist,function (response){
        if (response.success) {
                  $.get("tpl/item.html", function(data) {
                   $("#results").append(tplawesome(data, [{"title":response.titre, "videoid":response.videoId}]));
                   resetVideoHeight();
                 });
                } else {
                  
                }
      });
    }

    $(function() {

      
      $("form").on("submit", function(e) {
       e.preventDefault();
       $rootScope.form_data.name = $scope.selectedItem.name;
       $rootScope.form_data.artist =$scope.selectedItem.artist;
       
        getYoutubeId($rootScope.form_data.name,$rootScope.form_data.artist,function (response){
        if (response.success) {
          $("#results").html("");
                  $.get("tpl/item.html", function(data) {
                   $("#results").append(tplawesome(data, [{"title":response.titre, "videoid":response.videoId}]));
                   resetVideoHeight();
                 });
                } else {
                  console.log("bug");
                }
      });
       
     });
      $(window).on("resize", resetVideoHeight);
    }); 


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
  }
})();

function tplawesome(e,t){res=e;for(var n=0;n<t.length;n++){res=res.replace(/\{\{(.*?)\}\}/g,function(e,r){return t[n][r]})}return res}
function resetVideoHeight() {
      $(".video").css("height", $("#results").width() * 9/16);
    }
   
function init() {
      gapi.client.setApiKey("AIzaSyD5ImYyyM8eVEnAkvnI06AxBZX8KSB-3p4");
      gapi.client.load("youtube", "v3", function() {
        // yt api is ready
      });
    }

     /*var data = $rootScope.form_data;
      var request = gapi.client.youtube.search.list({

        q :   data.word,
        part: "snippet",
        type: "video",
        maxResults : 1
        
      }); 
       // execute the request
       request.execute(function(response) {
        var results = response.result;
        $("#results").html("");
        $.each(results.items, function(index, item) {
          $.get("tpl/item.html", function(data) {
            $("#results").append(tplawesome(data, [{"title":item.snippet.title, "videoid":item.id.videoId}]));
          });
        });
        resetVideoHeight();
        
      });*/
    

    /*LastFMAPI.search(vm.replace,function (response) {

                if (response.success) {
                  var artist = response.artist;
                  var name = response.name;
                  //on stock les noms pour pouvoir les récupérer
                  localStorage.setItem("name", name);
                  localStorage.setItem("artist", artist);
                  
                } else {
                  //dire que ca va pas
                    FlashService.Error(response.message);
                    vm.dataLoading = false;
                }
            });
    var name = localStorage.getItem("name");
      var artist = localStorage.getItem("artist");
      getYoutubeId(name,artist,function (response){
        if (response.success) {
            console.log("videoId : "+response.videoId);
                  $.get("tpl/item.html", function(data) {
                   $("#results").append(tplawesome(data, [{"title":response.titre, "videoid":response.videoId}]));
                   resetVideoHeight();
                 });
                } else {
                  
                }
      });
      resetVideoHeight();

      $(window).on("resize", resetVideoHeight);*/