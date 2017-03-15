import Ember from "ember";
import AuthenticatedRouteMixin from "ember-simple-auth/mixins/authenticated-route-mixin";

export default Ember.Route.extend(AuthenticatedRouteMixin, {
    photoUpdates: Ember.inject.service(),

    init() {
        this._super(...arguments);
        this.get("photoUpdates").on("newPhoto", this, this.onPhotoUpdate);
    },
    model(params) {
        return this.get("store").findRecord("photoCollection", params.id, {reload: true});
    },

    onPhotoUpdate(update) {
        Ember.Logger.log("photo-collection#onPhotoUpdate");
        this.get("store").findRecord("photo", update.get("photoId"), {reload: true}).then(photo => {
            Ember.Logger.log("photo-collection#onPhotoUpdate photo loaded " + photo.get("id"));
            if (photo.get("collectionId") === this.currentModel.get("id")) {
                Ember.Logger.log("photo-collection#onPhotoUpdate matched collection, refresh");
                this.refresh();
            }
        });
    }

});
