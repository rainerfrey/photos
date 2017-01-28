import Ember from "ember";
import EmberUploader from "ember-uploader";
import ENV from "photo-ui/config/environment";

export default EmberUploader.FileField.extend({
  session: Ember.inject.service(),

  filesDidChange: function (files) {
    let headers = {};
    this.get("session").authorize("authorizer:oauth2", function (name, value) {
      headers[name] = value;
    });
    let uploader = EmberUploader.Uploader.create({
      paramName: "image-file",
      url: ENV.PHOTOS.serviceUrl + this.get('url'),
      ajaxSettings: {headers}
    });

    if (!Ember.isEmpty(files)) {
      // this second argument is optional and can to be sent as extra data with the upload
      uploader.upload(files[0]);
    }
  }
});
