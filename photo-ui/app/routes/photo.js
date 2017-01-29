import Ember from "ember";
import AuthenticatedRouteMixin from "ember-simple-auth/mixins/authenticated-route-mixin";

export default Ember.Route.extend(AuthenticatedRouteMixin, {
  comments: Ember.inject.service(),

  model(params) {
    return this.get("store").findRecord("photo", params.id);
  },

  actions: {
    comment(photo, comment) {
      this.get("comments").comment(photo, comment);
    }
  }
});
