package org.jsoup.test; // Assuming a test package exists under org.jsoup

import org.junit.Test;
import org.jsoup.UncheckedIOException;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionCreation() {
        IOException cause = new IOException("Test cause");
        UncheckedIOException e = new UncheckedIOException(cause);
        assertEquals("Test cause", e.ioException().getMessage());
        // Test setCause() method to ensure it's not modifiable
        try {
            e.setCause(new IOException("New cause"));
        } catch (Throwable ignored) {
        }
        assertNotSame(new IOException("New cause"), e.ioException());
    }
}