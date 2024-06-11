import org.junit.Test;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {
    @Test
    public void testConstructor() {
        IOException ioException = new IOException("Expected IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

        // Verify that the UncheckedIOException constructor correctly wraps the given IOException
        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        assertEquals("Expected IOException", uncheckedIOException.getMessage());
    }
}