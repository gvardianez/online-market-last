(function () {
    angular
        .module('market', ['ngRoute', 'ngStorage'])
        .config(config)
        .run(run);

    function config($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'welcome/welcome.html',
                controller: 'welcomeController'
            })
            .when('/registration', {
                templateUrl: 'registration/registration.html',
                controller: 'registerController'
            })
            .when('/store', {
                templateUrl: 'store/store.html',
                controller: 'storeController'
            })
            .when('/cart', {
                templateUrl: 'cart/cart.html',
                controller: 'cartController'
            })
            .when('/orders', {
                templateUrl: 'orders/orders.html',
                controller: 'ordersController'
            })
            .when('/order_pay/:orderId', {
                templateUrl: 'order_pay/order_pay.html',
                controller: 'orderPayController'
            })
            .when('/confirm_email/:username/:email', {
                templateUrl: 'confirm_email/confirm_email.html',
                controller: 'confirmEmailController'
            })
            .when('/account', {
                templateUrl: 'account/account.html',
                controller: 'accountController'
            })
            .otherwise({
                redirectTo: '/'
            });
    }

    function run($rootScope, $http, $localStorage) {
        if ($localStorage.marketUser) {
            if ($localStorage.marketUser) {
                $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.marketUser.accessToken;
            }
        }
        if (!$localStorage.marchMarketGuestCartId) {
            $http.get('http://localhost:5555/cart/api/v1/cart/generate-id')
                .then(function (response) {
                    $localStorage.marchMarketGuestCartId = response.data.value;
                });
        }
    }
})();

angular.module('market').controller('indexController', function ($rootScope, $scope, $http, $location, $localStorage) {
    $scope.tryToAuth = function () {
        $http.post('http://localhost:5555/auth/api/v1/authenticate', $scope.user)
            .then(function successCallback(response) {
                if (response.data.accessToken) {
                    console.log(response.data.accessToken)
                    console.log(response.data.refreshToken)
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.accessToken;
                    $localStorage.marketUser = {
                        username: $scope.user.username,
                        accessToken: response.data.accessToken,
                        refreshToken: response.data.refreshToken
                    };

                    $scope.user.username = null;
                    $scope.user.password = null;

                    $http.get('http://localhost:5555/cart/api/v1/cart/' + $localStorage.marchMarketGuestCartId + '/merge')

                    $location.path('/');
                }
            }, function errorCallback(response) {
                alert(response.data.message);
            });
    };

    $scope.tryToLogout = function () {
        $scope.clearUser();
        $location.path('/');
    };

    $scope.clearUser = function () {
        delete $localStorage.marketUser;
        $http.defaults.headers.common.Authorization = '';
    };

    $rootScope.isUserLoggedIn = function () {
        if ($localStorage.marketUser) {
            return true;
        } else {
            return false;
        }
    };
});

