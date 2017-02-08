
function OrderSummary(/*json*/ source) {

  var _this = this

  this.source = source

  function applyChangesFromSource() {
    _this.year = source.year
    _this.month = monthToWord(source.month)
    _this.day = source.day
    _this.country = source.country
    _this.manufacturer = source.manufacturer
    _this.gender = genderToWord(source.gender)
    _this.size = source.size
    _this.colour = source.colour
    _this.style = source.style
    _this.count = source.count
  }

  applyChangesFromSource()

  function monthToWord(/*num|optional*/ month) {
    var months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
    var n = parseInt(month)
    return n > 0 ? months[n-1] : null
  }

  function genderToWord(/*str|optional*/ gender) {
    return gender == 'F' ? 'Female' : gender == 'M' ? 'Male' : null
  }

}
