import { inject as service } from '@ember/service';
import Evented from '@ember/object/evented';
import Component from '@ember/component';
import { isPresent, isEmpty } from '@ember/utils';
import { computed } from '@ember/object';
import Ember from "ember";
import EmberUploader from "ember-uploader";
import ENV from "photo-ui/config/environment";

export default Component.extend(Evented, {
    session: service(),
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
            uploader.on('didUpload', (event) => {
                this.trigger("didUpload", event);
                this.set("title", null);
                this.set("caption", null);
                this.set("previewUrl", null);
            });

            let title = this.get("title");
            let caption = this.get("caption");
            let collection = this.get("collection");

            let extra = {};
            if (isPresent(title)) {
                extra.title = title;
            }
            if (isPresent(caption)) {
                extra.caption = caption;
            }
            if (isPresent(collection)) {
                extra.collectionId = collection.get("id");
            }
            // this second argument is optional and can to be sent as extra data with the upload
            uploader.upload(files[0], extra).then(data=>{
                let onUpload = this.get("onUploadSubmitted");
                if(onUpload) {
                    onUpload(data);
                }
            });
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
