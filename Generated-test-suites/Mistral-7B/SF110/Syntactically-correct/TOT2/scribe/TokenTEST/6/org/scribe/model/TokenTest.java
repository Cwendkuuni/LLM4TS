import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenTest {

    @Test
    public void testGetToken() {
        String token = "abc123";
        Token tokenObject = new Token(token, "secret");
        assertEquals(token, tokenObject.getToken());
    }
}