import { alias } from '@ember/object/computed';
import { computed } from '@ember/object';
import { isPresent } from '@ember/utils';
import DS from "ember-data";
const {attr} = DS;

export default DS.Model.extend({
    fileName: attr("string"),
    owner: attr("string"),
    title: attr("string"),
    caption: attr("string"),
    metadata: attr(),
    comments: attr(),
    links: attr(),
    collectionId: attr("string"),
    thumbnailUrl: alias("links.image:thumbnail"),
    scaledUrl: alias("links.image:scaled"),
    originalUrl: alias("links.image:original"),
    displayName: computed("title", "fileName", function () {
        let title = this.title;
        if (isPresent(title)) {
            return title;
        }
        else {
            return this.fileName;
        }
    })
});
