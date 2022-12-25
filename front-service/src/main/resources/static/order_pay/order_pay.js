angular.module('market').controller('orderPayController', function ($scope, $http, $location, $localStorage, $routeParams) {

    $scope.loadOrder = function () {
        if ($localStorage.marketUser) {
            try {
                let jwt = $localStorage.marketUser.accessToken;
                let payload = JSON.parse(atob(jwt.split('.')[1]));
                let currentTime = parseInt(new Date().getTime() / 1000);
                if (currentTime > payload.exp) {
                    if ($localStorage.marketUser.refreshToken) {
                        $http.defaults.headers.common.Authorization = '';
                        $http({
                            url: 'http://localhost:5555/auth/api/v1/authenticate/refresh-tokens',
                            method: 'POST',
                            data: {
                                refreshToken: $localStorage.marketUser.refreshToken,
                            }
                        }).then(function successCallback(response) {
                            $http.defaults.headers.common.Authorization = '';
                            $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.accessToken;
                            $localStorage.marketUser.accessToken = response.data.accessToken;
                            $localStorage.marketUser.refreshToken = response.data.refreshToken;
                            $http({
                                url: 'http://localhost:5555/core/api/v1/orders/' + $routeParams.orderId,
                                method: 'GET'
                            }).then(function successCallback(response) {
                                $scope.order = response.data;
                                $scope.renderPaymentButtons();
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
                        delete $localStorage.marketUser;
                        $http.defaults.headers.common.Authorization = '';
                    }
                } else {
                    $http({
                        url: 'http://localhost:5555/core/api/v1/orders/' + $routeParams.orderId,
                        method: 'GET'
                    }).then(function successCallback(response) {
                        $scope.order = response.data;
                        $scope.renderPaymentButtons();
                    }, function errorCallback(response) {
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


    $scope.renderPaymentButtons = function () {
        paypal.Buttons({
            createOrder: function (data, actions) {
                return fetch('http://localhost:5555/core/api/v1/paypal/create/' + $scope.order.id, {
                    method: 'post',
                    headers: {
                        'content-type': 'application/json'
                    }
                }).then(function (response) {
                    return response.text();
                });
            },

            onApprove: function (data, actions) {
                return fetch('http://localhost:5555/core/api/v1/paypal/capture/' + data.orderID, {
                    method: 'post',
                    headers: {
                        'content-type': 'application/json'
                    }
                }).then(function (response) {
                    response.text().then(msg => alert(msg));
                });
            },

            onCancel: function (data) {
                console.log("Order canceled: " + data);
            },

            onError: function (err) {
                console.log(err);
            }
        }).render('#paypal-buttons');
    }

    $scope.loadOrder();
});