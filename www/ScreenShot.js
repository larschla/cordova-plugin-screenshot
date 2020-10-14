cordova.define("cordova-plugin-screenshotv2.ScreenShot", function(require, exports, module) {
    require('cordova/channel').onCordovaReady.subscribe(function() {
        require('cordova/exec')(win, null, 'ScreenShot', 'initialize', []);
        function win(message) {

            console.log("cordova-plugin-screenshotv2.ScreenShot:" + message);

        }
    });
});
