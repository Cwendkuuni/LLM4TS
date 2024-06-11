import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate_Basic() throws Exception {
 CharSequence input = "&amp;&#32;&#65;&#128;&#160;&#128;&#65;&#33;&#163;";
 Writer outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void write(char[] c, int off, int len) throws IOException {
 for (int i = off; i < off + len; i++) {
 System.out.print(c[i]);
 }
 }

 @Override
 public void flush() throws IOException {
 // empty
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 };

 int outputLength = unescaper.translate(input, 0, outWriter);
 assertEquals("&amp; \u0020 \u0041 \u0080 \u00A0 \u0080 \u0041 \u0023 \u00A3", new String(outWriter.getBuffer().toString().toCharArray(), 0, outputLength));
 }

 @Test
 public void testTranslate_Hex() throws Exception {
 CharSequence input = "&amp;&#x41;";
 Writer outWriter = new Writer() {
 // same as above
 };

 int outputLength = unescaper.translate(input, 0, outWriter);
 assertEquals("&amp;\u0041", new String(outWriter.getBuffer().toString().toCharArray(), 0, outputLength));
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidHex() throws Exception {
 CharSequence input = "&amp;&#xfx;";
 Writer outWriter = new Writer() {
 // same as above
 };

 unescaper.translate(input, 0, outWriter);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidNumber() throws Exception {
 CharSequence input = "&amp;&#abc;";
 Writer outWriter = new Writer() {
 // same as above
 };

 unescaper.translate(input, 0, outWriter);
 }

 @Test
 public void testTranslate_BigEntity() throws Exception {
 CharSequence input = "&amp;&#x10FFFF;";
 Writer outWriter = new Writer() {
 // same as above
 };

 int outputLength = unescaper.translate(input, 0, outWriter);
 assertEquals("&amp;\uD7FF", new String(outWriter.getBuffer().toString().toCharArray(), 0, outputLength));
 }

 @Test
 public void testTranslate_ValidSequenceButNoEntity() throws Exception {
 CharSequence input = "&amp;abcd";
 Writer outWriter = new Writer() {
 // same as above
 };

 int outputLength = unescaper.translate(input, 0, outWriter);
 assertEquals("&amp;abcd", new String(outWriter.getBuffer().toString().toCharArray(), 0, outputLength));
 }

 @Test
 public void testTranslate_LongSequence() throws Exception {
 CharSequence input = "&amp;aaaabbbbccccccddddddddddddddddddddddddddddd";
 Writer outWriter = new Writer() {
 // same as above
 };

 int outputLength = unescaper.translate(input, 0, outWriter);
 assertEquals("&amp;aaaabbbbccccccccddddddddddddddddddddddddddddd", new String(outWriter.getBuffer().toString().toCharArray(), 0, outputLength));
 }

 @Test
 public void testTranslate_MixedEntities() throws Exception {
 CharSequence input = "&amp;&#32;&#65;&#x41;&#128;&#160;&#128;&#65;&#33;&#x10FFFF;";
 Writer outWriter = new Writer() {
 // same as above
 };

 int outputLength = unescaper.translate(input, 0, outWriter);
 assertEquals("&amp;\u0020 \u0041 \u00A1\uD7FF", new String(outWriter.getBuffer().toString().toCharArray(), 0, outputLength));
 }
}