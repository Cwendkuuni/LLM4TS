package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test(expected = UncheckedIOException.class)
    public void testUncheckedIOExceptionThrowsCorrectException() throws IOException {
        IOException cause = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        throw uncheckedIOException;
    }

    @Test
    public void testGetCauseReturnsOriginalIOException() {
        IOException cause = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals(cause, uncheckedIOException.ioException());
    }

    @Test(expected = IOException.class)
    public void testGetCauseReturnsOriginalIOExceptionWhenThrown() throws IOException {
        IOException cause = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        uncheckedIOException.ioException();
    }

    @Test
    public void testToStringPrintsCorrectMessage() {
        IOException cause = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        String message = uncheckedIOException.toString();
        String expectedMessage = "org.jsoup.UncheckedIOException: Expected IOException";
        Assert.assertEquals(expectedMessage, message);
    }
}