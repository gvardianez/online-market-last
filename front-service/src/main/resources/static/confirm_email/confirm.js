angular.module('market').controller('confirmEmailController', function ($scope, $http, $location, $localStorage, $routeParams) {

    $scope.confirmEmail = function () {
        $http({
            url: 'http://localhost:5555/auth/confirm-email/' + $routeParams.userNameAndEmail,
            method: 'GET'
        }).then(function successCallback(response) {
            alert('Email успешно подтвержден');
        }, function errorCallback(response) {
            alert(response.data.message);
        });
    };

    $scope.confirmEmail();

});