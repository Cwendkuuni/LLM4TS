import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 @Test
 public void testNumericEntityUnescaper_Decimal() throws Exception {
 CharSequenceTranslator unescaper = new NumericEntityUnescaper();
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void flush() throws IOException {
 // Do nothing
 }

 @Override
 public void close() throws IOException {
 // Do nothing
 }
 };

 String input = "&amp;#123;&#160;&#45;&#171;&#8364;";
 int index = 0;

 int translatedChars = unescaper.translate(new String(input), index, out);
 assertEquals("123 \u00A0-\u0045 \u2032\u00A4", input.substring(0, index) + new String(out.getBuffer().toString()));
 assertEquals(7, translatedChars);
 index += translatedChars;
 }

 @Test
 public void testNumericEntityUnescaper_Hex() throws Exception {
 CharSequenceTranslator unescaper = new NumericEntityUnescaper();
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void flush() throws IOException {
 // Do nothing
 }

 @Override
 public void close() throws IOException {
 // Do nothing
 }
 };

 String input = "&amp;#x1F44D;";
 int index = 0;

 int translatedChars = unescaper.translate(new String(input), index, out);
 assertEquals("\uD835\uDD4D", input.substring(0, index) + new String(out.getBuffer().toString()));
 assertEquals(2, translatedChars);
 index += translatedChars;
 }

 @Test(expected = IOException.class)
 public void testNumericEntityUnescaper_InvalidDecimal() throws Exception {
 CharSequenceTranslator unescaper = new NumericEntityUnescaper();
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void flush() throws IOException {
 // Do nothing
 }

 @Override
 public void close() throws IOException {
 // Do nothing
 }
 };

 String input = "&amp;#xABC;";
 int index = 0;

 int translatedChars = unescaper.translate(new String(input), index, out);
 }

 @Test(expected = IOException.class)
 public void testNumericEntityUnescaper_InvalidHex() throws Exception {
 CharSequenceTranslator unescaper = new NumericEntityUnescaper();
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void flush() throws IOException {
 // Do nothing
 }

 @Override
 public void close() throws IOException {
 // Do nothing
 }
 };

 String input = "&amp;#xAB;";
 int index = 0;

 int translatedChars = unescaper.translate(new String(input), index, out);
 }

 @Test(expected = IOException.class)
 public void testNumericEntityUnescaper_MissingSemicolon() throws Exception {
 CharSequenceTranslator unescaper = new NumericEntityUnescaper();
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void flush() throws IOException {
 // Do nothing
 }

 @Override
 public void close() throws IOException {
 // Do nothing
 }
 };

 String input = "&amp;#123;";
 int index = 0;

 int translatedChars = unescaper.translate(new String(input), index, out);
 }

 @Test(expected = IOException.class)
 public void testNumericEntityUnescaper_MissingAmpersand() throws Exception {
 CharSequenceTranslator unescaper = new NumericEntityUnescaper();
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void flush() throws IOException {
 // Do nothing
 }

 @Override
 public void close() throws IOException {
 // Do nothing
 }
 };

 String input = "#123;";
 int index = 0;

 int translatedChars = unescaper.translate(new String(input), index, out);
 }
}