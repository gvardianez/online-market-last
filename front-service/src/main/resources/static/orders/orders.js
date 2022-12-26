angular.module('market').controller('ordersController', function ($rootScope, $scope, $http, $location, $localStorage) {

    const contextPath = 'http://localhost:5555/core/';
    $scope.loadOrders = function () {
        if ($localStorage.marketUser) {
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
                            $http.get(contextPath + 'api/v1/orders')
                                .then(function successCallback(response) {
                                    $scope.MyOrders = response.data;
                                }, function errorCallback(response) {
                                    alert(response.data.message);
                                });
                        }, function errorCallback(response) {
                            console.log("Token is expired!!!");
                            delete $localStorage.marketUser;
                            $http.defaults.headers.common.Authorization = '';
                            alert(response.data.message);
                            $location.path('/');
                        });
                    } else {
                        console.log("expired!!!");
                        delete $localStorage.marketUser;
                        $http.defaults.headers.common.Authorization = '';
                        $location.path('/');
                    }
                } else {
                    $http.get(contextPath + 'api/v1/orders')
                        .then(function successCallback(response) {
                            $scope.MyOrders = response.data;
                        }, function errorCallback(response) {
                            alert(response.data.message);
                        });
                }
            } catch (e) {
            }

            if ($localStorage.marketUser) {
                $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.marketUser.accessToken;
            }
        } else {
            $location.path('/');
        }
    }

    $scope.goToPay = function (orderId) {
        $location.path('/order_pay/' + orderId);
    }

    $scope.loadOrders();
});