(function () {
    'use strict';

angular.module('webTaskApp')
    .factory('spyService', ['$http', function ($http) {

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

    var scopeData = [];

    function _spyOnWord(word){
        var url = 'https://api.stackexchange.com/2.2/search?todate=1464048000&order=desc&sort=activity&tagged=' + word + '&site=stackoverflow'
            var response =  $http.get(url);
            return response;

    }

    function _sendMail(array){
            var response =  $http.put('/api/spy/sendLinks', array);
            return response;

        }
       return {
                spyOnWord: _spyOnWord,
                sendMail: _sendMail,
             };
    }]);
})();
