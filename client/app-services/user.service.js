(function () {
    'use strict';

    angular
        .module('app')
        .factory('UserService', UserService);

    UserService.$inject = ['$http','$q'];
    function UserService($http,$q) {
        var service = {};


        service.Create = Create;
        /*service.Update = Update;
        service.Delete = Delete;*/

        return service;


        function Create(user) {
            return $http({
              method: 'POST',
              url: 'http://localhost:8080/user',
              data: {"last_name": user.nom, "first_name": user.prenom, "email": user.email, "password": user.mot_de_passe}
            }).
            then(handleSuccess, handleError('Error creating user'));
        }
        /* Can be used if needed, not implemented yet, just an exemple
        function Update(user) {
            return $http.put('/api/users/' + user.id, user).then(handleSuccess, handleError('Error updating user'));
        }

        function Delete(id) {
            return $http.delete('/api/users/' + id).then(handleSuccess, handleError('Error deleting user'));
        }
*/
        // private functions

        function handleSuccess(res) {
            var deferred = $q.defer();
            deferred.resolve({ success: true, name: 'Username is ok' });
            return deferred.promise;
        }

        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        }
    }

})();
