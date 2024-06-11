package org.jsoup.test;

import org.junit.Test;
import org.jsoup.UncheckedIOException;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
    }

    @Test
    public void testGetCausedIoExceptionMessage() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertEquals("Test IO Exception", uncheckedIOException.getMessage());
    }

    @Test
    public void testUncheckedIOExceptionThrowsIoException() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertThrows(IOException.class, () -> uncheckedIOException.ioException().printStackTrace());
        assertEquals("Test IO Exception", uncheckedIOException.getMessage());
    }

    @Test
    public void testThrowsIoExceptionWhenGetCausedIoException() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertThrows(IOException.class, uncheckedIOException::ioException);
    }

    @Test
    public void testNullIoExceptionThrowsNPE() {
        assertNull(new UncheckedIOException(null));
    }
}