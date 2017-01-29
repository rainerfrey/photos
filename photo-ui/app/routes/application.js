import Ember from "ember";
import ApplicationRouteMixin from "ember-simple-auth/mixins/application-route-mixin";
import {isUnauthorizedError} from "ember-ajax/errors";

export default Ember.Route.extend(ApplicationRouteMixin, {
  session: Ember.inject.service(),
  events: Ember.inject.service(),
  ajax: Ember.inject.service(),
  updates: Ember.inject.service("photo-updates"),
  comments: Ember.inject.service(),
  root: null,

  model() {
    this.get("ajax").request("/").then((root) => {
      Ember.Logger.log(this.get("session.isAuthenticated"));
      if (this.get("session.isAuthenticated")) {
        this.get("events").trigger("loggedIn");
      }
      this.set("root", root);
    }).catch((error) => {
      Ember.Logger.warn(JSON.stringify(error));
      if (isUnauthorizedError(error) && this.get("session.isAuthenticated")) {
        this.get("session").invalidate();
      }
    });
    return Ember.Object.create({
      isAuthenticated: Ember.computed.alias("session.isAuthenticated"),
      updates: this.get("updates.myUpdates"),
      newPhotos: this.get("updates.newPhotos"),
      liveComments: this.get("comments.liveComments")
    });
  },

  // sessionInvalidated() {
  // },

  actions: {
    logout() {
      this.get("session").invalidate();
    }
  }
});
