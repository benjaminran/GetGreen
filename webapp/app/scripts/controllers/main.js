'use strict';

/**
 * @ngdoc function
 * @name GetGreenWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the GetGreenWebApp
 */
angular.module('GetGreenWebApp')
    .controller('MainCtrl', function ($scope) {
        $scope.todos = [];
        $scope.addTodo = function() {
            $scope.todos.push($scope.todo);
            $scope.todo = '';
        };
        $scope.removeTodo = function (index) {
            $scope.todos.splice(index, 1);
        };
    });
