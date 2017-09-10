import Ember from "ember";

export default Ember.Component.extend({
    classNames: ["xc_login-page", "xc_login-form"],
    tagName: "form",
    username: null,
    password: null,

    submit() {
        this.get("loginAction")(this.get("username"), this.get("password"));
        return false;
    }
});
