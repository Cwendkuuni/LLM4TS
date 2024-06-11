import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.CharacterInput;

public class TokeniserTest {
    @Test
    public void testReadMethod() {
        // Create a CharacterReader for testing
        CharacterInput reader = new CharacterInput("Hello World");
        Tokeniser tokeniser = new Tokeniser(reader);

        // Test reading a character token
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("H", token.content);

        // Test reading a start tag token
        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("html", token.tagName);

        // Test reading an end tag token
        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("html", token.tagName);

        // Test reading the end of the input stream
        token = tokeniser.read();
        assertNull(token);
    }
}