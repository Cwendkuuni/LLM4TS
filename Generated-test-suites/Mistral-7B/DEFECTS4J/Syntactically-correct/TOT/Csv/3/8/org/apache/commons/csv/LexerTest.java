package org.apache.commons.csv.test;

import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for {@link Lexer} class
 */
public class LexerTest {

    private static final String TEST_CSV_FILE = "test.csv";
    private static final String TEST_CSV_CONTENTS = "a,b,c\n" +
            "1,2,3\n" +
            "4,5,6\n" +
            "# this is a comment\n" +
            "7,8,9\n" +
            "10,11,12";

    private CSVFormat format;
    private ExtendedBufferedReader reader;
    private Lexer lexer;

    @Before
    public void setUp() throws IOException {
        format = new CSVFormat();
        reader = new ExtendedBufferedReader(new StringReader(TEST_CSV_CONTENTS));
        lexer = new Lexer(format, reader);
    }

    @After
    public void tearDown() throws IOException {
        reader.close();
    }

    @Test
    public void testNextToken_whenCalledForFirstTime_thenReturnDelimiterToken() throws IOException {
        Token token = lexer.nextToken(null);
        assertEquals(Lexer.TokenType.DELIMITER, token.getType());
        assertEquals(lexer.delimiter, token.getValue().charAt(0));
        assertEquals(lexer.getLineNumber(), token.getLineNumber());
    }

    @Test
    public void testNextToken_whenCalledForEndOfLine_thenReturnEndOfLineToken() throws IOException {
        lexer.nextToken(null);
        lexer.nextToken(null);
        Token token = lexer.nextToken(null);
        assertEquals(Lexer.TokenType.END_OF_LINE, token.getType());
    }

    @Test
    public void testNextToken_whenCalledForWhitespace_thenReturnWhitespaceToken() throws IOException {
        lexer.nextToken(null);
        lexer.nextToken(null);
        lexer.nextToken(null);
        Token token = lexer.nextToken(null);
        assertEquals(Lexer.TokenType.WHITESPACE, token.getType());
        assertTrue(Character.isWhitespace(token.getValue().charAt(0)));
    }

    @Test
    public void testNextToken_whenCalledForComment_thenReturnCommentToken() throws IOException {
        lexer.nextToken(null);
        lexer.nextToken(null);
        lexer.nextToken(null);
        lexer.nextToken(null);
        Token token = lexer.nextToken(null);
        assertEquals(Lexer.TokenType.COMMENT, token.getType());
        assertEquals("# this is a comment", token.getValue());
        assertEquals(lexer.getLineNumber(), token.getLineNumber());
    }

    @Test
    public void testNextToken_whenCalledForNumber_thenReturnNumberToken() throws IOException {
        lexer.nextToken(null);
        lexer.nextToken(null);
        Token token = lexer.nextToken(null);
        assertEquals(Lexer.TokenType.NUMBER, token.getType());
        assertEquals("1", token.getValue());
        assertEquals(lexer.getLineNumber(), token.getLineNumber());
    }

    @Test
    public void testNextToken_whenCalledForString_thenReturnStringToken() throws IOException {
        lexer.nextToken(null);
        lexer.nextToken(null);
        lexer.nextToken(null);
        lexer.nextToken(null);
        Token token = lexer.nextToken(null);
        assertEquals(Lexer.TokenType.STRING, token.getType());
        assertEquals("7,8,9", token.getValue());
        assertEquals(lexer.getLineNumber(), token.getLineNumber());
    }

    @Test
    public void testNextToken_whenCalledForQuotedString_thenReturnQuotedStringToken() throws IOException {
        lexer.nextToken(null);
        lexer.nextToken(null);
        lexer.nextToken(null);
        lexer.nextToken(null);
        lexer.nextToken(null);
        Token token = lexer.nextToken(null);
        assertEquals(Lexer.TokenType.QUOTED_STRING, token.getType());
        assertEquals("10,11,12", token.getValue());
        assertEquals(lexer.getLineNumber(), token.getLineNumber());
    }

    @Test
    public void testNextToken_whenCalledForDisabledCharacter_thenReturnUndefinedToken() throws IOException {
        lexer.nextToken(null);
        lexer.nextToken(null);
        lexer.nextToken(null);
        lexer.nextToken(null);
        lexer.nextToken(null);
        lexer.nextToken(null);
        Token token = lexer.nextToken(null);
        assertEquals(Lexer.TokenType.UNDEFINED, token.getType());
        assertEquals(lexer.escape, token.getValue().charAt(0));
        assertEquals(lexer.getLineNumber(), token.getLineNumber());
    }

    @Test(expected = IOException.class)
    public void testNextToken_whenCalledForEndOfFile_thenThrowIOException() throws IOException {
        lexer.nextToken(null);
        lexer.nextToken(null);
        lexer.nextToken(null);
        Token token = lexer.nextToken(null);
        lexer.nextToken(null);
        assertNull(token);
        lexer.nextToken(null);
    }
}