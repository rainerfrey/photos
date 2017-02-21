import Ember from "ember";
import EmberUploader from "ember-uploader";
import ENV from "photo-ui/config/environment";
const {isEmpty, isPresent, computed} = Ember;

export default Ember.Component.extend(Ember.Evented, {
  session: Ember.inject.service(),
  files: null,
  selected: computed("files.[]", function () {
    let files = this.get("files");
    if (isPresent(files)) {
      return files[0];
    }
    else {
      return null;
    }
  }),
  previewUrl: null,

  doUpload() {
    let files = this.get("files");
    if (!isEmpty(files)) {
      let headers = {};
      this.get("session").authorize("authorizer:oauth2", (name, value) => {
        headers[name] = value;
      });
      let uploader = EmberUploader.Uploader.create({
        paramName: "image-file",
        url: ENV.PHOTOS.serviceUrl + '/photos',
        ajaxSettings: {headers}
      });
      uploader.on('didUpload', () => {
        Ember.Logger.log("upload-form: didUpload called");
        this.trigger("didUpload");
        this.set("title", null);
        this.set("caption", null);
        this.set("previewUrl", null);
      });

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

  },
  actions: {
    filesChanged(files) {
      this.set("files", files);
      if (isEmpty(files)) {
        this.set("previewUrl", null);
      }
      else {
        let reader = new FileReader();
        reader.onload = (e) => {
          this.set("previewUrl", e.target.result);
        };
        reader.readAsDataURL(files[0]);
      }
      Ember.Logger.log("files changed");
    },
    upload() {
      Ember.Logger.log("upload called");
      this.doUpload();
    }
  }
});
