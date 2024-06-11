import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {
    private Lexer lexer;
    private ExtendedBufferedReader mockReader;
    private CSVFormat format;

    @Before
    public void setUp() throws Exception {
        format = new CSVFormat(',', '\u0002', '\u0002', '\u0002');
        mockReader = new MockExtendedBufferedReader();
        lexer = new Lexer(format, mockReader);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        assertEquals(1, lexer.getLineNumber());
        mockReader.setLineNumber(2);
        assertEquals(2, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape_CR() throws IOException {
        when(mockReader.read()).thenReturn('r');
        when(mockReader.lookAhead()).thenReturn('n');
        assertEquals('\r', lexer.readEscape());
        verify(mockReader).read();
        verify(mockReader).read();
    }

    @Test
    public void testReadEscape_LF() throws IOException {
        when(mockReader.read()).thenReturn('n');
        assertEquals('\n', lexer.readEscape());
        verify(mockReader).read();
    }

    @Test
    public void testReadEscape_InvalidEscapeSequence() throws IOException {
        when(mockReader.read()).thenReturn('X');
        try {
            lexer.readEscape();
            fail();
        } catch (IOException e) {
            // expected
        }
        verify(mockReader).read();
    }

    // Add other test cases for other methods in the Lexer class
}