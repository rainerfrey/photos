import Ember from "ember";
import config from "./config/environment";

const Router = Ember.Router.extend({
    location: config.locationType,
    rootURL: config.rootURL
});

Router.map(function () {
    this.route("login");
    this.route("photo", {path: "/photos/:id"});
    this.route("photos");
    this.route("upload");
    this.route("photoCollections", {path: "/photo-collections"});
    this.route("photoCollection", {path: "/photo-collections/:id"});
    this.route("newCollection", {path: "/photo-collections/new"});
});

export default Router;
