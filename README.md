# photos
Spring cloud demo application - photo processing and sharing. Experimenting with technologies, functional only by accident.

Needs MongoDB and RabbitMQ, default configuration expects them running on localhost with the respective default ports.

* `eureka-server`: Eureka service registry required for inter-service communication, port 8761
* `auth-server`: OAuth2 authorization server, setup for JWT tokens and password grants, port 9999
* `photo-store`: manages photo data in MongoDB, images are stored in MongoDB's GridFS. REST API requires OAuth2 authentication, image retrieval and upload of processed data is free. Port 8080
* `photo-processor`: extracts exif metadata and sends it to photo-store to save, port 9090 (management only)
* `image-scaler`: creates scaled versions of images and uploads them to photo-store, port 7070 (management only)
* `photo-ui`: EmberJS application to display, upload and comment photos, setup to access auth-server and photo-store on localhost. Port 4200 (with ember-cli)
