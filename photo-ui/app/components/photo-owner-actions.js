import {computed} from '@ember/object';
import { isEqual } from '@ember/utils';
import {inject as service} from '@ember/service';
import Component from '@ember/component';


export default Component.extend({
    tagName: '',
    currentUser: service(),
    isMyPhoto: computed('currentUser.user.name', 'photo.owner', function() {
        return isEqual(this.currentUser.user.name, this.photo.owner)
    }),
    actions: {
        deletePhoto() {
            this.deleteAction(this.photo);
        },
        reprocessPhoto() {
            this.reprocessAction(this.photo);
        }
    }
});
