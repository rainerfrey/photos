import Ember from "ember";
import {isUnauthorizedError} from "ember-ajax/errors";
export default Ember.Service.extend({
  ajax: Ember.inject.service(),
  session: Ember.inject.service(),
  user: null,

  load() {
    return new Ember.RSVP.Promise((resolve, reject) => {
      this.get("ajax").request("/user").then((user) => {
        this.set("user", user);
        resolve(user);
      }).catch((error) => {
        Ember.Logger.error(JSON.stringify(error));
        if (isUnauthorizedError(error) && this.get('session.isAuthenticated')) {
          this.get('session').invalidate();
        }
        reject(error);
      });
    });
  }
});
