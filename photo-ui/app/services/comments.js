import Ember from "ember";
import Comment from "photo-ui/models/comment";

const MAX_UPDATES = 10;

export default Ember.Service.extend({
    stomp: Ember.inject.service(),
    events: Ember.inject.service(),
    liveComments: null,

    init() {
        this._super(...arguments);
        this.set("liveComments", []);
        this.get("events").on("stompStarted", () => {
            this.get("stomp").subscribe("/topic/comments", this.onMessage, this);
        });
    },

    comment(photo, comment) {
        let message = {
            photoId: photo.get("id"),
            comment: comment
        };
        this.get("stomp").send("/app/comments", message);
    },

    onMessage(message) {
        Ember.Logger.info(message);
        let target = this.get("liveComments");
        target.pushObject(Comment.create(message));
        if (target.get("length") > MAX_UPDATES) {
            target.shiftObject();
        }
    }
});
