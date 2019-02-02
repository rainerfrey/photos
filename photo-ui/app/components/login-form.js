import Component from '@ember/component';

export default Component.extend({
    classNames: ["xc_login-page", "xc_login-form"],
    tagName: "form",
    username: null,
    password: null,

    submit() {
        this.loginAction(this.username, this.password);
        return false;
    }
});
