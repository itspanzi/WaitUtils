package com.thoughtworks.testing;

/**
 * @understands the concept of the maximum time that an operation should be performed for
 */
public interface Timeout {
    
    Timeout ONE_SECOND = new Timeout() {
        public long time() {
            return 1 * 1000L;
        }
    };
    Timeout TWO_SECONDS = new Timeout() {
        public long time() {
            return 2 * ONE_SECOND.time();
        }
    };
    Timeout FIVE_SECONDS = new Timeout() {
        public long time() {
            return 5 * ONE_SECOND.time();
        }
    };
    Timeout TEN_SECONDS = new Timeout() {
        public long time() {
            return 10 * ONE_SECOND.time();
        }
    };
    Timeout FIFTEEN_SECONDS = new Timeout() {
        public long time() {
            return 15 * ONE_SECOND.time();
        }
    };
    Timeout TWENTY_SECONDS = new Timeout() {
        public long time() {
            return 20 * ONE_SECOND.time();
        }
    };
    Timeout TWENTY_FIVE_SECONDS = new Timeout() {
        public long time() {
            return 25 * ONE_SECOND.time();
        }
    };
    Timeout THIRTY_SECONDS = new Timeout() {
        public long time() {
            return 30 * ONE_SECOND.time();
        }
    };
    Timeout ONE_MINUTE = new Timeout() {
        public long time() {
            return 60 * ONE_SECOND.time();
        }
    };
    Timeout TWO_MINUTES = new Timeout() {
        public long time() {
            return 2 * ONE_MINUTE.time();
        }
    };
    Timeout FIVE_MINUTES = new Timeout() {
        public long time() {
            return 2 * ONE_MINUTE.time();
        }
    };

    public long time();
}