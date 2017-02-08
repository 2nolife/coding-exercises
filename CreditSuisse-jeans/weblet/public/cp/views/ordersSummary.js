app.service('ordersSummaryService', function($http, notifyService, $q) {
  var service = this

})

app.controller('ordersSummaryController', function($scope) {

})

app.directive('topOrders', function(notifyService, apiOrdersService) {

  var controller = function($scope) {

    $scope.init = function(/*num*/ mode) {
      switch (mode) {
        case 1:
          queryTopOrders(['manufacturer', 'gender', 'country'])
          break
        case 2:
          queryTopOrders(['size', 'country'])
          break
        case 3:
          queryTopOrders(['month'])
          break
        case 4:
          queryTopOrders(['month', 'country'])
          break
        default:
         notifyService.featureNotImplemented()
      }
    }

   function queryTopOrders(/*array*/ groupBy) {
      apiOrdersService.topOrders(groupBy, function(/*[OrderSummary]*/ orders) {
        $scope.ordersTable = {
          headers: groupBy,
          data: orders
        }
      })
    }

  }

  return {

    restrict: 'E',

    scope: {
      mode: '=' /*num*/
    },

    templateUrl: 'views/templates/topOrders.html',

    controller: controller,

    link: function(scope, element, attrs) {
      scope.$watch('mode', function(newValue, oldValue) {
        var mode = parseInt(newValue)
        if (mode > 0) scope.init(mode)
      })
    }

  }
})

app.directive('topOrdersCollapse', function() {
  return {

    restrict: 'E',

    scope: {
      mode: '@', /*num*/
      title: '@' /*str*/
    },

    templateUrl: 'views/templates/topOrdersCollapse.html'

  }
})
