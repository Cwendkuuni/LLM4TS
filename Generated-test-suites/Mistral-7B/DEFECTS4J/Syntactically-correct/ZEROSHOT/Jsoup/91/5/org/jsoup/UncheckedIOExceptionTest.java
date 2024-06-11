package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionCreation() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals("Test IOException", uncheckedIOException.getMessage());
        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test(expected = UncheckedIOException.class)
    public void testMethodThatThrowsIOExceptionAndIsWrappedByUncheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        throw new MyCustomClassThatThrowsIOException(ioException);
    }

    private static class MyCustomClassThatThrowsIOException extends Exception {
        public MyCustomClassThatThrowsIOException(IOException cause) {
            super(cause);
        }
    }

    @Test
    public void testGetCauseIsIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertSame(ioException.getClass(), uncheckedIOException.getCause().getClass());
    }

    @Test
    public void testToString() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        String expectedToString = "UncheckedIOException: Test IOException";
        String actualToString = uncheckedIOException.toString();
        Assert.assertEquals(expectedToString, actualToString);
    }
}