import static org.junit.Assert.*;
import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

public class XPathLexerTest {

    @Test
    public void testConstructor() {
        XPathLexer lexer = new XPathLexer("");
        assertEquals("", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(0, lexer.endPosition());
        assertEquals(null, lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_EmptyString() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_Identifier() {
        XPathLexer lexer = new XPathLexer("foo");
        Token token = lexer.nextToken();
        assertEquals(15, token.getTokenType());
        assertEquals("foo", token.getText());
        assertEquals(3, lexer.currentPosition());
        assertEquals(6, lexer.endPosition());
    }

    @Test
    public void testNextToken_OperatorName() {
        XPathLexer lexer = new XPathLexer("and");
        Token token = lexer.nextToken();
        assertEquals(28, token.getTokenType());
        assertEquals("and", token.getText());
        assertEquals(3, lexer.currentPosition());
        assertEquals(4, lexer.endPosition());
    }

    @Test
    public void testNextToken_Number() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        assertEquals(31, token.getTokenType());
        assertEquals("123", token.getText());
        assertEquals(3, lexer.currentPosition());
        assertEquals(4, lexer.endPosition());
    }

    @Test
    public void testNextToken_Whitespace() {
        XPathLexer lexer = new XPathLexer(" foo ");
        Token token = lexer.nextToken();
        assertEquals(-2, token.getTokenType());
        assertEquals(0, lexer.currentPosition());
        assertEquals(3, lexer.endPosition());
    }

    @Test
    public void testNextToken_Literal() {
        XPathLexer lexer = new XPathLexer("\"foo\"");
        Token token = lexer.nextToken();
        assertEquals(27, token.getTokenType());
        assertEquals("foo", token.getText());
        assertEquals(6, lexer.currentPosition());
        assertEquals(10, lexer.endPosition());
    }

    @Test
    public void testNextToken_Dollar() {
        XPathLexer lexer = new XPathLexer("$foo");
        Token token = lexer.nextToken();
        assertEquals(26, token.getTokenType());
        assertEquals("$", token.getText());
        assertEquals(1, lexer.currentPosition());
        assertEquals(4, lexer.endPosition());
    }

    @Test
    public void testNextToken_Slashes() {
        XPathLexer lexer = new XPathLexer("/foo");
        Token token = lexer.nextToken();
        assertEquals(11, token.getTokenType());
        assertEquals("/", token.getText());
        assertEquals(1, lexer.currentPosition());
        assertEquals(2, lexer.endPosition());
    }

    @Test
    public void testNextToken_Comma() {
        XPathLexer lexer = new XPathLexer(",");
        Token token = lexer.nextToken();
        assertEquals(32, token.getTokenType());
        assertEquals(",", token.getText());
        assertEquals(1, lexer.currentPosition());
        assertEquals(1, lexer.endPosition());
    }

    @Test
    public void testNextToken_Equals() {
        XPathLexer lexer = new XPathLexer("=foo");
        Token token = lexer.nextToken();
        assertEquals(21, token.getTokenType());
        assertEquals("=", token.getText());
        assertEquals(1, lexer.currentPosition());
        assertEquals(2, lexer.endPosition());
    }

    @Test
    public void testNextToken_Minus() {
        XPathLexer lexer = new XPathLexer("-foo");
        Token token = lexer.nextToken();
        assertEquals(6, token.getTokenType());
        assertEquals("-", token.getText());
        assertEquals(1, lexer.currentPosition());
        assertEquals(2, lexer.endPosition());
    }

    @Test
    public void testNextToken_Plus() {
        XPathLexer lexer = new XPathLexer("+foo");
        Token token = lexer.nextToken();
        assertEquals(5, token.getTokenType());
        assertEquals("+", token.getText());
        assertEquals(1, lexer.currentPosition());
        assertEquals(2, lexer.endPosition());
    }

    @Test
    public void testNextToken_RelationalOperatorLT() {
        XPathLexer lexer = new XPathLexer("<foo");
        Token token = lexer.nextToken();
        assertEquals(7, token.getTokenType());
        assertEquals("<", token.getText());
        assertEquals(1, lexer.currentPosition());
        assertEquals(2, lexer.endPosition());
    }

    @Test
    public void testNextToken_RelationalOperatorLE() {
        XPathLexer lexer = new XPathLexer("<=");
        Token token = lexer.nextToken();
        assertEquals(8, token.getTokenType());
        assertEquals("<=", token.getText());
        assertEquals(2, lexer.currentPosition());
        assertEquals(4, lexer.endPosition());
    }

    @Test
    public void testNextToken_RelationalOperatorGT() {
        XPathLexer lexer = new XPathLexer(">foo");
        Token token = lexer.nextToken();
        assertEquals(9, token.getTokenType());
        assertEquals(">", token.getText());
        assertEquals(1, lexer.currentPosition());
        assertEquals(2, lexer.endPosition());
    }

    @Test
    public void testNextToken_RelationalOperatorGE() {
        XPathLexer lexer = new XPathLexer(">=foo");
        Token token = lexer.nextToken();
        assertEquals(10, token.getTokenType());
        assertEquals(">=", token.getText());
        assertEquals(2, lexer.currentPosition());
        assertEquals(5, lexer.endPosition());
    }

    @Test
    public void testNextToken_Not() {
        XPathLexer lexer = new XPathLexer("not(foo)");
        Token token = lexer.nextToken();
        assertEquals(23, token.getTokenType());
        assertEquals("not", token.getText());
        assertEquals(1, lexer.currentPosition());
        assertEquals(5, lexer.endPosition());
    }

    @Test
    public void testNextToken_NotEquals() {
        XPathLexer lexer = new XPathLexer("not equals(foo, bar)");
        Token token = lexer.nextToken();
        assertEquals(22, token.getTokenType());
        assertEquals("not equals", token.getText());
        assertEquals(1, lexer.currentPosition());
        assertEquals(11, lexer.endPosition());
    }

    @Test
    public void testNextToken_Star() {
        XPathLexer lexer = new XPathLexer("*foo");
        Token token = lexer.nextToken();
        assertEquals(20, token.getTokenType());
        assertEquals("*", token.getText());
        assertEquals(1, lexer.currentPosition());
        assertEquals(2, lexer.endPosition());
    }

    @Test
    public void testNextToken_LeftParenthesis() {
        XPathLexer lexer = new XPathLexer("(foo)");
        Token token = lexer.nextToken();
        assertEquals(1, token.getTokenType());
        assertEquals("(", token.getText());
        assertEquals(1, lexer.currentPosition());
        assertEquals(2, lexer.endPosition());
    }

    @Test
    public void testNextToken_RightParenthesis() {
        XPathLexer lexer = new XPathLexer("foo())");
        Token token = lexer.nextToken();
        assertEquals(2, token.getTokenType());
        assertEquals(")", token.getText());
        assertEquals(6, lexer.currentPosition());
        assertEquals(7, lexer.endPosition());
    }

    @Test
    public void testNextToken_LeftBracket() {
        XPathLexer lexer = new XPathLexer("[foo]");
        Token token = lexer.nextToken();
        assertEquals(3, token.getTokenType());
        assertEquals("[", token.getText());
        assertEquals(1, lexer.currentPosition());
        assertEquals(2, lexer.endPosition());
    }

    @Test
    public void testNextToken_RightBracket() {
        XPathLexer lexer = new XPathLexer("foo]");
        Token token = lexer.nextToken();
        assertEquals(4, token.getTokenType());
        assertEquals("]", token.getText());
        assertEquals(1, lexer.currentPosition());
        assertEquals(2, lexer.endPosition());
    }

    @Test
    public void testNextToken_Slashes_Double() {
        XPathLexer lexer = new XPathLexer("/foo/");
        Token token = lexer.nextToken();
        assertEquals(12, token.getTokenType());
        assertEquals("/", token.getText());
        assertEquals(1, lexer.currentPosition());
        assertEquals(2, lexer.endPosition());

        token = lexer.nextToken();
        assertEquals(11, token.getTokenType());
        assertEquals("/", token.getText());
        assertEquals(2, lexer.currentPosition());
        assertEquals(3, lexer.endPosition());
    }
}