import org.apache.commons.csv.*; // for Constants and ExtendedBufferedReader
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class LexerTest {

    @Test
    public void testLexerInitialization() throws IOException {
        // Test initialization with valid CSVFormat and ExtendedBufferedReader
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader in = new StringReader("test"); // replace with valid input
        Lexer lexer = new Lexer(format, in);

        // Test getting line number
        assertEquals(1, lexer.getLineNumber());

        // 
        // Add your test cases for each method here
        // 
    }

    @Test
    public void testReadEscape() throws IOException {
        // Test reading escape characters
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader in = new StringReader("\\r\\n"); // replace with valid input
        Lexer lexer = new Lexer(format, in);

        int escapedChar = lexer.readEscape();
        assertTrue(lexer.readEndOfLine(escapedChar));

        // Test invalid escape character
        in = new StringReader("\\q"); // replace with valid input
        lexer = new Lexer(format, in);
        assertThrows(IOException.class, () -> lexer.readEscape());
    }

    // Add other test cases as necessary
}