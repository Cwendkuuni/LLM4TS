package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionWithIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test(expected = UncheckedIOException.class)
    public void testMethodThatThrowsIOExceptionAndIsWrappedWithUncheckedIOException() throws IOException {
        Throwable exception = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(exception);
        throw uncheckedIOException;
    }

    @Test
    public void testGetMessageFromUncheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        String expectedMessage = "Test IOException";
        String actualMessage = uncheckedIOException.getMessage();
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetLocalizedMessageFromUncheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        String defaultMessage = uncheckedIOException.getLocalizedMessage();
        String expectedMessage = "ioexception.uncheckedioexception"; // Add a message key for localization if needed
        String actualMessage = uncheckedIOException.getLocalizedMessage();
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testgetCauseFromUncheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertSame(ioException, uncheckedIOException.getCause());
    }

    @Test
    public void testMethodThatDoesNotThrowIOException() {
        // You can add a test method that does not throw an IOException here to check for null or empty exception instances
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullCause() {
        IOException ioException = null;
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
    }

    @Test(expected = NullPointerException.class)
    public void testGetCauseFromNullUncheckedIOException() {
        UncheckedIOException uncheckedIOException = null;
        IOException ioException = uncheckedIOException.ioException();
    }
}