(function () {
    'use strict';

    angular
        .module('app')
        .factory('LastFMAPI', LastFMAPI);

    LastFMAPI.$inject = ['$rootScope','$http','$q'];
    function LastFMAPI($rootScope,$http,$q) {
        var service = {};
        service.search = Search;
        var name;
        var artist;
        return service;

       
        function Search(keyword,callback){

            var reponse;
            return $http({
              method: 'POST',
              url: 'http://ws.audioscrobbler.com/2.0/',
              data: 'method=track.search&' +
               'track='+keyword+"&"+
               'api_key=5bf9260e6a06dd0e62142e1185ba4e69&' +
               'limit=1&'+
               'format=json'
          }).then(function(response) {
            setResultsLastFM(response.data.results.trackmatches.track);
            reponse = {success : true,  name:name,artist:artist};
            callback(reponse);
        }, function(response) {
            alert("error");
            reponse = { success : false,message: 'Erreur de l\'une de nos plateforme, veuillez réessayer' };
            callback(reponse);
        });

        
       
        }
        function  setResultsLastFM(track) {

        name = (track[0].name);
        artist = (track[0].artist);
    };
        
    }

    

})();