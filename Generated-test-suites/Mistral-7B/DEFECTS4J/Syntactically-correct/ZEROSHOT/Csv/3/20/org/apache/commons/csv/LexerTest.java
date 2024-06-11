import org.apache.commons.csv.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LexerTest {

    private ExtendedBufferedReader in;
    private CSVFormat format;
    private Lexer lexer;

    @Before
    public void setUp() throws IOException {
        String testCsv = "a,b,\"c,d\",e,f,\"g\nh\r,\"i,j\"\n";
        in = new ExtendedBufferedReader(new StringReader(testCsv));
        format = new CSVFormatBuilder()
                .setDelimiter(' ')
                .setHeaderSeparator(Constants.COMMA)
                .build();
    }

    @After
    public void tearDown() throws IOException {
        if (in != null) {
            in.close();
        }
    }

    @Test
    public void testLexerConstruction() {
        lexer = new Lexer(format, in);
        assertNotNull(lexer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLexerNullFormat() {
        lexer = new Lexer(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLexerNullIn() {
        lexer = new Lexer(format, null);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        assertEquals(1, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        int c = lexer.read();
        assertEquals(Constants.SPACE, c);

        lexer.read(); // consume leading space
        int escapedChar = lexer.readEscape();
        assertEquals(Constants.CR, escapedChar);
        assertEquals(Constants.CR, lexer.read());
        assertEquals(Constants.LF, lexer.read());

        lexer.read(); // consume leading space
        c = lexer.read();
        assertEquals(Constants.TAB, c);

        escapedChar = lexer.readEscape();
        assertEquals(Constants.TAB, escapedChar);
        assertEquals(Constants.TAB, lexer.read());

        escapedChar = lexer.readEscape();
        assertEquals(Constants.BACKSPACE, escapedChar);
        assertEquals(Constants.BACKSPACE, lexer.read());

        escapedChar = lexer.readEscape();
        assertEquals(Constants.FF, escapedChar);
        assertEquals(Constants.FF, lexer.read());

        assertEquals(Constants.EndOfFileToken.getValue(), lexer.readEscape());
    }

    @Test(expected = IOException.class)
    public void testReadEscapeEndOfStream() throws IOException {
        lexer.read(); // consume leading space
        lexer.readEscape();
        lexer.read(); // consume CR
        lexer.read(); // consume LF

        lexer.read(); // consume leading space
        lexer.read(); // consume T
        lexer.read(); // consume a
        lexer.read(); // consume tab

        lexer.readEscape();
        lexer.read(); // consume Backspace
        lexer.read(); // consume B
        lexer.read(); // consume a
        lexer.read(); // consume space

        lexer.readEscape();
        lexer.read(); // consume FormFeed
        lexer.read(); // consume F
        lexer.read(); // consume F

        lexer.read(); // consume End of File
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        StringBuilder buffer = new StringBuilder(" a\tb ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("a\tb", buffer.toString());

        buffer.setLength(buffer.length() + 4);
        buffer.append(" cdef ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("a\tb", buffer.toString());

        buffer.setLength(buffer.length() + 3);
        buffer.append(" ghi");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("a\tbghi", buffer.toString());
    }

    @Test
    public void testIsStartOfLine() throws IOException {
        int c = lexer.read();
        assertFalse(lexer.isStartOfLine(c));

        lexer.read(); // consume space
        c = lexer.read();
        assertTrue(lexer.isStartOfLine(c));

        lexer.read(); // consume CR
        c = lexer.read();
        assertTrue(lexer.isStartOfLine(c));

        lexer.read(); // consume LF
        c = lexer.read();
        assertTrue(lexer.isStartOfLine(c));

        lexer.read(); // consume space
        c = lexer.read();
        assertFalse(lexer.isStartOfLine(c));
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        int c = lexer.read();
        assertFalse(lexer.isEndOfFile(c));

        lexer.read(); // consume space
        c = lexer.read();
        assertFalse(lexer.isEndOfFile(c));

        lexer.read(); // consume CR
        c = lexer.read();
        assertFalse(lexer.isEndOfFile(c));

        lexer.read(); // consume LF
        c = lexer.read();
        assertFalse(lexer.isEndOfFile(c));

        lexer.read(); // consume space
        c = lexer.read();
        assertFalse(lexer.isEndOfFile(c));

        lexer.read(); // consume a
        c = lexer.read();
        assertEquals(Constants.COMMA, c);

        lexer.read(); // consume comma
        c = lexer.read();
        assertEquals(Constants.SPACE, c);

        lexer.read(); // consume space
        c = lexer.read();
        assertFalse(lexer.isEndOfFile(c));

        c = lexer.read();
        assertEquals(Constants.EndOfFileToken.getValue(), c);
        assertTrue(lexer.isEndOfFile(c));
    }

    @Test
    public void testIsDelimiter() throws IOException {
        int c = lexer.read();
        assertFalse(lexer.isDelimiter(c));

        lexer.read(); // consume space
        c = lexer.read();
        assertFalse(lexer.isDelimiter(c));

        lexer.read(); // consume comma
        c = lexer.read();
        assertTrue(lexer.isDelimiter(c));

        lexer.read(); // consume comma
        c = lexer.read();
        assertTrue(lexer.isDelimiter(c));

        lexer.read(); // consume space
        c = lexer.read();
        assertFalse(lexer.isDelimiter(c));

        lexer.read(); // consume space
        c = lexer.read();
        assertFalse(lexer.isDelimiter(c));

        lexer.read(); // consume comma
        c = lexer.read();
        assertTrue(lexer.isDelimiter(c));
    }

    @Test
    public void testIsEscape() throws IOException {
        int c = lexer.read();
        assertFalse(lexer.isEscape(c));

        lexer.read(); // consume space
        c = lexer.read();
        assertFalse(lexer.isEscape(c));

        lexer.read(); // consume backslash
        c = lexer.read();
        assertFalse(lexer.isEscape(c));

        lexer.read(); // consume space
        c = lexer.read();
        assertFalse(lexer.isEscape(c));

        lexer.read(); // consume space
        lexer.read(); // consume backslash
        c = lexer.read();
        assertTrue(lexer.isEscape(c));
    }

    @Test
    public void testIsQuoteChar() throws IOException {
        int c = lexer.read();
        assertFalse(lexer.isQuoteChar(c));

        lexer.read(); // consume space
        c = lexer.read();
        assertFalse(lexer.isQuoteChar(c));

        lexer.read(); // consume quote
        c = lexer.read();
        assertTrue(lexer.isQuoteChar(c));

        lexer.read(); // consume quote
        c = lexer.read();
        assertFalse(lexer.isQuoteChar(c));

        lexer.read(); // consume space
        c = lexer.read();
        assertFalse(lexer.isQuoteChar(c));

        lexer.read(); // consume quote
        c = lexer.read();
        assertTrue(lexer.isQuoteChar(c));
    }

    @Test
    public void testIsCommentStart() throws IOException {
        int c = lexer.read();
        assertFalse(lexer.isCommentStart(c));

        lexer.read(); // consume space
        c = lexer.read();
        assertFalse(lexer.isCommentStart(c));

        lexer.read(); // consume comment start
        c = lexer.read();
        assertTrue(lexer.isCommentStart(c));

        lexer.read(); // consume comment start
        c = lexer.read();
        assertFalse(lexer.isCommentStart(c));

        lexer.read(); // consume space
        c = lexer.read();
        assertFalse(lexer.isCommentStart(c));

        lexer.read(); // consume comment start
        c = lexer.read();
        assertTrue(lexer.isCommentStart(c));
    }

    @Test
    public void testNextToken() throws IOException {
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);

        assertEquals(Constants.START_OF_STREAM_TOKEN.getValue(), nextToken.getType());
        assertEquals(-1, nextToken.getValue());

        lexer.read(); // consume space
        nextToken = lexer.nextToken(token);
        assertEquals(Constants.WHITESPACE_TOKEN.getValue(), nextToken.getType());
        assertEquals(Constants.SPACE, nextToken.getValue());

        lexer.read(); // consume comma
        nextToken = lexer.nextToken(token);
        assertEquals(Constants.DELIMITER_TOKEN.getValue(), nextToken.getType());
        assertEquals(Constants.COMMA, nextToken.getValue());

        lexer.read(); // consume space
        nextToken = lexer.nextToken(token);
        assertEquals(Constants.WHITESPACE_TOKEN.getValue(), nextToken.getType());
        assertEquals(Constants.SPACE, nextToken.getValue());

        nextToken = lexer.nextToken(token);
        assertEquals(Constants.STRING_TOKEN.getValue(), nextToken.getType());
        assertEquals("c,d", token.getValue());

        nextToken = lexer.nextToken(token);
        assertEquals(Constants.DELIMITER_TOKEN.getValue(), nextToken.getType());
        assertEquals(Constants.COMMA, nextToken.getValue());

        nextToken = lexer.nextToken(token);
        assertEquals(Constants.STRING_TOKEN.getValue(), nextToken.getType());
        assertEquals("e", token.getValue());

        nextToken = lexer.nextToken(token);
        assertEquals(Constants.WHITESPACE_TOKEN.getValue(), nextToken.getType());
        assertEquals(Constants.SPACE, nextToken.getValue());

        nextToken = lexer.nextToken(token);
        assertEquals(Constants.STRING_TOKEN.getValue(), nextToken.getType());
        assertEquals("g\nh", token.getValue());

        nextToken = lexer.nextToken(token);
        assertEquals(Constants.DELIMITER_TOKEN.getValue(), nextToken.getType());
        assertEquals(Constants.COMMA, nextToken.getValue());

        nextToken = lexer.nextToken(token);
        assertEquals(Constants.STRING_TOKEN.getValue(), nextToken.getType());
        assertEquals("i,j", token.getValue());

        nextToken = lexer.nextToken(token);
        assertEquals(Constants.END_OF_STREAM_TOKEN.getValue(), nextToken.getType());
        assertEquals(Constants.EndOfFileToken.getValue(), nextToken.getValue());
    }
}