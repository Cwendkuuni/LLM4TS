import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateSimple() {
 CharSequence input = "&num;";
 Writer output = new StringWriter();
 int length = unescaper.translate(input, 0, output);
 assertEquals("Expected 1 character to be written: ", '\u006E', output.getBuffer().charAt(0));
 assertEquals("Expected length to be 1 + 1", 2, length);
 }

 @Test
 public void testTranslateDecimal() {
 CharSequence input = "&#10;";
 Writer output = new StringWriter();
 int length = unescaper.translate(input, 0, output);
 assertEquals("Expected ASCII CR to be written: ", '\r', output.getBuffer().charAt(0));
 assertEquals("Expected length to be 1 + 1", 2, length);
 }

 @Test
 public void testTranslateHex() {
 CharSequence input = "&#x1F";
 Writer output = new StringWriter();
 int length = unescaper.translate(input, 0, output);
 assertEquals("Expected Unicode REPLACEMENT CHARACTER to be written: ", '\uFFFD', output.getBuffer().charAt(0));
 assertEquals("Expected length to be 1 + 3", 4, length);
 }

 @Test
 public void testTranslateHexLong() {
 CharSequence input = "&#x1F4A9;";
 Writer output = new StringWriter();
 int length = unescaper.translate(input, 0, output);
 assertEquals("Expected Unicode HEAVY DASHED CIRCLE to be written: ", '\u25CF', output.getBuffer().charAt(0));
 assertEquals("Expected length to be 1 + 6", 7, length);
 }

 @Test
 public void testTranslateMultipleEntities() {
 CharSequence input = "&num; &#x1F &#10 &#x1F4A9;";
 Writer output = new StringWriter();
 int length = 0;
 for (int i = 0; i < input.length(); i++) {
 length += unescaper.translate(input, i, output);
 }
 assertEquals("Expected 4 characters to be written: ", '\u006E', '\u0025', '\u00CF', '\u25CF');
 assertEquals("Expected length to be 12", 12, length);
 }

 @Test
 public void testTranslateEntityOutsideOfInput() {
 CharSequence input = "&num;";
 Writer output = new StringWriter();
 int length = unescaper.translate(input, input.length(), output);
 assertEquals("Expected 0 to be written: ", 0, length);
 }

 @Test
 public void testTranslateInvalidDecimal() {
 CharSequence input = "&num; &#X10;";
 Writer output = new StringWriter();
 int length = 0;
 for (int i = 0; i < input.length(); i++) {
 length += unescaper.translate(input, i, output);
 }
 assertEquals("Expected the input to be unchanged: ", "&num; &#X10;", new String(input.toString()));
 }

 @Test
 public void testTranslateInvalidHex() {
 CharSequence input = "&num; &#x1Fx;";
 Writer output = new StringWriter();
 int length = 0;
 for (int i = 0; i < input.length(); i++) {
 length += unescaper.translate(input, i, output);
 }
 assertEquals("Expected the input to be unchanged: ", "&num; &#x1Fx;", new String(input.toString()));
 }

 @Test
 public void testTranslateInvalidFormat() {
 CharSequence input = "&num; &#x1F %;";
 Writer output = new StringWriter();
 int length = 0;
 for (int i = 0; i < input.length(); i++) {
 length += unescaper.translate(input, i, output);
 }
 assertEquals("Expected the input to be unchanged: ", "&num; &#x1F %;", new String(input.toString()));
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidDecimalFormat() {
 CharSequence input = "&num; &#10z;";
 Writer output = new StringWriter();
 int length = unescaper.translate(input, 0, output);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidHexFormat() {
 CharSequence input = "&num; &#x1Fz;";
 Writer output = new StringWriter();
 int length = unescaper.translate(input, 0, output);
 }
}