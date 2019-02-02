import { inject as service } from '@ember/service';
import Route from '@ember/routing/route';
import AuthenticatedRouteMixin from "ember-simple-auth/mixins/authenticated-route-mixin";

export default Route.extend(AuthenticatedRouteMixin, {
    photoUpdates: service(),

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
