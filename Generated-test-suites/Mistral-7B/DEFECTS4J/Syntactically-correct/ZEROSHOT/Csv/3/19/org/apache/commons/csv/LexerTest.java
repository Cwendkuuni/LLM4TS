import org.apache.commons.csv.Constants;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    private ExtendedBufferedReader mockReader;
    private CSVFormat mockFormat;
    private Lexer lexer;

    @BeforeEach
    public void setup() {
        mockReader = mock(ExtendedBufferedReader.class);
        mockFormat = new CSVFormat();
        lexer = new Lexer(mockFormat, mockReader);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        when(mockReader.getLineNumber()).thenReturn(1);
        assertEquals(1, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape_CR() throws IOException {
        when(mockReader.read()).thenReturn((int) Constants.CR);
        assertEquals((int) Constants.CR, lexer.readEscape());
    }

    @Test
    public void testReadEscape_LF() throws IOException {
        when(mockReader.read()).thenReturn((int) Constants.LF);
        assertEquals((int) Constants.LF, lexer.readEscape());
    }

    @Test
    public void testReadEscape_TAB() throws IOException {
        when(mockReader.read()).thenReturn((int) Constants.TAB);
        assertEquals((int) Constants.TAB, lexer.readEscape());
    }

    @Test
    public void testReadEscape_Backspace() throws IOException {
        when(mockReader.read()).thenReturn((int) Constants.BACKSPACE);
        assertEquals((int) Constants.BACKSPACE, lexer.readEscape());
    }

    @Test
    public void testReadEscape_FF() throws IOException {
        when(mockReader.read()).thenReturn((int) Constants.FF);
        assertEquals((int) Constants.FF, lexer.readEscape());
    }

    @Test
    public void testReadEscape_InvalidCharacter() throws IOException {
        when(mockReader.read()).thenReturn((int) 'a');
        assertThrows(IOException.class, () -> lexer.readEscape());
    }

    @Test
    public void testReadEscape_EndOfStream() throws IOException {
        when(mockReader.read()).thenReturn(Constants.END_OF_STREAM);
        assertThrows(IOException.class, () -> lexer.readEscape());
    }

    @Test
    public void testIsStartOfLine_CR() {
        assertTrue(lexer.isStartOfLine((int) Constants.CR));
    }

    @Test
    public void testIsStartOfLine_LF() {
        assertTrue(lexer.isStartOfLine((int) Constants.LF));
    }

    @Test
    public void testIsStartOfLine_Other() {
        assertFalse(lexer.isStartOfLine((int) 'a'));
    }

    @Test
    public void testIsEndOfFile_CR() {
        assertFalse(lexer.isEndOfFile((int) Constants.CR));
    }

    @Test
    public void testIsEndOfFile_LF() {
        assertFalse(lexer.isEndOfFile((int) Constants.LF));
    }

    @Test
    public void testIsEndOfFile_EndOfStream() {
        assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
    }

    @Test
    public void testIsDelimiter_Delimiter() {
        assertTrue(lexer.isDelimiter((int) lexer.delimiter));
    }

    @Test
    public void testIsDelimiter_NotDelimiter() {
        assertFalse(lexer.isDelimiter((int) 'a'));
    }

    @Test
    public void testIsEscape_Escape() {
        assertTrue(lexer.isEscape((int) lexer.escape));
    }

    @Test
    public void testIsEscape_NotEscape() {
        assertFalse(lexer.isEscape((int) 'a'));
    }

    @Test
    public void testIsQuoteChar_QuoteChar() {
        assertTrue(lexer.isQuoteChar((int) lexer.quoteChar));
    }

    @Test
    public void testIsQuoteChar_NotQuoteChar() {
        assertFalse(lexer.isQuoteChar((int) 'a'));
    }

    @Test
    public void testIsCommentStart_CommentStart() {
        assertTrue(lexer.isCommentStart((int) lexer.commmentStart));
    }

    @Test
    public void testIsCommentStart_NotCommentStart() {
        assertFalse(lexer.isCommentStart((int) 'a'));
    }

    @Test
    public void testIsWhitespace_Whitespace() {
        char whitespace = ' ';
        assertTrue(lexer.isWhitespace((int) whitespace));
    }

    @Test
    public void testIsWhitespace_Delimiter() {
        assertFalse(lexer.isWhitespace((int) lexer.delimiter));
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        StringBuilder sb = new StringBuilder(" test   string ");
        lexer.trimTrailingSpaces(sb);
        assertEquals("test string", sb.toString());
    }

    @Test
    public void testReadEndOfLine_CR() throws IOException {
        assertTrue(lexer.readEndOfLine((int) Constants.CR));
    }

    @Test
    public void testReadEndOfLine_LF() throws IOException {
        assertTrue(lexer.readEndOfLine((int) Constants.LF));
    }

    @Test
    public void testReadEndOfLine_Other() throws IOException {
        assertFalse(lexer.readEndOfLine((int) 'a'));
    }
}