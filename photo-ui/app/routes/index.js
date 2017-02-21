import Ember from "ember";
import AuthenticatedRouteMixin from "ember-simple-auth/mixins/authenticated-route-mixin";

export default Ember.Route.extend(AuthenticatedRouteMixin, {
    photoUpdates: Ember.inject.service(),

    init() {
        this._super(...arguments);
        this.get("photoUpdates").on("newPhoto", () => {
            this.refresh();
        });
    },

    model() {
        return this.get("store").findAll("photo", {reload: true}).then((photos) => {
            return photos.get("lastObject");
        });
    }
});
