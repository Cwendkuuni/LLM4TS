import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import java.io.StringReader;
import java.io.IOException;

public class LexerTest {

    @Test
    public void testLexerConstructor() {
        CSVFormat format = new CSVFormat(',', '\u001e'); // use custom delimiter and quote char
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("")); // empty reader
        Lexer lexer = new Lexer(format, reader);
        assertTrue(lexer instanceof Lexer);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String testFile = "test.csv";
        CSVFormat format = new CSVFormat(',', '\u001e');
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new FileReader(testFile));
        Lexer lexer = new Lexer(format, reader);
        int lineNumber = lexer.getLineNumber();
        Assert.assertNotSame(0, lineNumber);
    }

    @Test
    public void testReadEscape() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u001e');
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        Lexer lexer = new Lexer(format, reader);
        int actual = lexer.readEscape();
        Assert.assertEquals(DISABLED, actual); // check default behavior

        reader = new ExtendedBufferedReader(new StringReader("\\r"));
        lexer = new Lexer(format, reader);
        actual = lexer.readEscape();
        Assert.assertEquals(CR, actual);

        reader = new ExtendedBufferedReader(new StringReader("\\n"));
        lexer = new Lexer(format, reader);
        actual = lexer.readEscape();
        Assert.assertEquals(LF, actual);

        reader = new ExtendedBufferedReader(new StringReader("\\t"));
        lexer = new Lexer(format, reader);
        actual = lexer.readEscape();
        Assert.assertEquals(TAB, actual);

        reader = new ExtendedBufferedReader(new StringReader("\\b"));
        lexer = new Lexer(format, reader);
        actual = lexer.readEscape();
        Assert.assertEquals(BACKSPACE, actual);

        reader = new ExtendedBufferedReader(new StringReader("\\f"));
        lexer = new Lexer(format, reader);
        actual = lexer.readEscape();
        Assert.assertEquals(FF, actual);

        reader = new ExtendedBufferedReader(new StringReader("\\\\")); // escape the escape character
        lexer = new Lexer(format, reader);
        actual = lexer.readEscape();
        Assert.assertEquals('\u001e', actual);

        reader = new ExtendedBufferedReader(new StringReader("\\r\\n")); // escape sequence
        lexer = new Lexer(format, reader);
        actual = lexer.readEscape();
        Assert.assertEquals(CR, actual);
        actual = lexer.readEscape();
        Assert.assertEquals(LF, actual);

        reader = new ExtendedBufferedReader(new StringReader("\\r\\n\\r")); // escape sequence followed by CR
        lexer = new Lexer(format, reader);
        actual = lexer.readEscape();
        Assert.assertEquals(CR, actual);
        actual = lexer.readEscape();
        Assert.assertEquals(CR, actual);

        reader = new ExtendedBufferedReader(new StringReader("\\r\\n\\r\\n")); // escape sequence followed by CR and LF
        lexer = new Lexer(format, reader);
        actual = lexer.readEscape();
        Assert.assertEquals(CR, actual);
        actual = lexer.readEscape();
        Assert.assertEquals(LF, actual);
        actual = lexer.readEscape();
        Assert.assertEquals(LF, actual);

        reader = new ExtendedBufferedReader(new StringReader("")); // EOF
        lexer = new Lexer(format, reader);
        try {
            lexer.readEscape();
            fail("Expected IOException when EOF detected");
        } catch (IOException e) {
            // expected
        }
    }

    // ...
}