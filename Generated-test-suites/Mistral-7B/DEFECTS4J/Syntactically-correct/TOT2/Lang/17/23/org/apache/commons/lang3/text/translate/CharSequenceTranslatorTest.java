import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CharSequenceTranslatorTest {

    @Test
    public void testDefaultTranslator() {
        CharSequenceTranslator translator = new ASCIITranslator(); // Assume ASCIITranslator is the default implementation
        String input = "Hello, World!";
        String expectedOutput = "Hello, World!";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslateMethod() throws IOException {
        CharSequenceTranslator translator = new HTMLTranslator(); // Assume HTMLTranslator is an available implementation
        String input = "Hello, <world>";
        Writer out = new StringWriter();
        int consumedCodePoints = translator.translate(input, 0, out);
        assertEquals("Hello, &lt;world&gt;", out.toString());
        assertEquals(7, consumedCodePoints);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWriter() {
        CharSequenceTranslator translator = new HTMLTranslator();
        String input = "Hello, World!";
        Writer out = null;
        translator.translate(input, out);
    }

    @Test(expected = IOException.class)
    public void testWriterIOException() throws IOException {
        CharSequenceTranslator translator = new HTMLTranslator();
        String input = "Hello, World!";
        Writer out = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }
        };
        translator.translate(input, out);
    }

    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator htmlTranslator = new HTMLTranslator();
        CharSequenceTranslator asciiTranslator = new ASCIITranslator();
        CharSequenceTranslator translator = htmlTranslator.with(asciiTranslator);
        String input = "Hello, <world>!";
        Writer out = new StringWriter();
        int consumedCodePoints = translator.translate(input, 0, out);
        assertEquals("Hello, &lt;world&gt;!", out.toString());
        assertEquals(10, consumedCodePoints); // 5 for 'Hello, ' + 1 for '<' + 3 for 'world' + 1 for '!'
    }
}