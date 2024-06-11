import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.SimpleTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequence input;

 @Test
 public void testTranslatorWithDefaultConstructor() {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 return 0;
 }
 };
 input = "Hello World!";
 String result = translator.translate(input);
 assertNotNull(result);
 assertEquals("Hello World!", result);
 }

 @Test
 public void testTranslateMethod() throws IOException {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 out.write('X');
 out.write('X');
 return 2;
 }
 };
 input = "Hello World!";
 String result = translator.translate(input);
 assertEquals("XXHello World!XX", result);
 }

 @Test
 public void testAggregateTranslator() throws IOException {
 SimpleTranslator simpleTranslator = new SimpleTranslator('X');
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 simpleTranslator.translate(input, index, out);
 return simpleTranslator.getConsumed();
 }
 }.with(simpleTranslator);
 input = "Hello World!";
 String result = translator.translate(input);
 assertEquals("XHello XWorld!X", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWriterIsNull() throws IOException {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 out.write('X');
 out.write('X');
 return 2;
 }
 }.with(new SimpleTranslator('X'));
 input = "Hello World!";
 translator.translate(input, null);
 }

}