import AjaxService from 'ember-ajax/services/ajax';
import ENV from "photo-ui/config/environment";

export default AjaxService.extend({
  trustedHosts: ["localhost","localhost:8080"],
  host: ENV.PHOTOS.serviceUrl
});
