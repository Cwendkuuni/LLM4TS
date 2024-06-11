package org.jsoup.test;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException ioException = new IOException("Expected IOException message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        assertInstanceOf(UncheckedIOException.class, uncheckedIOException);
    }

    @Test
    public void testIoeThrownAsRuntimeException() {
        IOException ioException = new IOException("Expected IOException message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertThrows(RuntimeException.class, () -> {
            throw uncheckedIOException;
        });
    }

    @Test(expected = RuntimeException.class)
    public void testIoExceptionThrownDirectly() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException();
        IOException ioException = new IOException("Expected IOException message");
        uncheckedIOException.initCause(ioException);

        throw uncheckedIOException;
    }

    @Test
    public void testGetCauseReturnsIoException() {
        IOException ioException = new IOException("Expected IOException message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        assertInstanceOf(IOException.class, uncheckedIOException.getCause());
    }

    @Test
    public void testIoExceptionGetterReturnsExpectedIoException() {
        IOException ioException = new IOException("Expected IOException message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        IOException returnedIoException = uncheckedIOException.ioException();

        assertInstanceOf(IOException.class, returnedIoException);
        assertInstanceOf(ioException.getClass(), returnedIoException);
        assertEquals(ioException.getMessage(), returnedIoException.getMessage());
    }
}