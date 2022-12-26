angular.module('market').controller('registerController', function ($rootScope, $scope, $http) {
    $scope.newRegistration = function () {
        $http.post('http://localhost:5555/auth/api/v1/registration', $scope.registerUserDto)
            .then(function successCallback(response) {
                $scope.registerUserDto = null;
                alert('My name: ' + response.data.username + '\n' + 'my email: ' + response.data.email)
            }, function errorCallback(response) {
                alert(response.data.message);
            });
    }
});