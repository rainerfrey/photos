import Evented from '@ember/object/evented';
import Service, { inject as service } from '@ember/service';
import Ember from "ember";
import Update from "photo-ui/models/update";

const MAX_UPDATES = 10;

export default Service.extend(Evented, {
    stomp: service(),
    events: service(),
    newPhotos: null,
    myUpdates: null,
    myCount: null,

    init() {
        this._super(...arguments);
        this.set("newPhotos", []);
        this.set("myUpdates", []);
        this.events.on("stompStarted", () => {
            this.stomp.subscribe("/app/count", (m) => {
                this.set("myCount", m.count);
            }, this);
            this.stomp.subscribe("/topic/photos", this.newPhoto, this);
            this.stomp.subscribe("/user/exchange/amq.direct/photo.update", this.photoUpdate, this);
            this.stomp.subscribe("/user/exchange/amq.direct/errors", this.onError, this);
        });
    },

    newPhoto(message) {
        this.updateList(message, this.newPhotos);
        this.trigger("newPhoto", Update.create(message));
    },

    photoUpdate(message) {
        this.updateList(message, this.myUpdates);
    },

    updateList(message, target) {
        target.pushObject(Update.create(message));
        if (target.get("length") > MAX_UPDATES) {
            target.shiftObject();
        }
    },

    onError(message) {
        Ember.Logger.warn(JSON.stringify(message));
    }
});
