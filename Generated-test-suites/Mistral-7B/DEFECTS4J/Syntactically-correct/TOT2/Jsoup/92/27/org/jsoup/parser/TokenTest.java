import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        // Given: A Doctype token
        Token doctype = new Token.Doctype();
        String name = "html";
        doctype.asDoctype().getName = name;

        // When: The token is reset
        Token resetDoctype = doctype.reset();

        // Then: The name property is reset to null
        assertEquals(null, resetDoctype.asDoctype().getName);
    }

    // Add test cases for other methods in the Token class...
}

