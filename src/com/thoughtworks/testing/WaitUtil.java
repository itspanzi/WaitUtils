package com.thoughtworks.testing;

/**
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
 *
 * @understands a collection of wait and assertion utils that gets rid off the need to explicitly wait using {@code Thread.sleep}.
 *
 */
public class WaitUtil {

    /**
     * Wait until the given predicate evaluates to true. However, bomb if the wait exceeds the given timeout.
     * <p/>
     * This method should be used when you want your code to wait for some condition to be true. This is typically useful in cases where you
     * perform an operation asynchronously. Imagine you are writing a functional test, where you click a button inside a browser and it does an
     * ajax request to fetch that data. Your test needs to wait until the data is fetched.
     * <p/>
     * If you continue, the test just fails. Typically one would resort to a Thread.sleep in this case. Instead, since you know exactly what the
     * result of your operation i.e. clicking the button is, you should wait for something more concrete like presence of a text etc.
     * <p/>
     * That is what you would encapsulate inside the predicate object - whether the text is present or not? waitUntil in turn waits a maximum for the given
     * timeout and then bombs. This makes sense because if the ajax call had a bug and did not return what you expected or did not return at all or did not return
     * within acceptable time limits, you want to fail the test.
     * <p/>
     * If the predicate returns a <strong>true</strong> within that time, the method returns. Otherwise it throws an exception.
     *
     * @param timeout The maximum timeout to wait for the predicate to be true
     * @param predicate The condition that needs to be waited for
     */
    public static void waitUntil(Timeout timeout, Predicate predicate) {
        long before = System.currentTimeMillis();
        while (true) {
            bombIfTimesUp(timeout, before, predicate.toString());
            try {
                if (predicate.call()) return;
                sleep(100);
            } catch (RuntimeException ignored) {
            }
        }
    }

    private static void bombIfTimesUp(Timeout timeout, long before, String message) {
        if ((System.currentTimeMillis() - before) > timeout.time()) {
            throw new RuntimeException("Wait timed out. Reason: " + message);
        }
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wait for a object to be present. Once the object is present, return it. However, bomb if the wait exceeds the given timeout.
     * <p/>
     * This method should be used when you want your code to get a response of some sort. This is particularly useful when you are testing
     * things with a rich domain model in tests. What that means is, for example, lets say you are using Selenium 2.0 (Webdriver) or White for
     * testing. They provide abstractions over the UI elements that are present like a "Button" object, "ListItem" object etc. If you perform some action
     * that is asynchronous and you need to wait for one of these objects to be present, then you can use the waitFor method.
     * <p/>
     * The Function object represents some operation that you need to perform which returns the object you are waiting for.
     * <p/>
     * The waitFor method returns the object that you are waiting for. This is what makes it useful. You can always create a {@code com.thoughtworks.tests.Predicate} to
     * make sure that you wait for the object to be present using a condition and use {@code waitUntil}. However, once the waitUntil returns, you need to duplicate the code
     * to get hold of that object again. This is what waitFor aims to avoid.
     * <p/>
     * waitFor in turn waits a maximum for the given timeout and then bombs. This makes sense because if the function did not return what you expected or returned a null or did not return
     * within acceptable time limits, you want to fail the test.
     * <p/>
     * If the function returns a non-null object within timeout time, the method returns that object. Otherwise it throws an exception.
     *
     * @param timeout The maximum timeout to wait for the predicate to be true
     * @param function The abstraction that represents an operation that would return the object that we are waiting for
     * @return The object returned by the param function
     */
    public static <T> T waitFor(Timeout timeout, Function<T> function) {
        long before = System.currentTimeMillis();
        while (true) {
            bombIfTimesUp(timeout, before, function.toString());
            try {
                T value = function.call();
                if (value != null) {
                    return value;
                }
                sleep(100);
            } catch (RuntimeException ignored) {
            }
        }
    }
}
