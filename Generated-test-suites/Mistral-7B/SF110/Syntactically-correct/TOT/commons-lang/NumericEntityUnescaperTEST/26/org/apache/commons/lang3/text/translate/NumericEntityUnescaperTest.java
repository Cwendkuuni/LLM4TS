import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateNumericEntityWithSemiColon() throws IOException {
 String input = "&amp;#x1F44D;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("&#x1F44D;", out.toString());
 assertEquals(7, length);
 }

 @Test
 public void testTranslateNumericEntityWithoutSemiColon() throws IOException {
 String input = "&amp;#x1F44D";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertFalse(out.toString().endsWith(";"));
 assertEquals(6, length);
 }

 @Test
 public void testTranslateNumericEntityWithErrorIfNoSemiColon() throws IOException {
 String input = "&amp;#x1F44D";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 assertThrows(IllegalArgumentException.class, () -> unescaper.translate(new StringReader(input), 0, out));
 }

 @Test
 public void testTranslateNumericEntityWithHexValue() throws IOException {
 String input = "&amp;#x0065";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("e", out.toString());
 assertEquals(3, length);
 }

 @Test
 public void testTranslateNumericEntityWithLargeValue() throws IOException {
 String input = "&amp;#123456";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(new StringReader(input), 0, out);
 char[] chars = Character.toChars(0x1234);
 out.write(chars[0]);
 out.write(chars[1]);
 out.write(chars[2]);
 out.write(chars[3]);
 assertEquals("ABCD", out.toString());
 assertEquals(4, length);
 }

 @Test
 public void testTranslateNumericEntityWithInvalidValue() throws IOException {
 String input = "&amp;#1234567";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 @Test
 public void testIsSet() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
 assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
 assertFalse(unescaper.isSet(OPTION.semiColonOptional));
 }
}