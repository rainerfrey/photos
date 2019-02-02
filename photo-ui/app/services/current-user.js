import { Promise } from 'rsvp';
import Service, { inject as service } from '@ember/service';
import Ember from "ember";
import { isUnauthorizedError } from "ember-ajax/errors";
export default Service.extend({
    ajax: service(),
    session: service(),
    user: null,

    load() {
        return new Promise((resolve, reject) => {
            this.ajax.request("/user").then((user) => {
                this.set("user", user);
                resolve(user);
            }).catch((error) => {
                Ember.Logger.error(JSON.stringify(error));
                if (isUnauthorizedError(error) && this.get('session.isAuthenticated')) {
                    this.session.invalidate();
                }
                reject(error);
            });
        });
    }
});
