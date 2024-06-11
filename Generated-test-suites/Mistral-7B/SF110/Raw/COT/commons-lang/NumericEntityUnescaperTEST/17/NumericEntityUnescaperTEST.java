import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper;

 @Test
 public void testNumericEntityUnescaperConstructorWithOptions() {
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
 assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
 }

 @Test
 public void testNumericEntityUnescaperConstructorWithoutOptions() {
 unescaper = new NumericEntityUnescaper();
 assertFalse(unescaper.isSet(OPTION.semiColonRequired));
 assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));
 }

 @Test
 public void testIsSetWithValidOption() {
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 }

 @Test
 public void testIsSetWithInvalidOption() {
 unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 assertFalse(unescaper.isSet(OPTION.semiColonRequired));
 }

 @Test
 public void testTranslateWithValidNumericEntity() throws IOException {
 String input = "&amp;#160;";
 Writer output = new StringWriter();
 int charsWritten = unescaper.translate(new StringReader(input), 0, output);
 assertEquals(" ", output.toString());
 assertEquals(3, charsWritten);
 }

 @Test
 public void testTranslateWithValidNumericEntityWithHex() throws IOException {
 String input = "&amp;#xA0;";
 Writer output = new StringWriter();
 int charsWritten = unescaper.translate(new StringReader(input), 0, output);
 assertEquals("\u00A0", output.toString());
 assertEquals(3, charsWritten);
 }

 @Test
 public void testTranslateWithValidNumericEntitiesWithSemiColon() throws IOException {
 String input = "some text &amp;#160;more text &amp;#x41 &amp;#xFF;";
 Writer output = new StringWriter();
 int charsWritten = unescaper.translate(new StringReader(input), 0, output);
 assertEquals("some text \u00A0more text A \uffff", output.toString());
 assertEquals(21, charsWritten);
 }

 @Test
 public void testTranslateWithValidNumericEntitiesWithoutSemiColon() throws IOException {
 String input = "some text &amp;#160;more text &amp;#x41 &amp;#xFF";
 String output = unescaper.translate(new StringReader(input), 0, null);
 assertEquals("some text \u00A0more text A \uffff", output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithInvalidNumericEntity() throws IOException {
 String input = "some text &amp;#abc;more text";
 Writer output = new StringWriter();
 int charsWritten = unescaper.translate(new StringReader(input), 0, output);
 assertEquals(0, charsWritten);
 }

 @Test
 public void testTranslateWithInvalidNumericEntityWithHex() throws IOException {
 String input = "some text &amp;#abc;more text";
 Writer output = new StringWriter();
 int charsWritten = unescaper.translate(new StringReader(input), 0, output);
 assertEquals(0, charsWritten);
 }

 @Test
 public void testTranslateWithInvalidNumericEntityWithInvalidCharacters() throws IOException {
 String input = "some text &amp;#%^&;more text";
 Writer output = new StringWriter();
 int charsWritten = unescaper.translate(new StringReader(input), 0, output);
 assertEquals(0, charsWritten);
 }

 @Test(expected = IOException.class)
 public void testTranslateWithNullInput() throws IOException {
 String input = null;
 Writer output = new StringWriter();
 int charsWritten = unescaper.translate(new StringReader(input), 0, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithNullOutput() throws IOException {
 String input = "some text";
 Writer output = null;
 int charsWritten = unescaper.translate(new StringReader(input), 0, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithEmptyInput() throws IOException {
 String input = "";
 Writer output = new StringWriter();
 int charsWritten = unescaper.translate(new StringReader(input), 0, output);
 assertEquals(0, charsWritten);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithNumericEntityWithoutHash() throws IOException {
 String input = "some text &amp;more text";
 Writer output = new StringWriter();
 int charsWritten = unescaper.translate(new StringReader(input), 0, output);
 assertEquals(0, charsWritten);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithNumericEntityWithMissingNumber() throws IOException {
 String input = "some text &amp;#;more text";
 Writer output = new StringWriter();
 int charsWritten = unescaper.translate(new StringReader(input), 0, output);
 assertEquals(0, charsWritten);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithNumericEntityWithMissingAmpersand() throws IOException {
 String input = "some text #160;more text";
 Writer output = new StringWriter();
 int charsWritten = unescaper.translate(new StringReader(input), 0, output);
 assertEquals(0, charsWritten);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithNumericEntityWithMissingHashAndAmpersand() throws IOException {
 String input = "some text 160more text";
 Writer output = new StringWriter();
 int charsWritten = unescaper.translate(new StringReader(input), 0, output);
 assertEquals(0, charsWritten);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithNumericEntityWithInvalidCharacterAfterHash() throws IOException {
 String input = "some text &amp;#%more text";
 Writer output = new StringWriter();
 int charsWritten = unescaper.translate(new StringReader(input), 0, output);
 assertEquals(0, charsWritten);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithNumericEntityWithInvalidCharacterBeforeHash() throws IOException {
 String input = "some text %amp;#160more text";
 Writer output = new StringWriter();
 int charsWritten = unescaper.translate(new StringReader(input), 0, output);
 assertEquals(0, charsWritten);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithNumericEntityWithNegativeNumber() throws IOException {
 String input = "some text &amp;#-160more text";
 Writer output = new StringWriter();
 int charsWritten = unescaper.translate(new StringReader(input), 0, output);
 assertEquals(0, charsWritten);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithNumericEntityWithTooLargeNumber() throws IOException {
 String input = "some text &amp;#65536more text";
 Writer output = new StringWriter();
 int charsWritten = unescaper.translate(new StringReader(input), 0, output);
 assertEquals(0, charsWritten);
 }
}