
import org.junit.Test;
import org.jsoup.UncheckedIOException;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {
    @Test
    public void testConstructor() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIoException = new UncheckedIOException(cause);

        assertInstanceOf(IOException.class, uncheckedIoException.ioException());
    }

    @Test
    public void testIoException() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIoException = new UncheckedIOException(cause);

        assertNotNull(uncheckedIoException.ioException());
    }

    @Test(expected = UncheckedIOException.class)
    public void testThrowsUncheckedIOException() {
        IOException cause = new IOException("Test IOException");
        throw new UncheckedIOException(cause);
    }

    @Test
    public void testNoArgsConstructor() {
        UncheckedIOException uncheckedIoException = new UncheckedIOException();

        assertNull(uncheckedIoException.ioException());
    }
}