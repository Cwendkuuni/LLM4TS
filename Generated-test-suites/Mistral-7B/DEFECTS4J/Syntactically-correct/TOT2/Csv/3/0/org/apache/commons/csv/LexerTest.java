import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.CSVFormat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LexerTest {
    @Test
    public void testGetLineNumber() throws IOException {
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        when(in.getLineNumber()).thenReturn(42);
        Lexer lexer = new Lexer(format, in);
        assertEquals(42, lexer.getLineNumber());
    }

    @Test
    public void testReadEscapeCR() throws IOException {
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        when(in.read()).thenReturn('r').thenReturn('n');
        Lexer lexer = new Lexer(format, in);
        assertEquals(LF, lexer.readEscape());
    }

    @Test(expected = IOException.class)
    public void testReadEscapeEndOfStream() throws IOException {
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        when(in.read()).thenReturn(END_OF_STREAM);
        Lexer lexer = new Lexer(format, in);
        lexer.readEscape();
    }
}