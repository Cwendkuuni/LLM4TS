import static org.junit.Assert.*;
import static com.werken.saxpath.XPathLexer.*;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testXPathLexer_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
    }

    @Test
    public void testXPathLexer_Identifiers() {
        XPathLexer lexer = new XPathLexer("a b c d_e Foo Bar_Baz");

        Token aToken = lexer.nextToken();
        Token bToken = lexer.nextToken();
        Token cToken = lexer.nextToken();
        Token dToken = lexer.nextToken();
        Token eToken = lexer.nextToken();
        Token fToken = lexer.nextToken();
        Token fooToken = lexer.nextToken();
        Token barToken = lexer.nextToken();
        Token bazToken = lexer.nextToken();

        assertEquals(TokenType.IDENTIFIER, aToken.getTokenType());
        assertEquals("a", aToken.getValue());

        assertEquals(TokenType.IDENTIFIER, bToken.getTokenType());
        assertEquals("b", bToken.getValue());

        assertEquals(TokenType.IDENTIFIER, cToken.getTokenType());
        assertEquals("c", cToken.getValue());

        assertEquals(TokenType.IDENTIFIER, dToken.getTokenType());
        assertEquals("d", dToken.getValue());

        assertEquals(TokenType.IDENTIFIER, eToken.getTokenType());
        assertEquals("e", eToken.getValue());

        assertEquals(TokenType.IDENTIFIER, fToken.getTokenType());
        assertEquals("Foo", fToken.getValue());

        assertEquals(TokenType.IDENTIFIER, fooToken.getTokenType());
        assertEquals("Foo", fooToken.getValue());

        assertEquals(TokenType.IDENTIFIER, barToken.getTokenType());
        assertEquals("Bar", barToken.getValue());

        assertEquals(TokenType.IDENTIFIER, bazToken.getTokenType());
        assertEquals("Baz", bazToken.getValue());
    }

    @Test
    public void testXPathLexer_Operators() {
        XPathLexer lexer = new XPathLexer("+ - = != < > > = <= <= >= != * and or div mod");

        Token plusToken = lexer.nextToken();
        Token minusToken = lexer.nextToken();
        Token equalToken = lexer.nextToken();
        Token notEqualToken = lexer.nextToken();
        Token lessThanToken = lexer.nextToken();
        Token lessThanOrEqualToken = lexer.nextToken();
        Token greaterThanToken = lexer.nextToken();
        Token greaterThanOrEqualToken = lexer.nextToken();
        Token notToken = lexer.nextToken();
        Token starToken = lexer.nextToken();
        Token andToken = lexer.nextToken();
        Token orToken = lexer.nextToken();
        Token divToken = lexer.nextToken();
        Token modToken = lexer.nextToken();

        assertEquals(TokenType.PLUS, plusToken.getTokenType());
        assertEquals("+", plusToken.getValue());

        assertEquals(TokenType.MINUS, minusToken.getTokenType());
        assertEquals("-", minusToken.getValue());

        assertEquals(TokenType.EQUALS, equalToken.getTokenType());
        assertEquals("=", equalToken.getValue());

        assertEquals(TokenType.NOT_EQUALS, notEqualToken.getTokenType());
        assertEquals("!=", notEqualToken.getValue());

        assertEquals(TokenType.LESS_THAN, lessThanToken.getTokenType());
        assertEquals("<", lessThanToken.getValue());

        assertEquals(TokenType.LESS_THAN_OR_EQUAL, lessThanOrEqualToken.getTokenType());
        assertEquals("<=", lessThanOrEqualToken.getValue());

        assertEquals(TokenType.GREATER_THAN, greaterThanToken.getTokenType());
        assertEquals(">", greaterThanToken.getValue());

        assertEquals(TokenType.GREATER_THAN_OR_EQUAL, greaterThanOrEqualToken.getTokenType());
        assertEquals(">=", greaterThanOrEqualToken.getValue());

        assertEquals(TokenType.NOT, notToken.getTokenType());
        assertEquals("!", notToken.getValue());

        assertEquals(TokenType.STAR, starToken.getTokenType());
        assertEquals("*", starToken.getValue());

        assertEquals(TokenType.AND, andToken.getTokenType());
        assertEquals("and", andToken.getValue());

        assertEquals(TokenType.OR, orToken.getTokenType());
        assertEquals("or", orToken.getValue());

        assertEquals(TokenType.DIV, divToken.getTokenType());
        assertEquals("/", divToken.getValue());

        assertEquals(TokenType.MOD, modToken.getTokenType());
        assertEquals("%", modToken.getValue());
    }

    @Test
    public void testXPathLexer_Numbers() {
        XPathLexer lexer = new XPathLexer("123 1.23 123.45 123.45e-4");

        Token token1 = lexer.nextToken();
        Token token2 = lexer.nextToken();
        Token token3 = lexer.nextToken();
        Token token4 = lexer.nextToken();

        assertEquals(TokenType.NUMBER, token1.getTokenType());
        assertEquals("123", token1.getValue());

        assertEquals(TokenType.NUMBER, token2.getTokenType());
        assertEquals("1.23", token2.getValue());

        assertEquals(TokenType.NUMBER, token3.getTokenType());
        assertEquals("123.45", token3.getValue());

        assertEquals(TokenType.NUMBER, token4.getTokenType());
        assertEquals("123.45e-4", token4.getValue());
    }

    @Test
    public void testXPathLexer_DollarSign() {
        XPathLexer lexer = new XPathLexer("$foo");

        Token token = lexer.nextToken();

        assertEquals(TokenType.DOLLAR, token.getTokenType());
        assertEquals("$", token.getValue());
    }

    @Test
    public void testXPathLexer_Brackets() {
        XPathLexer lexer = new XPathLexer("[1][2]");

        Token leftBracketToken = lexer.nextToken();
        Token token1 = lexer.nextToken();
        Token rightBracketToken1 = lexer.nextToken();
        Token leftBracketToken1 = lexer.nextToken();
        Token token2 = lexer.nextToken();
        Token rightBracketToken2 = lexer.nextToken();

        assertEquals(TokenType.LEFT_BRACKET, leftBracketToken.getTokenType());
        assertEquals("[", leftBracketToken.getValue());

        assertEquals(TokenType.NUMBER, token1.getTokenType());
        assertEquals("1", token1.getValue());

        assertEquals(TokenType.RIGHT_BRACKET, rightBracketToken1.getTokenType());
        assertEquals("]", rightBracketToken1.getValue());

        assertEquals(TokenType.LEFT_BRACKET, leftBracketToken1.getTokenType());
        assertEquals("[", leftBracketToken1.getValue());

        assertEquals(TokenType.NUMBER, token2.getTokenType());
        assertEquals("2", token2.getValue());

        assertEquals(TokenType.RIGHT_BRACKET, rightBracketToken2.getTokenType());
        assertEquals("]", rightBracketToken2.getValue());
    }

    @Test
    public void testXPathLexer_Slashes() {
        XPathLexer lexer = new XPathLexer("/foo/bar");

        Token slashToken1 = lexer.nextToken();
        Token identifierToken1 = lexer.nextToken();
        Token slashToken2 = lexer.nextToken();
        Token identifierToken2 = lexer.nextToken();

        assertEquals(TokenType.SLASH, slashToken1.getTokenType());
        assertEquals("/", slashToken1.getValue());

        assertEquals(TokenType.IDENTIFIER, identifierToken1.getTokenType());
        assertEquals("foo", identifierToken1.getValue());

        assertEquals(TokenType.SLASH, slashToken2.getTokenType());
        assertEquals("/", slashToken2.getValue());

        assertEquals(TokenType.IDENTIFIER, identifierToken2.getTokenType());
        assertEquals("bar", identifierToken2.getValue());
    }

    @Test
    public void testXPathLexer_Quotes() {
        XPathLexer lexer = new XPathLexer("\"foo\"");

        Token leftQuoteToken = lexer.nextToken();
        Token stringToken = lexer.nextToken();
        Token rightQuoteToken = lexer.nextToken();

        assertEquals(TokenType.LEFT_QUOTE, leftQuoteToken.getTokenType());
        assertEquals("\"", leftQuoteToken.getValue());

        assertEquals(TokenType.STRING, stringToken.getTokenType());
        assertEquals("foo", stringToken.getValue());

        assertEquals(TokenType.RIGHT_QUOTE, rightQuoteToken.getTokenType());
        assertEquals("\"", rightQuoteToken.getValue());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testXPathLexer_EndOfXPath() {
        XPathLexer lexer = new XPathLexer("foo");
        lexer.nextToken();
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testXPathLexer_InvalidXPath() {
        XPathLexer lexer = new XPathLexer("foo@bar");
        lexer.nextToken();
    }
}