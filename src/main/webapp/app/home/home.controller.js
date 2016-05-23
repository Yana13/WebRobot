(function() {
    'use strict';

    angular
        .module('webTaskApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'spyService'];

    function HomeController ($scope, Principal, LoginService, $state, spyService) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        $scope.model = {
            textToSpyOn: "Text"
        }
       $scope.spyOn = function(){
           spyService.spyOnWord($scope.model.textToSpyOn);
        }
        function register () {
            $state.go('register');
        }
    }
})();
