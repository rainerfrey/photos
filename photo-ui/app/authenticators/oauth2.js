import Ember from "ember";
import OAuth2PasswordGrant from 'ember-simple-auth/authenticators/oauth2-password-grant';

export default OAuth2PasswordGrant.extend({
  events: Ember.inject.service(),
  serverTokenEndpoint: 'http://localhost:9999/oauth/token',
  clientId: 'photo-ui'
});
