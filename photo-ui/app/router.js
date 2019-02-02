import EmberRouter from '@ember/routing/router';
import config from './config/environment';

const Router = EmberRouter.extend({
  location: config.locationType,
  rootURL: config.rootURL
});

Router.map(function () {
    this.route("login");
    this.route("photo", {path: "/photos/:id"});
    this.route("photos");
    this.route("upload");
    this.route("photo-collections", {path: "/photo-collections"});
    this.route("photo-collection", {path: "/photo-collections/:id"});
    this.route("new-collection", {path: "/photo-collections/new"});
});

export default Router;
