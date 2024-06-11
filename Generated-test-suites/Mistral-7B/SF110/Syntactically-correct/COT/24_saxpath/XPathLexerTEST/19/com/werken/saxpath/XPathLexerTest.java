import static org.junit.Assert.*;

import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

import org.junit.Test;

public class XPathLexerTest {
    @Test
    public void testEmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        assertNull(lexer.nextToken());
    }

    @Test
    public void testSimpleIdentifier() {
        XPathLexer lexer = new XPathLexer("testIdentifier");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("testIdentifier", token.getText());
    }

    @Test
    public void testSimpleOperator() {
        XPathLexer lexer = new XPathLexer("+");
        Token token = lexer.nextToken();
        assertEquals(TokenType.PLUS, token.getTokenType());
    }

    @Test
    public void testSimpleLiteral() {
        XPathLexer lexer = new XPathLexer("'test'");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LITERAL, token.getTokenType());
        assertEquals("test", token.getText());
    }

    @Test
    public void testSimpleNumber() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123", token.getText());
    }

    @Test
    public void testWhitespace() {
        XPathLexer lexer = new XPathLexer(" \t\n test ");
        Token token = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
    }

    @Test
    public void testComma() {
        XPathLexer lexer = new XPathLexer(",");
        Token token = lexer.nextToken();
        assertEquals(TokenType.COMMA, token.getTokenType());
    }

    @Test
    public void testEquals() {
        XPathLexer lexer = new XPathLexer("=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());
    }

    @Test
    public void testMinus() {
        XPathLexer lexer = new XPathLexer("-");
        Token token = lexer.nextToken();
        assertEquals(TokenType.MINUS, token.getTokenType());
    }

    @Test
    public void testPlus() {
        XPathLexer lexer = new XPathLexer("+");
        Token token = lexer.nextToken();
        assertEquals(TokenType.PLUS, token.getTokenType());
    }

    @Test
    public void testDollar() {
        XPathLexer lexer = new XPathLexer("$");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOLLAR, token.getTokenType());
    }

    @Test
    public void testPipe() {
        XPathLexer lexer = new XPathLexer("|");
        Token token = lexer.nextToken();
        assertEquals(TokenType.PIPE, token.getTokenType());
    }

    @Test
    public void testAt() {
        XPathLexer lexer = new XPathLexer("@");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AT, token.getTokenType());
    }

    @Test
    public void testColon() {
        XPathLexer lexer = new XPathLexer(":");
        Token token = lexer.nextToken();
        assertEquals(TokenType.COLON, token.getTokenType());
    }

    @Test
    public void testDoubleColon() {
        XPathLexer lexer = new XPathLexer("::");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOUBLE_COLON, token.getTokenType());
    }

    @Test
    public void testNot() {
        XPathLexer lexer = new XPathLexer("!");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NOT, token.getTokenType());
    }

    @Test
    public void testNotEquals() {
        XPathLexer lexer = new XPathLexer("!=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NOT_EQUALS, token.getTokenType());
    }

    @Test
    public void testRelationalOperatorWithEquals() {
        XPathLexer lexer = new XPathLexer(">=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.GREATER_EQUALS, token.getTokenType());
    }

    @Test
    public void testRelationalOperatorWithoutEquals() {
        XPathLexer lexer = new XPathLexer(">");
        Token token = lexer.nextToken();
        assertEquals(TokenType.GREATER_THAN, token.getTokenType());
    }

    @Test
    public void testIdentifierFollowedByOperator() {
        XPathLexer lexer = new XPathLexer("testIdentifier +");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("testIdentifier", token.getText());
        token = lexer.nextToken();
        assertEquals(TokenType.PLUS, token.getTokenType());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidCharacter() {
        XPathLexer lexer = new XPathLexer("x&%");
        lexer.nextToken();
    }

    @Test
    public void testInvalidCharacterAfterIdentifier() {
        XPathLexer lexer = new XPathLexer("testIdentifier&");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("testIdentifier", token.getText());
        token = lexer.nextToken();
        assertEquals(TokenType.ERROR, token.getTokenType());
    }

    @Test
    public void testInvalidCharacterAfterOperator() {
        XPathLexer lexer = new XPathLexer("+&");
        Token token = lexer.nextToken();
        assertEquals(TokenType.PLUS, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.ERROR, token.getTokenType());
    }

    @Test
    public void testInvalidCharacterAtEnd() {
        XPathLexer lexer = new XPathLexer("testIdentifier");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("testIdentifier", token.getText());
        lexer.consume(); // consume the end of the string
        token = lexer.nextToken();
        assertEquals(TokenType.ERROR, token.getTokenType());
    }

    @Test
    public void testMalformedNumber() {
        XPathLexer lexer = new XPathLexer("123a");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123", token.getText());
        lexer.consume(); // consume the 'a'
        token = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
    }

    @Test
    public void testMalformedNumberAtEnd() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123", token.getText());
        lexer.consume(); // consume the end of the string
        token = lexer.nextToken();
        assertEquals(TokenType.ERROR, token.getTokenType());
    }

    @Test
    public void testMalformedNumberWithoutDigits() {
        XPathLexer lexer = new XPathLexer("1a");
        Token token = lexer.nextToken();
        assertEquals(TokenType.ERROR, token.getTokenType());
    }

    @Test
    public void testMalformedNumberWithLeadingZero() {
        XPathLexer lexer = new XPathLexer("0123");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123", token.getText());
    }

    @Test
    public void testMalformedNumberWithTrailingZero() {
        XPathLexer lexer = new XPathLexer("1230");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123", token.getText());
    }

    @Test
    public void testMalformedNumberWithExponent() {
        XPathLexer lexer = new XPathLexer("1e3");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1e3", token.getText());
    }

    @Test
    public void testMalformedNumberWithExponentAndDigits() {
        XPathLexer lexer = new XPathLexer("1e34");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1e34", token.getText());
    }

    @Test
    public void testMalformedNumberWithExponentAndDigitsAndSign() {
        XPathLexer lexer = new XPathLexer("1e34+");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1e34", token.getText());
        token = lexer.nextToken();
        assertEquals(TokenType.PLUS, token.getTokenType());
    }

    @Test
    public void testMalformedNumberWithExponentAndDigitsAndSignAndTrailingZero() {
        XPathLexer lexer = new XPathLexer("1e34+0");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1e34", token.getText());
        token = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.ERROR, token.getTokenType());
    }

    @Test
    public void testMalformedNumberWithExponentAndDigitsAndSignAndTrailingZeroAndDigits() {
        XPathLexer lexer = new XPathLexer("1e34+01");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1e34", token.getText());
        token = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("1", token.getText());
        token = lexer.nextToken();
        assertEquals(TokenType.ERROR, token.getTokenType());
    }
}