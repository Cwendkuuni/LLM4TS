import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testNumericEntityUnescaper_Decimal() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp;#123;#45;#67;");

 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);

 String expected = "\u007B\u0041\u0045\u0053";
 String actual = out.toString().substring(0, length);
 assertEquals(expected, actual);
 }

 @Test
 public void testNumericEntityUnescaper_Hex() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp;#x61;#x34;#x42;");

 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);

 String expected = "\u0061\u0034\u0042";
 String actual = out.toString().substring(0, length);
 assertEquals(expected, actual);
 }

 @Test
 public void testNumericEntityUnescaper_InvalidDecimal() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp;#abcd;");

 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);

 assertEquals(0, length);
 assertEquals("FAIL: &amp;#abcd;[0][5]", System.err.getError().getMessage());
 }

 @Test
 public void testNumericEntityUnescaper_InvalidHex() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp;#xzx;");

 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);

 assertEquals(0, length);
 assertEquals("FAIL: &amp;#xzx;[0][2]", System.err.getError().getMessage());
 }

 @Test
 public void testNumericEntityUnescaper_MissingSemicolon() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp;#123;#x45");

 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);

 assertEquals(0, length);
 assertEquals("FAIL: &amp;#123;#x45;[0][5]", System.err.getError().getMessage());
 }

 @Test
 public void testNumericEntityUnescaper_LargeEntity() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp;#100000;");

 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);

 char[] expectedChars = Character.toChars(0x100000);
 String expected = new String(expectedChars);
 String actual = out.toString().substring(0, length);
 assertEquals(expected, actual);
 }
}