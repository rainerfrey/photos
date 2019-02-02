import Component from '@ember/component';

export default Component.extend({
    comment: null,
    actions: {
        doComment() {
            this.get("comment-action")(this.get("photo"), this.get("comment"));
            this.set("comment", null);
        }
    }
});
