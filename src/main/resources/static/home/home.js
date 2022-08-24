angular.module('app').controller('homeController', function ($scope, $http) {
    // const contextPath = 'http://localhost:7980/handbook/ldap';
    const contextPath = 'http://10.160.3.255:7980/handbook/ldap';

    $scope.showMailslistt = 0;

    $scope.getPersonsByOrganization = function (nameSpase) {
        $http({
            url: contextPath + '/persons',
            method: 'GET',
            params: {
                nameSpase: nameSpase
            }
        }).then(function (response) {
            $scope.PersonsList = response.data;
            console.log($scope.PersonsList);
        }).catch(function (response) {
            console.log(response);
        });
    };

    $scope.submitSaveEmployee = function (){
        $http.post(contextPath + '/maillist', $scope.newMaillist)
            .then(function successCallback(response) {
                $scope.newMaillist = null;
                $scope.getMailslists();
            }, function errorCallback(response) {
            });
    };

    $scope.getMailslists = function () {
        $http({
            url: contextPath + '/mailslists',
            method: 'GET',
            params: {
            }
        }).then(function (response) {
            $scope.MailsLists = response.data.mailslist;
            console.log($scope.MailsLists);
        }).catch(function (response) {
            console.log(response);
        });
    }

    $scope.showMailslist = function (id, name){
        $http({
            url: contextPath + '/mailslist',
            method: 'GET',
            params: {
                mailslistId: id,
                mailslistName: name
            }
        }).then(function (response) {
            $scope.showMailslistt = 1;
            $scope.PersonsList = response.data.persons;
            $scope.Emails = response.data.emails;
            console.log($scope.PersonsList);
        }).catch(function (response) {
            console.log(response);
        });
    }

    $scope.mainPage = function (){
        $scope.showMailslistt = 0;
        $scope.getMailslists();
    }

    $scope.generateMailslistIndexes = function (length) {
        let arr = [];
        for (let i = 0; i < length; i++) {
            arr.push(i+1);
        }
        return arr;
    }
});