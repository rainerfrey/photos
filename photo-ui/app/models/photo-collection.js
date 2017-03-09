import DS from "ember-data";

const {Model, attr, hasMany} = DS;

export default Model.extend({
    title: attr("string"),
    description: attr("string"),
    owner: attr("string"),
    gpsLocation: attr(),
    photos: hasMany("photo")
});
