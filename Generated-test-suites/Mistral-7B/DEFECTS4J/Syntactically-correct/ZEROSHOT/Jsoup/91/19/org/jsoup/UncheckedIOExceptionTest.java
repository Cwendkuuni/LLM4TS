package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionWithIORuntimeException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test(expected = IOException.class)
    public void testGetCauseThrowsIOException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(new IOException("Test IOException"));
        uncheckedIOException.getCause();
    }

    @Test
    public void testGetMessage() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals("Test IOException", uncheckedIOException.getMessage());
    }

    @Test
    public void testToString() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals("UncheckedIOException: Test IOException", uncheckedIOException.toString());
    }

    @Test
    public void testConstructWithString() {
        String message = "Test UncheckedIOException";
        UncheckedIOException uncheckedIOException = new UncheckedIOException(message);
        Assert.assertEquals(message, uncheckedIOException.getMessage());
    }

    @Test
    public void testConstructWithStringAndCause() {
        String message = "Test UncheckedIOException";
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(message, ioException);
        Assert.assertEquals(message, uncheckedIOException.getMessage());
        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }
}