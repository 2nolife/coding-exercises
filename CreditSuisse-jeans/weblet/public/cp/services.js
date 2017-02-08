app.service('notifyService', function($timeout) {
  var service = this

  var defaultOptions = {
  }

  var defaultSettings = {
    newest_on_top: true,
    placement: {
      align: 'center'
    }
  }

  service.notify = function(/*json|str*/ options, /*json|str*/ settings) {
    options = $.extend(true, {}, defaultOptions, typeof options === 'object' ? options : { message: options })
    settings = $.extend(true, {}, defaultSettings, typeof settings === 'object' ? settings : { type: settings })
    $.notify(options, settings)
  }

  service.featureNotImplemented = function() {
    service.notify('This feature is not implemented!', 'danger')
  }

})
