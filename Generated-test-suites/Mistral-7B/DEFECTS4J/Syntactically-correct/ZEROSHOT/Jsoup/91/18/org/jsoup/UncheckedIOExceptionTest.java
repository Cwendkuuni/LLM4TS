import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionWithIOException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);

        Assert.assertSame(cause, uncheckedIOException.ioException());
    }

    @Test(expected = IOException.class)
    public void testGetCauseThrowsOriginalIOException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);

        uncheckedIOException.getCause();
    }

    @Test
    public void testNewInstanceWithNullCause() {
        try {
            new UncheckedIOException(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testNewInstanceWithNonIOExceptionCause() {
        RuntimeException cause = new RuntimeException("Test RuntimeException");
        try {
            new UncheckedIOException(cause);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testNewInstanceWithNullMessage() {
        IOException cause = new IOException("Test IOException");
        try {
            new UncheckedIOException(cause, null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testNewInstanceWithEmptyMessage() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause, "");
        String message = uncheckedIOException.getMessage();
        Assert.assertEquals("", message);
    }

    @Test
    public void testNewInstanceWithMessage() {
        IOException cause = new IOException("Test IOException");
        String expectedMessage = "Test IOException: ";
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause, "Test Message");
        String message = uncheckedIOException.getMessage();
        Assert.assertEquals(expectedMessage, message);
    }
}