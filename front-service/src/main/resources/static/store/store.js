angular.module('market').controller('storeController', function ($scope, $http, $localStorage) {

    $scope.loadProducts = function (page = 1) {
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
                                url: 'http://localhost:5555/core/api/v1/products',
                                method: 'GET',
                                params: {
                                    p: page,
                                    title_part: $scope.filter ? $scope.filter.title_part : null,
                                    min_price: $scope.filter ? $scope.filter.min_price : null,
                                    max_price: $scope.filter ? $scope.filter.max_price : null
                                }
                            }).then(function successCallback(response) {
                                $scope.productsPage = response.data;
                                console.log(response.data)
                                $scope.generatePagesList($scope.productsPage.totalPages);
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
                        url: 'http://localhost:5555/core/api/v1/products',
                        method: 'GET',
                        params: {
                            p: page,
                            title_part: $scope.filter ? $scope.filter.title_part : null,
                            min_price: $scope.filter ? $scope.filter.min_price : null,
                            max_price: $scope.filter ? $scope.filter.max_price : null
                        }
                    }).then(function successCallback(response) {
                        $scope.productsPage = response.data;
                        console.log(response.data)
                        $scope.generatePagesList($scope.productsPage.totalPages);
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
            $http({
                url: 'http://localhost:5555/core/api/v1/products',
                method: 'GET',
                params: {
                    p: page,
                    title_part: $scope.filter ? $scope.filter.title_part : null,
                    min_price: $scope.filter ? $scope.filter.min_price : null,
                    max_price: $scope.filter ? $scope.filter.max_price : null
                }
            }).then(function successCallback(response) {
                $scope.productsPage = response.data;
                console.log(response.data)
                $scope.generatePagesList($scope.productsPage.totalPages);
            }, function errorCallback(response) {
                alert(response.data.message);
            });
        }
    }

    $scope.addToCart = function (id) {
        $http.get('http://localhost:5555/cart/api/v1/cart/' + $localStorage.marchMarketGuestCartId + '/add/' + id)
            .then(function (response) {
            });
    }

    $scope.generatePagesList = function (totalPages) {
        out = [];
        for (let i = 0; i < totalPages; i++) {
            out.push(i + 1);
        }
        $scope.pagesList = out;
    }

    $scope.loadProducts();
});