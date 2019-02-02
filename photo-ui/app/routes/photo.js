import { inject as service } from '@ember/service';
import Route from '@ember/routing/route';
import AuthenticatedRouteMixin from "ember-simple-auth/mixins/authenticated-route-mixin";

export default Route.extend(AuthenticatedRouteMixin, {
    comments: service(),

    model(params) {
        return this.store.findRecord("photo", params.id);
    },

    actions: {
        comment(photo, comment) {
            this.comments.comment(photo, comment);
        }
    }
});
