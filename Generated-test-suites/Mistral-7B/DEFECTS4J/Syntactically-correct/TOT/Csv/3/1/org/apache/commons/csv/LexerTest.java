import org.apache.commons.csv.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class LexerTest {

    @Mock
    private ExtendedBufferedReader mockReader;

    private CSVFormat format;
    private Lexer lexer;

    @Before
    public void setUp() throws IOException {
        format = new DefaultCSVFormat();
        lexer = new Lexer(format, new BufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])))) {
            @Override
            public ExtendedBufferedReader getIn() {
                return mockReader;
            }
        };
    }

    @Test
    public void testNextToken_Whitespace() throws IOException {
        // Given: A leading whitespace character
        when(mockReader.read()).thenReturn(32); // space

        Token token = lexer.nextToken(null);

        // Then: The whitespace character should be consumed and returned as a token of type WHITESPACE
        assertNotNull(token);
        assertEquals(CSVTokenType.WHITESPACE, token.getType());
        assertEquals(32, token.getValue().charAt(0));

        // And: The next character should be read and available for testing
        assertEquals(32, lexer.getIn().getLastChar());
    }

    @Test
    public void testNextToken_Delimiter() throws IOException {
        // Given: A delimiter character
        when(mockReader.read()).thenReturn(format.getDelimiter());

        Token token = lexer.nextToken(null);

        // Then: The delimiter character should be consumed and returned as a token of type DELIMITER
        assertNotNull(token);
        assertEquals(CSVTokenType.DELIMITER, token.getType());
        assertEquals(format.getDelimiter(), token.getValue());

        // And: The next character should be read and available for testing
        assertEquals(format.getDelimiter(), lexer.getIn().getLastChar());
    }

    @Test
    public void testNextToken_QuotedString() throws IOException {
        // Given: A string enclosed in double quotes
        String text = "\"Hello, World!\"";
        when(mockReader.read()).thenReturn(60, text.charAt(0), text.charAt(text.length() - 1), 62); // < "Hello, World!" >

        Token token = lexer.nextToken(null);

        // Then: The string should be consumed and returned as a token of type STRING
        assertNotNull(token);
        assertEquals(CSVTokenType.STRING, token.getType());
        assertEquals(text, token.getValue());

        // And: The next character should be read and available for testing
        assertEquals(format.getDelimiter(), lexer.getIn().getLastChar());
    }

    @Test(expected = IOException.class)
    public void testNextToken_UnexpectedEndOfStream() throws IOException {
        // Given: An unexpected end of stream
        when(mockReader.read()).thenThrow(new IOException());

        Token token = lexer.nextToken(null);

        // Then: An IOException should be thrown
        fail();
    }

    @Test(expected = IOException.class)
    public void testNextToken_UnexpectedCharacter() throws IOException {
        // Given: An unexpected character
        when(mockReader.read()).thenReturn('X');

        Token token = lexer.nextToken(null);

        // Then: An IOException should be thrown
        fail();
    }

    @Test
    public void testReadEscape() throws IOException {
        // Given: An escape character followed by a line feed
        when(mockReader.read()).thenReturn(escape, LF);

        int escapedCharacter = lexer.readEscape();

        // Then: The line feed character should be consumed and the previous character (the escape character) should be returned
        assertEquals(LF, lexer.getIn().getLastChar());
        assertEquals(LF,
                lexer.readEscape()); // Check that reading the next character does not consume the line feed
        assertEquals(CR, lexer.readEscape()); // Check that reading the next character (which should be a CR) does not consume that character
        assertEquals(CR, lexer.readEscape()); // Check that reading the next character again returns CR

        // And: The escaped character should have been returned
        assertEquals(LF, escapedCharacter);
    }

    @Test
    public void testReadEscape_UnexpectedCharacter() throws IOException {
        // Given: An unexpected character following an escape character
        when(mockReader.read()).thenReturn(escape, 'X');

        // Then: An IOException should be thrown
        try {
            lexer.readEscape();
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testIsStartOfLine() throws IOException {
        // Given: A line-terminator character
        when(mockReader.read()).thenReturn(LF);

        // Then: The method should return true
        assertTrue(lexer.isStartOfLine(LF));

        // And: The next character should be read and available for testing
        assertEquals(UNDEFINED, lexer.getIn().getLastChar());
    }

    @Test
    public void testIsStartOfLine_UnexpectedCharacter() throws IOException {
        // Given: A non-line-terminator character
        when(mockReader.read()).thenReturn(32); // space

        // Then: The method should return false
        assertFalse(lexer.isStartOfLine(32));

        // And: The next character should be read and available for testing
        assertEquals(32, lexer.getIn().getLastChar());
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        // Given: The end of the stream
        when(mockReader.read()).thenReturn(END_OF_STREAM);

        // Then: The method should return true
        assertTrue(lexer.isEndOfFile(END_OF_STREAM));

        // And: The next character should be at the end of the stream and unavailable for testing
        assertEquals(END_OF_STREAM, lexer.getIn().read());
    }

    @Test
    public void testIsEndOfFile_UnexpectedCharacter() throws IOException {
        // Given: A non-end-of-stream character
        when(mockReader.read()).thenReturn('X');

        // Then: The method should return false
        assertFalse(lexer.isEndOfFile('X'));

        // And: The next character should be read and available for testing
        assertEquals('X', lexer.getIn().read());
    }

    @Test
    public void testIsDelimiter() throws IOException {
        // Given: A delimiter character
        when(mockReader.read()).thenReturn(delimiter);

        // Then: The method should return true
        assertTrue(lexer.isDelimiter(delimiter));

        // And: The next character should be read and available for testing
        assertEquals(delimiter, lexer.getIn().getLastChar());
    }

    @Test
    public void testIsDelimiter_UnexpectedCharacter() throws IOException {
        // Given: A non-delimiter character
        when(mockReader.read()).thenReturn(32); // space

        // Then: The method should return false
        assertFalse(lexer.isDelimiter(32));

        // And: The next character should be read and available for testing
        assertEquals(32, lexer.getIn().getLastChar());
    }

    @Test
    public void testIsEscape() throws IOException {
        // Given: An escape character
        when(mockReader.read()).thenReturn(escape);

        // Then: The method should return true
        assertTrue(lexer.isEscape(escape));

        // And: The next character should be read and available for testing
        assertEquals(UNDEFINED, lexer.getIn().getLastChar());
    }

    @Test
    public void testIsEscape_UnexpectedCharacter() throws IOException {
        // Given: A non-escape character
        when(mockReader.read()).thenReturn(32); // space

        // Then: The method should return false
        assertFalse(lexer.isEscape(32));

        // And: The next character should be read and available for testing
        assertEquals(32, lexer.getIn().getLastChar());
    }

    @Test
    public void testIsQuoteChar() throws IOException {
        // Given: A quote character
        when(mockReader.read()).thenReturn(quoteChar);

        // Then: The method should return true
        assertTrue(lexer.isQuoteChar(quoteChar));

        // And: The next character should be read and available for testing
        assertEquals(UNDEFINED, lexer.getIn().getLastChar());
    }

    @Test
    public void testIsQuoteChar_UnexpectedCharacter() throws IOException {
        // Given: A non-quote character
        when(mockReader.read()).thenReturn(32); // space

        // Then: The method should return false
        assertFalse(lexer.isQuoteChar(32));

        // And: The next character should be read and available for testing
        assertEquals(32, lexer.getIn().getLastChar());
    }

    @Test
    public void testIsCommentStart() throws IOException {
        // Given: A comment start character
        when(mockReader.read()).thenReturn(commmentStart);

        // Then: The method should return true
        assertTrue(lexer.isCommentStart(commmentStart));

        // And: The next character should be read and available for testing
        assertEquals(UNDEFINED, lexer.getIn().getLastChar());
    }

    @Test
    public void testIsCommentStart_UnexpectedCharacter() throws IOException {
        // Given: A non-comment-start character
        when(mockReader.read()).thenReturn(32); // space

        // Then: The method should return false
        assertFalse(lexer.isCommentStart(32));

        // And: The next character should be read and available for testing
        assertEquals(32, lexer.getIn().getLastChar());
    }
}