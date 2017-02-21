import Ember from "ember";

export default Ember.Component.extend({
    comment: null,
    actions: {
        doComment() {
            this.get("comment-action")(this.get("photo"), this.get("comment"));
            this.set("comment", null);
        }
    }
});
