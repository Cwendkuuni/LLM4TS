import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 /**
 * Test translating valid numeric entities with semicolon.
 */
 @Test
 public void testValidNumericEntitiesWithSemicolon() {
 String input = "&amp;#x1F601; &amp;#x00A9; &amp;#x1F304; &amp;#032; &amp;#xFFFF;";
 String expectedOutput = "🤓 © 💔 ";
 try (Writer out = new java.io.StringWriter()) {
 unescaper.translate(new StringReader(input), 0, out, new StringWriter());
 String output = out.toString();
 assertEquals(expectedOutput, output);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException encountered during testing.");
 }
 }

 /**
 * Test translating valid numeric entities without semicolon.
 */
 @Test
 public void testValidNumericEntitiesWithoutSemicolon() {
 String input = "&amp;#x1F601 &amp;#x00A9 &amp;#x1F304 &amp;#032 &amp;#xFFFF";
 String expectedOutput = "🤓 © 💔 ";
 try (Writer out = new java.io.StringWriter()) {
 unescaper.translate(new StringReader(input), 0, out, new StringWriter());
 String output = out.toString();
 assertEquals(expectedOutput, output);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException encountered during testing.");
 }
 }

 /**
 * Test translating invalid numeric entities with semicolon.
 */
 @Test
 public void testInvalidNumericEntitiesWithSemicolon() {
 String input = "&amp;#xABCD;";
 try {
 unescaper.translate(new StringReader(input), 0, new java.io.StringWriter(), new java.io.StringWriter());
 fail("Expected NumberFormatException.");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 /**
 * Test translating invalid numeric entities without semicolon.
 */
 @Test
 public void testInvalidNumericEntitiesWithoutSemicolon() {
 String input = "&amp;#xABCD &amp;#12345";
 try {
 unescaper.translate(new StringReader(input), 0, new java.io.StringWriter(), new java.io.StringWriter());
 fail("Expected NumberFormatException.");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 /**
 * Test translating empty string.
 */
 @Test
 public void testEmptyString() {
 String input = "";
 String expectedOutput = "";
 try (Writer out = new java.io.StringWriter()) {
 unescaper.translate(new StringReader(input), 0, out, new StringWriter());
 String output = out.toString();
 assertEquals(expectedOutput, output);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException encountered during testing.");
 }
 }

 /**
 * Test translating null input.
 */
 @Test (expected = NullPointerException.class)
 public void testNullInput() {
 unescaper.translate(null, 0, new java.io.StringWriter(), new java.io.StringWriter());
 }
}