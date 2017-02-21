import Ember from "ember";
import DS from "ember-data";
const {attr} = DS;
const {computed, isPresent} = Ember;
const {alias} = computed;

export default DS.Model.extend({
    fileName: attr("string"),
    owner: attr("string"),
    title: attr("string"),
    caption: attr("string"),
    metadata: attr(),
    comments: attr(),
    links: attr(),
    thumbnailUrl: alias("links.image:thumbnail"),
    scaledUrl: alias("links.image:scaled"),
    originalUrl: alias("links.image:original"),
    displayName: computed("title", "fileName", function () {
        let title = this.get("title");
        if (isPresent(title)) {
            return title;
        }
        else {
            return this.get("fileName");
        }
    })
});
