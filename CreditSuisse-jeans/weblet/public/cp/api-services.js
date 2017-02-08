app.service('apiOrdersService', function($http, notifyService) {
  var service = this

  function notifyResponseError(/*obj*/ response) {
    notifyService.notify('<strong>'+response.status+'</strong>', 'danger')
  }

  service.topOrders = function(/*array*/ by, /*fn*/ callback, /*fn*/ statusCallback) {
    var params = by.map(function(v) { return "by="+v }).join("&")
    $http.get('/api/orders/top?'+params)
      .then(
        function successCallback(response) {
          var orders = response.data
          callback(orders.map(function(order) { return new OrderSummary(order) }))
          if (statusCallback) statusCallback('success')
        },
        function errorCallback(response) {
          notifyResponseError(response)
          if (statusCallback) statusCallback('error')
        })
  }

})

