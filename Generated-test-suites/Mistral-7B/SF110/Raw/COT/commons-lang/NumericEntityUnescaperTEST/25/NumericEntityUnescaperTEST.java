import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private CharSequence testInput;
 private Writer testOutput = new StringWriter();
 private NumericEntityUnescaper unescaper;

 @Test
 public void testNumericEntityUnescaperConstructorNoOptions() {
 unescaper = new NumericEntityUnescaper();
 }

 @Test
 public void testNumericEntityUnescaperConstructorWithOptions() {
 OPTION[] options = {OPTION.semiColonRequired};
 unescaper = new NumericEntityUnescaper(options);
 }

 @Test
 public void testIsSetOption() {
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 unescaper = new NumericEntityUnescaper();
 assertFalse(unescaper.isSet(OPTION.semiColonRequired));
 }

 @Test
 public void testTranslateMethodBasicCase() throws IOException {
 testInput = "This is a &num;123 text.";
 unescaper = new NumericEntityUnescaper();
 unescaper.translate(testInput, 0, testOutput);
 assertEquals("This is a 123 text.", testOutput.getBuffer().toString());
 }

 @Test
 public void testTranslateMethodHexCase() throws IOException {
 testInput = "This is a &#x1234 text.";
 unescaper = new NumericEntityUnescaper();
 unescaper.translate(testInput, 0, testOutput);
 assertEquals("This is a ५६७८ text.", testOutput.getBuffer().toString()); // Unicode representation of hex value 1234
 }

 @Test
 public void testTranslateMethodErrorIfNoSemiColon() throws IOException {
 testInput = "This is a &num;123";
 unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 try {
 unescaper.translate(testInput, 0, testOutput);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertTrue(true);
 }
 }

 @Test
 public void testTranslateMethodSemiColonRequired() throws IOException {
 testInput = "This is a &num;123";
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 try {
 unescaper.translate(testInput, 0, testOutput);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertTrue(true);
 }

 testInput = "This is a &num;123;";
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 unescaper.translate(testInput, 0, testOutput);
 assertEquals("This is a 123; text.", testOutput.getBuffer().toString());
 }

 @Test
 public void testTranslateMethodLargeNumber() throws IOException {
 testInput = "This is a &num;65536 text.";
 unescaper = new NumericEntityUnescaper();
 unescaper.translate(testInput, 0, testOutput);
 assertEquals("This is a ƒ text.", testOutput.getBuffer().toString()); // Unicode representation of hex value 65536
 }

 @Test
 public void testTranslateMethodInvalidEntity() throws IOException {
 testInput = "This is a &num;invalid text.";
 unescaper = new NumericEntityUnescaper();
 unescaper.translate(testInput, 0, testOutput);
 assertEquals("This is a &num;invalid text.", testOutput.getBuffer().toString());
 }

 @Test
 public void testTranslateMethodInvalidNumericCharacter() throws IOException {
 testInput = "This is a &num;ab text.";
 unescaper = new NumericEntityUnescaper();
 unescaper.translate(testInput, 0, testOutput);
 assertEquals("This is a &num;ab text.", testOutput.getBuffer().toString());
 }

 @Test
 public void testTranslateMethodInvalidHexCharacter() throws IOException {
 testInput = "This is a &#xab text.";
 unescaper = new NumericEntityUnescaper();
 unescaper.translate(testInput, 0, testOutput);
 assertEquals("This is a &#xab text.", testOutput.getBuffer().toString());
 }

 @Test
 public void testTranslateMethodEmpty() throws IOException {
 testInput = "";
 unescaper = new NumericEntityUnescaper();
 unescaper.translate(testInput, 0, testOutput);
 assertEquals("", testOutput.getBuffer().toString());
 }

}