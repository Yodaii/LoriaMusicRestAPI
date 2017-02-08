(function () {
  'use strict';


var key = '2776b29967e1bfd49cfc90686cd48b6c';
var lastfm = 'http://ws.audioscrobbler.com/2.0/?api_key=' + key + '&format=json';

  angular
  .module('app')
  .controller('SearchController', SearchController);


  SearchController.$inject = ['LastFMAPI','$location','UserService', '$rootScope','$scope','autoComplete'];
  function SearchController(LastFMAPI,$location,UserService, $rootScope,$scope,autoComplete) {

    var vm = this;
    vm.navSearch = navSearch;
    vm.radioSearch = radioSearch;

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
   
    // initialisation des paramètres lorsque l'utilisateur choisis le mode radio
    function radioSearch(){
      //vérification, l'utilisateur a t'il choisis un objet dans la liste de proposition ou non
      //si oui initialisation des paramètres avec les noms déja corrigés
       if($scope.selectedItem)
      {
        $rootScope.form_data.name = $scope.selectedItem.name;
        $rootScope.form_data.artist = $scope.selectedItem.artist;
      }
      //si non, appel à api lasfm pour corriger ce qu'il a écris
      else
      {
        LastFMAPI.search($scope.searchText, function(response){
          if(response)
          {
            $rootScope.form_data.name =response.name;
            $rootScope.form_data.artist = response.artist;
            $location.path('/radio');
          }
          else
          {
            $rootScope.form_data.name = "erreur";
            $rootScope.form_data.artist = "erreur";
            $location.path('/search');

          }
        });
      }
      
    }

    // initialisation des paramètres lorsque l'utilisateur choisis le mode navigation
    function navSearch(){
      // voir explications dans radioSearch()
      if($scope.selectedItem)
      {
        $rootScope.form_data.name = $scope.selectedItem.name;
        $rootScope.form_data.artist = $scope.selectedItem.artist;
      }
      //si non, appel à api lasfm pour corriger ce qu'il a écris
      else
      {
        LastFMAPI.search($scope.searchText, function(response){
          if(response)
          {
            $rootScope.form_data.name =response.name;
            $rootScope.form_data.artist = response.artist;
            $location.path('/nav');
          }
          else
          {
            $rootScope.form_data.name = "erreur";
            $rootScope.form_data.artist = "erreur";
            $location.path('/search');

          }
        });
      }
      
    };
   
   

  }
})();

