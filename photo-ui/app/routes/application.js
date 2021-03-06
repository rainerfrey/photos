import { alias } from '@ember/object/computed';
import EmberObject from '@ember/object';
import { inject as service } from '@ember/service';
import Route from '@ember/routing/route';
import ApplicationRouteMixin from "ember-simple-auth/mixins/application-route-mixin";

export default Route.extend(ApplicationRouteMixin, {
    session: service(),
    events: service(),
    ajax: service(),
    updates: service("photo-updates"),
    comments: service(),
    currentUser: service(),
    root: null,

    model() {
        this._loadCurrentUser().then(() => {
            this.events.trigger("loggedIn");
        });
        return EmberObject.create({
            isAuthenticated: alias("session.isAuthenticated"),
            updates: this.get("updates.myUpdates"),
            newPhotos: this.get("updates.newPhotos"),
            liveComments: this.get("comments.liveComments")
        });
    },

    sessionAuthenticated() {
        this._super(...arguments);
        console.info("Session authenticated");
        this._loadCurrentUser();
    },

    _loadCurrentUser() {
        return this.currentUser.load();
    },

    actions: {
        logout() {
            this.session.invalidate();
        }
    }
});
