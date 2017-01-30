import Ember from "ember";

export default Ember.Service.extend( {
    ajax: Ember.inject.service(),
    session: Ember.inject.service(),
    user: null,

    load() {
        this.get( "ajax" ).request( "/user" ).then( ( user ) => {
            this.set( "user", user );
        } ).catch( () => {
            if( this.get( 'session.isAuthenticated' ) ) {
                this.get( 'session' ).invalidate();
            }
        } );
    }
} );