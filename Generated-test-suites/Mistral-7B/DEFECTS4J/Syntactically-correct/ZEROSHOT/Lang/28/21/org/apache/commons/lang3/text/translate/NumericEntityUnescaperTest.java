import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateHex() throws IOException {
 char[] input = {'&', '#', 'x', '41', ';'};
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.write(ch);
 assertEquals((char) 0xC1, ch); // Assert the translated character
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 };

 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 int length = translator.translate(new String(input), 0, out);
 assertEquals(3, length); // The length of the translated string
 }

 @Test
 public void testTranslateDecimal() throws IOException {
 char[] input = {'&', '#', '1', '3', ';'};
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.write(ch);
 assertEquals((char) 13, ch); // Assert the translated character
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 };

 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 int length = translator.translate(new String(input), 0, out);
 assertEquals(3, length); // The length of the translated string
 }

 @Test(expected = IOException.class)
 public void testTranslateInvalidEntity() throws IOException {
 char[] input = {'&', '#', 'x', 'a', ';'};
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 // empty
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 };

 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 int length = translator.translate(new String(input), 0, out);
 }

 @Test(expected = IOException.class)
 public void testTranslateMissingSemicolon() throws IOException {
 char[] input = {'&', '#', '41'};
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 // empty
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 };

 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 int length = translator.translate(new String(input), 0, out);
 }
}