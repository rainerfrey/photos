import { alias } from '@ember/object/computed';
import { inject as service } from '@ember/service';
import Component from '@ember/component';

export default Component.extend({
    currentUser: service(),
    photoUpdates: service(),
    user: alias("currentUser.user.name")
});
