angular.module('market').controller('accountController', function ($scope, $http, $location, $localStorage, $routeParams, $rootScope) {

        $scope.loadAccount = function () {
            if ($localStorage.marketUser) {
                console.log("account")
                try {
                    let jwt = $localStorage.marketUser.accessToken;
                    let payload = JSON.parse(atob(jwt.split('.')[1]));
                    let currentTime = parseInt(new Date().getTime() / 1000);
                    if (currentTime > payload.exp) {
                        if ($localStorage.marketUser.refreshToken) {
                            $http.defaults.headers.common.Authorization = '';
                            console.log("new token")
                            $http({
                                url: 'http://localhost:5555/auth/api/v1/authenticate/refresh-tokens',
                                method: 'POST',
                                data: {
                                    refreshToken: $localStorage.marketUser.refreshToken,
                                }
                            }).then(function successCallback(response) {
                                console.log("success")
                                $http.defaults.headers.common.Authorization = '';
                                $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.accessToken;
                                $localStorage.marketUser.accessToken = response.data.accessToken;
                                $localStorage.marketUser.refreshToken = response.data.refreshToken;
                                $http.get('http://localhost:5555/auth/api/v1/account')
                                    .then(function successCallback(response) {
                                        $scope.userProfile = response.data;
                                    }, function errorCallback(response) {
                                        alert(response.data.message);
                                    });
                            }, function errorCallback(response) {
                                console.log("Token is expired!!!");
                                delete $localStorage.marketUser;
                                $http.defaults.headers.common.Authorization = '';
                                alert(response.data.message);
                            });
                        } else {
                            console.log("expired!!!");
                            delete $localStorage.marketUser;
                            $http.defaults.headers.common.Authorization = '';
                        }
                    } else {
                        $http.get('http://localhost:5555/auth/api/v1/account')
                            .then(function successCallback(response) {
                                $scope.userProfile = response.data;
                            }, function errorCallback(response) {
                                console.log("load account")
                                alert(response.data.message);
                            });
                    }
                } catch (e) {
                }

                if ($localStorage.marketUser) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.marketUser.accessToken;
                }
            }

        }

        $scope.changePassword = function () {
            $http.put('http://localhost:5555/auth/api/v1/account/change-password', $scope.changePasswordDto)
                .then(function successCallback(response) {
                    $scope.changePasswordDto = null;
                    alert('Пароль успешно изменен');
                }, function errorCallback(response) {
                    alert(response.data.message);
                });
        }

        $scope.recoverPassword = function () {
            $http.get('http://localhost:5555/auth/api/v1/account/recover-password')
                .then(function successCallback(response) {
                    alert('Новый пароль выслан на ваш Email');
                }, function errorCallback(response) {
                    alert(response.data.message);
                });
        }

        $scope.confirmEmail = function () {
            $http.get('http://localhost:5555/auth/api/v1/account/confirm-email')
                .then(function successCallback(response) {
                    alert('Ссылка для подтверждения Email отправлена на почту');
                }, function errorCallback(response) {
                    alert(response.data.message);
                });
        };

        $scope.isEmailConfirmed = function () {
            return $scope.userProfile.emailStatus === 'MAIL_CONFIRMED';
        };

        $scope.loadAccount();

    }
);