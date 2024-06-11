import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOException() {
        IOException cause = new IOException("Unexpected error occurred.");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals("Unexpected error occurred.", uncheckedIOException.getMessage());
        Assert.assertEquals(IOException.class, uncheckedIOException.getCause().getClass());
    }
}