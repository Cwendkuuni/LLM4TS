package org.jsoup.test; // You might need to adjust the package name according to your project structure

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.UncheckedIOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        Assert.assertEquals("Test IOException", uncheckedIOException.getMessage());
        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionGetCause() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        Assert.assertSame(ioException, uncheckedIOException.getCause());
    }

    @Test
    public void testUncheckedIOExceptionThrowsOriginalException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        try {
            throw uncheckedIOException;
        } catch (IOException e) {
            Assert.assertEquals("Test IOException", e.getMessage());
        }
    }
}