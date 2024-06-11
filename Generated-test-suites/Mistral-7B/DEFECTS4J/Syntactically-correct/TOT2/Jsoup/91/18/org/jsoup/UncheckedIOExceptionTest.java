import org.junit.Test;
import org.junit.Assert;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals(cause, uncheckedIOException.ioException());
    }
}