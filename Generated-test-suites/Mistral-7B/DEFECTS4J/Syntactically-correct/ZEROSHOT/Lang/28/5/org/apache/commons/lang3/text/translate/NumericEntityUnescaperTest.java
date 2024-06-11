import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private final CharSequenceTranslator translator = new NumericEntityUnescaper();

 @Test
 public void testTranslateXHex() {
 String input = "&rgb;&#x61;#x1e6;&#x1f4;#x4d;";
 try {
 Writer output = new Writer() {
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
 int length = translator.translate(new String(input), 0, output);
 assertEquals("àªµ¸À¸À×", input.substring(0, 6));
 assertEquals(6, length);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslateXDec() {
 String input = "&rgb;&#x61;#252;&#244;#140;";
 try {
 Writer output = new Writer() {
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
 int length = translator.translate(new String(input), 0, output);
 assertEquals("àªµ¸À¸À×", input.substring(0, 6));
 assertEquals(6, length);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslateDecimal() {
 String input = "&rgb;&#123;#10;#6;";
 try {
 Writer output = new Writer() {
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
 int length = translator.translate(new String(input), 0, output);
 assertEquals("{\\n  ", input.substring(0, 3));
 assertEquals(4, length);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslateInvalidHex() {
 String input = "&rgb;&#x61;#x1ef;";
 try {
 Writer output = new Writer() {
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
 int length = translator.translate(new String(input), 0, output);
 assertEquals("&rgb;&#x61;#x1ef;", input);
 assertEquals(0, length);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslateInvalidDec() {
 String input = "&rgb;&#abc;";
 try {
 Writer output = new Writer() {
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
 int length = translator.translate(new String(input), 0, output);
 assertEquals("&rgb;&#abc;", input);
 assertEquals(0, length);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslateEmpty() {
 String input = "";
 try {
 Writer output = new Writer() {
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
 int length = translator.translate(new String(input), 0, output);
 assertEquals("", input);
 assertEquals(0, length);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslateNull() {
 try {
 Writer output = new Writer() {
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
 int length = translator.translate(null, 0, output);
 assertEquals(-1, length);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslateNegativeIndex() {
 String input = "&rgb;";
 try {
 Writer output = new Writer() {
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
 int length = translator.translate(new String(input), -1, output);
 assertEquals(-1, length);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
}