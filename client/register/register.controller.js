(function () {
    'use strict';

    angular
        .module('app')
        .controller('RegisterController', RegisterController);

    RegisterController.$inject = ['UserService', '$location', '$rootScope', 'FlashService'];
    function RegisterController(UserService, $location, $rootScope, FlashService) {
        var vm = this;

        vm.register = register;

        function register() {
            vm.dataLoading = true;
            UserService.Create(vm.user)
                .then(function (response) {
                    if (response.success) {
                        console.log(response.name);
                        FlashService.Success('Enregistrement effectué avec succès', true);
                        $location.path('/login');
                    } else {
                        FlashService.Error("inscription refusée");
                        vm.dataLoading = false;
                    }
                });
        }
    }

})();
