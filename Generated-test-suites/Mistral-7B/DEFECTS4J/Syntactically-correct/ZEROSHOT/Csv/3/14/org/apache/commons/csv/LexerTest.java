package org.apache.commons.csv.test;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Lexer.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link Lexer}
 *
 */
public class LexerTest {

    private static final int LF_NUM = LF;
    private static final int CR_NUM = CR;
    private static final int TAB_NUM = TAB;
    private static final int BACKSPACE_NUM = BACKSPACE;
    private static final int FF_NUM = FF;
    private static final int END_OF_STREAM_NUM = END_OF_STREAM;
    private static final int UNDEFINED_NUM = UNDEFINED;

    private CSVFormat format;
    private ExtendedBufferedReader reader;
    private Lexer lexer;

    @Before
    public void setUp() throws IOException {
        format = new CSVFormatBuilder().setDelimiter(';').build();
        reader = new ExtendedBufferedReader(new StringReader("a\nb;c\r\td;e\n"));
        lexer = new Lexer(format, reader);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        assertEquals(1, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        assertEquals(CR, lexer.readEscape());
        assertEquals(LF, lexer.readEscape());
        assertEquals(TAB, lexer.readEscape());
        assertEquals(BACKSPACE, lexer.readEscape());
        assertEquals(FF, lexer.readEscape());
        assertEquals(UNDEFINED, lexer.readEscape());
        assertEquals(END_OF_STREAM, lexer.readEscape());
    }

    @Test(expected = IOException.class)
    public void testReadEscapeEndOfStream() throws IOException {
        lexer.readEscape();
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        StringBuilder buffer = new StringBuilder(" a   b  \t c\t ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("a b c", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        assertTrue(lexer.readEndOfLine(LF_NUM));
        assertTrue(lexer.readEndOfLine(CR_NUM));
        assertFalse(lexer.readEndOfLine(TAB_NUM));
    }

    @Test
    public void testIsStartOfLine() throws IOException {
        assertTrue(lexer.isStartOfLine(LF_NUM));
        assertTrue(lexer.isStartOfLine(CR_NUM));
        assertFalse(lexer.isStartOfLine(TAB_NUM));
        assertFalse(lexer.isStartOfLine(UNDEFINED_NUM));
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        assertTrue(lexer.isEndOfFile(END_OF_STREAM_NUM));
        assertFalse(lexer.isEndOfFile(UNDEFINED_NUM));
    }

    @Test
    public void testIsDelimiter() throws IOException {
        assertTrue(lexer.isDelimiter(59));
        assertFalse(lexer.isDelimiter(44));
    }

    @Test
    public void testIsEscape() throws IOException {
        assertTrue(lexer.isEscape(92));
        assertFalse(lexer.isEscape(44));
    }

    @Test
    public void testIsQuoteChar() throws IOException {
        assertTrue(lexer.isQuoteChar(34));
        assertFalse(lexer.isQuoteChar(44));
    }

    @Test
    public void testIsCommentStart() throws IOException {
        assertTrue(lexer.isCommentStart(45));
        assertFalse(lexer.isCommentStart(34));
    }

    @Test
    public void testNextToken() throws IOException {
        Token token = new Token();
        Token result = lexer.nextToken(token);

        assertNotNull(result);
        assertEquals("a", result.getValue());
        assertEquals(TokenType.STRING, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        assertNotNull(result);
        assertEquals("", result.getValue());
        assertEquals(TokenType.EMPTY, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        assertNotNull(result);
        assertEquals("b", result.getValue());
        assertEquals(TokenType.STRING, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        assertNotNull(result);
        assertEquals("c", result.getValue());
        assertEquals(TokenType.STRING, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        assertNotNull(result);
        assertEquals("", result.getValue());
        assertEquals(TokenType.EMPTY, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        assertNull(result);
    }

    @Test
    public void testNextTokenWithQuotes() throws IOException {
        Token token = new Token();
        CSVParser parser = new CSVParser(reader, format);
        lexer = parser.getReader().getLexer();

        token.reset();
        Token result = lexer.nextToken(token);

        assertNotNull(result);
        assertEquals("a", result.getValue());
        assertEquals(TokenType.STRING, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        assertNotNull(result);
        assertEquals("\"b\"", result.getValue());
        assertEquals(TokenType.QUOTED_STRING, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        assertNotNull(result);
        assertEquals("c", result.getValue());
        assertEquals(TokenType.STRING, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        assertNotNull(result);
        assertEquals("\"d\"", result.getValue());
        assertEquals(TokenType.QUOTED_STRING, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        assertNull(result);
    }

    @Test
    public void testNextTokenWithQuotesAndEscape() throws IOException {
        Token token = new Token();
        CSVFormat format = new CSVFormatBuilder().setDelimiter(';')
                .setEscapeCharacter('\\')
                .setQuoteMode(QuoteMode.BACKSLASH)
                .build();
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a;\"b\\\"c\\\"d;e"));
        CSVParser parser = new CSVParser(reader, format);
        lexer = parser.getReader().getLexer();

        token.reset();
        Token result = lexer.nextToken(token);

        assertNotNull(result);
        assertEquals("a", result.getValue());
        assertEquals(TokenType.STRING, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        assertNotNull(result);
        assertEquals("b\"c", result.getValue());
        assertEquals(TokenType.QUOTED_STRING, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        assertNotNull(result);
        assertEquals("d", result.getValue());
        assertEquals(TokenType.STRING, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        assertNotNull(result);
        assertEquals("e", result.getValue());
        assertEquals(TokenType.STRING, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        assertNull(result);
    }
}