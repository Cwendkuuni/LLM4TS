import org.junit.Test;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionConstruction() throws IOException {
        IOException cause = new IOException("Test IO Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        assertSame(cause, uncheckedIOException.ioException());
    }
}