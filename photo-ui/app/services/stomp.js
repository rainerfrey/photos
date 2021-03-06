import { Promise } from 'rsvp';
import { A } from '@ember/array';
import { equal } from '@ember/object/computed';
import Service, { inject as service } from '@ember/service';
import Stomp from "stompjs";
import SockJS from "sockjs";
import ENV from "photo-ui/config/environment";

const States = {
    CONNECTED: "connected",
    DISCONNECTED: "disconnected",
    CONNECTING: "connecting"
};

const MAX_RECONNECT_COUNT = 3;

export default Service.extend({
    events: service(),
    session: service(),
    subscriptions: null,
    connectionPromise: null,
    connected: States.DISCONNECTED,
    reconnectCount: 0,

    isConnected: equal("connected", States.CONNECTED),
    isConnecting: equal("connected", States.CONNECTING),

    messagingSocketURL: function () {
        let token = "";
        let server = ENV.PHOTOS.serviceUrl || "http://localhost:8080";
        let value = this.get("session.data.authenticated.access_token");
        token = "?access_token=" + value;
        return server + "/ws" + token;
    },

    init: function () {
        this._super(...arguments);
        this.subscriptions = A();
        this.events.on("loggedIn", this, this.start);
        // this.get("session").on("authenticationSucceeded", this, this.start);
        // this.start();
    },

    start: function () {
        this.set("connectionPromise", this._doConnect());
        if (this.connectionPromise !== null) {
            this.events.trigger("stompStarted");
        }
    },

    subscribe: function (target, action, context) {
        this.connectionPromise.then((client) => {
            let subscription = this._doSubscribe(client, target, action, context);
            this.subscriptions.pushObject(subscription);
        }).catch((error) => {
            if (this._reconnect()) {
                this.subscribe(target, action, context);
            }
            else {
                console.error("STOMP subscription failed permanently for ", target);
            }
        });
    },

    send: function (target, message) {
        this.connectionPromise.then((client => {
            this._doSend(client, target, message);
        })).catch((error) => {
            if (this._reconnect()) {
                this.send(target, message);
            }
            else {
                console.error("STOMP sending failed permanently for ", target);
            }
        });
    },

    _doSubscribe: function (client, target, action, context) {
        let messageHandler = function (response) {
            let message = JSON.parse(response.body);
            action.call(context, message);
        };
        return client.subscribe(target, messageHandler);
    },

    _doSend: function (client, target, message) {
        client.send(target, {}, JSON.stringify(message));
        // client.send(target, {}, message);
    },

    _doConnect: function () {
        if (this.isConnecting) {
            return this.connectionPromise;
        }
        this.set("connected", States.CONNECTING);
        let url = this.messagingSocketURL();
        let socket = new SockJS(url);
        let stompClient = Stomp.over(socket);
        return new Promise((resolve, reject) => {
            stompClient.connect({}, () => {
                this.set("connected", States.CONNECTED);
                this.set("reconnectCount", 0);
                console.info("Stomp connected to: ", url);
                resolve(stompClient);
            }, (error) => {
                this.set("connected", States.DISCONNECTED);
                console.error("Connection error on: ", url, error);
                reject(error);
            });
        });
    },

    _reconnect: function () {
        this.incrementProperty("reconnectCount");
        if (this.reconnectCount <= MAX_RECONNECT_COUNT) {
            this.set("connectionPromise", this._doConnect());
            return true;
        }
        else {
            return false;
        }
    }
});
