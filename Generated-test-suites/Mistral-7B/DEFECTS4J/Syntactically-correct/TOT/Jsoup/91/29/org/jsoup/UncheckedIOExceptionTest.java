
package org.jsoup.test;

import org.junit.Test;
import org.jsoup.UncheckedIOException;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals("Test IOException", uncheckedIOException.getMessage());
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionNullCause() {
        try {
            new UncheckedIOException(null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testIoExceptionNull() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        assertNull(uncheckedIOException.ioException());
    }

    @Test
    public void testIoExceptionEmptyMessage() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals("", uncheckedIOException.getMessage());
    }

    @Test
    public void testIoExceptionWithMessage() {
        IOException ioException = new IOException("Test IOException with message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals("Test IOException with message", uncheckedIOException.getMessage());
    }
}