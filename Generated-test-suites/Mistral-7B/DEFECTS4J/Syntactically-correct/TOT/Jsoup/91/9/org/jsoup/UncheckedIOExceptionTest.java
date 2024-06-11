
package org.jsoup.test; // or another test package

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {
    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals("Test IOException", uncheckedIOException.getMessage());
        assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testIoException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testErrorScenario() {
        try {
            throw new UncheckedIOException(new IOException("Test IOException"));
        } catch (UncheckedIOException e) {
            assertEquals(IOException.class, e.getClass());
            assertEquals("Test IOException", e.ioException().getMessage());
        }
    }
}
