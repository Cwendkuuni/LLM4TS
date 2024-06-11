import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 /**
 * Test translating a simple numeric entity.
 */
 @Test
 public void testTranslateSimpleEntity() {
 String input = "&amp;#123;";
 String expectedOutput = "abc"; // Assuming 'a' has Unicode value 97 and 'b' has Unicode value 98, so '#123;' translates to 'abc'
 try {
 CharSequence cs = new StringSelection(input);
 Writer out = new StringWriter();
 int length = unescaper.translate(cs, 0, out);
 assertEquals(expectedOutput.length(), out.getBuffer().length());
 assertEquals(expectedOutput, out.getBuffer().toString());
 } catch (IOException e) {
 // Should not happen since StringWriter is not a Writer but a StringBuffer
 }
 }

 /**
 * Test translating an entity with an optional semicolon.
 */
 @Test
 public void testTranslateEntityWithOptionalSemicolon() {
 String input = "&amp;#123";
 String expectedOutput = "abc";
 try {
 CharSequence cs = new StringSelection(input);
 Writer out = new StringWriter();
 int length = unescaper.translate(cs, 0, out);
 assertEquals(expectedOutput.length(), out.getBuffer().length());
 assertEquals(expectedOutput, out.getBuffer().toString());
 } catch (IOException e) {
 // Should not happen since StringWriter is not a Writer but a StringBuffer
 }
 }

 /**
 * Test translating an invalid entity.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testTranslateInvalidEntity() {
 String input = "&amp;#InvalidEntity;";
 String expectedErrorMessage = "FAIL: InvalidEntity[" + 10 + ":" + 13 + "]";
 String output = "";
 try {
 CharSequence cs = new StringSelection(input);
 Writer out = new StringWriter();
 int length = unescaper.translate(cs, 0, out);
 } catch (IOException e) {
 // StringWriter is not a Writer but a StringBuffer, so we don't care about this exception
 }
 assertEquals(expectedErrorMessage, System.err.getError());
 }

 /**
 * Test translating an entity with a character that needs more than one char.
 */
 @Test
 public void testTranslateHighUnicodeEntity() {
 String input = "&amp;#65533;";
 String expectedOutput = "\uD800";
 try {
 CharSequence cs = new StringSelection(input);
 Writer out = new StringWriter();
 int length = unescaper.translate(cs, 0, out);
 assertEquals(expectedOutput.length(), out.getBuffer().length());
 assertEquals(expectedOutput, out.getBuffer().toString());
 } catch (IOException e) {
 // Should not happen since StringWriter is not a Writer but a StringBuffer
 }
 }

 /**
 * Test translating an entity with a character that needs two chars.
 */
 @Test
 public void testTranslateEntityWithTwoChars() {
 String input = "&amp;#110001;";
 String expectedOutput = "\uD800\uDC00";
 try {
 CharSequence cs = new StringSelection(input);
 Writer out = new StringWriter();
 int length = unescaper.translate(cs, 0, out);
 assertEquals(expectedOutput.length(), out.getBuffer().length());
 assertEquals(expectedOutput, out.getBuffer().toString());
 } catch (IOException e) {
 // Should not happen since StringWriter is not a Writer but a StringBuffer
 }
 }
}