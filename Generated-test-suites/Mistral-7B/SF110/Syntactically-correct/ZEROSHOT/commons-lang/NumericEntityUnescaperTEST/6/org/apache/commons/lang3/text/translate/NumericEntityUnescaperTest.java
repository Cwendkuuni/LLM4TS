import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testNumericEntityUnescaperWithDefaultOptions() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 CharSequence input = "&num;abc";
 Writer outputWriter = new StringWriter();
 int length = unescaper.translate(input, 0, outputWriter);
 assertEquals("abc", outputWriter.getBuffer().toString());
 assertEquals(3, length);
 }

 @Test
 public void testNumericEntityUnescaperWithSemiColonRequired() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 CharSequence input = "&num;123";
 Writer outputWriter = new StringWriter();
 int length = unescaper.translate(input, 0, outputWriter);
 assertEquals("123", outputWriter.getBuffer().toString());
 assertEquals(3, length);

 input = "&num;123";
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 outputWriter = new StringWriter();
 length = unescaper.translate(input, 0, outputWriter);
 assertNotEquals("", outputWriter.getBuffer().toString()); // Ensure an empty string is not returned

 input = "&num;123";
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 outputWriter = new StringWriter();
 length = unescaper.translate(input, 0, outputWriter);
 assertThrows(IllegalArgumentException.class, () -> unescaper.translate(input, 0, outputWriter));
 }

 @Test
 public void testNumericEntityUnescaperWithSemiColonOptional() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 CharSequence input = "&num;123";
 Writer outputWriter = new StringWriter();
 int length = unescaper.translate(input, 0, outputWriter);
 assertEquals("123", outputWriter.getBuffer().toString());
 assertEquals(3, length);

 input = "&num;123";
 outputWriter = new StringWriter();
 unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 length = unescaper.translate(input, 0, outputWriter);
 assertEquals("", outputWriter.getBuffer().toString()); // Semi-colon is optional
 }

 @Test
 public void testNumericEntityUnescaperWithErrorIfNoSemiColon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 CharSequence input = "&num;123";
 Writer outputWriter = new StringWriter();
 int length = unescaper.translate(input, 0, outputWriter);
 assertEquals("", outputWriter.getBuffer().toString());
 assertThrows(IllegalArgumentException.class, () -> unescaper.translate(input, 0, outputWriter));
 }

 @Test
 public void testNumericEntityUnescaperHexValue() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 CharSequence input = "&num;#x0123";
 Writer outputWriter = new StringWriter();
 int length = unescaper.translate(input, 0, outputWriter);
 assertEquals("\u0123", outputWriter.getBuffer().toString());
 assertEquals(5, length);
 }

 @Test
 public void testNumericEntityUnescaperInvalidHexValue() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 CharSequence input = "&num;#xz0123";
 Writer outputWriter = new StringWriter();
 int length = unescaper.translate(input, 0, outputWriter);
 assertEquals("", outputWriter.getBuffer().toString());
 assertThrows(IllegalArgumentException.class, () -> unescaper.translate(input, 0, outputWriter));
 }

 @Test
 public void testNumericEntityUnescaperInvalidDecimalValue() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 CharSequence input = "&num;#0123a";
 Writer outputWriter = new StringWriter();
 int length = unescaper.translate(input, 0, outputWriter);
 assertEquals("", outputWriter.getBuffer().toString());
 assertThrows(IllegalArgumentException.class, () -> unescaper.translate(input, 0, outputWriter));
 }
}