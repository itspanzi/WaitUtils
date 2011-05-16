package com.thoughtworks.testing;

/**
 * @understands a collection of wait and assertion utils that gets rid off the need to explicitly wait using {@code Thread.sleep}.
 * <p/>
 * Typically functional tests need to wait for one or the other thing - may be clicking a link will do an ajax call which fetches
 * a bunch of text from a server. If you are manually testing this, you might have some sort of an indication - may be there is spinny
 * or the browser status bar is still active or may be you can just make out from an image that gets returned.
 * <p/>
 * One can think of number of ways to figure this out. However, if a functional tests needs to do the same, the wait becomes very subjective.
 * In fact, it is so subjective that typically, people end up with code which looks like this:
 * <p/>
 * <pre>
 *     Thread.sleep(10 * 1000);
 * </pre>
 * <p/>
 * What this means is: I, as the writer of this test, have no measurable way of saying what you, my test, should wait for. So, I am taking
 * a guess that you should always wait for 10 seconds. While this may work for most of the cases, this is not correct for a bunch of reasons.
 * <p/>
 * The most obvious way to solve it is just like how a human being does this. Wait for something that tells you that you need not
 * wait anymore. Thus, one can start using the API that the WaitUtil exposes safely in a way such that you would never have to rely on
 * the naive Thread.sleep approach.
 * <p/>
 * In fact, if this util is used right, one should never write Thread.sleep in their functional tests. They should always do a targeted wait
 */
public class WaitUtil {
    public static void waitUntil(Timeout timeout, Predicate predicate) {
        long before = System.currentTimeMillis();
        while (true) {
            bombIfTimesUp(timeout, before, predicate);
            try {
                if (predicate.call()) return;
                sleep(100);
            } catch (RuntimeException ignored) {
            }
        }
    }

    private static void bombIfTimesUp(Timeout timeout, long before, Predicate predicate) {
        if ((System.currentTimeMillis() - before) > timeout.time()) {
            throw new RuntimeException("Wait timed out. Reason: " + predicate.toString());
        }
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
