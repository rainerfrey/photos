import HalAdapter from "ember-data-hal-9000/adapter";
import ENV from "photo-ui/config/environment";
import DataAdapterMixin from "ember-simple-auth/mixins/data-adapter-mixin";

export default HalAdapter.extend(DataAdapterMixin, {
    authorizer: "authorizer:oauth2",
    host: ENV.PHOTOS.serviceUrl,
    headers: {
        Accept: 'application/hal+json'
    }
});
