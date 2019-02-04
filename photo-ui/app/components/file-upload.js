import EmberUploader from "ember-uploader";

export default EmberUploader.FileField.extend({
    filesDidChange(files) {
        this.filesChangedAction(files);
    },
    didInsertElement() {
        this.uploadEvents.on("didUpload", () => {
            console.log("file-upload: didUpload called");
            this.$().val(null);
        });
    }
});
