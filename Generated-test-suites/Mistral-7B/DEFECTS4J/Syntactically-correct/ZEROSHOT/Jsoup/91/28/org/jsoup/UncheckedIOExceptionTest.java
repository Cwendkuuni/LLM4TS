package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionInitialization() {
        IOException cause = new IOException("Expected IOException message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertThat(uncheckedIOException.getCause(), is(cause));
        Assert.assertThat(uncheckedIOException.ioException(), is(cause));
    }

    @Test
    public void testGetCause() {
        IOException cause = new IOException("Expected IOException message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertSame(cause, uncheckedIOException.getCause());
    }

    @Test
    public void testIoException() {
        IOException cause = new IOException("Expected IOException message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertSame(cause, uncheckedIOException.ioException());
    }

    @Test(expected = UncheckedIOException.class)
    public void testThrowUncheckedIOException() {
        IOException cause = new IOException("Expected IOException message");
        throw new UncheckedIOException(cause);
    }

    @Test
    public void testToString() {
        IOException cause = new IOException("Expected IOException message");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        String expectedMessage = String.format("UncheckedIOException: %s", cause.getMessage());
        String actualMessage = uncheckedIOException.toString();
        Assert.assertEquals(expectedMessage, actualMessage);
    }
}