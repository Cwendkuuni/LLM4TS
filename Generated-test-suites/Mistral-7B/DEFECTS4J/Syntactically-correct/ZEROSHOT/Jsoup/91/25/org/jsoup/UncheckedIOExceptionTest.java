package org.jsoup.test;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.jsoup.UncheckedIOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionWithIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        assertNull(uncheckedIOException.getMessage());
    }

    @Test
    public void testGetMessageWhenIOExceptionHasMessage() {
        IOException ioException = new IOException("Test IOException with message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        String message = uncheckedIoException.getMessage();
        assertNull(message);
    }

    @Test(expected = IOException.class)
    public void testGetCauseThrowsIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        uncheckedIOException.ioException();
    }

    @Test
    public void testGetCauseReturnsIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        IOException cause = uncheckedIOException.ioException();
        assertInstanceOf(IOException.class, cause);
    }

    @Test
    public void testEmptyConstructor() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException();
        assertNull(uncheckedIOException.ioException());
        assertNull(uncheckedIOException.getMessage());
    }
}