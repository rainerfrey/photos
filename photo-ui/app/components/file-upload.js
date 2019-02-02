import Ember from "ember";
import EmberUploader from "ember-uploader";

export default EmberUploader.FileField.extend({
    filesDidChange(files) {
        this.filesChangedAction(files);
    },
    didInsertElement() {
        this.uploadEvents.on("didUpload", () => {
            Ember.Logger.log("file-upload: didUpload called");
            this.$().val(null);
        });
    }
});
