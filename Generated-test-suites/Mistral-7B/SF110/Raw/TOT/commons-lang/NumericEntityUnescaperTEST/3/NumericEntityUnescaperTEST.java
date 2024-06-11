import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateWithValidNumericEntity() throws Exception {
 String input = "&num;This text has a &#x415; character.";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 unescaper.translate(new StringReader(input), 0, out);
 String expected = "This text has a Æ character.";
 assertEquals(expected, out.toString());
 }

 @Test
 public void testTranslateWithValidDecimalNumericEntity() throws Exception {
 String input = "&num;This text has a &#123; character.";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 unescaper.translate(new StringReader(input), 0, out);
 String expected = "This text has a { character.";
 assertEquals(expected, out.toString());
 }

 @Test
 public void testTranslateWithValidHexNumericEntity() throws Exception {
 String input = "&num;This text has a &#x1F44D; character.";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 unescaper.translate(new StringReader(input), 0, out);
 String expected = "This text has a 🔝 character.";
 assertEquals(expected, out.toString());
 }

 @Test
 public void testTranslateWithMissingSemiColon() throws Exception {
 String input = "&num;This text has a &#123; character.";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 StringWriter out = new StringWriter();
 try {
 unescaper.translate(new StringReader(input), 0, out);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testTranslateWithInvalidNumericEntity() throws Exception {
 String input = "&num;This text has a &#xinvalid; character.";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals(0, length);
 }

 @Test
 public void testTranslateWithEmptyInput() throws Exception {
 String input = "";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals(0, length);
 }

 @Test
 public void testTranslateWithNullInput() throws Exception {
 String input = null;
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 try {
 unescaper.translate(new StringReader(input), 0, out);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testTranslateWithNullWriter() throws Exception {
 String input = "&num;This text has a &#123; character.";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 try {
 unescaper.translate(new StringReader(input), 0, null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testIsSetWithValidOption() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 }

 @Test
 public void testIsSetWithInvalidOption() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 assertFalse(unescaper.isSet(OPTION.semiColonRequired));
 }
}