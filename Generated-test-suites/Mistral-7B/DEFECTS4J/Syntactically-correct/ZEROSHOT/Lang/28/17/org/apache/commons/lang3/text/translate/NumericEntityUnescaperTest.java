import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateSimpleNumericEntity() throws IOException {
 CharSequence input = "&amp;";
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void flush() throws IOException {
 // do nothing
 }

 @Override
 public void close() throws IOException {
 // do nothing
 }
 };

 int length = unescaper.translate(input, 0, out);
 assertEquals("&", input.subSequence(0, 1).toString());
 assertEquals("A", String.valueOf((char) (0x0041)));
 assertEquals(3, length);
 }

 @Test
 public void testTranslateSimpleHexNumericEntity() throws IOException {
 CharSequence input = "&#[x41];";
 Writer out = new Writer() {
 // ... same as above
 };

 int length = unescaper.translate(input, 0, out);
 assertEquals("&", input.subSequence(0, 1).toString());
 assertEquals("A", String.valueOf((char) (0x0041)));
 assertEquals(3, length);
 }

 @Test
 public void testTranslateComplexNumericEntity() throws IOException {
 CharSequence input = "&#[x65];&#x6C;&#x6C;&#x6F;&#x20;(&#x3B;#x26;&#x20;)&#x3B;";
 Writer out = new Writer() {
 // ... same as above
 };

 int length = unescaper.translate(input, 0, out);
 assertEquals("abc def (&amp;)&amp;", input.toString());
 assertEquals("abc def (amp;&amp;)", out.toString());
 assertEquals(21, length);
 }

 @Test(expected = IOException.class)
 public void testTranslateMalformedNumericEntity() throws IOException {
 CharSequence input = "&amp;&#";
 Writer out = new Writer() {
 // ... same as above
 };
 unescaper.translate(input, 0, out);
 fail("Expected IOException: Numeric entities must have an equal number of '#' and ';' characters.");
 }

 @Test(expected = IOException.class)
 public void testTranslateHexNumericEntityWithInvalidHex() throws IOException {
 CharSequence input = "&#[xz];";
 Writer out = new Writer() {
 // ... same as above
 };
 unescaper.translate(input, 0, out);
 fail("Expected IOException: Invalid hexadecimal character 'z'.");
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidNumericEntity() throws IOException {
 CharSequence input = "&invalid;";
 Writer out = new Writer() {
 // ... same as above
 };
 unescaper.translate(input, 0, out);
 fail("Expected NumberFormatException: For input string: \"invalid\"");
 }
}