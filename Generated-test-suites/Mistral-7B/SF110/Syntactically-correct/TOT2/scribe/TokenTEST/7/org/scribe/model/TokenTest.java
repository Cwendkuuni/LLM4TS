import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testGetToken() {
        Token token = new Token("testToken", "testSecret");
        String expectedToken = "testToken";
        String actualToken = token.getToken();
        assertEquals(expectedToken, actualToken);
    }
}