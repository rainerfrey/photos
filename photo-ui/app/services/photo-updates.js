import Ember from "ember";
import Update from "photo-ui/models/update";

const MAX_UPDATES = 10;

export default Ember.Service.extend({
  stomp: Ember.inject.service(),
  events: Ember.inject.service(),
  newPhotos: null,
  myUpdates: null,
    myCount: null,

  init() {
    this._super(...arguments);
    this.set("newPhotos", []);
    this.set("myUpdates", []);
    this.get("events").on("stompStarted", () => {
      this.get("stomp").subscribe("/app/count", (m)=> {this.set("myCount", m.count);}, this);
      this.get("stomp").subscribe("/topic/photos", (m)=>{this.onMessage(m,this.get("newPhotos"));}, this);
      this.get("stomp").subscribe("/user/exchange/amq.direct/photo.update", (m)=>{this.onMessage(m,this.get("myUpdates"));}, this);
    });
  },

  onMessage(message, target) {
    target.pushObject(Update.create(message));
    if (target.get("length") > MAX_UPDATES) {
      target.shiftObject();
    }
  }
});
