import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateXDecimalEntity() {
 char[] input = {'&', '#', 'x', '3', ';'};
 char expectedOutput = '\u0033';

 try (Writer out = new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 assertEquals(Character.toString((char) c), Character.toString((char) (c + length)), "Unexpected output");
 }

 @Override
 public void flush() throws IOException {
 // Flush is not used in this test
 }
 }) {
 int length = new NumericEntityUnescaper().translate(new String(input), 0, out);
 assertEquals(1, length);
 }
 }

 @Test
 public void testTranslateXHexEntity() {
 char[] input = {'&', '#', 'x', '2', 'a', ';'};
 char expectedOutput = '\u002a';

 try (Writer out = new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 assertEquals(Character.toString((char) c), Character.toString((char) (c + length)), "Unexpected output");
 }

 @Override
 public void flush() throws IOException {
 // Flush is not used in this test
 }
 }) {
 int length = new NumericEntityUnescaper().translate(new String(input), 0, out);
 assertEquals(1, length);
 }
 }

 @Test
 public void testTranslateDecimalEntity() {
 char[] input = {'&', '#', '1', '3', ';'};
 char expectedOutput = '\u0013';

 try (Writer out = new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 assertEquals(Character.toString((char) c), Character.toString((char) (c + length)), "Unexpected output");
 }

 @Override
 public void flush() throws IOException {
 // Flush is not used in this test
 }
 }) {
 int length = new NumericEntityUnescaper().translate(new String(input), 0, out);
 assertEquals(1, length);
 }
 }

 @Test
 public void testTranslateEntityWithMultipleDigits() {
 char[] input = {'&', '#', 'x', '2', 'f', 'f', ';'};
 char expectedOutput = '\u002F';

 try (Writer out = new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 assertEquals(Character.toString((char) c), Character.toString((char) (c + length)), "Unexpected output");
 }

 @Override
 public void flush() throws IOException {
 // Flush is not used in this test
 }
 }) {
 int length = new NumericEntityUnescaper().translate(new String(input), 0, out);
 assertEquals(1, length);
 }
 }

 @Test
 public void testTranslateEntityWithLeadingZero() {
 char[] input = {'&', '#', '0', '3', ';'};
 char expectedOutput = '\u0033';

 try (Writer out = new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 assertEquals(Character.toString((char) c), Character.toString((char) (c + length)), "Unexpected output");
 }

 @Override
 public void flush() throws IOException {
 // Flush is not used in this test
 }
 }) {
 int length = new NumericEntityUnescaper().translate(new String(input), 0, out);
 assertEquals(1, length);
 }
 }

 @Test
 public void testTranslateEntityWithTrailingSemicolon() {
 char[] input = {'&', '#', 'x', '3', ';', ';'};
 char expectedOutput = '\u0033';

 try (Writer out = new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 assertEquals(Character.toString((char) c), Character.toString((char) (c + length)), "Unexpected output");
 }

 @Override
 public void flush() throws IOException {
 // Flush is not used in this test
 }
 }) {
 int length = new NumericEntityUnescaper().translate(new String(input), 0, out);
 assertEquals(1, length);
 }
 }

 @Test
 public void testTranslateEntityWithTrailingZero() {
 char[] input = {'&', '#', 'x', '3', ';', '0'};
 char expectedOutput = '\u0033';

 try (Writer out = new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 assertEquals(Character.toString((char) c), Character.toString((char) (c + length)), "Unexpected output");
 }

 @Override
 public void flush() throws IOException {
 // Flush is not used in this test
 }
 }) {
 int length = new NumericEntityUnescaper().translate(new String(input), 0, out);
 assertEquals(1, length);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateEntityWithMissingSemicolon() {
 char[] input = {'&', '#', 'x', '3'};

 try (Writer out = new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 // Should not be called
 }

 @Override
 public void flush() throws IOException {
 // Flush is not used in this test
 }
 }) {
 int length = new NumericEntityUnescaper().translate(new String(input), 0, out);
 fail("Expected IllegalArgumentException but got " + length);
 }
 }
}