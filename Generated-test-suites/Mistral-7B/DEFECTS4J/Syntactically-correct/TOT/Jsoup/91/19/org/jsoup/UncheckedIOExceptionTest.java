import org.junit.Test;
import org.jsoup.UncheckedIOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertEquals("Test IOException", uncheckedIOException.getMessage());
        assertEquals(ioException, uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionThrowsIOException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertThrows(IOException.class, () -> uncheckedIOException.ioException());
    }

    @Test
    public void testUncheckedIOExceptionThrowsOriginalException() {
        IOException ioException = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertThrows(IOException.class, () -> {
            throw uncheckedIOException;
        });
    }

    @Test
    public void testUncheckedIOExceptionWithNullCause() {
        assertThrows(NullPointerException.class, () -> new UncheckedIOException(null));
    }

}