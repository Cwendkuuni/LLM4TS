import static org.junit.Assert.*;
import com.google.javascript.rhino.TokenStream;

public class TokenStreamTest {
    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("else if"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("switch"));
        // Add more true cases

        assertFalse(TokenStream.isKeyword("myVariable")); // false case
        // Add more false cases
    }
}