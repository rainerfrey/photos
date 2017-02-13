import Ember from "ember";
import EmberUploader from "ember-uploader";
import ENV from "photo-ui/config/environment";
const {isPresent} = Ember;

export default EmberUploader.FileField.extend({
  session: Ember.inject.service(),

  filesDidChange(files) {
    let headers = {};
    this.get("session").authorize("authorizer:oauth2", (name, value) => {
      headers[name] = value;
    });
    let uploader = EmberUploader.Uploader.create({
      paramName: "image-file",
      url: ENV.PHOTOS.serviceUrl + this.get('url'),
      ajaxSettings: {headers}
    });
    uploader.on('didUpload', () => {
      this.$().val(null);
    });

    if (!Ember.isEmpty(files)) {
      let title = this.get("title");
      let caption = this.get("caption");

      let extra = {};
      if (isPresent(title)) {
        extra.title = title;
      }
      if (isPresent(caption)) {
        extra.caption = caption;
      }
      // this second argument is optional and can to be sent as extra data with the upload
      uploader.upload(files[0], extra);
    }
  }
});
