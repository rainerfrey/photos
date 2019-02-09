import {inject as service} from '@ember/service';
import Route from '@ember/routing/route';
import AuthenticatedRouteMixin from "ember-simple-auth/mixins/authenticated-route-mixin";

export default Route.extend(AuthenticatedRouteMixin, {
    comments: service(),
    photoCommands: service(),

    model(params) {
        return this.store.findRecord("photo", params.id);
    },

    actions: {
        comment(photo, comment) {
            this.comments.comment(photo, comment);
        },
        delete(photo) {
            return photo.destroyRecord().then(() => this.transitionTo("index"));
        },
        reprocess(photo) {
            return this.photoCommands.reprocess(photo.id);
        }
    }
});
