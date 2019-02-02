## Module Report
### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/components/file-upload.js` at line 10

```js
    didInsertElement() {
        this.get("uploadEvents").on("didUpload", () => {
            Ember.Logger.log("file-upload: didUpload called");
            this.$().val(null);
        });
```

### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/components/upload-form.js` at line 76

```js
                reader.readAsDataURL(files[0]);
            }
            Ember.Logger.log("files changed");
        },
        upload() {
```

### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/components/upload-form.js` at line 79

```js
        },
        upload() {
            Ember.Logger.log("upload called");
            this.doUpload();
        }
```

### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/services/comments.js` at line 28

```js

    onMessage(message) {
        Ember.Logger.info(message);
        let target = this.get("liveComments");
        target.pushObject(Comment.create(message));
```

### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/services/current-user.js` at line 14

```js
                resolve(user);
            }).catch((error) => {
                Ember.Logger.error(JSON.stringify(error));
                if (isUnauthorizedError(error) && this.get('session.isAuthenticated')) {
                    this.get('session').invalidate();
```

### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/services/stomp.js` at line 56

```js
            }
            else {
                Ember.Logger.error("STOMP subscription failed permanently for ", target);
            }
        });
```

### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/services/stomp.js` at line 69

```js
            }
            else {
                Ember.Logger.error("STOMP sending failed permanently for ", target);
            }
        });
```

### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/services/stomp.js` at line 99

```js
                this.set("connected", States.CONNECTED);
                this.set("reconnectCount", 0);
                Ember.Logger.info("Stomp connected to: ", url);
                resolve(stompClient);
            }, (error) => {
```

### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/services/stomp.js` at line 103

```js
            }, (error) => {
                this.set("connected", States.DISCONNECTED);
                Ember.Logger.error("Connection error on: ", url, error);
                reject(error);
            });
```

### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/services/photo-updates.js` at line 44

```js

    onError(message) {
        Ember.Logger.warn(JSON.stringify(message));
    }
});
```

### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/routes/application.js` at line 27

```js
    sessionAuthenticated() {
        this._super(...arguments);
        Ember.Logger.info("Session authenticated");
        this._loadCurrentUser();
    },
```

### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/routes/login.js` at line 9

```js
    actions: {
        login: function (username, password) {
            Ember.Logger.info("Logging in");
            return this.get("session").authenticate("authenticator:oauth2", username, password, "photo-ui").then(() => {
                Ember.Logger.info("Logged in");
```

### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/routes/login.js` at line 11

```js
            Ember.Logger.info("Logging in");
            return this.get("session").authenticate("authenticator:oauth2", username, password, "photo-ui").then(() => {
                Ember.Logger.info("Logged in");
                Ember.Logger.info(JSON.stringify(this.get("session.data")));
                this.get("events").trigger("loggedIn");
```

### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/routes/login.js` at line 12

```js
            return this.get("session").authenticate("authenticator:oauth2", username, password, "photo-ui").then(() => {
                Ember.Logger.info("Logged in");
                Ember.Logger.info(JSON.stringify(this.get("session.data")));
                this.get("events").trigger("loggedIn");
            }).catch( (error) => {
```

### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/routes/login.js` at line 15

```js
                this.get("events").trigger("loggedIn");
            }).catch( (error) => {
                Ember.Logger.error(JSON.stringify(error));
                throw error;
            });
```

### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/routes/photo-collection.js` at line 16

```js

    onPhotoUpdate(update) {
        Ember.Logger.log("photo-collection#onPhotoUpdate");
        this.get("store").findRecord("photo", update.get("photoId"), {reload: true}).then(photo => {
            Ember.Logger.log("photo-collection#onPhotoUpdate photo loaded " + photo.get("id"));
```

### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/routes/photo-collection.js` at line 18

```js
        Ember.Logger.log("photo-collection#onPhotoUpdate");
        this.get("store").findRecord("photo", update.get("photoId"), {reload: true}).then(photo => {
            Ember.Logger.log("photo-collection#onPhotoUpdate photo loaded " + photo.get("id"));
            if (photo.get("collectionId") === this.currentModel.get("id")) {
                Ember.Logger.log("photo-collection#onPhotoUpdate matched collection, refresh");
```

### Unknown Global

**Global**: `Ember.Logger`

**Location**: `app/routes/photo-collection.js` at line 20

```js
            Ember.Logger.log("photo-collection#onPhotoUpdate photo loaded " + photo.get("id"));
            if (photo.get("collectionId") === this.currentModel.get("id")) {
                Ember.Logger.log("photo-collection#onPhotoUpdate matched collection, refresh");
                this.refresh();
            }
```
