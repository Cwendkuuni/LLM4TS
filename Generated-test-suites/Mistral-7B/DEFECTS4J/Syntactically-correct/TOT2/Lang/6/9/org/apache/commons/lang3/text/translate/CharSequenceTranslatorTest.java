import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator(); // Replace this with an instance of your custom translator
        CharSequence input = "Hello World";
        Writer out = new StringWriter();
        int expectedConsumed = input.length();
        int actualConsumed = translator.translate(input, 0, out);
        assertEquals(expectedConsumed, actualConsumed);
        String actualOutput = out.toString();
        String expectedOutput = "Hello World";
        assertEquals(expectedOutput, actualOutput);
    }
}