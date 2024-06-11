import org.junit.Test;
import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {
    @Test
    public void testConstructWithIOException() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertTrue(uncheckedIOException instanceof UncheckedIOException);
        assertTrue(uncheckedIOException.getCause() instanceof IOException);
        assertEquals(ioException.getMessage(), uncheckedIOException.getMessage());
    }
    
}