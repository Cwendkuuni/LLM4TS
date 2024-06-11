import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;
import com.werken.saxpath.XPathLexer.LA;

public class XPathLexerTest {
    private static final String XPATH = "abc/def$ghi*jkl+mno[pqr]";
    private XPathLexer lexer;
    
    @Before
    public void setUp() {
        lexer = new XPathLexer(XPATH);
    }

    @Test
    public void testNextToken_InitialState() {
        assertNull(lexer.nextToken());
        assertEquals(0, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_EmptyXPath() {
        lexer = new XPathLexer("");
        assertNull(lexer.nextToken());
        assertEquals(0, lexer.currentPosition());
        assertEquals(0, lexer.endPosition());
    }

    @Test
    public void testNextToken_DollarToken() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.DOLLAR, token.getType());
        assertEquals("abc", token.getText());
        assertEquals(1, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_Identifier() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("abc", token.getText());
        assertEquals(4, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_Slashes() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.SLASHES, token.getType());
        assertEquals("/", token.getText());
        assertEquals(4, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_Comma() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.COMMA, token.getType());
        assertEquals(",", token.getText());
        assertEquals(11, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_LeftParen() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.LEFT_PAREN, token.getType());
        assertEquals("(", token.getText());
        assertEquals(13, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_RightParen() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.RIGHT_PAREN, token.getType());
        assertEquals(")", token.getText());
        assertEquals(14, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_LeftBracket() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.LEFT_BRACKET, token.getType());
        assertEquals("[", token.getText());
        assertEquals(16, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_RightBracket() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.RIGHT_BRACKET, token.getType());
        assertEquals("]", token.getText());
        assertEquals(17, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextToken_EOF() {
        Token token = lexer.nextToken();
        assertNull(token);
        assertEquals(XPATH.length(), lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
        lexer.nextToken();
    }

    @Test
    public void testNextToken_Plus() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.PLUS, token.getType());
        assertEquals("+", token.getText());
        assertEquals(19, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_Minus() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.MINUS, token.getType());
        assertEquals("-", token.getText());
        assertEquals(20, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_RelationalOperator() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.LESS_THAN, token.getType());
        assertEquals("<", token.getText());
        assertEquals(22, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_Equals() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.EQUALS, token.getType());
        assertEquals("=", token.getText());
        assertEquals(24, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_NotEquals() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.NOT_EQUALS, token.getType());
        assertEquals("!=", token.getText());
        assertEquals(25, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_Not() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.NOT, token.getType());
        assertEquals("!", token.getText());
        assertEquals(26, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_Pipe() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.PIPE, token.getType());
        assertEquals("|", token.getText());
        assertEquals(27, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_At() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.AT, token.getType());
        assertEquals("@", token.getText());
        assertEquals(28, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_Colon() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.COLON, token.getType());
        assertEquals(":", token.getText());
        assertEquals(29, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_DoubleColon() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.DOUBLE_COLON, token.getType());
        assertEquals("::", token.getText());
        assertEquals(30, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_Star() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.ASTERISK, token.getType());
        assertEquals("*", token.getText());
        assertEquals(31, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_Number() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.NUMBER, token.getType());
        assertEquals("1", token.getText());
        assertEquals(6, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_Dot() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.DOT, token.getType());
        assertEquals(".", token.getText());
        assertEquals(7, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_Whitespace() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.WHITESPACE, token.getType());
        assertNull(token.getText());
        assertEquals(5, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_IdentifierOrOperatorName() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER_OR_OPERATOR_NAME, token.getType());
        assertEquals("def", token.getText());
        assertEquals(8, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_OperatorName() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.OPERATOR_NAME, token.getType());
        assertEquals("mod", token.getText());
        assertEquals(11, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_And() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.AND, token.getType());
        assertEquals("and", token.getText());
        assertEquals(13, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_Or() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.OR, token.getType());
        assertEquals("or", token.getText());
        assertEquals(15, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_Div() {
        Token token = lexer.nextToken();
        assertEquals(Token.Type.DIV, token.getType());
        assertEquals("div", token.getText());
        assertEquals(17, lexer.currentPosition());
        assertEquals(XPATH.length(), lexer.endPosition());
    }

    @Test
    public void testNextToken_Div2() {
        lexer = new XPathLexer("div");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.DIV, token.getType());
        assertEquals("div", token.getText());
        assertEquals(0, lexer.currentPosition());
        assertEquals(4, lexer.endPosition());
    }

    @Test
    public void testNextToken_Star2() {
        lexer = new XPathLexer("star");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.ASTERISK, token.getType());
        assertEquals("star", token.getText());
        assertEquals(0, lexer.currentPosition());
        assertEquals(5, lexer.endPosition());
    }

    @Test
    public void testNextToken_At2() {
        lexer = new XPathLexer("@at");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.AT, token.getType());
        assertEquals("@at", token.getText());
        assertEquals(0, lexer.currentPosition());
        assertEquals(3, lexer.endPosition());
    }

    @Test
    public void testPreviousToken_Identical() {
        Token previous = lexer.getPreviousToken();
        assertNull(previous);
        Token token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("abc", token.getText());
        previous = lexer.getPreviousToken();
        assertEquals(Token.Type.IDENTIFIER, previous.getType());
        assertEquals("abc", previous.getText());
    }

    @Test
    public void testPreviousToken_PreviousIsDifferent() {
        Token previous = lexer.getPreviousToken();
        assertNull(previous);
        Token token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("abc", token.getText());
        token = lexer.nextToken();
        assertEquals(Token.Type.SLASHES, token.getType());
        assertEquals("/", token.getText());
        previous = lexer.getPreviousToken();
        assertEquals(Token.Type.IDENTIFIER, previous.getType());
        assertEquals("abc", previous.getText());
    }

    @Test(expected = IllegalStateException.class)
    public void testPreviousToken_NoPreviousToken() {
        Token previous = lexer.getPreviousToken();
        assertNull(previous);
        lexer.nextToken();
        previous = lexer.getPreviousToken();
        throw new IllegalStateException();
    }
}