package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link Lexer}.
 */
public class LexerTest {

    private ExtendedBufferedReader input;
    private CSVFormat format;
    private Lexer lexer;

    @Before
    public void setUp() throws IOException {
        format = new CSVFormatBuilder().setDelimiter(',').build();
        input = new ExtendedBufferedReader(new InputStreamReader(getClass().getResourceAsStream("/test.csv")));
        lexer = new Lexer(format, input) {
            @Override
            abstract Token nextToken(Token reusableToken) throws IOException;
        };
    }

    @Test
    public void testGetLineNumber() throws IOException {
        assertEquals(1, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        final int c = in.read();
        when(input.lookAhead()).thenReturn(LF);

        assertEquals(CR, lexer.readEscape());
        assertEquals(LF, in.read());
    }

    @Test(expected = IOException.class)
    public void testReadEscapeEndOfFile() throws IOException {
        final int c = END_OF_STREAM;
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        final StringBuilder buffer = new StringBuilder("   abc  ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("abc", buffer.toString());
    }

    @Test
    public void testIsWhitespace() throws IOException {
        assertFalse(lexer.isWhitespace(5));
        assertTrue(Character.isWhitespace(lexer.isWhitespace(13)));
    }

    @Test
    public void testIsStartOfLine() throws IOException {
        assertEquals(true, lexer.isStartOfLine(UNDEFINED));
        assertEquals(false, lexer.isStartOfLine(13));
        assertEquals(true, lexer.isStartOfLine(LF));
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        assertFalse(lexer.isEndOfFile(UNDEFINED));
        assertFalse(lexer.isEndOfFile(13));
        assertTrue(lexer.isEndOfFile(END_OF_STREAM));
    }

    @Test
    public void testIsDelimiter() throws IOException {
        assertTrue(lexer.isDelimiter(44));
        assertFalse(lexer.isDelimiter(45));
    }

    @Test
    public void testIsEscape() throws IOException {
        assertFalse(lexer.isEscape(UNDEFINED));
        assertFalse(lexer.isEscape(44));
        assertTrue(lexer.isEscape(92));
    }

    @Test
    public void testIsQuoteChar() throws IOException {
        assertFalse(lexer.isQuoteChar(UNDEFINED));
        assertFalse(lexer.isQuoteChar(34));
        assertTrue(lexer.isQuoteChar(34));
    }

    @Test
    public void testIsCommentStart() throws IOException {
        assertFalse(lexer.isCommentStart(UNDEFINED));
        assertFalse(lexer.isCommentStart(34));
        assertFalse(lexer.isCommentStart(45));
        assertTrue(lexer.isCommentStart(42));
    }

    @Test
    public void testNextToken() throws IOException {
        final Token token = new Token(Token.Type.STRING, "abc");
        final Token expected = new Token(Token.Type.STRING, "abc");
        when(input.read()).thenReturn(44);
        when(input.read()).thenReturn(34);
        when(input.read()).thenReturn(65);
        when(input.read()).thenReturn(65);
        when(input.read()).thenReturn(34);
        when(input.read()).thenReturn(44);

        final Token result = lexer.nextToken(token);

        assertSame(expected, result);
        assertEquals(5, lexer.getLineNumber());
        assertEquals(6, input.getLineNumber());
    }

    @Test(expected = IOException.class)
    public void testNextTokenEndOfFile() throws IOException {
        when(input.read()).thenThrow(new IOException());

        lexer.nextToken(new Token());
    }

    @Test
    public void testNextTokenEmptyLine() throws IOException {
        when(input.read()).thenReturn(LF);
        when(input.read()).thenReturn(CR);

        final Token result = lexer.nextToken(new Token());

        assertNull(result);
        assertEquals(7, lexer.getLineNumber());
        assertEquals(7, input.getLineNumber());
    }

    @Test
    public void testNextTokenComment() throws IOException {
        when(input.read()).thenReturn(42);
        when(input.read()).thenReturn(45);
        when(input.readLine()).thenReturn("abc");
        when(input.read()).thenReturn(CR);
        when(input.read()).thenReturn(CR);

        final Token result = lexer.nextToken(new Token());

        assertNull(result);
        assertEquals(8, lexer.getLineNumber());
        assertEquals(8, input.getLineNumber());
    }

    @Test
    public void testNextTokenInvalidEscapeSequence() throws IOException {
        when(input.read()).thenReturn(92);
        when(input.read()).thenReturn(60);

        final Token result = lexer.nextToken(new Token());

        assertEquals(Token.Type.INVALID_ESCAPE_SEQUENCE, result.getType());
        assertEquals(9, lexer.getLineNumber());
        assertEquals(9, input.getLineNumber());
    }

    @Test
    public void testNextTokenEmptyField() throws IOException {
        when(input.read()).thenReturn(' ');
        when(input.read()).thenReturn(' ');
        when(input.read()).thenReturn(CR);
        when(input.read()).thenReturn(CR);

        final Token result = lexer.nextToken(new Token());

        assertNull(result);
        assertEquals(10, lexer.getLineNumber());
        assertEquals(10, input.getLineNumber());
    }

    @Test
    public void testNextTokenFieldWithQuotes() throws IOException {
        when(input.read()).thenReturn(34);
        when(input.read()).thenReturn(65);
        when(input.read()).thenReturn(65);
        when(input.read()).thenReturn(34);
        when(input.read()).thenReturn(44);

        final Token result = lexer.nextToken(new Token());

        final Token expected = new Token(Token.Type.STRING, "abc");
        assertSame(expected, result);
        assertEquals(11, lexer.getLineNumber());
        assertEquals(11, input.getLineNumber());
    }

    @Test
    public void testNextTokenFieldWithoutQuotes() throws IOException {
        when(input.read()).thenReturn('a');
        when(input.read()).thenReturn('b');
        when(input.read()).thenReturn('c');
        when(input.read()).thenReturn(44);

        final Token result = lexer.nextToken(new Token());

        final Token expected = new Token(Token.Type.STRING, "abc");
        assertSame(expected, result);
        assertEquals(12, lexer.getLineNumber());
        assertEquals(12, input.getLineNumber());
    }

    @Test
    public void testNextTokenDelimiter() throws IOException {
        when(input.read()).thenReturn(44);

        final Token result = lexer.nextToken(new Token());

        final Token expected = new Token(Token.Type.DELIMITER, ",");
        assertSame(expected, result);
        assertEquals(13, lexer.getLineNumber());
        assertEquals(13, input.getLineNumber());
    }

    @Test
    public void testNextTokenEndOfLine() throws IOException {
        when(input.read()).thenReturn(CR);
        when(input.read()).thenReturn(CR);

        final Token result = lexer.nextToken(new Token());

        assertNull(result);
        assertEquals(14, lexer.getLineNumber());
        assertEquals(14, input.getLineNumber());
    }

    @Test
    public void testNextTokenEndOfFile() throws IOException {
        when(input.read()).thenThrow(new IOException());

        final Token result = lexer.nextToken(new Token());

        assertNull(result);
        assertEquals(15, lexer.getLineNumber());
    }
}