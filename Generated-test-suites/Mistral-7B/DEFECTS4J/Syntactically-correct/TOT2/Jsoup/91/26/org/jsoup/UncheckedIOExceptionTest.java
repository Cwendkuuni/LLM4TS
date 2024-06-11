import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

public class UncheckedIOExceptionTest {
    @Test
    public void testUncheckedIOException() {
        IOException ioException = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        assertThat(uncheckedIOException.ioException(), instanceOf(IOException.class));
    }
}