import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

public class XPathLexerTest {
    private XPathLexer lexer;

    @Before
    public void setUp() {
        this.lexer = new XPathLexer("");
    }

    @After
    public void tearDown() {
        this.lexer = null;
    }

    @Test
    public void testEmptyXPath() {
        assertEquals(new Token(-2, "", 0, 0), this.lexer.nextToken());
    }

    @Test
    public void testNextTokenDollar() {
        this.lexer.setXPath("$");
        assertEquals(new Token(26, "", 0, 1), this.lexer.nextToken());
    }

    @Test
    public void testNextTokenIdentifier() {
        this.lexer.setXPath("myIdentifier");
        Token token = this.lexer.nextToken();
        assertEquals(15, token.getTokenType());
        assertEquals("myIdentifier", token.getValue());
    }

    @Test
    public void testNextTokenWhitespace() {
        this.lexer.setXPath("   myIdentifier   ");
        Token token = this.lexer.nextToken();
        assertEquals(-2, token.getTokenType());
    }

    @Test
    public void testNextTokenLiteral() {
        this.lexer.setXPath("'myString'");
        Token token = this.lexer.nextToken();
        assertEquals(27, token.getTokenType());
        assertEquals("myString", token.getValue());
    }

    @Test
    public void testNextTokenSlash() {
        this.lexer.setXPath("/");
        Token token = this.lexer.nextToken();
        assertEquals(11, token.getTokenType());
    }

    @Test
    public void testNextTokenComma() {
        this.lexer.setXPath(",");
        Token token = this.lexer.nextToken();
        assertEquals(32, token.getTokenType());
    }

    @Test
    public void testNextTokenLeftParen() {
        this.lexer.setXPath("(");
        Token token = this.lexer.nextToken();
        assertEquals(1, token.getTokenType());
    }

    @Test
    public void testNextTokenRightParen() {
        this.lexer.setXPath(")");
        Token token = this.lexer.nextToken();
        assertEquals(2, token.getTokenType());
    }

    @Test
    public void testNextTokenLeftBracket() {
        this.lexer.setXPath("[");
        Token token = this.lexer.nextToken();
        assertEquals(3, token.getTokenType());
    }

    @Test
    public void testNextTokenRightBracket() {
        this.lexer.setXPath("]");
        Token token = this.lexer.nextToken();
        assertEquals(4, token.getTokenType());
    }

    @Test
    public void testNextTokenPlus() {
        this.lexer.setXPath("+");
        Token token = this.lexer.nextToken();
        assertEquals(5, token.getTokenType());
    }

    @Test
    public void testNextTokenMinus() {
        this.lexer.setXPath("-");
        Token token = this.lexer.nextToken();
        assertEquals(6, token.getTokenType());
    }

    @Test
    public void testNextTokenEquals() {
        this.lexer.setXPath("=");
        Token token = this.lexer.nextToken();
        assertEquals(21, token.getTokenType());
    }

    @Test
    public void testNextTokenNotEquals() {
        this.lexer.setXPath("!=");
        Token token = this.lexer.nextToken();
        assertEquals(22, token.getTokenType());
    }

    @Test
    public void testNextTokenGreaterThan() {
        this.lexer.setXPath(">");
        Token token = this.lexer.nextToken();
        assertEquals(9, token.getTokenType());
    }

    @Test
    public void testNextTokenLessThan() {
        this.lexer.setXPath("<");
        Token token = this.lexer.nextToken();
        assertEquals(7, token.getTokenType());
    }

    @Test
    public void testNextTokenGreaterThanOrEqual() {
        this.lexer.setXPath(">=");
        Token token = this.lexer.nextToken();
        assertEquals(10, token.getTokenType());
    }

    @Test
    public void testNextTokenLessThanOrEqual() {
        this.lexer.setXPath("<=");
        Token token = this.lexer.nextToken();
        assertEquals(8, token.getTokenType());
    }

    @Test
    public void testNextTokenAsterisk() {
        this.lexer.setXPath("*");
        Token token = this.lexer.nextToken();
        assertEquals(20, token.getTokenType());
    }

    @Test
    public void testNextTokenPeriod() {
        this.lexer.setXPath(".");
        Token token = this.lexer.nextToken();
        if (token.getTokenType() == 14) {
            assertEquals(".", token.getValue());
        } else {
            assertEquals(13, token.getTokenType());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextTokenInvalidChar() {
        this.lexer.setXPath("@");
        this.lexer.nextToken();
        fail("Expected IllegalArgumentException");
    }
}