import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

 private final CharSequenceTranslator unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate() {
 final String input = "&#x1F601;&#x263A;&#10;&#13;&#x2014;&#x2713;&#x2714;&#x2718;";
 final String expectedOutput = "\uD83D\uDC01&\u0026\u003A\n\r—\u2713\u2714\u2718";

 try ( Writer writer = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void flush() throws IOException {
 // empty
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 }) {
 unescaper.translate(new String(input), 0, writer);
 String output = writer.toString();
 assertEquals(expectedOutput, output);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidEntity() {
 final String input = "&invalidEntity;";
 try {
 unescaper.translate(new String(input), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 // empty
 }

 @Override
 public void flush() throws IOException {
 // empty
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 });
 } catch (IOException e) {
 throw new AssertionError("Should throw IllegalArgumentException", e);
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testEntityOutOfBounds() {
 final String input = "&#x1F601;&#x263A;&#10&";
 try {
 unescaper.translate(new String(input), 10, new Writer() {
 @Override
 public void write(int c) throws IOException {
 // empty
 }

 @Override
 public void flush() throws IOException {
 // empty
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 });
 } catch (IOException e) {
 throw new AssertionError("Should throw IndexOutOfBoundsException", e);
 }
 }
}