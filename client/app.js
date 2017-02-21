(function () {
    'use strict';
// injection of tool used for redirection and autocompletion
    angular
        .module('app', ['ngRoute', 'ngCookies','ngMaterial'])
        .config(config)
        .run(run);

    config.$inject = ['$routeProvider', '$locationProvider'];
    // definition of every controller for each views
    function config($routeProvider, $locationProvider) {
        $routeProvider
            .when('/login', {
                controller: 'LoginController',
                templateUrl: 'login/login.view.html',
                controllerAs: 'vm'
            })

            .when('/register', {
                controller: 'RegisterController',
                templateUrl: 'register/register.view.html',
                controllerAs: 'vm'
            })
            .when('/search', {
                controller: 'SearchController',
                templateUrl: 'search/search.view.html',
                controllerAs: 'vm'
            })
            .when('/radio', {
                controller: 'RadioController',
                templateUrl: 'radio/radio.view.html',
                controllerAs: 'vm'
            })
            .when('/nav', {
                controller: 'NavController',
                templateUrl: 'nav/nav.view.html',
                controllerAs: 'vm'
            })
            .otherwise({ redirectTo: '/login' });
    }
    // initialisation of rootscope data
    run.$inject = ['$rootScope', '$location', '$cookies', '$http'];
    function run($rootScope, $location, $cookies, $http) {
        $rootScope.form_data = {};
        var player;
        // keep user logged in after page refresh
        $rootScope.globals = $cookies.getObject('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata;
        }

        $rootScope.$on('$stateChangeStart', function (event){
          if (forbit){
            event.preventDefault()
          }
          else{  
            return
          }
        });

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in and trying to access a restricted page
            var restrictedPage = $.inArray($location.path(), ['/login', '/register']) === -1;
            var loggedIn = $rootScope.globals.currentUser;
            if (restrictedPage && !loggedIn) {
                $location.path('/login');
            }

        });

    }

})();