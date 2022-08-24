(function ($localStorage) {
    'use strict';
    angular
        .module('app', ['ngRoute', 'ngStorage'])
        .config(config)
        .directive('fileModel', ['$parse', function ($parse) {
            return {
                restrict: 'A',
                link: function(scope, element, attrs) {
                    var model = $parse(attrs.fileModel);
                    var modelSetter = model.assign;

                    element.bind('change', function() {
                        scope.$apply(function() {
                            modelSetter(scope, element[0].files[0]);
                        });
                    });
                }
            };
        }])
        .service('fileUpload', ['$http', function ($http) {
        this.uploadFileToUrl = function(file, uploadUrl) {
            var fd = new FormData();
            fd.append('file', file);

            $http.post(uploadUrl, fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
                .success(function() {
                })
                .error(function() {
                });
        }
    }])
        .run(run);

    function config($routeProvider, $httpProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'home/home.html',
                controller: 'homeController'
            })
            .when('/addPersonToMailList', {
                templateUrl: 'addPersonToMailList/addPersonToMailList.html',
                controller: 'addPersonToMailList'
            })
            .when('/groups/dev', {
                templateUrl: 'groups/dev/dev.html',
                controller: 'addPersonToMailList',
            })
            .when('/groups/allCompany', {
                templateUrl: 'groups/allCompany/allCompany.html',
                controller: 'addPersonToMailList',
            }).when('/groups/invest', {
                templateUrl: 'groups/invest/invest.html',
                controller: 'addPersonToMailList',
            }).when('/groups/dudergof', {
                templateUrl: 'groups/dudergof/dudergof.html',
                controller: 'addPersonToMailList',
            }).when('/groups/service', {
                templateUrl: 'groups/service/service.html',
                controller: 'addPersonToMailList',
            }).when('/groups/cis', {
                templateUrl: 'groups/cis/cis.html',
                controller: 'addPersonToMailList',
            }).when('/groups/flame', {
                templateUrl: 'groups/flame/flame.html',
                controller: 'addPersonToMailList',
            }).when('/groups/mk', {
                templateUrl: 'groups/mk/mk.html',
                controller: 'addPersonToMailList',
            }).when('/groups/monolit', {
                templateUrl: 'groups/monolit/monolit.html',
                controller: 'addPersonToMailList',
            }).when('/groups/nasledie', {
                templateUrl: 'groups/nasledie/nasledie.html',
                controller: 'addPersonToMailList',
            }).when('/groups/psb', {
                templateUrl: 'groups/psb/psb.html',
                controller: 'addPersonToMailList',
            }).when('/groups/szic', {
                templateUrl: 'groups/szic/szic.html',
                controller: 'addPersonToMailList',
            }).when('/groups/zenit', {
                templateUrl: 'groups/zenit/zenit.html',
                controller: 'addPersonToMailList',
            })
            .when('/saveEmployee', {
                templateUrl: 'saveEmployee/saveEmployee.html',
                controller: 'saveEmployeeController',
            })
            .when('/sendMassage', {
                templateUrl: 'sendMassage/sendMassage.html',
                controller: 'sendMassageController',
            })
            .otherwise({
                redirectTo: '/'
            });


        $httpProvider.interceptors.push(function ($q, $location) {
            return {
                'responseError': function (rejection, $localStorage, $http) {
                    var defer = $q.defer();
                    if (rejection.status == 401 || rejection.status == 403) {
                        console.log('error: 401-403');
                        $location.path('/auth');
                        if (!(localStorage.getItem("localUser") === null)) {
                            delete $localStorage.currentUser;
                            $http.defaults.headers.common.Authorization = '';
                        }
                        console.log(rejection.data);
                        var answer = JSON.parse(rejection.data);
                        console.log(answer);
                        // window.alert(answer.message);
                    }
                    defer.reject(rejection);
                    return defer.promise;
                }
            };
        });
    }

    function run($rootScope, $http, $localStorage) {
        if ($localStorage.currentUser) {
            $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
        }
    }
})();

angular.module('app').controller('indexController', function ($scope, $http, $localStorage) {
    const contextPath = 'http://10.160.3.255:7980/handbook';

    $scope.role = null;

    $scope.tryToAuth = function () {
        $http.post(contextPath + '/auth', $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.currentUser = {username: $scope.user.username, token: response.data.token};
                    $scope.currentUserName = $scope.user.username;

                    $scope.user.username = null;

                    $scope.user.password = null;

                    $scope.role = response.data.admin;
                    console.log($scope.role);
                }

            }, function errorCallback(response) {
            });
    };

    $scope.tryToLogout = function () {
        $scope.clearUser();
        if ($scope.user.username) {
            $scope.user.username = null;
        }
        if ($scope.user.password) {
            $scope.user.password = null;
        }
    };

    $scope.clearUser = function () {
        delete $localStorage.currentUser;
        $http.defaults.headers.common.Authorization = '';
    };

    $scope.isUserLoggedIn = function () {
        if ($localStorage.currentUser) {
            return true;
        } else {
            return false;
        }
    };
});