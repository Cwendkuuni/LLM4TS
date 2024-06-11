import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

 private final CharSequence[][] lookup = {{"foo", "bar"}, {"hello", "world"}};

 @Test
 public void testLookupTranslator() {
 LookupTranslator translator = new LookupTranslator(lookup);
 CharSequence input = "foohello";

 int expectedTranslationLength = 4 + 6; // length of "foo" and "hello"
 int translationLength = translator.translate(input, 0, new StringWriter(null)).length();
 assertEquals("Unexpected translation length", expectedTranslationLength, translationLength);
 assertEquals("Unexpected translation output", "barworld", translator.translate(input, 0, new StringWriter(null)).toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullLookupTable() {
 LookupTranslator translator = new LookupTranslator(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testLookupTableOutOfBounds() {
 LookupTranslator translator = new LookupTranslator(lookup);
 CharSequence input = "foobarfoobar";
 int index = 4;
 int expectedTranslationLength = 3; // length of "bar"
 int translationLength = translator.translate(input, index, new StringWriter(null)).length();
 assertEquals("Unexpected translation length", expectedTranslationLength, translationLength);
 assertEquals("Unexpected translation output", "bar", translator.translate(input, index, new StringWriter(null)).toString());

 index = 7;
 translator.translate(input, index, new StringWriter(null)); // should not throw an exception
 }

 @Test
 public void testEmptyInput() {
 LookupTranslator translator = new LookupTranslator(lookup);
 CharSequence input = "";
 int expectedTranslationLength = 0;
 int translationLength = translator.translate(input, 0, new StringWriter(null)).length();
 assertEquals("Unexpected translation length", expectedTranslationLength, translationLength);
 }

 @Test
 public void testEmptySubsequence() {
 LookupTranslator translator = new LookupTranslator(lookup);
 CharSequence input = "foo";
 int index = 3;
 int expectedTranslationLength = 0;
 int translationLength = translator.translate(input, index, new StringWriter(null)).length();
 assertEquals("Unexpected translation length", expectedTranslationLength, translationLength);
 }

 @Test
 public void testSubsequenceBeyondInputBounds() {
 LookupTranslator translator = new LookupTranslator(lookup);
 CharSequence input = "foohello";
 int index = input.length() + 1;
 int translationLength = translator.translate(input, index, new StringWriter(null)).length();
 assertEquals("Unexpected translation length", 0, translationLength);
 }

 @Test
 public void testSubsequenceNotPresentInLookupTable() {
 LookupTranslator translator = new LookupTranslator(lookup);
 CharSequence input = "foobaz";
 int index = 3;
 int translationLength = translator.translate(input, index, new StringWriter(null)).length();
 assertEquals("Unexpected translation length", 0, translationLength);
 }

 @Test
 public void testSubsequencePresentInLookupTableWithLeadingWhiteSpaces() {
 LookupTranslator translator = new LookupTranslator(lookup);
 CharSequence input = "   foo   ";
 int index = 2;
 int expectedTranslationLength = 3; // length of "bar"
 int translationLength = translator.translate(input, index, new StringWriter(null)).length();
 assertEquals("Unexpected translation length", expectedTranslationLength, translationLength);
 assertEquals("Unexpected translation output", "bar", translator.translate(input, index, new StringWriter(null)).toString());
 }

 @Test
 public void testSubsequencePresentInLookupTableWithTrailingWhiteSpaces() {
 LookupTranslator translator = new LookupTranslator(lookup);
 CharSequence input = "foo   ";
 int index = 0;
 int expectedTranslationLength = 4; // length of "bar"
 int translationLength = translator.translate(input, index, new StringWriter(null)).length();
 assertEquals("Unexpected translation length", expectedTranslationLength, translationLength);
 assertEquals("Unexpected translation output", "bar", translator.translate(input, index, new StringWriter(null)).toString());
 }

 @Test
 public void testSubsequencePresentInLookupTableWithLeadingAndTrailingWhiteSpaces() {
 LookupTranslator translator = new LookupTranslator(lookup);
 CharSequence input = "   foo   ";
 int index = 2;
 int expectedTranslationLength = 3; // length of "bar"
 int translationLength = translator.translate(input, index, new StringWriter(null)).length();
 assertEquals("Unexpected translation length", expectedTranslationLength, translationLength);
 assertEquals("Unexpected translation output", "bar", translator.translate(input, index, new StringWriter(null)).toString());
 }

 @Test
 public void testSubsequencePresentInLookupTableWithMixedWhiteSpaces() {
 LookupTranslator translator = new LookupTranslator(lookup);
 CharSequence input = " foo   bar  ";
 int index = 1;
 int expectedTranslationLength = 4; // length of "world"
 int translationLength = translator.translate(input, index, new StringWriter(null)).length();
 assertEquals("Unexpected translation length", expectedTranslationLength, translationLength);
 assertEquals("Unexpected translation output", "world", translator.translate(input, index, new StringWriter(null)).toString());
 }
}