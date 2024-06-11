import static org.junit.Assert.*;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_BasicFunctionality() {
        XPathLexer lexer = new XPathLexer("/test");
        assertEquals(TokenType.SLASH, lexer.nextToken().getTokenType());
    }

    @Test
    public void testNextToken_Identifier() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("test", token.getValue());
    }

    @Test
    public void testNextToken_Whitespace() {
        XPathLexer lexer = new XPathLexer("   /test   ");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextToken_NegativePosition() {
        XPathLexer lexer = new XPathLexer("test");
        lexer.setCurrentPosition(-1);
        lexer.nextToken();
    }

    @Test(expected = IllegalStateException.class)
    public void testNextToken_EndPositionExceeded() {
        XPathLexer lexer = new XPathLexer("test");
        lexer.setEndPosition(0);
        lexer.nextToken();
    }

    @Test
    public void testNextToken_DollarToken() {
        XPathLexer lexer = new XPathLexer("$xpath");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOLLAR, token.getTokenType());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextToken_UnexpectedCharacter() {
        XPathLexer lexer = new XPathLexer("@test");
        lexer.nextToken();
        lexer.nextToken(); // should be an identifier or operator name
        lexer.nextToken(); // should be an at symbol (@)
        lexer.nextToken(); // should be an identifier
        lexer.nextToken(); // should be an identifier or operator name
        lexer.nextToken(); // should be a colon (:)
        lexer.nextToken(); // should be an identifier
        lexer.nextToken(); // should be an unexpected character
    }

    @Test
    public void testNextToken_Number() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123", token.getValue());
    }

    @Test
    public void testNextToken_Dot() {
        XPathLexer lexer = new XPathLexer(".");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOT, token.getTokenType());
    }

    @Test
    public void testNextToken_DoubleDot() {
        XPathLexer lexer = new XPathLexer("..");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOTS, token.getTokenType());
    }

    @Test
    public void testNextToken_Comma() {
        XPathLexer lexer = new XPathLexer(",");
        Token token = lexer.nextToken();
        assertEquals(TokenType.COMMA, token.getTokenType());
    }

    @Test
    public void testNextToken_LeftParenthesis() {
        XPathLexer lexer = new XPathLexer("(");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LEFT_PAREN, token.getTokenType());
    }

    @Test
    public void testNextToken_RightParenthesis() {
        XPathLexer lexer = new XPathLexer(")");
        Token token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());
    }

    @Test
    public void testNextToken_LeftBracket() {
        XPathLexer lexer = new XPathLexer("[");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
    }

    @Test
    public void testNextToken_RightBracket() {
        XPathLexer lexer = new XPathLexer("]");
        Token token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
    }

    @Test
    public void testNextToken_Slash() {
        XPathLexer lexer = new XPathLexer("/");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test
    public void testNextToken_AtSymbol() {
        XPathLexer lexer = new XPathLexer("@");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AT, token.getTokenType());
    }

    @Test
    public void testNextToken_Colon() {
        XPathLexer lexer = new XPathLexer(":");
        Token token = lexer.nextToken();
        assertEquals(TokenType.COLON, token.getTokenType());
    }

    @Test
    public void testNextToken_DoubleColon() {
        XPathLexer lexer = new XPathLexer("::");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOUBLE_COLON, token.getTokenType());
    }

    @Test
    public void testNextToken_Plus() {
        XPathLexer lexer = new XPathLexer("+");
        Token token = lexer.nextToken();
        assertEquals(TokenType.PLUS, token.getTokenType());
    }

    @Test
    public void testNextToken_Minus() {
        XPathLexer lexer = new XPathLexer("-");
        Token token = lexer.nextToken();
        assertEquals(TokenType.MINUS, token.getTokenType());
    }

    @Test
    public void testNextToken_Equals() {
        XPathLexer lexer = new XPathLexer("=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());
    }

    @Test
    public void testNextToken_NotEquals() {
        XPathLexer lexer = new XPathLexer("!=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NOT_EQUALS, token.getTokenType());
    }

    @Test
    public void testNextToken_LessThan() {
        XPathLexer lexer = new XPathLexer("<");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LESS_THAN, token.getTokenType());
    }

    @Test
    public void testNextToken_GreaterThan() {
        XPathLexer lexer = new XPathLexer(">");
        Token token = lexer.nextToken();
        assertEquals(TokenType.GREATER_THAN, token.getTokenType());
    }

    @Test
    public void testNextToken_LessThanOrEqual() {
        XPathLexer lexer = new XPathLexer("<=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LESS_THAN_OR_EQUAL, token.getTokenType());
    }

    @Test
    public void testNextToken_GreaterThanOrEqual() {
        XPathLexer lexer = new XPathLexer(">=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.GREATER_THAN_OR_EQUAL, token.getTokenType());
    }

    @Test
    public void testNextToken_And() {
        XPathLexer lexer = new XPathLexer("and");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AND, token.getTokenType());
    }

    @Test
    public void testNextToken_Or() {
        XPathLexer lexer = new XPathLexer("or");
        Token token = lexer.nextToken();
        assertEquals(TokenType.OR, token.getTokenType());
    }

    @Test
    public void testNextToken_Not() {
        XPathLexer lexer = new XPathLexer("not");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NOT, token.getTokenType());
    }

    @Test
    public void testNextToken_Mod() {
        XPathLexer lexer = new XPathLexer("mod");
        Token token = lexer.nextToken();
        assertEquals(TokenType.MOD, token.getTokenType());
    }

    @Test
    public void testNextToken_Div() {
        XPathLexer lexer = new XPathLexer("div");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DIV, token.getTokenType());
    }
}