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
        $scope.items = [];
         $scope.intervalId;
         $scope.stopSpying = function(){
         if($scope.intervalId){
             window.clearInterval($scope.intervalId);
         }
         }

       $scope.spyOn = function(){
      $scope.intervalId =  window.setInterval(function(){
           var result = spyService.spyOnWord($scope.model.textToSpyOn);
           result.success(function(data){

                    var array = [];
                               var count = data.items.length < 10? data.items.length : 10;
                               for (var i = 0; i < 10; i++) {
                                    array.push(data.items[i].link);
                               }
                               if( $scope.items.length <= 0 || !arraysEqual( $scope.items, data.items)){
                                       $scope.items = angular.copy(data.items);
                                       spyService.sendMail(array);
                               }
                   });
                   }, 10000);
        }

        function arraysEqual(a, b) {
              if (a === b) return true;
              if (a == null || b == null) return false;
              if (a.length != b.length) return false;

              // If you don't care about the order of the elements inside
              // the array, you should sort both arrays here.

              for (var i = 0; i < a.length; ++i) {
                if (a[i] !== b[i]) return false;
              }
              return true;
            }

        function register () {
            $state.go('register');
        }
    }
})();
