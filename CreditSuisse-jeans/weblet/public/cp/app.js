var app = angular.module('app', [
  'ngResource',
  'ngRoute'
])

app.config(function($routeProvider) {

  $routeProvider
    .when('/', { templateUrl: 'views/ordersSummary.html', controller: 'ordersSummaryController' })
    .otherwise({ redirectTo: '/' })

})
