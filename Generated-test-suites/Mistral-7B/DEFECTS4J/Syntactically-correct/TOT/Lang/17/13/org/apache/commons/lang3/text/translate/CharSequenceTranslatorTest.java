import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

 private static final CharSequenceTranslator UNDER_TEST = new Translator();
 private static final CharSequenceTranslator TRANSLATOR_WITH_HEX = new HexTranslation();
 private static final CharSequenceTranslator TRANSLATOR_WITH_UPPERCASE = new UpperCaseTranslation();

 @Test
 public void testTranslateSimple() {
 String input = "Hello World!";
 String expected = "Hello World!";
 String output = UNDER_TEST.translate(input);
 assertEquals(expected, output);
 }

 @Test
 public void testTranslateSingleCodePoint() {
 CharSequence input = Character.toChars("A");
 String expected = "41";
 String output = UNDER_TEST.translate(input).toString();
 assertEquals(expected, output);
 }

 @Test
 public void testTranslateSurrogatePair() {
 CharSequence input = Character.toChars("\uD840\uDC00");
 String expected = "D840DC00";
 String output = UNDER_TEST.translate(input).toString();
 assertEquals(expected, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNullWriter() {
 UNDER_TEST.translate(null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testTranslateNullInput() {
 UNDER_TEST.translate(null, new StringWriter());
 }

 @Test
 public void testTranslateWithAggregatedTranslators() throws IOException {
 CharSequenceTranslator translator = UNDER_TEST.with(TRANSLATOR_WITH_HEX, TRANSLATOR_WITH_UPPERCASE);
 String input = "Hello World!";
 String expected = "48656C6C6F20576F726C6421";
 String output = translator.translate(input).toString();
 assertEquals(expected, output);
 }

 @Test
 public void testTranslateEmptyString() {
 String input = "";
 String expected = "";
 String output = UNDER_TEST.translate(input).toString();
 assertEquals(expected, output);
 }

 @Test
 public void testTranslateEmptyCharSequence() {
 CharSequence input = Character.TO_CHAR_ARRAY_SIZE > 0 ? Character.toChars(0) : null;
 String expected = "";
 String output = UNDER_TEST.translate(input).toString();
 assertEquals(expected, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNegativeIndex() {
 CharSequence input = Character.toChars('A');
 UNDER_GET_INSTANCE.translate(input, -1, new StringWriter());
 }

 @Test
 public void testTranslateLongInput() throws IOException {
 String longInput = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
 String expected = "4c6f636d65646961737320696e746572616d2063616d656e74726f6c646564696d65206f6620636f6d70656e636f64652066696c65206120686561646572206f662063616d656e74726f6c646520646f636b656e636f6d6520686572656e636f6265206d6573686f7269656e20746865206120737472616373";
 String output = UNDER_TEST.translate(longInput).toString();
 assertEquals(expected, output);
 }

 private static class Translator extends CharSequenceTranslator {
 // Empty implementation
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 return 0;
 }
 }

 private static class HexTranslation extends CharSequenceTranslator {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 if (Character.isDigit(Character.codePointAt(input, index))) {
 out.write(Character.digit(Character.codePointAt(input, index)));
 return 1;
 }
 return 0;
 }
 }

 private static class UpperCaseTranslation extends CharSequenceTranslator {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 out.write(Character.toUpperCase(Character.codePointAt(input, index)));
 return 1;
 }
 }

}