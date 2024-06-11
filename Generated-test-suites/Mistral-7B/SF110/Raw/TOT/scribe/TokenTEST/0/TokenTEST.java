import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {
    @Test
    public void testTokenConstructionAndGetToken() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObject = new Token(token, secret);

        String actualToken = tokenObject.getToken();
        String expectedToken = token;

        assert(actualToken.equals(expectedToken));
    }
}
