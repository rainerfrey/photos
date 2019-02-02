import { inject as service } from '@ember/service';
import Route from '@ember/routing/route';
import Ember from "ember";
import AuthenticatedRouteMixin from "ember-simple-auth/mixins/authenticated-route-mixin";

export default Route.extend(AuthenticatedRouteMixin, {
    photoUpdates: service(),

    init() {
        this._super(...arguments);
        this.photoUpdates.on("newPhoto", this, this.onPhotoUpdate);
    },
    model(params) {
        return this.store.findRecord("photoCollection", params.id, {reload: true});
    },

    onPhotoUpdate(update) {
        Ember.Logger.log("photo-collection#onPhotoUpdate");
        this.store.findRecord("photo", update.get("photoId"), {reload: true}).then(photo => {
            Ember.Logger.log("photo-collection#onPhotoUpdate photo loaded " + photo.get("id"));
            if (photo.get("collectionId") === this.currentModel.get("id")) {
                Ember.Logger.log("photo-collection#onPhotoUpdate matched collection, refresh");
                this.refresh();
            }
        });
    }

});
