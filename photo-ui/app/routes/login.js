import { inject as service } from '@ember/service';
import Route from '@ember/routing/route';
import Ember from "ember";
import UnauthenticatedRouteMixin from "ember-simple-auth/mixins/unauthenticated-route-mixin";

export default Route.extend(UnauthenticatedRouteMixin, {
    session: service(),
    events: service(),
    actions: {
        login: function (username, password) {
            Ember.Logger.info("Logging in");
            return this.session.authenticate("authenticator:oauth2", username, password, "photo-ui").then(() => {
                Ember.Logger.info("Logged in");
                Ember.Logger.info(JSON.stringify(this.get("session.data")));
                this.events.trigger("loggedIn");
            }).catch( (error) => {
                Ember.Logger.error(JSON.stringify(error));
                throw error;
            });
        }
    }
});
