package org.jsoup.test;

import org.junit.Test;
import org.jsoup.UncheckedIOException;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionCreation() {
        IOException ioException = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertInstanceOf(UncheckedIOException.class, uncheckedIOException);
        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        assertEquals("Expected IOException", uncheckedIOException.getMessage());
        assertEquals("Expected IOException", uncheckedIOException.ioException().getMessage());
    }

    @Test(expected = UncheckedIOException.class)
    public void testThrowUncheckedIOException() {
        IOException ioException = new IOException("Expected IOException");
        throw new UncheckedIOException(ioException);
    }

    @Test
    public void testGetCause() {
        IOException ioException = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertSame(ioException, uncheckedIOException.ioException());
    }
}