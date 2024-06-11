import static org.junit.Assert.*;

import org.junit.Test;

import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

public class XPathLexerTest {

    @Test
    public void testEmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testSimpleIdentifier() {
        XPathLexer lexer = new XPathLexer("myIdentifier");
        Token expected = new Token(15, "", 0, 11);
        Token token = lexer.nextToken();
        assertEquals(expected.getType(), token.getType());
        assertEquals(expected.getValue(), token.getValue());
    }

    @Test
    public void testSimpleOperator() {
        XPathLexer lexer = new XPathLexer("myIdentifier op myIdentifier");
        Token expectedOperator = new Token(21, "", 11, 12); // 'op' is assumed to be an operator
        Token token = lexer.nextToken();
        assertEquals(expectedOperator.getType(), token.getType());
        assertEquals(expectedOperator.getValue(), token.getValue());
    }

    @Test
    public void testSimpleNumber() {
        XPathLexer lexer = new XPathLexer("myIdentifier 123");
        Token expected = new Token(31, "", 14, 17);
        Token token = lexer.nextToken();
        assertEquals(expected.getType(), token.getType());
        assertEquals(expected.getValue(), token.getValue());
    }

    @Test
    public void testSimpleNumberWithDecimal() {
        XPathLexer lexer = new XPathLexer("myIdentifier 123.45");
        Token expected = new Token(30, "", 14, 18);
        Token token = lexer.nextToken();
        assertEquals(expected.getType(), token.getType());
        assertEquals(expected.getValue(), token.getValue());
    }

    @Test
    public void testWhitespace() {
        XPathLexer lexer = new XPathLexer("\tmyIdentifier\n op myIdentifier\r");
        Token expected = new Token(-2, "", 0, 0);
        Token token = lexer.nextToken();
        assertEquals(expected.getType(), token.getType());
        assertEquals(expected.getValue(), token.getValue());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidCharacter() {
        XPathLexer lexer = new XPathLexer("myIdentifier#");
        Token token = lexer.nextToken();
        fail("Expected an error when parsing invalid character '#'");
    }

    @Test
    public void testDollarSymbol() {
        XPathLexer lexer = new XPathLexer("$myIdentifier");
        Token expected = new Token(26, "", 0, 6);
        Token token = lexer.nextToken();
        assertEquals(expected.getType(), token.getType());
        assertEquals(expected.getValue(), token.getValue());
    }

    @Test
    public void testComma() {
        XPathLexer lexer = new XPathLexer("myIdentifier, myIdentifier");
        Token expected = new Token(32, "", 11, 12);
        Token token = lexer.nextToken();
        assertEquals(expected.getType(), token.getType());
        assertEquals(expected.getValue(), token.getValue());
    }

    @Test
    public void testEqualsSign() {
        XPathLexer lexer = new XPathLexer("myIdentifier = myIdentifier");
        Token expected = new Token(21, "", 11, 13);
        Token token = lexer.nextToken();
        assertEquals(expected.getType(), token.getType());
        assertEquals(expected.getValue(), token.getValue());
    }

    @Test
    public void testMinusSign() {
        XPathLexer lexer = new XPathLexer("myIdentifier - myIdentifier");
        Token expected = new Token(6, "", 11, 13);
        Token token = lexer.nextToken();
        assertEquals(expected.getType(), token.getType());
        assertEquals(expected.getValue(), token.getValue());
    }

    @Test
    public void testPlusSign() {
        XPathLexer lexer = new XPathLexer("myIdentifier + myIdentifier");
        Token expected = new Token(5, "", 11, 13);
        Token token = lexer.nextToken();
        assertEquals(expected.getType(), token.getType());
        assertEquals(expected.getValue(), token.getValue());
    }

    @Test
    public void testSlashes() {
        XPathLexer lexer = new XPathLexer("/myIdentifier");
        Token expected = new Token(11, "", 0, 6);
        Token token = lexer.nextToken();
        assertEquals(expected.getType(), token.getType());
        assertEquals(expected.getValue(), token.getValue());
    }

    @Test
    public void testDoubleSlashes() {
        XPathLexer lexer = new XPathLexer("//myIdentifier");
        Token expected = new Token(12, "", 0, 7);
        Token token = lexer.nextToken();
        assertEquals(expected.getType(), token.getType());
        assertEquals(expected.getValue(), token.getValue());
    }

    @Test
    public void testLeftParenthesis() {
        XPathLexer lexer = new XPathLexer("(myIdentifier)");
        Token expected = new Token(1, "", 0, 6);
        Token token = lexer.nextToken();
        assertEquals(expected.getType(), token.getType());
        assertEquals(expected.getValue(), token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testRightParenthesis() {
        XPathLexer lexer = new XPathLexer("myIdentifier)");
        Token expectedError = new Token(-1, "", 11, 12);
        Token token = lexer.nextToken();
        assertNotEquals(expectedError.getType(), token.getType());
        fail("Expected an error when parsing right parenthesis without a left parenthesis");
    }

    @Test
    public void testLeftBracket() {
        XPathLexer lexer = new XPathLexer("[myIdentifier]");
        Token expected = new Token(3, "", 0, 7);
        Token token = lexer.nextToken();
        assertEquals(expected.getType(), token.getType());
        assertEquals(expected.getValue(), token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testRightBracket() {
        XPathLexer lexer = new XPathLexer("myIdentifier]");
        Token expectedError = new Token(-1, "", 11, 12);
        Token token = lexer.nextToken();
        assertNotEquals(expectedError.getType(), token.getType());
        fail("Expected an error when parsing right bracket without a left bracket");
    }

    @Test
    public void testStar() {
        XPathLexer lexer = new XPathLexer("myIdentifier*");
        Token expected = new Token(20, "", 11, 13);
        Token token = lexer.nextToken();
        assertEquals(expected.getType(), token.getType());
        assertEquals(expected.getValue(), token.getValue());
    }
}