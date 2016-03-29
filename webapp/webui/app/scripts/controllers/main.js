'use strict';

/**
 * @ngdoc function
 * @name GetGreenWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the GetGreenWebApp
 */
angular.module('GetGreenWebApp')
  .controller('MainCtrl', ['$scope', 'Upload', function ($scope, Upload) {
    $scope.recordingFile = null;

    $scope.analyzeRecording = function() {
      Upload.upload({
        url: 'upload/url',
        data: {file: $scope.recordingFile, 'username': $scope.username}
      }).then(function (resp) {
        console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
      }, function (resp) {
        console.log('Error status: ' + resp.status);
      }, function (evt) {
        var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
        console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
      });
    };
  }]);
