import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateSimple() throws Exception {
        CharSequenceTranslator translator = new MySimpleTranslator();
        String input = "Hello World!";
        String expectedOutput = "Hello%20World%21";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslateWithAggregate() throws Exception {
        CharSequenceTranslator translator = new AggregateTranslator(new MySimpleTranslator(), new MyUpperCaseTranslator());
        String input = "Hello World!";
        String expectedOutput = "Hello%20World%21HELLO";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() throws Exception {
        CharSequenceTranslator translator = new MySimpleTranslator();
        String input = null;
        translator.translate(input);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IOException.class)
    public void testTranslateIOException() throws Exception {
        CharSequenceTranslator translator = new MyIOExceptionTranslator();
        String input = "Hello World!";
        translator.translate(input);
        fail("Expected IOException");
    }

    @Test
    public void testTranslateEmptyString() throws Exception {
        CharSequenceTranslator translator = new MySimpleTranslator();
        String input = "";
        String expectedOutput = "";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    // Add edge cases and other test cases as necessary
}

class MySimpleTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int consumed = 1;
        char c = Character.codePointAt(input, index);
        out.write(Character.toChars(c));
        if (Character.isHighSurrogate(c)) {
            consumed += Character.charCount(Character.codePointAt(input, index + 1));
        }
        return consumed;
    }
}

class MyUpperCaseTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int consumed = 1;
        char c = Character.codePointAt(input, index);
        if (Character.isLetter(c)) {
            out.write(Character.toChars(Character.toUpperCase(c)));
            consumed++;
        } else {
            out.write(Character.toChars(c));
        }
        return consumed;
    }
}

class MyIOExceptionTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        throw new IOException("Simulated IOException");
    }
}