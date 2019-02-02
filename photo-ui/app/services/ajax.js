import { computed } from '@ember/object';
import { inject as service } from '@ember/service';
import AjaxService from "ember-ajax/services/ajax";
import ENV from "photo-ui/config/environment";

export default AjaxService.extend({
    session: service(),
    trustedHosts: ["localhost", "localhost:8080"],
    host: ENV.PHOTOS.serviceUrl,
    headers: computed('session.data.authenticated.access_token', function () {
        let headers = {};
        this.get('session').authorize('authorizer:oauth2', (headerName, headerValue) => {
            headers[headerName] = headerValue;
        });
        return headers;
    })
});
