package org.jsoup.test;

import org.jsoup.UncheckedIOException;
import org.junit.Test;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionWithIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
    }

    @Test
    public void testGetCauseReturnsIoException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertInstanceOf(IOException.class, uncheckedIOException.getCause());
    }

    @Test(expected = IOException.class)
    public void testUncheckedIOExceptionThrowsWrappedIOExceptionWhenInvoked() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        uncheckedIOException.printStackTrace();
    }

    @Test
    public void testNullIoException() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        assertNull(uncheckedIOException.ioException());
    }

    @Test
    public void testEmptyConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException();
        assertNull(uncheckedIOException.ioException());
    }
}