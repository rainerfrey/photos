import Ember from "ember";
const {computed, isPresent} = Ember;

export default Ember.Component.extend({
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
