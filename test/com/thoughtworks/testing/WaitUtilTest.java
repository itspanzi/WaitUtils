package com.thoughtworks.testing;

import org.junit.Test;
import org.mockito.internal.matchers.GreaterThan;

import static junit.framework.Assert.fail;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.Mockito.*;

public class WaitUtilTest {

    @Test
    public void waitUntil_shouldReturnAsSoonAsThePredicateIsTrue() {
        Predicate predicate = mock(Predicate.class);
        when(predicate.call()).thenReturn(false).thenThrow(new RuntimeException("Still not true yet")).thenReturn(true);

        WaitUtil.waitUntil(Timeout.TWENTY_SECONDS, predicate);

        verify(predicate, times(3)).call();
    }

    @Test
    public void waitUntil_shouldWaitForAMaximumOfTimeoutWhenThePredicateIsFalse() throws InterruptedException {
        Predicate predicate = mock(Predicate.class);
        when(predicate.call()).thenReturn(false);

        long before = System.currentTimeMillis();
        Thread.sleep(1);
        try {
            WaitUtil.waitUntil(Timeout.TEN_SECONDS, predicate);
            fail("Should have thrown an exception as we the predicate always returns false");
        } catch (Exception e) {
            long actual = System.currentTimeMillis() - before;
            assertThat(String.format("The wait should have waited for at least 10 seconds. Looks like it returned within: %s", actual), actual > 10 * 1000, is(true));
        }
    }

    @Test
    public void waitUntil_shouldThrowAnExceptionWithAGoodMessage() throws InterruptedException {
        Predicate predicate = mock(Predicate.class);
        when(predicate.call()).thenReturn(false);
        String message = "Did not find element with id 'iFeelLucky'";
        when(predicate.toString()).thenReturn(message);

        try {
            WaitUtil.waitUntil(Timeout.ONE_SECOND, predicate);
            fail("Should have thrown an exception as we the predicate always returns false");
        } catch (Exception e) {
            assertThat(e.getMessage(), is(String.format("Wait timed out. Reason: %s", message)));
        }
    }

    @Test
    public void waitFor_shouldReturnAsSoonAsTheFunctionReturnsAnObjectOfRequiredType() throws InterruptedException {
        Function<Object> func = mock(Function.class);
        Object expected = new Object();
        when(func.call()).thenReturn(null).thenThrow(new RuntimeException("Still not true yet")).thenReturn(expected);
        Object actual = WaitUtil.waitFor(Timeout.ONE_SECOND, func);

        assertTrue("Expected to get the same instance back as was passed stubbed in the function call", expected == actual);
        verify(func, times(3)).call();
    }

    @Test
    public void waitFor_shouldWaitForAMaximumOfTimeoutWhenTheFunctionBombsOrReturnsNullIsFalse() throws InterruptedException {
        Function<Object> func = mock(Function.class);
        when(func.call()).thenThrow(new RuntimeException("Still not true yet"));

        long before = System.currentTimeMillis();
        Thread.sleep(1);
        try {
            WaitUtil.waitFor(Timeout.TEN_SECONDS, func);
            fail("Should have thrown an exception as we the predicate always returns false");
        } catch (Exception e) {
            long actual = System.currentTimeMillis() - before;
            assertThat(String.format("The wait should have waited for at least 10 seconds. Looks like it returned within: %s", actual), actual > 10 * 1000, is(true));
        }
    }

    @Test
    public void waitFor_shouldThrowAnExceptionWithAGoodMessage() throws InterruptedException {
        Function<Object> func = mock(Function.class);
        when(func.call()).thenThrow(new RuntimeException("Still not true yet"));
        String message = "Did not find element with id 'iFeelLucky'";
        when(func.toString()).thenReturn(message);

        try {
            WaitUtil.waitFor(Timeout.ONE_SECOND, func);
            fail("Should have thrown an exception as we the predicate always returns false");
        } catch (Exception e) {
            assertThat(e.getMessage(), is(String.format("Wait timed out. Reason: %s", message)));
        }
    }

}
