import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

public class XPathLexerTest {

    @org.junit.Test
    public void testNextToken() {
        String xpath = "/a/b[1]";
        XPathLexer lexer = new XPathLexer(xpath);
        Token token;

        token = lexer.nextToken();
        assertEquals(Token.Type.SLASH, token.getType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertEquals(Token.Type.SLASH, token.getType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("b", token.getValue());

        token = lexer.nextToken();
        assertEquals(Token.Type.LEFT_BRACKET, token.getType());

        token = lexer.nextToken();
        assertEquals(Token.Type.NUMBER, token.getType());
        assertEquals("1", token.getValue());

        token = lexer.nextToken();
        assertEquals(Token.Type.RIGHT_BRACKET, token.getType());

        token = lexer.nextToken();
        assertNull(token);
    }
}