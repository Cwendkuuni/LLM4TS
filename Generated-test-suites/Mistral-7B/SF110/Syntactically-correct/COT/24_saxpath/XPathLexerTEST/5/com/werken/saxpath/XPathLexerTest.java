import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XPathLexerTest {

    private XPathLexer lexer;

    @Test
    public void testConstructor() {
        lexer = new XPathLexer("");
        assertEquals("", lexer.getXPath());
    }

    @Test
    public void testEmptyXPath() {
        lexer = new XPathLexer("");
        assertNull(lexer.nextToken());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullXPath() {
        lexer = new XPathLexer(null);
    }

    @Test
    public void testNextToken_Dollar() {
        lexer = new XPathLexer("$foo");
        assertEquals(TokenType.DOLLAR, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_Literal() {
        lexer = new XPathLexer("\"foo\"");
        assertEquals(TokenType.STRING, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_Slashes() {
        lexer = new XPathLexer("/foo");
        assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_Comma() {
        lexer = new XPathLexer(",foo");
        assertEquals(TokenType.COMMA, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_LeftParen() {
        lexer = new XPathLexer("(foo");
        assertEquals(TokenType.LEFT_PAREN, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_RightParen() {
        lexer = new XPathLexer("foo)");
        assertEquals(TokenType.RIGHT_PAREN, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_LeftBracket() {
        lexer = new XPathLexer("[foo]");
        assertEquals(TokenType.LEFT_BRACKET, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_RightBracket() {
        lexer = new XPathLexer("foo]");
        assertEquals(TokenType.RIGHT_BRACKET, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_Plus() {
        lexer = new XPathLexer("foo+");
        assertEquals(TokenType.PLUS, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_Minus() {
        lexer = new XPathLexer("foo-");
        assertEquals(TokenType.MINUS, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_RelationalOperatorLessThan() {
        lexer = new XPathLexer("foo<");
        assertEquals(TokenType.RELATIONAL_OPERATOR_LESS_THAN, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_RelationalOperatorGreaterThan() {
        lexer = new XPathLexer("foo>");
        assertEquals(TokenType.RELATIONAL_OPERATOR_GREATER_THAN, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_Equals() {
        lexer = new XPathLexer("foo=");
        assertEquals(TokenType.EQUALS, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_NotEquals() {
        lexer = new XPathLexer("foo!=");
        assertEquals(TokenType.NOT_EQUALS, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_Not() {
        lexer = new XPathLexer("not(foo)");
        assertEquals(TokenType.NOT, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_And() {
        lexer = new XPathLexer("and(foo)");
        assertEquals(TokenType.AND, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_Or() {
        lexer = new XPathLexer("or(foo)");
        assertEquals(TokenType.OR, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_Mod() {
        lexer = new XPathLexer("mod(foo, 2)");
        assertEquals(TokenType.MOD, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_Div() {
        lexer = new XPathLexer("div(foo, 2)");
        assertEquals(TokenType.DIV, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_IdentiferOrOperatorName() {
        lexer = new XPathLexer("foo:bar");
        assertEquals(TokenType.IDENTIFIER_OR_OPERATOR_NAME, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_Identifier() {
        lexer = new XPathLexer("foo");
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_OperatorName() {
        lexer = new XPathLexer("and");
        assertEquals(TokenType.AND, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_Number() {
        lexer = new XPathLexer("123");
        assertEquals(TokenType.NUMBER, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_Whitespace() {
        lexer = new XPathLexer(" foo ");
        assertEquals(TokenType.WHITESPACE, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_CommaAfterIdentifier() {
        lexer = new XPathLexer("foo,");
        assertEquals(TokenType.COMMA, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_MultipleIdentifiers() {
        lexer = new XPathLexer("foo bar");
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken().getTokenType());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextToken_EndOfXPath() {
        lexer = new XPathLexer("foo");
        lexer.nextToken();
        lexer.nextToken();
        lexer.nextToken(); // Should throw an IllegalStateException
    }
}