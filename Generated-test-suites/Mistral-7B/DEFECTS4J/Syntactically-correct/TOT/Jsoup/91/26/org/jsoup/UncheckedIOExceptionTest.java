import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.UncheckedIOException;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOExceptionInstanceOfIOException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);

        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionThrowsIOException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);

        assertThrows(IOException.class, () -> {
            throw uncheckedIOException;
        });
    }

    @Test
    public void testUncheckedIOExceptionDoesNotHideOriginalException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);

        try {
            throw uncheckedIOException;
        } catch (IOException e) {
            assertSame(cause, e.getCause());
        }
    }
}