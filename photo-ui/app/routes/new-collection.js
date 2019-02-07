import Route from '@ember/routing/route';
import AuthenticatedRouteMixin from "ember-simple-auth/mixins/authenticated-route-mixin";

export default Route.extend(AuthenticatedRouteMixin, {
    model() {
        return this.store.createRecord("photoCollection");
    },

    actions: {
        create(collection) {
            return collection.save().then((newCollection)=> {this.transitionTo("photo-collection", newCollection.id);});
        },

        willTransition() {
            if (this.currentModel.get("isNew")) {
                this.currentModel.rollbackAttributes();
            }
            return true;
        }
    }
});
