package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionInstance() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertSame(ioException, uncheckedIOException.ioException());
    }

    @Test(expected = UncheckedIOException.class)
    public void testThrowsUncheckedIOException() throws IOException {
        IOException ioException = new IOException();
        throw new UncheckedIOException(ioException);
    }

    @Test
    public void testGetCauseReturnsSameInstance() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        Assert.assertSame(ioException, uncheckedIOException.getCause());
    }

    @Test
    public void testToString() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        String expected = "org.jsoup.UncheckedIOException: " + ioException.getMessage();
        String actual = uncheckedIOException.toString();
        Assert.assertEquals(expected, actual);
    }
}