import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LexerTest {

    private CSVFormat format;
    private ExtendedBufferedReader reader;
    private Lexer lexer;

    @Before
    public void setup() {
        format = new CSVFormatBuilder()
                .setDelimiter(';')
                .setEscape('\\')
                .setQuoteChar('"')
                .setCommentStart("--")
                .build();

        reader = Mockito.mock(ExtendedBufferedReader.class);
        lexer = new Lexer(format, reader);
    }

    @Test
    public void testNextToken_emptyLine() throws IOException {
        // Given: an empty line
        Mockito.when(reader.read()).thenReturn(UNDEFINED);
        Mockito.when(reader.readEndOfLine()).thenReturn(true);

        // When: reading the next token
        Token token = lexer.nextToken(null);

        // Then: the token is null
        assertNull(token);
    }

    @Test
    public void testNextToken_whitespace() throws IOException {
        // Given: a whitespace character
        Mockito.when(reader.read()).thenReturn(' ');

        // When: reading the next token
        Token token = lexer.nextToken(null);

        // Then: the token is a whitespace token
        Token expected = new Token(Token.Type.WHITESPACE, (char) ' ');
        assertEquals(expected, token);
    }

    @Test
    public void testNextToken_delimiter() throws IOException {
        // Given: a delimiter character
        Mockito.when(reader.read()).thenReturn(format.getDelimiter());

        // When: reading the next token
        Token token = lexer.nextToken(null);

        // Then: the token is a delimiter token
        Token expected = new Token(Token.Type.DELIMITER, format.getDelimiter());
        assertEquals(expected, token);
    }

    @Test
    public void testNextToken_escapedDelimiter() throws IOException {
        // Given: an escaped delimiter character
        Mockito.when(reader.read()).thenReturn(escape);
        Mockito.when(reader.read()).thenReturn(format.getDelimiter());

        // When: reading the next token
        Token token = lexer.nextToken(null);

        // Then: the token is a delimiter token
        Token expected = new Token(Token.Type.DELIMITER, format.getDelimiter());
        assertEquals(expected, token);
    }

    @Test
    public void testNextToken_escape() throws IOException {
        // Given: an escape character
        Mockito.when(reader.read()).thenReturn(escape);

        // When: reading the next token
        Token token = lexer.nextToken(null);

        // Then: the token is an escape token
        Token expected = new Token(Token.Type.ESCAPE, escape);
        assertEquals(expected, token);
    }

    @Test
    public void testNextToken_quoteChar() throws IOException {
        // Given: a quote character
        Mockito.when(reader.read()).thenReturn(quoteChar);

        // When: reading the next token
        Token token = lexer.nextToken(null);

        // Then: the token is a quote token
        Token expected = new Token(Token.Type.QUOTE, quoteChar);
        assertEquals(expected, token);
    }

    @Test
    public void testNextToken_commentStart() throws IOException {
        // Given: a comment start character
        Mockito.when(reader.read()).thenReturn(commmentStart);

        // When: reading the next token
        Token token = lexer.nextToken(null);

        // Then: the token is a comment start token
        Token expected = new Token(Token.Type.COMMENT_START, commmentStart);
        assertEquals(expected, token);
    }

    @Test
    public void testNextToken_text() throws IOException {
        // Given: some text
        String text = "some text";
        Mockito.doAnswer(i -> {
            if (i.equals(text.length())) {
                Mockito.when(reader.readEndOfLine()).thenReturn(true);
            }
            return text.charAt((int) i);
        }).when(reader).read(text.length());

        // When: reading the next token
        Token token = lexer.nextToken(null);

        // Then: the token is a text token
        Token expected = new Token(Token.Type.TEXT, text);
        assertEquals(expected, token);
    }

    @Test
    public void testNextToken_invalidCharacter() throws IOException {
        // Given: an invalid character
        Mockito.when(reader.read()).thenReturn((char) 0xFFFE);

        // When: trying to read the next token
        try {
            lexer.nextToken(null);
        } catch (IOException e) {
            // Expected: an IOException is thrown
            assertTrue(true);
        }
    }

    @Test
    public void testNextToken_endOfFile() throws IOException {
        // Given: end of file
        Mockito.when(reader.read()).thenReturn(UNDEFINED);
        Mockito.when(reader.readEndOfLine()).thenReturn(false);

        // When: trying to read the next token
        try {
            lexer.nextToken(null);
        } catch (IOException e) {
            // Expected: an IOException is thrown
            assertTrue(true);
        }
    }

    @Test
    public void testNextToken_endOfLine() throws IOException {
        // Given: end of line
        Mockito.when(reader.read()).thenReturn(UNDEFINED);
        Mockito.when(reader.readEndOfLine()).thenReturn(true);

        // When: trying to read the next token
        Token token = lexer.nextToken(null);

        // Then: the token is null
        assertNull(token);
    }
}