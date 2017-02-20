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
    // Search function with the text entered by the user
    $scope.searchTextChange = function (str) {

    return autoComplete.SearchBarAutoComplete(str);
    }
   
    // init param when user select radio mode
    function radioSearch(){
      //check if user seleted item or not, if not, search with text only
       if($scope.selectedItem)
      {
        $rootScope.form_data.name = $scope.selectedItem.name;
        $rootScope.form_data.artist = $scope.selectedItem.artist;
        $location.path('/radio');
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

    // init param when user select nav mode
    function navSearch(){
      //check if user seleted item or not, if not, search with text only
      if($scope.selectedItem)
      {
        $rootScope.form_data.name = $scope.selectedItem.name;
        $rootScope.form_data.artist = $scope.selectedItem.artist;
        $location.path('/nav');
      }
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
            $location.path('/search/');

          }
        });
      }
      
    };
  }
})();

