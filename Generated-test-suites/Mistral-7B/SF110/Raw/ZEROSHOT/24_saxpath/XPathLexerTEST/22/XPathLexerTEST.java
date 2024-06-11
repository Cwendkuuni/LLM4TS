import org.junit.Test;
import static org.junit.Assert.*;
import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

public class XPathLexerTest {

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
    }

    @Test
    public void testNextToken_SimpleXPath() {
        XPathLexer lexer = new XPathLexer("/foo/bar");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("bar", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_XPathWithDollar() {
        XPathLexer lexer = new XPathLexer("$foo");

        Token token = lexer.nextToken();
        assertEquals(TokenType.DOLLAR, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_XPathWithNumber() {
        XPathLexer lexer = new XPathLexer("123");

        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_XPathWithWhiteSpace() {
        XPathLexer lexer = new XPathLexer("  /foo  /bar  ");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("bar", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_XPathWithComma() {
        XPathLexer lexer = new XPathLexer("/foo, /bar");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.COMMA, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("bar", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_XPathWithRelationalOperator() {
        XPathLexer lexer = new XPathLexer("/foo > /bar");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.RELATIONAL_OPERATOR, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("bar", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_XPathWithEquals() {
        XPathLexer lexer = new XPathLexer("/foo = /bar");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("bar", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_XPathWithNotEquals() {
        XPathLexer lexer = new XPathLexer("/foo != /bar");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.NOT_EQUALS, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("bar", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_XPathWithNot() {
        XPathLexer lexer = new XPathLexer("/not(/foo)");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.NOT, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_XPathWithAnd() {
        XPathLexer lexer = new XPathLexer("/foo and /bar");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.AND, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("bar", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_XPathWithOr() {
        XPathLexer lexer = new XPathLexer("/foo or /bar");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OR, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("bar", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_XPathWithMod() {
        XPathLexer lexer = new XPathLexer("/foo mod /bar");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.MOD, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("bar", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_XPathWithDiv() {
        XPathLexer lexer = new XPathLexer("/foo div /bar");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.DIV, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("bar", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_XPathWithDoubleColon() {
        XPathLexer lexer = new XPathLexer("/foo::bar");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.DOUBLE_COLON, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("bar", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_XPathWithAt() {
        XPathLexer lexer = new XPathLexer("/@foo");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.AT_SYMBOL, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_XPathWithStar() {
        XPathLexer lexer = new XPathLexer("/*");

        Token token = lexer.nextToken();
        assertEquals(TokenType.ASTERISK, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.ASTERISK, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_XPathWithLiteral() {
        XPathLexer lexer = new XPathLexer("/'foo'");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.APOSTROPHE, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.APOSTROPHE, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }
}