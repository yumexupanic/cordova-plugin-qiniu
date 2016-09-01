
var cordova = require('cordova');

var qiniu = {
	upload: function(options,successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, 'upload', 'uploadFile',[options]);
    }
}

module.exports = qiniu
