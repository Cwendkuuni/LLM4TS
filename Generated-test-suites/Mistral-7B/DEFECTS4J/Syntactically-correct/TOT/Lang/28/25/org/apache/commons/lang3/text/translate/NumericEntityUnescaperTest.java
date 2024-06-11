import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testNumericEntityWithDecimalValue() throws IOException {
 CharSequence input = "&amp;#123;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("123", out.toString());
 assertEquals(3, length);
 }

 @Test
 public void testNumericEntityWithHexValue() throws IOException {
 CharSequence input = "&amp;#x7B;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("{", out.toString());
 assertEquals(3, length);
 }

 @Test
 public void testEmptyEntity() throws IOException {
 CharSequence input = "&amp;#;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 @Test(expected = IOException.class)
 public void testEntityWithInvalidFormat() throws IOException {
 CharSequence input = "&amp;#abc;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 unescaper.translate(input, 0, out);
 fail("Expected IOException but no exception was thrown.");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testEntityWithOutOfBoundsIndex() throws IOException {
 CharSequence input = "&amp;#1234567890;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 try {
 int length = unescaper.translate(input, 12, out);
 } catch (IndexOutOfBoundsException e) {
 return;
 }
 fail("Expected IndexOutOfBoundsException but no exception was thrown.");
 }

 @Test
 public void testEntityWithNegativeIndex() throws IOException {
 CharSequence input = "&amp;#123;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 try {
 int length = unescaper.translate(input, -1, out);
 } catch (IndexOutOfBoundsException e) {
 return;
 }
 fail("Expected IndexOutOfBoundsException but no exception was thrown.");
 }

 @Test
 public void testEntityWithNonExistentCharacter() throws IOException {
 CharSequence input = "&amp;#65536;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("\uD800", out.toString());
 assertEquals(3, length);
 }

 @Test
 public void testEntityWithHighCharacterValue() throws IOException {
 CharSequence input = "&amp;#1114111;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("\uD800\uDFFF\uD800\uDFFF\uD800\uDFFF", out.toString());
 assertEquals(15, length);
 }

 @Test
 public void testEntityWithLeadingTrailingSpace() throws IOException {
 CharSequence input = " &amp;#123; ";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 1, out);
 assertEquals("123", out.toString());
 assertEquals(3, length);
 }
}