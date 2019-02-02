import { inject as service } from '@ember/service';
import OAuth2PasswordGrant from "ember-simple-auth/authenticators/oauth2-password-grant";

export default OAuth2PasswordGrant.extend({
    events: service(),
    serverTokenEndpoint: 'http://localhost:9999/oauth/token',
    clientId: 'photo-ui'
});
