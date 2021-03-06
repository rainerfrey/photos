import {get} from '@ember/object';
import HalSerializer from "ember-data-hal-9000/serializer";

export default HalSerializer.extend({
    serialize(snapshot, options) {
        let json = {};

        if (options && options.includeId) {
            let id = snapshot.id;

            if (id) {
                json[this.primaryKey] = id;
            }
        }

        snapshot.eachAttribute((key, attribute) => {
            this.serializeAttribute(snapshot, json, key, attribute);
        });

        snapshot.eachRelationship((key, relationship) => {
            if (relationship.kind === 'belongsTo') {
                this.serializeBelongsTo(snapshot, json, relationship, options);
            } else if (relationship.kind === 'hasMany') {
                this.serializeHasMany(snapshot, json, relationship, options);
            }
        });

        return json;
    },

    serializeAttribute(snapshot, json, key, attribute) {
        if (this._canSerialize(key)) {
            let type = attribute.type;
            let value = snapshot.attr(key);
            if (type) {
                let transform = this.transformFor(type);
                value = transform.serialize(value, attribute.options);
            }

            // if provided, use the mapping provided by `attrs` in
            // the serializer
            let payloadKey = this._getMappedKey(key, snapshot.type);

            if (payloadKey === key && this.keyForAttribute) {
                payloadKey = this.keyForAttribute(key, 'serialize');
            }

            json[payloadKey] = value;
        }
    },

    serializeBelongsTo(snapshot, json, relationship, options) {
        let key = relationship.key;

        if (this._canSerialize(key)) {
            let belongsTo = snapshot.belongsTo(key);
            if (belongsTo !== undefined) {


                let payloadKey = this._getMappedKey(key, snapshot.type);
                if (payloadKey === key) {
                    payloadKey = this.keyForRelationship(key, 'belongsTo', 'serialize');
                }

                if (belongsTo) {
                    json._embedded = json._embedded || {};
                    json._embedded[key] = this.serialize(belongsTo, options);
                }
            }
        }
    },
    serializeHasMany(snapshot, json, relationship, options) {
        let key = relationship.key;

        if (this.shouldSerializeHasMany(snapshot, key, relationship)) {
            let hasMany = snapshot.hasMany(key);
            if (hasMany !== undefined) {

                json._embedded = json._embedded || {};
                json._embedded[key] = json._embedded[key] || [];

                let payloadKey = this._getMappedKey(key, snapshot.type);
                if (payloadKey === key && this.keyForRelationship) {
                    payloadKey = this.keyForRelationship(key, 'hasMany', 'serialize');
                }

                hasMany.forEach((item) => {
                    json._embedded[key].push(this.serialize(item, options));
                });
            }
        }
    }
});
