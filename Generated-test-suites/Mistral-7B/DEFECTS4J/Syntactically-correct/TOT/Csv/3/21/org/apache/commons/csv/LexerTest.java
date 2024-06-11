import org.apache.commons.csv.*;
import org.apache.commons.csv.exceptions.MalformedCSVException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

public class LexerTest {

    private ExtendedBufferedReader mockReader;
    private CSVFormat format;
    private Lexer lexer;

    @Before
    public void setUp() {
        format = new CSVFormat(',', '\u0002', '\u0002', '\u0023'); // setting up custom delimiter, escape, and quote char
        mockReader = Mockito.mock(ExtendedBufferedReader.class);
        lexer = new Lexer(format, mockReader);
    }

    @Test
    public void testNextToken_NormalCase() throws IOException, MalformedCSVException {
        // Given: Input stream with valid CSV data
        String input = "Name,Age,City\nJohn Doe,30,New York\nMary Smith,25,San Francisco";
        Mockito.when(mockReader.readLine()).thenReturn(input);

        // When: Parsing CSV data
        Token token = lexer.nextToken(null);

        // Then: Verify the first token is a START_RECORD token
        assertEquals(CSVToken.START_RECORD, token.getType());

        // When: Parsing the next token
        token = lexer.nextToken(token);

        // Then: Verify the second token is a FIELD token
        assertEquals(CSVToken.FIELD, token.getType());
        assertEquals("Name", token.getValue());

        // Repeat for all tokens in the record
    }

    @Test
    public void testNextToken_EmptyLine() throws IOException, MalformedCSVException {
        // Given: Input stream with an empty line
        Mockito.when(mockReader.readLine()).thenReturn("");

        // When: Parsing an empty line
        Token token = lexer.nextToken(null);

        // Then: Verify the token is an EMPTY_TOKEN
        assertEquals(CSVToken.EMPTY_TOKEN, token.getType());
    }

    @Test(expected = MalformedCSVException.class)
    public void testNextToken_InvalidCharacter() throws IOException {
        // Given: Input stream with an invalid character
        Mockito.when(mockReader.readLine()).thenReturn("Name,Age,City\nJohn Doe,30,New York\nMary Smith,25,San Francisco\u0000");

        // When: Parsing an invalid character
        lexer.nextToken(null);
        lexer.nextToken(null); // consume the first two tokens (START_RECORD and FIELD)
        Token token = lexer.nextToken(null);

        // Then: Verify an exception is thrown
    }

    @Test(expected = IOException.class)
    public void testNextToken_EndOfStream() throws IOException, MalformedCSVException {
        // Given: Input stream has reached the end of the stream
        Mockito.when(mockReader.readLine()).thenReturn(null);

        // When: Parsing the next token
        Token token = lexer.nextToken(null);

        // Then: Verify an IOException is thrown
    }

    @Test
    public void testReadEscape_NormalCase() throws IOException {
        // Given: Input stream with a valid escape sequence
        String input = "Name,Age,City\nJohn Doe,\"New York\\n\",30";
        Mockito.when(mockReader.read()).thenReturn('\\'); // set up the backslash character
        Mockito.when(mockReader.read()).thenReturn('n'); // set up the line feed character

        // When: Reading an escape sequence
        int escapedChar = lexer.readEscape();

        // Then: Verify the escaped character is a line feed
        assertEquals(LF, escapedChar);
    }

    @Test(expected = IOException.class)
    public void testReadEscape_InvalidSequence() throws IOException {
        // Given: Input stream with an invalid escape sequence
        String input = "Name,Age,City\nJohn Doe,\"New York\\x\",30"; // x is an invalid escape sequence
        Mockito.when(mockReader.read()).thenReturn('\\'); // set up the backslash character
        Mockito.when(mockReader.read()).thenReturn('x'); // set up the invalid escape sequence character

        // When: Reading an escape sequence
        lexer.readEscape();

        // Then: Verify an IOException is thrown
    }
}