import Ember from "ember";

export default Ember.Component.extend({
    currentUser: Ember.inject.service(),
    photoUpdates: Ember.inject.service(),
    user: Ember.computed.alias("currentUser.user.name")
});
