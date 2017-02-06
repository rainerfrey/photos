import Ember from "ember";
import DS from "ember-data";
const {attr} = DS;
const {computed} = Ember;

export default DS.Model.extend({
  fileName: attr("string"),
  owner: attr("string"),
  caption: attr("string"),
  metadata: attr(),
  comments: attr(),
  thumbnailUrl: computed.alias("data.links.image:thumbnail"),
  scaledUrl: computed.alias("data.links.image:scaled")
});
