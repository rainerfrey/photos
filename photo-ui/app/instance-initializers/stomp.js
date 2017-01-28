export function initialize(instance) {
  instance.lookup( "service:events" );
  instance.lookup( "service:stomp" );
  instance.lookup( "service:photo-updates" );
}

export default {
  name: 'stomp',
  initialize
};
