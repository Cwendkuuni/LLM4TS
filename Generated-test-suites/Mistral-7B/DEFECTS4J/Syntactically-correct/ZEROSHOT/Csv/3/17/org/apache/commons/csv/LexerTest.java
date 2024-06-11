import org.apache.commons.csv.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class LexerTest {

    @Mock
    private ExtendedBufferedReader mockReader;

    private CSVFormat format;
    private Lexer lexer;

    @Before
    public void setUp() {
        format = new CSVFormatBuilder()
                .setDelimiter(',')
                .setHeaderSeparator(Constants.DEFAULT_HEADER_SEPARATOR)
                .setIgnoreEmptyLines(true)
                .setNullString("")
                .setIgnoreSurroundingSpaces(true)
                .build();
        lexer = new Lexer(format, mockReader);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Mockito.when(mockReader.getLineNumber()).thenReturn(1);
        assertEquals(1, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape_CR() throws IOException {
        Mockito.when(mockReader.read()).thenReturn('r');
        Mockito.when(mockReader.lookAhead()).thenReturn('n');
        assertEquals(Constants.CR, lexer.readEscape());
    }

    @Test
    public void testReadEscape_LF() throws IOException {
        Mockito.when(mockReader.read()).thenReturn('n');
        assertEquals(Constants.LF, lexer.readEscape());
    }

    @Test
    public void testReadEscape_Tab() throws IOException {
        Mockito.when(mockReader.read()).thenReturn('\t');
        assertEquals(Constants.TAB, lexer.readEscape());
    }

    @Test(expected = IOException.class)
    public void testReadEscape_EndOfStream() throws IOException {
        Mockito.when(mockReader.read()).thenReturn(Constants.END_OF_STREAM);
        lexer.readEscape();
    }

    @Test
    public void testIsWhitespace() {
        for (char c = 0; c < 256; c++) {
            boolean expected = Character.isWhitespace(c);
            boolean actual = lexer.isWhitespace((int) c);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testIsStartOfLine() {
        for (int c = 0; c < 256; c++) {
            boolean actual = lexer.isStartOfLine(c);
            if (c == Constants.LF || c == Constants.CR) {
                assertTrue(actual);
            } else if (c == Constants.UNDEFINED) {
                assertTrue(actual);
            } else {
                assertFalse(actual);
            }
        }
    }

    @Test
    public void testIsEndOfFile() {
        Mockito.when(mockReader.read()).thenReturn(Constants.END_OF_STREAM);
        assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
    }

    @Test
    public void testIsDelimiter() {
        assertFalse(lexer.isDelimiter(Constants.CR));
        assertFalse(lexer.isDelimiter(Constants.LF));
        assertFalse(lexer.isDelimiter('A'));
        assertTrue(lexer.isDelimiter(','));
    }

    @Test
    public void testIsEscape() {
        assertFalse(lexer.isEscape(Constants.CR));
        assertFalse(lexer.isEscape(Constants.LF));
        assertFalse(lexer.isEscape('A'));
        assertTrue(lexer.isEscape(lexer.escape));
    }

    @Test
    public void testIsQuoteChar() {
        assertFalse(lexer.isQuoteChar(Constants.CR));
        assertFalse(lexer.isQuoteChar(Constants.LF));
        assertFalse(lexer.isQuoteChar('A'));
        assertTrue(lexer.isQuoteChar(lexer.quoteChar));
    }

    @Test
    public void testIsCommentStart() {
        assertFalse(lexer.isCommentStart(Constants.CR));
        assertFalse(lexer.isCommentStart(Constants.LF));
        assertFalse(lexer.isCommentStart('A'));
        assertTrue(lexer.isCommentStart(lexer.commmentStart));
    }

    @Test
    public void testNextToken_Empty() throws IOException {
        String expected = "";
        String result = "";
        Token token = new Token();
        lexer.nextToken(token);
        assertEquals(expected, result);
        assertNotNull(token.getBegin());
        assertNotNull(token.getEnd());
        assertEquals(TokenType.EMPTY, token.getType());
    }

    @Test
    public void testNextToken_Whitespace() throws IOException {
        String expected = " ";
        String result = "";
        Token token = new Token();
        Mockito.when(mockReader.read()).thenReturn(' ');
        lexer.nextToken(token);
        assertEquals(expected, result);
        assertNotNull(token.getBegin());
        assertNotNull(token.getEnd());
        assertEquals(5, token.getLength());
        assertEquals(TokenType.WHITESPACE, token.getType());
    }

    @Test
    public void testNextToken_Delimiter() throws IOException {
        String expected = ",";
        String result = "";
        Token token = new Token();
        Mockito.when(mockReader.read()).thenReturn(',');
        lexer.nextToken(token);
        assertEquals(expected, result);
        assertNotNull(token.getBegin());
        assertNotNull(token.getEnd());
        assertEquals(1, token.getLength());
        assertEquals(TokenType.DELIMITER, token.getType());
    }

    @Test
    public void testNextToken_QuotedString() throws IOException {
        String expected = "\"abc\"";
        String result = "";
        Token token = new Token();
        Mockito.when(mockReader.read()).thenReturn('"');
        lexer.nextToken(token);
        Mockito.when(mockReader.read()).thenReturn('a');
        lexer.nextToken(token);
        Mockito.when(mockReader.read()).thenReturn('b');
        lexer.nextToken(token);
        Mockito.when(mockReader.read()).thenReturn('c');
        lexer.nextToken(token);
        Mockito.when(mockReader.read()).thenReturn('"');
        lexer.nextToken(token);
        assertEquals(expected, result);
        assertNotNull(token.getBegin());
        assertNotNull(token.getEnd());
        assertEquals(7, token.getLength());
        assertEquals(TokenType.STRING, token.getType());
    }

    @Test
    public void testNextToken_EscapedCharacter() throws IOException {
        String expected = "\\r";
        String result = "";
        Token token = new Token();
        Mockito.when(mockReader.read()).thenReturn('\\');
        lexer.nextToken(token);
        Mockito.when(mockReader.read()).thenReturn('r');
        lexer.nextToken(token);
        assertEquals(expected, result);
        assertNotNull(token.getBegin());
        assertNotNull(token.getEnd());
        assertEquals(2, token.getLength());
        assertEquals(TokenType.ESCAPED_CHARACTER, token.getType());
    }

    @Test
    public void testNextToken_Comment() throws IOException {
        String expected = "#comment";
        String result = "";
        Token token = new Token();
        Mockito.when(mockReader.read()).thenReturn(lexer.commmentStart);
        lexer.nextToken(token);
        Mockito.when(mockReader.read()).thenReturn('c');
        Mockito.when(mockReader.read()).thenReturn('o');
        Mockito.when(mockReader.read()).thenReturn('m');
        Mockito.when(mockReader.read()).thenReturn('m');
        Mockito.when(mockReader.read()).thenReturn('e');
        Mockito.when(mockReader.read()).thenReturn('n');
        Mockito.when(mockReader.read()).thenReturn('t');
        Mockito.when(mockReader.read()).thenReturn('\n');
        lexer.nextToken(token);
        assertEquals(expected, result);
        assertNotNull(token.getBegin());
        assertNotNull(token.getEnd());
        assertEquals(8, token.getLength());
        assertEquals(TokenType.COMMENT, token.getType());
    }

    @Test
    public void testNextToken_QuotedBlockString() throws IOException {
        String expected = "\"block\"";
        String result = "";
        Token token = new Token();
        Mockito.when(mockReader.read()).thenReturn('"');
        lexer.nextToken(token);
        Mockito.when(mockReader.read()).thenReturn('b');
        Mockito.when(mockReader.read()).thenReturn('l');
        Mockito.when(mockReader.read()).thenReturn('o');
        Mockito.when(mockReader.read()).thenReturn('c');
        Mockito.when(mockReader.read()).thenReturn('k');
        Mockito.when(mockReader.read()).thenReturn('\"');
        lexer.nextToken(token);
        assertEquals(expected, result);
        assertNotNull(token.getBegin());
        assertNotNull(token.getEnd());
        assertEquals(7, token.getLength());
        assertEquals(TokenType.STRING, token.getType());
    }

    @Test
    public void testNextToken_QuotedBlockStringWithEscape() throws IOException {
        String expected = "\"block\\t\"";
        String result = "";
        Token token = new Token();
        Mockito.when(mockReader.read()).thenReturn('"');
        lexer.nextToken(token);
        Mockito.when(mockReader.read()).thenReturn('b');
        Mockito.when(mockReader.read()).thenReturn('l');
        Mockito.when(mockReader.read()).thenReturn('o');
        Mockito.when(mockReader.read()).thenReturn('c');
        Mockito.when(mockReader.read()).thenReturn('k');
        Mockito.when(mockReader.read()).thenReturn('\\');
        Mockito.when(mockReader.read()).thenReturn('\t');
        Mockito.when(mockReader.read()).thenReturn('"');
        lexer.nextToken(token);
        assertEquals(expected, result);
        assertNotNull(token.getBegin());
        assertNotNull(token.getEnd());
        assertEquals(9, token.getLength());
        assertEquals(TokenType.STRING, token.getType());
    }

    @Test
    public void testNextToken_QuotedBlockStringWithComment() throws IOException {
        String expected = "\"block#comment\"";
        String result = "";
        Token token = new Token();
        Mockito.when(mockReader.read()).thenReturn('"');
        lexer.nextToken(token);
        Mockito.when(mockReader.read()).thenReturn('b');
        Mockito.when(mockReader.read()).thenReturn('l');
        Mockito.when(mockReader.read()).thenReturn('o');
        Mockito.when(mockReader.read()).thenReturn('c');
        Mockito.when(mockReader.read()).thenReturn('k');
        Mockito.when(mockReader.read()).thenReturn('#');
        Mockito.when(mockReader.read()).thenReturn('c');
        Mockito.when(mockReader.read()).thenReturn('o');
        Mockito.when(mockReader.read()).thenReturn('m');
        Mockito.when(mockReader.read()).thenReturn('m');
        Mockito.when(mockReader.read()).thenReturn('e');
        Mockito.when(mockReader.read()).thenReturn('n');
        Mockito.when(mockReader.read()).thenReturn('t');
        Mockito.when(mockReader.read()).thenReturn('\n');
        lexer.nextToken(token);
        assertEquals(expected, result);
        assertNotNull(token.getBegin());
        assertNotNull(token.getEnd());
        assertEquals(15, token.getLength());
        assertEquals(TokenType.STRING, token.getType());
    }

    @Test
    public void testNextToken_Field() throws IOException {
        String expected = "field";
        String result = "";
        Token token = new Token();
        String[] fields = {"field1", "field2", "field"};
        List<String> list = new ArrayList<>();
        list.addAll(List.of(fields));
        Mockito.when(mockReader.readAll(Mockito.anyInt())).thenReturn(list);
        lexer.nextToken(token);
        assertEquals(expected, result);
        assertNotNull(token.getBegin());
        assertNotNull(token.getEnd());
        assertEquals(5, token.getLength());
        assertEquals(TokenType.FIELD, token.getType());
    }

    @Test
    public void testNextToken_SkippedEmptyLine() throws IOException {
        String expected = "";
        String result = "";
        Token token = new Token();
        Mockito.when(mockReader.readLine()).thenReturn(null);
        Mockito.when(mockReader.read()).thenReturn(Constants.LF);
        lexer.nextToken(token);
        assertEquals(expected, result);
        assertNotNull(token.getBegin());
        assertNotNull(token.getEnd());
        assertEquals(0, token.getLength());
        assertEquals(TokenType.EMPTY, token.getType());
    }

    @Test
    public void testNextToken_SkippedWhitespace() throws IOException {
        String expected = "";
        String result = "";
        Token token = new Token();
        Mockito.when(mockReader.read()).thenReturn(' ');
        lexer.nextToken(token);
        assertEquals(expected, result);
        assertNotNull(token.getBegin());
        assertNotNull(token.getEnd());
        assertEquals(0, token.getLength());
        assertEquals(TokenType.WHITESPACE, token.getType());
    }

    @Test
    public void testNextToken_SkippedComment() throws IOException {
        String expected = "";
        String result = "";
        Token token = new Token();
        Mockito.when(mockReader.read()).thenReturn(lexer.commmentStart);
        lexer.nextToken(token);
        Mockito.when(mockReader.read()).thenReturn('c');
        Mockito.when(mockReader.read()).thenReturn('o');
        Mockito.when(mockReader.read()).thenReturn('m');
        Mockito.when(mockReader.read()).thenReturn('m');
        Mockito.when(mockReader.read()).thenReturn('e');
        Mockito.when(mockReader.read()).thenReturn('n');
        Mockito.when(mockReader.read()).thenReturn('t');
        Mockito.when(mockReader.read()).thenReturn('\n');
        lexer.nextToken(token);
        assertEquals(expected, result);
        assertNotNull(token.getBegin());
        assertNotNull(token.getEnd());
        assertEquals(0, token.getLength());
        assertEquals(TokenType.COMMENT, token.getType());
    }
}