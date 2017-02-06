import Ember from "ember";
import ApplicationRouteMixin from "ember-simple-auth/mixins/application-route-mixin";

export default Ember.Route.extend(ApplicationRouteMixin, {
  session: Ember.inject.service(),
  events: Ember.inject.service(),
  ajax: Ember.inject.service(),
  updates: Ember.inject.service("photo-updates"),
  comments: Ember.inject.service(),
  currentUser: Ember.inject.service(),
  root: null,

  model() {
    this._loadCurrentUser().then(() => {
      this.get("events").trigger("loggedIn");
    });
    return Ember.Object.create({
      isAuthenticated: Ember.computed.alias("session.isAuthenticated"),
      updates: this.get("updates.myUpdates"),
      newPhotos: this.get("updates.newPhotos"),
      liveComments: this.get("comments.liveComments")
    });
  },

  sessionAuthenticated() {
    this._super(...arguments);
    Ember.Logger.info("Session authenticated");
    this._loadCurrentUser();
  },

  _loadCurrentUser() {
    return this.get('currentUser').load();
  },

  actions: {
    logout() {
      this.get("session").invalidate();
    }
  }
});
