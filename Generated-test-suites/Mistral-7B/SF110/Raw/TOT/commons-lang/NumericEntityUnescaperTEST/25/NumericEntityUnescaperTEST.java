import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslate_BasicNumericEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 String input = "&amp;#160;"; // Unicode space entity
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("&nbsp;", out.toString());
 assertEquals(5, length);
 }

 @Test
 public void testTranslate_HexNumericEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 String input = "&amp;#x1F44D;"; // Unicode heart symbol entity
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("♥", out.toString());
 assertEquals(3, length);
 }

 @Test
 public void testTranslate_LargeNumericEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 String input = "&amp;#65536;"; // Unicode high surrogate character
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 char[] expected = Character.toChars(0x10000);
 assertTrue(Arrays.equals(expected, out.toString().toCharArray()));
 assertEquals(4, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_NoSemiColon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 String input = "&amp;#160;"; // Unicode space entity
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 fail("Expected IllegalArgumentException with no semi-colon at the end");
 }

 @Test
 public void testTranslate_InvalidNumericEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 String input = "&amp;#invalid;"; // Invalid numeric entity
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals(0, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_InvalidNumericEntityWithoutSemiColon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 String input = "&amp;#invalid;"; // Invalid numeric entity
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 fail("Expected IllegalArgumentException with errorIfNoSemiColon option");
 }

 @Test
 public void testTranslate_EmptyInput() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 String input = "";
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_ValidInputWithEmptyOptions() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 String input = "&amp;#160;"; // Unicode space entity
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("&nbsp;", out.toString());
 assertEquals(5, length);
 }

 @Test
 public void testTranslate_ValidInputWithAllOptions() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
 String input = "&amp;#160;"; // Unicode space entity
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("&nbsp;", out.toString());
 assertEquals(5, length);
 }
}