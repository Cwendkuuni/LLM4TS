import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenTest {
    @Test
    public void testTokenType() {
        Token token = new Token().reset(); // Reset the token
        assertEquals("Token", token.tokenType());
    }
}