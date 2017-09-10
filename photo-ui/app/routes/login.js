import Ember from "ember";
import UnauthenticatedRouteMixin from "ember-simple-auth/mixins/unauthenticated-route-mixin";

export default Ember.Route.extend(UnauthenticatedRouteMixin, {
    session: Ember.inject.service(),
    events: Ember.inject.service(),
    actions: {
        login: function (username, password) {
            Ember.Logger.info("Logging in");
            return this.get("session").authenticate("authenticator:oauth2", username, password, "photo-ui").then(() => {
                Ember.Logger.info("Logged in");
                Ember.Logger.info(JSON.stringify(this.get("session.data")));
                this.get("events").trigger("loggedIn");
            }).catch( (error) => {
                Ember.Logger.error(JSON.stringify(error));
                throw error;
            });
        }
    }
});
