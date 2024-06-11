import org.apache.commons.csv.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() throws IOException {
        // Given
        ExtendedBufferedReader mockReader = new ExtendedBufferedReader(new StringReader("line1\nline2\n"));
        CSVFormat format = new CSVFormat(',', null);
        Lexer lexer = new Lexer(format, mockReader);

        // When
        long actualLineNumber = lexer.getLineNumber();

        // Then
        assertEquals(1, actualLineNumber);
    }

    @Test
    public void testReadEscapeCR() throws IOException {
        // Given
        ExtendedBufferedReader mockReader = new ExtendedBufferedReader(new StringReader("\\r"));
        Lexer lexer = new Lexer(new CSVFormat(',', null), mockReader);

        // When
        int readEscape = lexer.readEscape();

        // Then
        assertEquals('\r', readEscape);
    }

    @Test(expected = IOException.class)
    public void testReadEscapeInvalidCharacter() throws IOException {
        // Given
        ExtendedBufferedReader mockReader = new ExtendedBufferedReader(new StringReader("\\x"));
        Lexer lexer = new Lexer(new CSVFormat(',', null), mockReader);

        // When
        lexer.readEscape();

        // Then
        // Assertion error expected
    }
}