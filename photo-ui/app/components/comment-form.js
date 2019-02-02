import Component from '@ember/component';

export default Component.extend({
    comment: null,
    actions: {
        doComment() {
            this["comment-action"](this.photo, this.comment);
            this.set("comment", null);
        }
    }
});
