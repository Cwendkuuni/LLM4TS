import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateXHex() throws IOException {
 CharSequence input = "&xa;&xb;&x1f;&x41;";
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 assertEquals(Integer.toHexString(0x0a).toCharArray(), new Character[]{'&', 'x', 'a'});
 assertEquals(Integer.toHexString(0x0b).toCharArray(), new Character[]{'&', 'x', 'b'});
 assertEquals(Integer.toHexString(0x1f).toCharArray(), new Character[]{'&', '#', 'x', '1', 'f'});
 assertEquals(Character.toChars(0x41), new Character[]{'&', '#', 'x', '4', '1'});
 }
 };
 unescaper.translate(input, 0, out);
 }

 @Test
 public void testTranslateXDec() throws IOException {
 CharSequence input = "&xa;&xb;&x1f;&x41;";
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 assertEquals(new Character[]{'&', 'x', 'a'}, new Object[]{"&xa".toCharArray()});
 assertEquals(new Character[]{'&', 'x', 'b'}, new Object[]{"&xb".toCharArray()});
 assertEquals(Character.toChars(0x1f), new Character[]{'&', '#', 'x', '1', 'f'});
 assertEquals(Character.toChars(0x41), new Character[]{'&', '#', 'x', '4', '1'});
 }
 };
 unescaper.translate(input, 0, out);
 }

 @Test
 public void testTranslateDecimal() throws IOException {
 CharSequence input = "&#10;&#11;&#123;&#124;";
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 assertEquals(new Integer(10).toString().toCharArray(), new Character[]{'&', '#', '1', '0'});
 assertEquals(new Integer(11).toString().toCharArray(), new Character[]{'&', '#', '1', '1'});
 assertEquals(Character.toChars(123), new Character[]{'&', '#', '1', '2', '3'});
 assertEquals(Character.toChars(124), new Character[]{'&', '#', '1', '2', '4'});
 }
 };
 unescaper.translate(input, 0, out);
 }

 @Test
 public void testTranslateDecimalWithLeadingHash() throws IOException {
 CharSequence input = "&#x10;&#x11;&#x123;&#x124;";
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 assertEquals(new Character[]{'&', '#', 'x', '1', '0'}, new Object[]{"&#{x}10".toCharArray()});
 assertEquals(new Character[]{'&', '#', 'x', '1', '1'}, new Object[]{"&#{x}11".toCharArray()});
 assertEquals(Character.toChars(123), new Character[]{'&', '#', 'x', '1', '2', '3'});
 assertEquals(Character.toChars(124), new Character[]{'&', '#', 'x', '1', '2', '4'});
 }
 };
 unescaper.translate(input, 0, out);
 }

 @Test
 public void testTranslateEmptyEntity() throws IOException {
 CharSequence input = "&e;";
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 assertEquals(new Character[0], new Object[0]);
 }
 };
 unescaper.translate(input, 0, out);
 }

 @Test (expected = IOException.class)
 public void testTranslateInvalidEntity() throws IOException {
 CharSequence input = "&invalid;";
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }
 };
 unescaper.translate(input, 0, out);
 }

 @Test
 public void testTranslateEmptyInput() throws IOException {
 CharSequence input = "";
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 assertEquals(new Character[0], new Object[0]);
 }
 };
 unescaper.translate(input, 0, out);
 }
}