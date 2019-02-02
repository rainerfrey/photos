import Component from '@ember/component';
import { computed } from '@ember/object';
import { isPresent } from '@ember/utils';

export default Component.extend({
    caption: computed("photo.caption", "photo.fileName", function () {
        let caption = this.get("photo.caption");
        if (isPresent(caption)) {
            return caption;
        }
        else {
            return this.get("photo.fileName");
        }
    })
});
