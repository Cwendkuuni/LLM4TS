import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testNumericEntityUnescaper_DecimalEntity() throws IOException {
 CharSequence input = "&amp;123;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 Writer out = new StringWriter();
 translator.translate(input, 0, out);
 String output = out.toString();
 assertEquals("123", output);
 }

 @Test
 public void testNumericEntityUnescaper_HexadecimalEntity() throws IOException {
 CharSequence input = "&amp;#x7F;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 Writer out = new StringWriter();
 translator.translate(input, 0, out);
 String output = out.toString();
 assertEquals("127", output);
 }

 @Test
 public void testNumericEntityUnescaper_InvalidDecimalEntity() throws IOException {
 CharSequence input = "&amp;#abc;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 Writer out = new StringWriter();
 int length = translator.translate(input, 0, out);
 assertEquals(0, length);
 }

 @Test
 public void testNumericEntityUnescaper_InvalidHexadecimalEntity() throws IOException {
 CharSequence input = "&amp;#xCDabc;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 Writer out = new StringWriter();
 int length = translator.translate(input, 0, out);
 assertEquals(0, length);
 }

 @Test
 public void testNumericEntityUnescaper_EmptyInput() throws IOException {
 CharSequence input = "";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 Writer out = new StringWriter();
 int length = translator.translate(input, 0, out);
 assertEquals(0, length);
 }

 @Test
 public void testNumericEntityUnescaper_InputWithMultipleEntities() throws IOException {
 CharSequence input = "&amp;#123; &amp;#x7F; &amp;#abcd;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 Writer out = new StringWriter();
 int length = translator.translate(input, 0, out);
 String output = out.toString();
 assertEquals("123 127 abcd", output);
 }
}