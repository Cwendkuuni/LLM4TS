import org.junit.Assert;
import org.junit.Test;

import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

public class XPathLexerTest {

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        Assert.assertEquals(-1, token.getTokenType());
    }

    @Test
    public void testNextToken_SimpleXPath() {
        XPathLexer lexer = new XPathLexer("/a[1]/b[1]");
        Token token = null;
        while (token.getTokenType() != Token.TOKEN_TYPE_SLASH) {
            token = lexer.nextToken();
        }
        Assert.assertEquals(Token.TOKEN_TYPE_SLASH, token.getTokenType());

        token = lexer.nextToken();
        Assert.assertEquals(Token.TOKEN_TYPE_IDENTIFIER, token.getTokenType());
        Assert.assertEquals("a", token.getValue());

        token = lexer.nextToken();
        Assert.assertEquals(Token.TOKEN_TYPE_LEFT_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        Assert.assertEquals(Token.TOKEN_TYPE_NUMBER, token.getTokenType());
        Assert.assertEquals("1", token.getValue());

        token = lexer.nextToken();
        Assert.assertEquals(Token.TOKEN_TYPE_RIGHT_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        Assert.assertEquals(Token.TOKEN_TYPE_SLASH, token.getTokenType());

        token = lexer.nextToken();
        Assert.assertEquals(Token.TOKEN_TYPE_IDENTIFIER, token.getTokenType());
        Assert.assertEquals("b", token.getValue());

        token = lexer.nextToken();
        Assert.assertEquals(Token.TOKEN_TYPE_LEFT_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        Assert.assertEquals(Token.TOKEN_TYPE_NUMBER, token.getTokenType());
        Assert.assertEquals("1", token.getValue());

        token = lexer.nextToken();
        Assert.assertEquals(Token.TOKEN_TYPE_RIGHT_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        Assert.assertEquals(-1, token.getTokenType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetXPath_NullXPath() {
        XPathLexer lexer = new XPathLexer(null);
    }

    @Test
    public void testIdentifierOrOperatorName_NoPreviousToken() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.identifierOrOperatorName();
        Assert.assertEquals(Token.TOKEN_TYPE_IDENTIFIER, token.getTokenType());
    }

    @Test
    public void testIdentifierOrOperatorName_PreviousTokenIsIdentifier() {
        XPathLexer lexer = new XPathLexer("a/b");
        Token previousToken = new Token(Token.TOKEN_TYPE_IDENTIFIER, "a", 0, 0);
        lexer.setPreviousToken(previousToken);
        Token token = lexer.identifierOrOperatorName();
        Assert.assertEquals(Token.TOKEN_TYPE_IDENTIFIER, token.getTokenType());
        Assert.assertEquals("b", token.getValue());
    }

    @Test
    public void testIdentifierOrOperatorName_PreviousTokenIsOperator() {
        XPathLexer lexer = new XPathLexer("a/b");
        Token previousToken = new Token(Token.TOKEN_TYPE_PLUS, "", 0, 0);
        lexer.setPreviousToken(previousToken);
        Token token = lexer.identifierOrOperatorName();
        Assert.assertEquals(Token.TOKEN_TYPE_IDENTIFIER, token.getTokenType());
        Assert.assertEquals("b", token.getValue());
    }

    @Test
    public void testIdentifier_SimpleIdentifier() {
        XPathLexer lexer = new XPathLexer("a");
        Token token = lexer.identifier();
        Assert.assertEquals(Token.TOKEN_TYPE_IDENTIFIER, token.getTokenType());
        Assert.assertEquals("a", token.getValue());
    }

    @Test
    public void testIdentifier_IdentifierWithDot() {
        XPathLexer lexer = new XPathLexer("a.b");
        Token token = lexer.identifier();
        Assert.assertEquals(Token.TOKEN_TYPE_IDENTIFIER, token.getTokenType());
        Assert.assertEquals("a.b", token.getValue());
    }

    @Test
    public void testOperatorName_SingleCharacterOperator() {
        XPathLexer lexer = new XPathLexer("a/+");
        Token token = lexer.operatorName();
        Assert.assertEquals(Token.TOKEN_TYPE_PLUS, token.getTokenType());
    }

    @Test
    public void testOperatorName_MultiCharacterOperator() {
        XPathLexer lexer = new XPathLexer("a/mod");
        Token token = lexer.operatorName();
        Assert.assertEquals(Token.TOKEN_TYPE_MOD, token.getTokenType());
    }

    @Test
    public void testNumber_SingleDigitNumber() {
        XPathLexer lexer = new XPathLexer("1");
        Token token = lexer.number();
        Assert.assertEquals(Token.TOKEN_TYPE_NUMBER, token.getTokenType());
        Assert.assertEquals("1", token.getValue());
    }

    @Test
    public void testNumber_MultiDigitNumber() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.number();
        Assert.assertEquals(Token.TOKEN_TYPE_NUMBER, token.getTokenType());
        Assert.assertEquals("123", token.getValue());
    }

    @Test
    public void testNumber_NumberWithDecimalPoint() {
        XPathLexer lexer = new XPathLexer("123.45");
        Token token = lexer.number();
        Assert.assertEquals(Token.TOKEN_TYPE_NUMBER, token.getTokenType());
        Assert.assertEquals("123.45", token.getValue());
    }

    @Test
    public void testWhitespace_SimpleWhitespace() {
        XPathLexer lexer = new XPathLexer("  a");
        Token token = lexer.whitespace();
        Assert.assertEquals(Token.TOKEN_TYPE_WHITESPACE, token.getTokenType());
    }

    @Test
    public void testWhitespace_WhitespaceWithIdentifier() {
        XPathLexer lexer = new XPathLexer(" a ");
        Token token = lexer.nextToken();
        Assert.assertEquals(Token.TOKEN_TYPE_IDENTIFIER, token.getTokenType());
        Assert.assertEquals("a", token.getValue());

        token = lexer.whitespace();
        Assert.assertEquals(Token.TOKEN_TYPE_WHITESPACE, token.getTokenType());
    }

    @Test
    public void testComma() {
        XPathLexer lexer = new XPathLexer("a, b");
        Token token = lexer.comma();
        Assert.assertEquals(Token.TOKEN_TYPE_COMMA, token.getTokenType());
    }

    @Test
    public void testEquals() {
        XPathLexer lexer = new XPathLexer("a = b");
        Token token = lexer.equals();
        Assert.assertEquals(Token.TOKEN_TYPE_EQUALS, token.getTokenType());
    }

    @Test
    public void testMinus() {
        XPathLexer lexer = new XPathLexer("a - b");
        Token token = lexer.minus();
        Assert.assertEquals(Token.TOKEN_TYPE_MINUS, token.getTokenType());
    }

    @Test
    public void testPlus() {
        XPathLexer lexer = new XPathLexer("a + b");
        Token token = lexer.plus();
        Assert.assertEquals(Token.TOKEN_TYPE_PLUS, token.getTokenType());
    }

    @Test
    public void testDollar() {
        XPathLexer lexer = new XPathLexer("$a");
        Token token = lexer.dollar();
        Assert.assertEquals(Token.TOKEN_TYPE_DOLLAR, token.getTokenType());
    }

    @Test
    public void testPipe() {
        XPathLexer lexer = new XPathLexer("a | b");
        Token token = lexer.pipe();
        Assert.assertEquals(Token.TOKEN_TYPE_PIPE, token.getTokenType());
    }

    @Test
    public void testAt() {
        XPathLexer lexer = new XPathLexer("@a");
        Token token = lexer.at();
        Assert.assertEquals(Token.TOKEN_TYPE_AT, token.getTokenType());
    }

    @Test
    public void testColon() {
        XPathLexer lexer = new XPathLexer(":a");
        Token token = lexer.colon();
        Assert.assertEquals(Token.TOKEN_TYPE_COLON, token.getTokenType());
    }

    @Test
    public void testDoubleColon() {
        XPathLexer lexer = new XPathLexer("::a");
        Token token = lexer.doubleColon();
        Assert.assertEquals(Token.TOKEN_TYPE_DOUBLE_COLON, token.getTokenType());
    }

    @Test
    public void testNot() {
        XPathLexer lexer = new XPathLexer("not(a)");
        Token token = lexer.not();
        Assert.assertEquals(Token.TOKEN_TYPE_NOT, token.getTokenType());
    }

    @Test
    public void testNotEquals() {
        XPathLexer lexer = new XPathLexer("not(a) = b");
        Token token = lexer.notEquals();
        Assert.assertEquals(Token.TOKEN_TYPE_NOT_EQUALS, token.getTokenType());
    }

    @Test
    public void testRelationalOperator_LessThan() {
        XPathLexer lexer = new XPathLexer("a < b");
        Token token = lexer.relationalOperator();
        Assert.assertEquals(Token.TOKEN_TYPE_LESS_THAN, token.getTokenType());
    }

    @Test
    public void testRelationalOperator_LessThanOrEqual() {
        XPathLexer lexer = new XPathLexer("a <= b");
        Token token = lexer.relationalOperator();
        Assert.assertEquals(Token.TOKEN_TYPE_LESS_THAN_OR_EQUAL, token.getTokenType());
    }

    @Test
    public void testRelationalOperator_GreaterThan() {
        XPathLexer lexer = new XPathLexer("a > b");
        Token token = lexer.relationalOperator();
        Assert.assertEquals(Token.TOKEN_TYPE_GREATER_THAN, token.getTokenType());
    }

    @Test
    public void testRelationalOperator_GreaterThanOrEqual() {
        XPathLexer lexer = new XPathLexer("a >= b");
        Token token = lexer.relationalOperator();
        Assert.assertEquals(Token.TOKEN_TYPE_GREATER_THAN_OR_EQUAL, token.getTokenType());
    }

    @Test
    public void testStar() {
        XPathLexer lexer = new XPathLexer("a/*");
        Token token = lexer.star();
        Assert.assertEquals(Token.TOKEN_TYPE_STAR, token.getTokenType());
    }

    @Test
    public void testLiteral() {
        XPathLexer lexer = new XPathLexer("\"abc\"");
        Token token = lexer.literal();
        Assert.assertEquals(Token.TOKEN_TYPE_LITERAL, token.getTokenType());
        Assert.assertEquals("abc", token.getValue());

        lexer = new XPathLexer("'abc'");
        token = lexer.literal();
        Assert.assertEquals(Token.TOKEN_TYPE_LITERAL, token.getTokenType());
        Assert.assertEquals("abc", token.getValue());
    }

    @Test
    public void testDots() {
        XPathLexer lexer = new XPathLexer("..");
        Token token = lexer.dots();
        Assert.assertEquals(Token.TOKEN_TYPE_DOTS, token.getTokenType());

        lexer = new XPathLexer("a..b");
        token = lexer.dots();
        Assert.assertEquals(Token.TOKEN_TYPE_DOTS, token.getTokenType());
    }

    @Test
    public void testLeftBracket() {
        XPathLexer lexer = new XPathLexer("[a]");
        Token token = lexer.leftBracket();
        Assert.assertEquals(Token.TOKEN_TYPE_LEFT_BRACKET, token.getTokenType());
    }

    @Test
    public void testRightBracket() {
        XPathLexer lexer = new XPathLexer("[a]");
        Token token = lexer.nextToken();
        Assert.assertEquals(Token.TOKEN_TYPE_IDENTIFIER, token.getTokenType());
        Assert.assertEquals("a", token.getValue());

        token = lexer.rightBracket();
        Assert.assertEquals(Token.TOKEN_TYPE_RIGHT_BRACKET, token.getTokenType());
    }

    @Test
    public void testLeftParen() {
        XPathLexer lexer = new XPathLexer("(a)");
        Token token = lexer.leftParen();
        Assert.assertEquals(Token.TOKEN_TYPE_LEFT_PAREN, token.getTokenType());
    }

    @Test
    public void testRightParen() {
        XPathLexer lexer = new XPathLexer("(a)");
        Token token = lexer.nextToken();
        Assert.assertEquals(Token.TOKEN_TYPE_IDENTIFIER, token.getTokenType());
        Assert.assertEquals("a", token.getValue());

        token = lexer.rightParen();
        Assert.assertEquals(Token.TOKEN_TYPE_RIGHT_PAREN, token.getTokenType());
    }

    @Test
    public void testSlashes() {
        XPathLexer lexer = new XPathLexer("/a/b");
        Token token = lexer.slashes();
        Assert.assertEquals(Token.TOKEN_TYPE_SLASH, token.getTokenType());
    }
}