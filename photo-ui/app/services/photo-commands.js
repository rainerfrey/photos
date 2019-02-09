import Service, {inject as service} from '@ember/service';


export default Service.extend({
    stomp: service(),

    reprocess(photoId) {
        this.stomp.send(`/app/reprocess/${photoId}`, {
            command: "ALL"
        });
    }
});
