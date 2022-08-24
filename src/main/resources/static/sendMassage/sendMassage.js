angular.module('app').controller('sendMassageController', function($scope, fileUpload, $http) {
    $scope.uploadFile = function() {

        var file = $scope.myFile;
        console.log('file is ' );
        console.dir(file);
        var uploadUrl = "http://10.160.3.255:7980/handbook/ldap/file";
        fileUpload.uploadFileToUrl(file, uploadUrl);
    };

    const contextPath = 'http://10.160.3.255:7980/handbook/ldap';

    $scope.init = function () {
        $http({
            url: contextPath + '/mailslists',
            method: 'GET',
            params: {

            }
        }).then(function (response) {
            $scope.Mailslist = response.data.mailslist;
            $scope.Fromlist = response.data.listFrom;
            console.log($scope.Mailslist);
            console.log($scope.Fromlist);
        })
    };

    $scope.sendMessage = function () {
        console.log($scope.Message);
        $http({
            url: contextPath + '/message',
            method: 'POST',
            data: $scope.Message
            // params: {
            //     message: $scope.Message
            // }
        }).then(function (response) {
            console.log(response.data)
        });
    };

});


// angular.module('app').controller('sendMassageController', function ($scope, $http) {
//     const contextPath = 'http://10.160.3.255:7980/handbook/ldap';
//
//     $scope.init = function () {
//         $http({
//             url: contextPath + '/mailslists',
//             method: 'GET',
//             params: {
//
//             }
//         }).then(function (response) {
//             $scope.Mailslist = response.data;
//             console.log($scope.Mailslist);
//         })
//     };
//
//     $scope.sendMessage = function () {
//         console.log($scope.Message);
//         $http({
//             url: contextPath + '/message',
//             method: 'POST',
//             data: $scope.Message
//             // params: {
//             //     message: $scope.Message
//             // }
//         }).then(function (response) {
//             console.log(response.data)
//         });
//     };
//
// });
