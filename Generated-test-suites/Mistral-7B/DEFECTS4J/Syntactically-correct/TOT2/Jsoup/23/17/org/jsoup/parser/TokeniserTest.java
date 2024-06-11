import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.CharacterInputSource;
import java.io.IOException;

public class TokeniserTest {

    @Test
    public void testReadToken() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterInputSource("abcdef"), null);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("b", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("c", token.data);

        // Test reading a tag
        tokeniser.emit(new Token.StartTag("p"));
        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("p", token.data.name());
    }
}