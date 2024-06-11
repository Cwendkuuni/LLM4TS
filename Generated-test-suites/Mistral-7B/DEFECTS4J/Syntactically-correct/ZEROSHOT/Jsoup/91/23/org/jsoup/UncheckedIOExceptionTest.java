package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionInstance() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertEquals("Test IOException", uncheckedIOException.getMessage());
        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testGetCauseUncheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertSame(ioException, uncheckedIOException.getCause());
    }

    @Test(expected = IOException.class)
    public void testIOExceptionThrownWhenGettingCause() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(new UncheckedIOException(new IOException("Test IOException")));
        try {
            uncheckedIOException.getCause();
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testToString() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        String expected = String.format("UncheckedIOException: Test IOException%nCause: java.io.IOException: Test IOException%n", new Object[0]);
        Assert.assertEquals(expected, uncheckedIOException.toString());
    }

    @Test
    public void testNewUncheckedIOExceptionWithMessage() {
        String message = "Test UncheckedIOException with message";
        UncheckedIOException uncheckedIOException = new UncheckedIOException(message);
        Assert.assertEquals(message, uncheckedIOException.getMessage());
        Assert.assertNull(uncheckedIOException.ioException());
    }
}