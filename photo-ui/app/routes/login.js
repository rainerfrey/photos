import { inject as service } from '@ember/service';
import Route from '@ember/routing/route';
import UnauthenticatedRouteMixin from "ember-simple-auth/mixins/unauthenticated-route-mixin";

export default Route.extend(UnauthenticatedRouteMixin, {
    session: service(),
    events: service(),
    actions: {
        login: function (username, password) {
            console.info("Logging in");
            return this.session.authenticate("authenticator:oauth2", username, password, "photo-ui").then(() => {
                console.info("Logged in");
                console.info(JSON.stringify(this.get("session.data")));
                this.events.trigger("loggedIn");
            }).catch( (error) => {
                console.error(JSON.stringify(error));
                throw error;
            });
        }
    }
});
