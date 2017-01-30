import Ember from  "ember";
import AjaxService from 'ember-ajax/services/ajax';
import ENV from "photo-ui/config/environment";

export default AjaxService.extend({
  session: Ember.inject.service(),
  trustedHosts: ["localhost","localhost:8080"],
  host: ENV.PHOTOS.serviceUrl,
    headers: Ember.computed('session.data.authenticated.access_token', function() {
        let headers = {};
        this.get('session').authorize('authorizer:oauth2', (headerName, headerValue) => {
            headers[headerName] = headerValue;
        });
        return headers;
    })
});
