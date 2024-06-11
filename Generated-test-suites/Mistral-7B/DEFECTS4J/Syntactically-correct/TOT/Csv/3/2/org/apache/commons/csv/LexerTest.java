package org.apache.commons.csv.test;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.csv.CSVFormat;
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
    public void testLexerConstruction() {
        String csvData = "\"field1\",\"field2\",field3,field4\n"
                + "\"field1,field2\",\"field3\tfield4\",field5\n"
                + "field1,\"field2\",\"field3\"\n"
                + "field1,field2,field3\n";

        CSVFormat format = new CSVFormat().withDelimiter(',')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(true);

        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(inputStream);

        Lexer lexer = new Lexer(format, reader);

        assertEquals(format, lexer.getFormat());
        assertEquals(reader, lexer.getInput());
        assertEquals(format.getDelimiter(), lexer.getDelimiter());
        assertEquals(format.getEscape(), lexer.getEscape());
        assertEquals(format.getQuoteChar(), lexer.getQuoteChar());
        assertEquals(format.getCommentStart(), lexer.getCommentStart());
        assertEquals(format.isIgnoreSurroundingSpaces(), lexer.isIgnoreSurroundingSpaces());
        assertEquals(format.isIgnoreEmptyLines(), lexer.isIgnoreEmptyLines());
    }

    /**
     * Test method for {@link Lexer#getLineNumber()}
     * @throws IOException
     */
    @org.junit.Test
    public void testGetLineNumber() throws IOException {
        String csvData = "field1\n" + "field2\n" + "field3";

        CSVFormat format = new CSVFormat().withDelimiter(',');

        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(inputStream);

        Lexer lexer = new Lexer(format, reader);

        assertEquals(1, lexer.getLineNumber());
        assertEquals(2, lexer.getLineNumber());
        assertEquals(3, lexer.getLineNumber());
    }

    /**
     * Test method for {@link Lexer#readEscape()}
     * @throws IOException
     */
    @org.junit.Test
    public void testReadEscape() throws IOException {
        String csvData = "field1,\"field2\\n\",field3,\"field4\\ \"field5\"";

        CSVFormat format = new CSVFormat().withDelimiter(',');

        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(inputStream);

        Lexer lexer = new Lexer(format, reader);

        assertEquals(LF, lexer.readEscape());
        assertEquals(CR, lexer.readEscape());
        assertEquals(TAB, lexer.readEscape());
        assertEquals(BACKSPACE, lexer.readEscape());
        assertEquals(FF, lexer.readEscape());
        assertEquals(CR, lexer.readEscape());
        assertEquals(LF, lexer.readEscape());
        assertEquals(TAB, lexer.readEscape());
        assertEquals(BACKSPACE, lexer.readEscape());
        assertEquals(FF, lexer.readEscape());
        assertEquals(UNDEFINED, lexer.readEscape());
        assertEquals(END_OF_STREAM, lexer.readEscape());
    }

    /**
     * Test method for {@link Lexer#trimTrailingSpaces(StringBuilder)}
     */
    @org.junit.Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("field1\t");
        Lexer lexer = new Lexer(null, null);
        lexer.trimTrailingSpaces(buffer);
        assertEquals("field1", buffer.toString());
    }

    /**
     * Test method for {@link Lexer#readEndOfLine(int)}
     * @throws IOException
     */
    @org.junit.Test
    public void testReadEndOfLine() throws IOException {
        String csvData = "field1\r\nfield2\nfield3";

        CSVFormat format = new CSVFormat().withDelimiter(',');

        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(inputStream);

        Lexer lexer = new Lexer(format, reader);

        assertTrue(lexer.readEndOfLine(CR));
        assertTrue(lexer.readEndOfLine(LF));
        assertTrue(lexer.readEndOfLine(CR));
        assertFalse(lexer.readEndOfLine(TAB));
        assertFalse(lexer.readEndOfLine(BACKSPACE));
        assertFalse(lexer.readEndOfLine(FF));
        assertFalse(lexer.readEndOfLine(UNDEFINED));
        assertFalse(lexer.readEndOfLine(END_OF_STREAM));
    }

    /**
     * Test method for {@link Lexer#nextToken(Token)}
     * @throws IOException
     */
    @org.junit.Test
    public void testNextToken() throws IOException {
        String csvData = "field1,field2,field3,field4\n"
                + "\"field1,field2\",\"field3\tfield4\",field5\n"
                + "field1,\"field2\",\"field3\"\n";

        CSVFormat format = new CSVFormat().withDelimiter(',')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(true);

        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(inputStream);

        Lexer lexer = new Lexer(format, reader);

        Token token = new Token();

        // Test empty line
        lexer.nextToken(token);
        assertEquals(null, token.getValue());

        // Test empty field
        lexer.nextToken(token);
        assertEquals("field1", token.getValue());

        // Test quoted field with escape sequence
        lexer.nextToken(token);
        assertEquals("field2", token.getValue());

        // Test quoted field with embedded quote
        lexer.nextToken(token);
        assertEquals("field3\tfield4", token.getValue());

        // Test quoted field with embedded delimiter
        lexer.nextToken(token);
        assertEquals("field5", token.getValue());

        // Test field with delimiter
        lexer.nextToken(token);
        assertEquals("field1,field2", token.getValue());

        // Test delimiter
        lexer.nextToken(token);
        assertEquals(null, token.getValue());

        // Test empty line
        lexer.nextToken(token);
        assertEquals(null, token.getValue());

        // Test end of file
        lexer.nextToken(token);
        assertEquals(null, token.getValue());
    }

    /**
     * Test method for {@link Lexer#isWhitespace(int)}
     */
    @org.junit.Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(null, null);

        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
        assertTrue(lexer.isWhitespace('\r'));
        assertTrue(lexer.isWhitespace('\n'));
        assertTrue(lexer.isWhitespace('\f'));

        assertFalse(lexer.isWhitespace(','));
        assertFalse(lexer.isWhitespace('A'));
        assertFalse(lexer.isWhitespace('Z'));
        assertFalse(lexer.isWhitespace('0'));
        assertFalse(lexer.isWhitespace('9'));
        assertFalse(lexer.isWhitespace('\ufffe'));
    }

    /**
     * Test method for {@link Lexer#isStartOfLine(int)}
     */
    @org.junit.Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(null, null);

        assertFalse(lexer.isStartOfLine(','));
        assertFalse(lexer.isStartOfLine(' '));
        assertFalse(lexer.isStartOfLine('\t'));
        assertFalse(lexer.isStartOfLine('\r'));
        assertFalse(lexer.isStartOfLine('\n'));
        assertFalse(lexer.isStartOfLine('\f'));
        assertFalse(lexer.isStartOfLine('\ufffe'));

        assertTrue(lexer.isStartOfLine(LF));
        assertTrue(lexer.isStartOfLine(CR));
        assertTrue(lexer.isStartOfLine(UNDEFINED));
    }

    /**
     * Test method for {@link Lexer#isEndOfFile(int)}
     */
    @org.junit.Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(null, null);

        assertFalse(lexer.isEndOfFile(' '));
        assertFalse(lexer.isEndOfFile('\t'));
        assertFalse(lexer.isEndOfFile('\r'));
        assertFalse(lexer.isEndOfFile('\n'));
        assertFalse(lexer.isEndOfFile('\f'));
        assertFalse(lexer.isEndOfFile('\ufffe'));
        assertFalse(lexer.isEndOfFile(','));

        assertTrue(lexer.isEndOfFile(END_OF_STREAM));
    }

    /**
     * Test method for {@link Lexer#isDelimiter(int)}
     */
    @org.junit.Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(null, null);

        assertFalse(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter(' '));
        assertFalse(lexer.isDelimiter('\t'));
        assertFalse(lexer.isDelimiter('\r'));
        assertFalse(lexer.isDelimiter('\n'));
        assertFalse(lexer.isDelimiter('\f'));
        assertFalse(lexer.isDelimiter('\ufffe'));

        assertTrue(lexer.isDelimiter(','));
    }

    /**
     * Test method for {@link Lexer#isEscape(int)}
     */
    @org.junit.Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(null, null);

        assertFalse(lexer.isEscape(' '));
        assertFalse(lexer.isEscape('\t'));
        assertFalse(lexer.isEscape('\r'));
        assertFalse(lexer.isEscape('\n'));
        assertFalse(lexer.isEscape('\f'));
        assertFalse(lexer.isEscape('\ufffe'));
        assertFalse(lexer.isEscape(','));

        assertTrue(lexer.isEscape('\\'));
    }

    /**
     * Test method for {@link Lexer#isQuoteChar(int)}
     */
    @org.junit.Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(null, null);

        assertFalse(lexer.isQuoteChar(' '));
        assertFalse(lexer.isQuoteChar('\t'));
        assertFalse(lexer.isQuoteChar('\r'));
        assertFalse(lexer.isQuoteChar('\n'));
        assertFalse(lexer.isQuoteChar('\f'));
        assertFalse(lexer.isQuoteChar('\ufffe'));
        assertFalse(lexer.isQuoteChar(','));

        assertTrue(lexer.isQuoteChar('"'));
    }

    /**
     * Test method for {@link Lexer#isCommentStart(int)}
     */
    @org.junit.Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(null, null);

        assertFalse(lexer.isCommentStart(' '));
        assertFalse(lexer.isCommentStart('\t'));
        assertFalse(lexer.isCommentStart('\r'));
        assertFalse(lexer.isCommentStart('\n'));
        assertFalse(lexer.isCommentStart('\f'));
        assertFalse(lexer.isCommentStart('\ufffe'));
        assertFalse(lexer.isCommentStart(','));

        assertTrue(lexer.isCommentStart('/'));
    }
}