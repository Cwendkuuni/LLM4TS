import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Exception message");
        int expectedStatusCode = -1;
        int actualStatusCode = exception.getStatusCode();
        assertEquals(expectedStatusCode, actualStatusCode);
    }
}