import static org.junit.Assert.*;
import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;
import com.werken.saxpath.TokenType;

public class XPathLexerTest {

    @Test
    public void testNextToken() {
        XPathLexer lexer = new XPathLexer("//element[@attribute='value']");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_WhitespaceOnlyXPath() {
        XPathLexer lexer = new XPathLexer("   ");
        Token token = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
    }

    @Test
    public void testNextToken_SpecialCharacter() {
        XPathLexer lexer = new XPathLexer("//$attribute[@value]");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOLLAR, token.getTokenType());
    }

    @Test
    public void testNextToken_Slash() {
        XPathLexer lexer = new XPathLexer("//element[@attribute='value']/child::node()");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test
    public void testNextToken_Identifier() {
        XPathLexer lexer = new XPathLexer("//element[@attribute='value']");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("element", token.getValue());
    }

    @Test
    public void testNextToken_Operator() {
        XPathLexer lexer = new XPathLexer("//element[@attribute='value']/child::node()=3");
        Token token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());
    }

    @Test
    public void testNextToken_Number() {
        XPathLexer lexer = new XPathLexer("//element[@attribute='3']");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("3", token.getValue());
    }

    @Test
    public void testNextToken_SingleQuoteLiteral() {
        XPathLexer lexer = new XPathLexer("//element[@attribute='single\'quote']");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LITERAL, token.getTokenType());
        assertEquals("single'", token.getValue());
    }

    @Test
    public void testNextToken_DoubleQuoteLiteral() {
        XPathLexer lexer = new XPathLexer("//element[@attribute='double\"quote']");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LITERAL, token.getTokenType());
        assertEquals("double\"quote", token.getValue());
    }

    @Test
    public void testNextToken_Comment() {
        XPathLexer lexer = new XPathLexer("//comment()");
        Token token = lexer.nextToken();
        assertEquals(TokenType.COMMENT, token.getTokenType());
        assertEquals("<!--", token.getValue());
    }

    @Test
    public void testNextToken_ProcessingInstruction() {
        XPathLexer lexer = new XPathLexer("processing-instruction('xml')");
        Token token = lexer.nextToken();
        assertEquals(TokenType.PROCESSING_INSTRUCTION, token.getTokenType());
        assertEquals("xml", token.getValue());
    }

    @Test
    public void testNextToken_CDATASection() {
        XPathLexer lexer = new XPathLexer("<![CDATA[this is a CDATA section]]>");
        Token token = lexer.nextToken();
        assertEquals(TokenType.CDATA, token.getTokenType());
        assertEquals("this is a CDATA section", token.getValue());
    }

    @Test
    public void testIdentifierOrOperatorName() {
        XPathLexer lexer = new XPathLexer("//and[a='b']");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AND, token.getTokenType());

        lexer.setPreviousToken(null); // Reset the previous token for the next test

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());
    }

    @Test
    public void testIdentifier() {
        XPathLexer lexer = new XPathLexer("//element[@attribute='value']");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("attribute", token.getValue());
    }

    @Test
    public void testOperatorName() {
        XPathLexer lexer = new XPathLexer("//div[@a=3]");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DIV, token.getTokenType());
    }
}