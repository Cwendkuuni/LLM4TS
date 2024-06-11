package org.jsoup.test;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;
import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException ioException = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals("Expected IOException", uncheckedIOException.getMessage());
        assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testGetCauseIsInstanceOfIOException() {
        IOException ioException = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertTrue(ioException.getClass().isAssignableFrom(uncheckedIOException.getCause().getClass()));
    }

    @Test(expected = UncheckedIOException.class)
    public void testUncheckedIOExceptionThrowsExpectedException() throws IOException {
        IOException ioException = new IOException("Expected IOException");
        throw new UncheckedIOException(ioException);
    }

    @Test(expected = IOException.class)
    public void testGetCauseThrowsIOException() {
        IOException ioException = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertThrows(IOException.class, () -> uncheckedIOException.ioException().printStackTrace());
    }

    @Test
    public void testToString() {
        IOException ioException = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        String expectedToString = "org.jsoup.UncheckedIOException: Expected IOException";
        String actualToString = uncheckedIOException.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testHashCode() {
        IOException ioException1 = new IOException("Exception 1");
        UncheckedIOException uncheckedIOException1 = new UncheckedIOException(ioException1);

        IOException ioException2 = new IOException("Exception 2");
        UncheckedIOException uncheckedIOException2 = new UncheckedIOException(ioException2);

        int expectedHashCode1 = ioException1.hashCode() + super.getClass().hashCode();
        int actualHashCode1 = uncheckedIOException1.hashCode();
        assertEquals(expectedHashCode1, actualHashCode1);

        int expectedHashCode2 = ioException2.hashCode() + super.getClass().hashCode();
        int actualHashCode2 = uncheckedIOException2.hashCode();
        assertEquals(expectedHashCode2, actualHashCode2);

        assertNotEquals(actualHashCode1, actualHashCode2);
    }

    @Test
    public void testEquals() {
        IOException ioException1 = new IOException("Exception 1");
        UncheckedIOException uncheckedIOException1 = new UncheckedIOException(ioException1);

        IOException ioException2 = new IOException("Exception 2");
        UncheckedIOException uncheckedIOException2 = new UncheckedIOException(ioException2);

        UncheckedIOException uncheckedIOException3 = new UncheckedIOException(ioException1);

        assertTrue(uncheckedIOException1.equals(uncheckedIOException3));

        assertFalse(uncheckedIOException1.equals(uncheckedIOException2));
        assertFalse(uncheckedIOException1.equals(null));
        assertFalse(uncheckedIOException1.equals(new Object()));
    }
}