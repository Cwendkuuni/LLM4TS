import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    private static final String TEST_FILE = "testData.csv";
    private CSVFormat format;
    private ExtendedBufferedReader in;
    private Lexer lexer;

    @Before
    public void setUp() throws Exception {
        format = new CSVFormat(); // Default format
        in = new ExtendedBufferedReader(System.in); // For testing, uses system input stream
        lexer = new Lexer(format, in);
    }

    @Test
    public void testNextToken_SimpleToken() throws Exception {
        // Given: A simple token, e.g. "hello"
        in = new ExtendedBufferedReader(getClass().getResourceAsStream(TEST_FILE)); // Use a test file
        String expected = "hello";

        // When: Read next token
        Token token = lexer.nextToken(null);

        // Then: Check if the token is correct
        assertNotNull(token);
        assertEquals(TokenType.STRING, token.getType());
        assertEquals(expected, token.getValue());
    }

    @Test
    public void testNextToken_EmptyToken() throws Exception {
        // Given: An empty token, e.g. ""
        in = new ExtendedBufferedReader(getClass().getResourceAsStream(TEST_FILE));

        // When: Read next token
        Token token = lexer.nextToken(null);

        // Then: Check if the token is correct
        assertNotNull(token);
        assertEquals(TokenType.EMPTY, token.getType());
    }

    @Test
    public void testNextToken_WhitespaceToken() throws Exception {
        // Given: A whitespace token, e.g. " "
        in = new ExtendedBufferedReader(getClass().getResourceAsStream(TEST_FILE));

        // When: Read next token
        Token token = lexer.nextToken(null);

        // Then: Check if the token is correct
        assertNotNull(token);
        assertEquals(TokenType.WHITESPACE, token.getType());
    }

    @Test
    public void testNextToken_DelimiterToken() throws Exception {
        // Given: A delimiter token, e.g. ","
        in = new ExtendedBufferedReader(getClass().getResourceAsStream(TEST_FILE));

        // When: Read next token
        Token token = lexer.nextToken(null);

        // Then: Check if the token is correct
        assertNotNull(token);
        assertEquals(TokenType.DELIMITER, token.getType());
        assertEquals(format.getDelimiter(), token.getValue());
    }

    @Test
    public void testNextToken_EscapeSequence() throws Exception {
        // Given: An escape sequence, e.g. "\r" or "\n"
        in = new ExtendedBufferedReader(getClass().getResourceAsStream(TEST_FILE));

        // When: Read next token
        Token token = lexer.nextToken(null);

        // Then: Check if the token is correct
        assertNotNull(token);
        assertEquals(TokenType.ESCAPE, token.getType());

        // Check the next character
        int nextChar = in.read();
        assertTrue(in.hasNextChar());

        switch (token.getValue()) {
            case CR:
                assertEquals('\r', nextChar);
                break;
            case LF:
                assertEquals('\n', nextChar);
                break;
            // Add cases for other escape sequences like '\t', '\b', '\f' as needed
            default:
                fail("Unexpected escape sequence: " + token.getValue());
        }
    }

    @Test
    public void testNextToken_Comment() throws Exception {
        // Given: A comment, e.g. "# This is a comment"
        in = new ExtendedBufferedReader(getClass().getResourceAsStream(TEST_FILE));

        // When: Read next token
        Token token = lexer.nextToken(null);

        // Then: Check if the token is skipped because it is a comment
        assertTrue(token.getType() == TokenType.COMMENT);
    }

    @Test
    public void testNextToken_EndOfFile() throws Exception {
        // Given: End of file
        in = new ExtendedBufferedReader(getClass().getResourceAsStream(TEST_FILE));

        // When: Read next token
        Token token = lexer.nextToken(null);

        // Then: Check if the token represents end of file
        assertTrue(token.getType() == TokenType.END_OF_FILE);
    }

    @Test
    public void testNextToken_InvalidCharacter() throws Exception {
        // Given: An invalid character, e.g. "\ufffe"
        in = new ExtendedBufferedReader(getClass().getResourceAsStream(TEST_FILE));

        // When: Read next token
        in.read(); // Read the invalid character
        Token token = lexer.nextToken(null);

        // Then: Check if the token is an error
        assertTrue(token.getType() == TokenType.ERROR);
        assertEquals("Invalid character: \\ufffe", token.getValue());
    }
}