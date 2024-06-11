import org.junit.Test;
import org.junit.Assert;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionConstructor() throws IOException {
        IOException cause = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals(cause, uncheckedIOException.ioException());
    }
}