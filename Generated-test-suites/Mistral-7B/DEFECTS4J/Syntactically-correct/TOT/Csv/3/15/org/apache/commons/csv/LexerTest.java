import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class LexerTest {

    private CSVFormat format;
    private ExtendedBufferedReader in;
    private Lexer lexer;

    @Before
    public void setUp() throws IOException {
        format = new CSVFormatBuilder().setDelimiter(';').build();
        String input = "Hello, World!\n" +
                "1, 2, 3\n" +
                "\"Hello, World!\"\n" +
                "\"Hello, World!\"\n" +
                "Hello, World!\n" +
                "1, 2, 3,\n" +
                "1, 2, 3\n" +
                "# This is a comment\n" +
                "1, 2, 3\n" +
                "";
        in = new ExtendedBufferedReader(new StringReader(input));
        lexer = new Lexer(format, in);
    }

    @Test
    public void testNextToken_Normal() throws IOException {
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);

        Assert.assertEquals("Hello, World!", token.getString());
        Assert.assertEquals(Lexer.TokenType.STRING, token.getType());
        Assert.assertEquals(Lexer.TokenType.NUMBER, nextToken.getType());
        Assert.assertEquals("1", nextToken.getString());

        token = new Token();
        nextToken = lexer.nextToken(token);

        Assert.assertEquals("1", token.getString());
        Assert.assertEquals(Lexer.TokenType.NUMBER, token.getType());
        Assert.assertEquals(Lexer.TokenType.NUMBER, nextToken.getType());
        Assert.assertEquals("2", nextToken.getString());

        token = new Token();
        nextToken = lexer.nextToken(token);

        Assert.assertEquals("2", token.getString());
        Assert.assertEquals(Lexer.TokenType.NUMBER, token.getType());
        Assert.assertEquals(Lexer.TokenType.DELIMITER, nextToken.getType());
        Assert.assertEquals(format.getDelimiter(), nextToken.getString().charAt(0));

        token = new Token();
        nextToken = lexer.nextToken(token);

        Assert.assertEquals("3", token.getString());
        Assert.assertEquals(Lexer.TokenType.NUMBER, token.getType());
        Assert.assertEquals(Lexer.TokenType.END_OF_LINE, nextToken.getType());

        token = new Token();
        nextToken = lexer.nextToken(token);

        Assert.assertEquals("\"Hello, World!\"", token.getString());
        Assert.assertEquals(Lexer.TokenType.STRING, token.getType());
        Assert.assertEquals(Lexer.TokenType.END_OF_LINE, nextToken.getType());

        token = new Token();
        nextToken = lexer.nextToken(token);

        Assert.assertEquals("\"Hello, World!\"", token.getString());
        Assert.assertEquals(Lexer.TokenType.STRING, token.getType());
        Assert.assertEquals(Lexer.TokenType.END_OF_LINE, nextToken.getType());

        token = new Token();
        nextToken = lexer.nextToken(token);

        Assert.assertEquals("Hello, World!", token.getString());
        Assert.assertEquals(Lexer.TokenType.STRING, token.getType());
        Assert.assertEquals(Lexer.TokenType.END_OF_LINE, nextToken.getType());

        token = new Token();
        nextToken = lexer.nextToken(token);

        Assert.assertEquals("1", token.getString());
        Assert.assertEquals(Lexer.TokenType.NUMBER, token.getType());
        Assert.assertEquals(Lexer.TokenType.DELIMITER, nextToken.getType());
        Assert.assertEquals(format.getDelimiter(), nextToken.getString().charAt(0));

        token = new Token();
        nextToken = lexer.nextToken(token);

        Assert.assertEquals("2", token.getString());
        Assert.assertEquals(Lexer.TokenType.NUMBER, token.getType());
        Assert.assertEquals(Lexer.TokenType.DELIMITER, nextToken.getType());
        Assert.assertEquals(format.getDelimiter(), nextToken.getString().charAt(0));

        token = new Token();
        nextToken = lexer.nextToken(token);

        Assert.assertEquals("3", token.getString());
        Assert.assertEquals(Lexer.TokenType.NUMBER, token.getType());
        Assert.assertEquals(Lexer.TokenType.END_OF_LINE, nextToken.getType());

        token = new Token();
        nextToken = lexer.nextToken(token);

        Assert.assertEquals("# This is a comment", token.getString());
        Assert.assertEquals(Lexer.TokenType.COMMENT, token.getType());
        Assert.assertEquals(Lexer.TokenType.END_OF_LINE, nextToken.getType());

        token = new Token();
        nextToken = lexer.nextToken(token);

        Assert.assertEquals("1", token.getString());
        Assert.assertEquals(Lexer.TokenType.NUMBER, token.getType());
        Assert.assertEquals(Lexer.TokenType.END_OF_LINE, nextToken.getType());
    }

    @Test
    public void testNextToken_EmptyLine() throws IOException {
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);

        Assert.assertEquals("", token.getString());
        Assert.assertEquals(Lexer.TokenType.EMPTY, token.getType());
        Assert.assertEquals(Lexer.TokenType.END_OF_LINE, nextToken.getType());
    }

    @Test(expected = IOException.class)
    public void testNextToken_EOF() throws IOException {
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);

        Assert.assertEquals("", token.getString());
        Assert.assertEquals(Lexer.TokenType.END_OF_LINE, token.getType());
        lexer.nextToken(token); // consume the end of line token

        lexer.nextToken(token); // should throw an IOException
    }

    @Test
    public void testNextToken_Whitespace() throws IOException {
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);

        Assert.assertEquals("", token.getString());
        Assert.assertEquals(Lexer.TokenType.WHITESPACE, token.getType());

        String whitespace = " \t\r\f";
        for (int i = 0; i < whitespace.length(); i++) {
            nextToken = lexer.nextToken(token);
            Assert.assertEquals(whitespace.charAt(i), nextToken.getString().charAt(0));
            Assert.assertEquals(Lexer.TokenType.WHITESPACE, nextToken.getType());
        }

        nextToken = lexer.nextToken(token);
        Assert.assertEquals("1", nextToken.getString());
        Assert.assertEquals(Lexer.TokenType.NUMBER, nextToken.getType());
    }

    @Test
    public void testNextToken_Comment() throws IOException {
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);

        Assert.assertEquals("# This is a comment", token.getString());
        Assert.assertEquals(Lexer.TokenType.COMMENT, token.getType());

        nextToken = lexer.nextToken(token);
        Assert.assertEquals("1", nextToken.getString());
        Assert.assertEquals(Lexer.TokenType.NUMBER, nextToken.getType());
    }

    @Test
    public void testNextToken_EndOfFile() throws IOException {
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);

        Assert.assertEquals("", token.getString());
        Assert.assertEquals(Lexer.TokenType.END_OF_LINE, token.getType());

        nextToken = lexer.nextToken(token);
        Assert.assertEquals("", token.getString());
        Assert.assertEquals(Lexer.TokenType.END_OF_FILE, token.getType());
    }

    @Test
    public void testNextToken_QuotedString() throws IOException {
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);

        Assert.assertEquals("\"Hello, World!\"", token.getString());
        Assert.assertEquals(Lexer.TokenType.STRING, token.getType());

        nextToken = lexer.nextToken(token);
        Assert.assertEquals("\"Hello, World!\"", token.getString());
        Assert.assertEquals(Lexer.TokenType.STRING, token.getType());

        nextToken = lexer.nextToken(token);
        Assert.assertEquals("Hello, World!", token.getString());
        Assert.assertEquals(Lexer.TokenType.STRING, token.getType());

        nextToken = lexer.nextToken(token);
        Assert.assertEquals("", token.getString());
        Assert.assertEquals(Lexer.TokenType.END_OF_LINE, token.getType());
    }

    @Test
    public void testNextToken_EscapedCharacter() throws IOException {
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);

        Assert.assertEquals("Hello, World!", token.getString());
        Assert.assertEquals(Lexer.TokenType.STRING, token.getType());

        nextToken = lexer.nextToken(token);
        Assert.assertEquals("1", token.getString());
        Assert.assertEquals(Lexer.TokenType.NUMBER, token.getType());

        Assert.assertEquals('\\', in.read()); // consume backslash
        nextToken = lexer.nextToken(token);
        Assert.assertEquals("r", token.getString());
        Assert.assertEquals(Lexer.TokenType.STRING, token.getType());

        nextToken = lexer.nextToken(token);
        Assert.assertEquals("", token.getString());
        Assert.assertEquals(Lexer.TokenType.END_OF_LINE, token.getType());

        nextToken = lexer.nextToken(token);
        Assert.assertEquals("2", token.getString());
        Assert.assertEquals(Lexer.TokenType.NUMBER, token.getType());
    }

    @Test
    public void testNextToken_TrailingWhitespace() throws IOException {
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);

        Assert.assertEquals("Hello, World!", token.getString());
        Assert.assertEquals(Lexer.TokenType.STRING, token.getType());

        nextToken = lexer.nextToken(token);
        Assert.assertEquals("1", token.getString());
        Assert.assertEquals(Lexer.TokenType.NUMBER, token.getType());

        nextToken = lexer.nextToken(token);
        Assert.assertEquals("2", token.getString());
        Assert.assertEquals(Lexer.TokenType.NUMBER, token.getType());

        nextToken = lexer.nextToken(token);
        Assert.assertEquals("", token.getString());
        Assert.assertEquals(Lexer.TokenType.WHITESPACE, token.getType());

        nextToken = lexer.nextToken(token);
        Assert.assertEquals("3", token.getString());
        Assert.assertEquals(Lexer.TokenType.NUMBER, token.getType());

        nextToken = lexer.nextToken(token);
        Assert.assertEquals("", token.getString());
        Assert.assertEquals(Lexer.TokenType.END_OF_LINE, token.getType());
    }
}