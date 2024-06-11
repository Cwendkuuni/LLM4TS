import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testGetToken() {
        String token = "sampleToken";
        Token tokenObj = new Token(token, null);
        assertEquals(token, tokenObj.getToken());
    }
}