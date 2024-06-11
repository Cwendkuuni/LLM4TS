import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequenceTranslator hexTranslator;

 @Test
 public void testTranslate() throws Exception {
 // Initialize translators
 translator = new TranslatingCharSequenceTranslator();
 hexTranslator = new HexTranslator();

 // Test translation with no input
 assertEquals("", translator.translate(CharSequence.of("")));

 // Test translation with simple input
 CharSequence input = CharSequence.of("Hello World!");
 String expected = "Hello World!";
 String actual = translator.translate(input);
 assertEquals(expected, actual);

 // Test translation with special characters
 input = CharSequence.of("&<>\n\r\"'/()\\[]{}|:\"  ");
 expected = "&amp;<&gt;\\n\\r\\&quot;\\'/()\\\\[]{\\}\\|:\\ ";
 actual = translator.translate(input);
 assertEquals(expected, actual);

 // Test translation with hex codes
 input = CharSequence.of("\\u00E1\\u03B4\\u2603");
 expected = "ᶁᴾ🔶";
 actual = hexTranslator.translate(translator.translate(input));
 assertEquals(expected, actual);

 // Test translation with multiple translators
 CharSequenceTranslator[] translators = new CharSequenceTranslator[]{translator, hexTranslator};
 CharSequenceTranslator compositeTranslator = new AggregateTranslator(translators);
 expected = "ᶁᴾ🔶&amp;<&gt;\\n\\r\\&quot;\\'/()\\\\[]{\\}\\|:\\ ";
 actual = compositeTranslator.translate(input);
 assertEquals(expected, actual);
 }

 private static class TranslatingCharSequenceTranslator extends CharSequenceTranslator {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 int codepoint = Character.codePointAt(input, index);
 int consumed = Character.isSurrogate(codepoint) ? 2 : 1;
 out.write(Character.toChars(codepoint)[0]);
 return consumed;
 }
 }

 private static class HexTranslator extends CharSequenceTranslator {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 int codepoint = Character.codePointAt(input, index);
 if (Character.isDigit(codepoint) || Character.isLetter(codepoint)) {
 out.write(Character.toChar(Character.toUpperCase(codepoint) - '0' + 0x20));
 } else {
 throw new IllegalArgumentException("Invalid hex code: " + Character.toChars(codepoint)[0]);
 }
 return 1;
 }
 }

}