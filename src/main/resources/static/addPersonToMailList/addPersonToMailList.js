angular.module('app').controller('addPersonToMailList', function ($scope, $http) {
    // const contextPath = 'http://localhost:7980/handbook/ldap';
    const contextPath = 'http://10.160.3.255:7980/handbook/ldap';

    $scope.arr = [{"id": "Отобразить", "print": "true"}, {"id": "Скрыть", "print": "false"}];
    $scope.selectedMailslistId = null;

    // $scope.initHeaders = function () {
    //     let Origin = 'Access-Control-Allow-Origin';
    //     $http.defaults.headers. = '*';
    //
    // };
    //
    // $scope.initHeaders();

    $scope.getPersonsByOrganization = function (nameSpase) {
        $http({
            url: contextPath + '/persons',
            method: 'GET',
            params: {
                nameSpase: nameSpase
            }
        }).then(function (response) {
            $scope.PersonsList = response.data;
            $scope.getMailslists();
            console.log($scope.PersonsList);
        }).catch(function (response) {
            console.log(response);
        });
    };

    $scope.getMailslists = function () {
        $http({
            url: contextPath + '/mailslists',
            method: 'GET',
            params: {
            }
        }).then(function (response) {
            $scope.Mailslist = response.data.mailslist;
            console.log($scope.Mailslist);
        });
    };

    $scope.addToMailsList = function (selectedMailslist, email, fullName, nameSpace) {
        console.log($scope.selectedMailslistId);
        $http({
            url: contextPath + '/person',
            method: 'POST',
            params: {
                email: email,
                selectedMailslistId: $scope.selectedMailslistId,
                fullName: fullName
            }
        }).then(function (response) {
            console.log(nameSpace)
            $scope.getEmployeesOffTheMailslist(nameSpace, $scope.selectedMailslistId)
        });
    };

    $scope.testButton = function (mail) {
        $http({
            url: contextPath + '/testButton',
            method: 'GET',
            params: {
                email: mail
            }
        }).catch(function (response) {
            console.log(response);
            console.log($scope.selectedMailslist);
        });
    };

    $scope.selectMailslist = function (mailslistName, mailslistId, nameSpace) {
        $scope.selectedMailslist = mailslistName;
        $scope.selectedMailslistId = mailslistId;
        $scope.getEmployeesOffTheMailslist(nameSpace, mailslistId);
        // console.log(mailslistId);
    };

    $scope.getEmployeesOffTheMailslist = function (nameSpace, id) {
        console.log($scope.selectedMailslistId);
        $http({
            url: contextPath + '/employeesOffTheMailslist',
            method: 'GET',
            params: {
                nameSpace: nameSpace,
                mailslistId: id
            }
        }).then(function (response) {
            console.log(response.data);
            $scope.PersonsList = response.data;
        });
    };
});