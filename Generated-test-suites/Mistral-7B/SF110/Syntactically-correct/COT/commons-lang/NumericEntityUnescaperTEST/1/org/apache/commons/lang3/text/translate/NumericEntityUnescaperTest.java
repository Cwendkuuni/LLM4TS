import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper;

 @Test
 public void testDefaultConstructor() {
 unescaper = new NumericEntityUnescaper();
 }

 @Test
 public void testConstructorWithOptions() {
 OPTION[] options = { OPTION.semiColonRequired, OPTION.errorIfNoSemiColon };
 unescaper = new NumericEntityUnescaper(options);
 }

 @Test
 public void testIsSet() {
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 assertFalse(unescaper.isSet(OPTION.semiColonOptional));
 }

 @Test
 public void testTranslate_NoNumericEntity() throws IOException {
 String input = "This is a test string.";
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals(input.length(), length);
 }

 @Test
 public void testTranslate_ValidNumericEntity() throws IOException {
 String input = "This text contains a numeric entity: &amp;#128;";
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("This text contains a numeric entity: \u0080", out.toString());
 }

 @Test
 public void testTranslate_InvalidNumericEntity_NoSemiColon() throws IOException {
 String input = "This text contains an invalid numeric entity: &amp;#128";
 Writer out = new StringWriter();
 try {
 unescaper.translate(new StringReader(input), 0, out);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testTranslate_InvalidNumericEntity_Overflow() throws IOException {
 String input = "This text contains an invalid numeric entity: &amp;#65536";
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals(input.length() + 3, length);
 // Checking the output for the character at index 1 and 2 is not possible in JUnit
 // They are the high byte and low byte of the Unicode character, respectively.
 }

 @Test
 public void testTranslate_ValidNumericEntity_Hex() throws IOException {
 String input = "This text contains a hexadecimal numeric entity: &amp;#x41;";
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("This text contains a hexadecimal numeric entity: A", out.toString());
 }

 @Test
 public void testTranslate_ValidNumericEntity_WithoutSemiColon() throws IOException {
 String input = "This text contains a numeric entity: &amp;#128";
 Writer out = new StringWriter();
 int length = unescaper.newInstance().translate(new StringReader(input), 0, out);
 assertEquals(input.length() + 2, length);
 }

 @Test
 public void testTranslate_ValidNumericEntity_WithSemiColon() throws IOException {
 String input = "This text contains a numeric entity: &amp;#128;";
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("This text contains a numeric entity: \u0080", out.toString());
 }

 @Test
 public void testTranslate_ValidNumericEntity_ErrorIfNoSemiColon() throws IOException {
 String input = "This text contains a numeric entity: &amp;#128";
 unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 Writer out = new StringWriter();
 try {
 unescaper.translate(new StringReader(input), 0, out);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

}