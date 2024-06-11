import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class NumericEntityUnescaperTest {

 private static final NumericEntityUnescaper UNESCAPER = new NumericEntityUnescaper();

 @Test
 public void testTranslateNumericEntities() {
 char[] expected = {'&', '#', 'x', '3', '1', '5', ';', ' ', 'A', '#', 'x', '4', '1', '7', ';'};
 CharSequence input = "&#x315;&#x417;";
 int length = UNESCAPER.translate(input, 0, null);
 char[] output = new char[length];
 try {
 UNESCAPER.translate(input, 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 output[UNESCAPER.index++] = (char) c;
 }

 @Override
 public void flush() throws IOException {
 // Flush is not used in this test
 }

 int index = 0;
 });
 } catch (IOException e) {
 e.printStackTrace();
 }

 assertEquals(expected, output);
 }

 @Test
 public void testTranslateInvalidNumericEntity() {
 char[] input = {"&", '#', 'x', 'a'};
 int length = UNESCAPER.translate(new String(input), 0, null);
 assertEquals(0, length);
 }

 @Test
 public void testTranslateNumericEntityWithSemicolonMissing() {
 char[] input = {"&", '#', 'x', '3', '1', '5'};
 int length = UNESCAPER.translate(new String(input), 0, null);
 assertEquals(0, length);
 }

 @Test
 public void testTranslateNumericEntityWithSemicolon() {
 char[] expected = {'&', '#', 'x', '3', '1', '5', ';'};
 CharSequence input = "&#x315;";
 int length = UNESCAPER.translate(input, 0, null);
 char[] output = new char[length];
 try {
 UNESCAPER.translate(input, 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 output[UNESCAPER.index++] = (char) c;
 }

 @Override
 public void flush() throws IOException {
 // Flush is not used in this test
 }

 int index = 0;
 });
 } catch (IOException e) {
 e.printStackTrace();
 }

 assertEquals(expected, output);
 }

 @Test
 public void testTranslateNumericEntityWithOutsideLimit() {
 char[] input = {"&", '#', 'x', '3', '1', '5', ';', ' ', '&', '#', 'x', 'f', 'f', 'f', ';'};
 int length = UNESCAPER.translate(input, 0, null);
 char[] output = new char[length];
 try {
 UNESCAPER.translate(input, 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 output[UNESCAPER.index++] = (char) c;
 }

 @Override
 public void flush() throws IOException {
 // Flush is not used in this test
 }

 int index = 0;
 });
 } catch (IOException e) {
 e.printStackTrace();
 }

 assertArrayEquals(new char[]{'&', '&', '#', 'x', 'f', 'f', 'f'}, output);
 }
}