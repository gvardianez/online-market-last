angular.module('market').controller('confirmEmailController', function ($scope, $http, $location, $localStorage, $routeParams) {

    $scope.confirmEmail = function () {
        $http({
            url: 'http://localhost:5555/auth/api/v1/registration/confirm-email/',
            method: 'GET',
            params: {
                username: $routeParams["username"],
                email: $routeParams["email"]
            }
        }).then(function successCallback(response) {
            alert('Email успешно подтвержден');
        }, function errorCallback(response) {
            alert(response.data.message);
        });
    };

    $scope.confirmEmail();

});