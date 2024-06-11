import org.junit.Test;
import org.jsoup.parser.Token;
import static org.junit.Assert.assertEquals;
import static org.jsoup.parser.Token.Character;
import static org.jsoup.parser.Token.TokenType;

public class TokeniserTest {

    @Test
    public void testRead() throws Exception {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader());
        Token token = tokeniser.read();
        assertEquals(TokenType.Character, token.type);
        assertEquals("", token.data);
    }
}