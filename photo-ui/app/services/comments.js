import Service, { inject as service } from '@ember/service';
import Comment from "photo-ui/models/comment";

const MAX_UPDATES = 10;

export default Service.extend({
    stomp: service(),
    events: service(),
    liveComments: null,

    init() {
        this._super(...arguments);
        this.set("liveComments", []);
        this.events.on("stompStarted", () => {
            this.stomp.subscribe("/topic/comments", this.onMessage, this);
        });
    },

    comment(photo, comment) {
        let message = {
            photoId: photo.get("id"),
            comment: comment
        };
        this.stomp.send("/app/comments", message);
    },

    onMessage(message) {
        console.info(message);
        let target = this.liveComments;
        target.pushObject(Comment.create(message));
        if (target.get("length") > MAX_UPDATES) {
            target.shiftObject();
        }
    }
});
