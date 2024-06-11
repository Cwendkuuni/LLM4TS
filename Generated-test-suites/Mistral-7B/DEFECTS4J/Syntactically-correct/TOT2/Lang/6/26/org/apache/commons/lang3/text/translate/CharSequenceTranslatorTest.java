
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.NoOpTranslator;
import org.apache.commons.lang3.text.translate.HexTranslator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testTranslate_BasicFunctionality() {
        CharSequenceTranslator translator = new NoOpTranslator();
        String input = "Hello World!";
        String output = translator.translate(input);
        assertEquals(input, output);
    }

    @Test
    public void testTranslate_EdgeCases() {
        CharSequenceTranslator translator = new AggregateTranslator(new NoOpTranslator(), new HexTranslator());
        String input = "Hello 世界!"; // Contains a surrogate pair and a Chinese character
        String expectedOutput = "Hello %s%s%s!"; // %s represents the Chinese character
        String output = translator.translate(input);
        assertEquals(String.format(expectedOutput, hex(0x4E2D), hex(0x65E5), hex(0x6714)), output);
    }

    @Test
    public void testTranslate_ExceptionScenario() {
        CharSequenceTranslator translator = new AggregateTranslator(new HexTranslator(), new BrokenTranslator());
        String input = "Hello World!";
        expectedException.expect(RuntimeException.class);
        translator.translate(input);
    }

    static class NoOpTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return Character.isWhitespace(Character.codePointAt(input, index)) ? 1 : 0;
        }
    }

    static class HexTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            if (Character.isDigit(c) || Character.isLetter(c) || c == '_') {
                out.write(c);
                return 1;
            } else {
                return 0;
            }
        }
    }

    static class BrokenTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new RuntimeException();
        }
    }

    static class AggregateTranslator extends CharSequenceTranslator {
        private CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator... translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int consumed = 0;
            for (CharSequenceTranslator translator : translators) {
                consumed += translator.translate(input, index, out);
            }
            return consumed;
        }
    }
}