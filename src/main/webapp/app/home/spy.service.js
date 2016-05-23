(function () {
    'use strict';

angular.module('webTaskApp')
    .factory('spyService', ['$http', function ($http) {
    function _spyOnWord(word){
        return $http.get('/api/spy/' + word);
    }
       return {
                    spyOnWord: _spyOnWord,
                };
    }]);
})();
