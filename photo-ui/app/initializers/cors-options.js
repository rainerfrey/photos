import Ember from "ember";

export default {
    name: "corsOptions",
    initialize: function () {
        Ember.$.ajaxPrefilter(function (options) {
            if (!options.xhrFields) {
                options.xhrFields = {};
            }
            options.xhrFields.withCredentials = true;
            options.crossDomain = true;
        });
    }
};
