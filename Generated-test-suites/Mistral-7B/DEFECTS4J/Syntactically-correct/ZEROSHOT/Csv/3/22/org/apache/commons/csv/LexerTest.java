package org.apache.commons.csv.test;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;

/**
 * Test class for {@link Lexer}
 */
public class LexerTest {

    /**
     * Test method for {@link Lexer#Lexer(CSVFormat, ExtendedBufferedReader)}
     */
    @org.junit.Test
    public void testLexer() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(';').withHeader());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("header1,header2\r\n1,2\r\n3,4".getBytes())));
        Lexer lexer = new Lexer(format, reader);
        assertNotNull(lexer);
        assertEquals(CSVFormat.DEFAULT.getDelimiter(), lexer.delimiter);
        assertEquals(format.getEscape(), lexer.escape);
        assertEquals(format.getQuoteChar(), lexer.quoteChar);
        assertEquals(format.getCommentStart(), lexer.commmentStart);
        assertEquals(format.isIgnoreSurroundingSpaces(), lexer.ignoreSurroundingSpaces);
        assertEquals(format.isIgnoreEmptyLines(), lexer.ignoreEmptyLines);
        assertEquals(reader, lexer.in);
    }

    /**
     * Test method for {@link Lexer#mapNullToDisabled(Character)}
     */
    @org.junit.Test
    public void testMapNullToDisabled() {
        assertEquals(UNDEFINED, Lexer.mapNullToDisabled(null));
        assertEquals('a', Lexer.mapNullToDisabled('a'));
    }

    /**
     * Test method for {@link Lexer#getLineNumber()}
     */
    @org.junit.Test
    public void testGetLineNumber() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("header1,header2\r\n1,2\r\n3,4".getBytes())));
        Lexer lexer = new Lexer(format, reader);
        assertEquals(1, lexer.getLineNumber());
    }

    /**
     * Test method for {@link Lexer#readEscape()}
     */
    @org.junit.Test
    public void testReadEscape() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("\\r\\n".getBytes())));
        Lexer lexer = new Lexer(format, reader);
        assertEquals(CR, lexer.readEscape());
        assertEquals(LF, lexer.readEscape());
    }

    /**
     * Test method for {@link Lexer#trimTrailingSpaces(StringBuilder)}
     */
    @org.junit.Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("   foo   ");
        Lexer lexer = new Lexer(null, null);
        lexer.trimTrailingSpaces(buffer);
        assertEquals("foo", buffer.toString());
    }

    /**
     * Test method for {@link Lexer#readEndOfLine(int)}
     */
    @org.junit.Test
    public void testReadEndOfLine() throws IOException {
        assertEquals(false, Lexer.readEndOfLine(LF));
        assertEquals(false, Lexer.readEndOfLine(CR));
        assertEquals(true, Lexer.readEndOfLine(LF + CR));
    }

    /**
     * Test method for {@link Lexer#isWhitespace(int)}
     */
    @org.junit.Test
    public void testIsWhitespace() {
        assertTrue(Lexer.isWhitespace(LF));
        assertTrue(Lexer.isWhitespace(TAB));
        assertFalse(Lexer.isWhitespace('a'));
    }

    /**
     * Test method for {@link Lexer#isStartOfLine(int)}
     */
    @org.junit.Test
    public void testIsStartOfLine() {
        assertTrue(Lexer.isStartOfLine(UNDEFINED));
        assertTrue(Lexer.isStartOfLine(CR));
        assertTrue(Lexer.isStartOfLine(LF));
        assertFalse(Lexer.isStartOfLine('a'));
    }

    /**
     * Test method for {@link Lexer#isEndOfFile(int)}
     */
    @org.junit.Test
    public void testIsEndOfFile() {
        assertFalse(Lexer.isEndOfFile(UNDEFINED));
        assertFalse(Lexer.isEndOfFile(CR));
        assertFalse(Lexer.isEndOfFile(LF));
        assertTrue(Lexer.isEndOfFile(END_OF_STREAM));
    }

    /**
     * Test method for {@link Lexer#isDelimiter(int)}
     */
    @org.junit.Test
    public void testIsDelimiter() {
        assertEquals(false, Lexer.isDelimiter(CR));
        assertEquals(false, Lexer.isDelimiter(LF));
        assertEquals(true, Lexer.isDelimiter(';'));
    }

    /**
     * Test method for {@link Lexer#isEscape(int)}
     */
    @org.junit.Test
    public void testIsEscape() {
        assertEquals(false, Lexer.isEscape(CR));
        assertEquals(false, Lexer.isEscape(LF));
        assertEquals(false, Lexer.isEscape(';'));
        assertEquals(true, Lexer.isEscape('\\'));
    }

    /**
     * Test method for {@link Lexer#isQuoteChar(int)}
     */
    @org.junit.Test
    public void testIsQuoteChar() {
        assertEquals(false, Lexer.isQuoteChar(CR));
        assertEquals(false, Lexer.isQuoteChar(LF));
        assertEquals(false, Lexer.isQuoteChar(';'));
        assertEquals(true, Lexer.isQuoteChar('"'));
    }

    /**
     * Test method for {@link Lexer#isCommentStart(int)}
     */
    @org.junit.Test
    public void testIsCommentStart() {
        assertEquals(false, Lexer.isCommentStart(CR));
        assertEquals(false, Lexer.isCommentStart(LF));
        assertEquals(false, Lexer.isCommentStart(';'));
        assertEquals(true, Lexer.isCommentStart('/'));
    }

    /**
     * Test method for {@link Lexer#nextToken(Token)}
     */
    @org.junit.Test
    public void testNextToken() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("\"foo,bar\"\n1,2".getBytes())));
        Lexer lexer = new Lexer(format, reader);
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);
        assertNotNull(nextToken);
        assertEquals(TokenType.STRING, nextToken.getType());
        assertEquals("foo,bar", nextToken.getValue());
        nextToken = lexer.nextToken(token);
        assertEquals(TokenType.INTEGER, nextToken.getType());
        assertEquals(1, nextToken.getValue());
        nextToken = lexer.nextToken(token);
        assertNull(nextToken);
    }
}