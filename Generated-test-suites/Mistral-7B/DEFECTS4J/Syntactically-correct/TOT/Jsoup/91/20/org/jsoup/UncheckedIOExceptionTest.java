package org.jsoup.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertInstanceOf(UncheckedIOException.class, uncheckedIOException);
        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        assertNotNull(uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionThrowsExpectedIOException() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertThrows(IOException.class, () -> {
            throw uncheckedIOException;
        });
    }

    @Test
    public void testUncheckedIOExceptionThrowsExpectedIOExceptionWithMessage() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertThrows(IOException.class, () -> {
            throw uncheckedIOException;
        }, "Expected IOException with message: Test IO Exception");
    }
}