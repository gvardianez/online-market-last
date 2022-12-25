angular.module('market').controller('cartController', function ($scope, $http, $localStorage) {

    const contextPath = 'http://localhost:5555/cart/api/v1/cart/';

    $scope.loadCart = function () {
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
                            $http.get(contextPath + $localStorage.marchMarketGuestCartId)
                                .then(function successCallback(response) {
                                    $scope.cart = response.data;
                                }, function errorCallback(response) {
                                    alert(response.data.message);
                                });
                        }, function errorCallback(response) {
                            delete $localStorage.marketUser;
                            $http.defaults.headers.common.Authorization = '';
                            alert(response.data.message);
                        });
                    } else {
                        delete $localStorage.marketUser;
                        $http.defaults.headers.common.Authorization = '';
                    }
                } else {
                    $http.get(contextPath + $localStorage.marchMarketGuestCartId)
                        .then(function successCallback(response) {
                            $scope.cart = response.data;
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
            $http.get(contextPath + $localStorage.marchMarketGuestCartId)
                .then(function successCallback(response) {
                    $scope.cart = response.data;
                }, function errorCallback(response) {
                    alert(response.data.message);
                });
        }
    }

    $scope.changeQuantity = function (productId, delta) {
        $http({
            url: contextPath + $localStorage.marchMarketGuestCartId + '/change-quantity',
            method: 'GET',
            params: {
                productId: productId,
                delta: delta
            }
        }).then(function (response) {
            $scope.loadCart();
        });
    }

    $scope.setNewQuantity = function (productId, newQuantity) {
        $http({
            url: contextPath + $localStorage.marchMarketGuestCartId + '/set-quantity',
            method: 'GET',
            params: {
                productId: productId,
                newQuantity: newQuantity
            }
        }).then(function (response) {
            $scope.loadCart();
        });
    }

    $scope.removeProductFromCart = function (id) {
        $http.get(contextPath + $localStorage.marchMarketGuestCartId + '/remove/' + id)
            .then(function (response) {
                $scope.loadCart();
            });
    }

    $scope.clearCart = function () {
        $http.get(contextPath + $localStorage.marchMarketGuestCartId + '/clear')
            .then(function (response) {
                $scope.loadCart();
            });
    }

    $scope.createOrder = function () {
        $http.post('http://localhost:5555/core/api/v1/orders', $scope.orderDetails)
            .then(function (response) {
                $scope.orderDetails = null;
                $scope.loadCart();
            });
    }

    $scope.guestCreateOrder = function () {
        alert('Для оформления заказа необходимо войти в учетную запись');
    }

    $scope.loadCart();
});