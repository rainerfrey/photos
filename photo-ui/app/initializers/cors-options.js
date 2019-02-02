import $ from 'jquery';

export default {
    name: "corsOptions",
    initialize: function () {
        $.ajaxPrefilter(function (options) {
            if (!options.xhrFields) {
                options.xhrFields = {};
            }
            options.xhrFields.withCredentials = true;
            options.crossDomain = true;
        });
    }
};
