package org.jsoup.test;

import org.junit.Test;
import org.jsoup.UncheckedIOException;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionWithNullArgument() {
        assertThrows(NullPointerException.class, () -> new UncheckedIOException(null));
    }

    @Test
    public void testUncheckedIOException() {
        IOException ioException = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIoException = new UncheckedIOException(ioException);

        assertInstanceOf(IOException.class, uncheckedIoException.ioException());
        assertSame(ioException, uncheckedIoException.getCause());
    }

    @Test
    public void testUncheckedIOExceptionWrongType() {
        String exceptionMessage = "Test exception";
        RuntimeException runtimeException = new RuntimeException(exceptionMessage);
        UncheckedIOException uncheckedIoException = new UncheckedIOException((Throwable) runtimeException);

        assertFalse(Exception.class.isAssignableFrom(runtimeException.getClass()));
        assertInstanceOf(IOException.class, uncheckedIoException.ioException());
    }
}