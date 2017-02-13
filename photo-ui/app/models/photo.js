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
  thumbnailUrl: alias("data.links.image:thumbnail"),
  scaledUrl: alias("data.links.image:scaled"),
  originalUrl: alias("data.links.image:original"),
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
