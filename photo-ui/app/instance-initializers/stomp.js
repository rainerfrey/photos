export function initialize(instance) {
    instance.lookup("service:events");
    instance.lookup("service:stomp");
    instance.lookup("service:photo-updates");
    instance.lookup("service:comments");
}

export default {
    name: 'stomp',
    initialize
};
