import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslatorFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequenceTranslator escapedTranslator;
 private CharSequenceTranslator unescapedTranslator;

 @Test
 public void testInit() {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 throw new UnsupportedOperationException();
 }
 };
 assertNull(translator.translate(null));
 assertNull(translator.translate(""));

 escapedTranslator = new CharSequenceTranslatorFactory("\\{0}", new String[]{"{0}"})
 .setCharset("UTF-8")
 .getTranslator();

 unescapedTranslator = new AggregateTranslator(new CharSequenceTranslator[]{escapedTranslator, new IdentityTranslator()});
 }

 @Test
 public void testTranslateNull() {
 String result = translator.translate(null);
 assertNull(result);
 }

 @Test
 public void testTranslateEmpty() {
 String result = translator.translate("");
 assertEquals("", result);
 }

 @Test
 public void testIdentityTranslate() {
 String input = "Hello World!";
 String result = unescapedTranslator.translate(input);
 assertEquals(input, result);
 }

 @Test
 public void testIdentityTranslateWriter() throws IOException {
 String input = "Hello World!";
 Writer writer = new StringWriter();
 int consumed = unescapedTranslator.translate(input, 0, writer);
 assertEquals(input.length(), consumed);
 String result = writer.toString();
 assertEquals(input, result);
 }

 @Test
 public void testEscapeTranslate() {
 String input = "Hello \\{0} World!";
 String result = escapedTranslator.translate(input);
 assertEquals("Hello \\{0} World!", result);
 }

 @Test
 public void testEscapeTranslateWriter() throws IOException {
 String input = "Hello \\{0} World!";
 Writer writer = new StringWriter();
 int consumed = escapedTranslator.translate(input, 0, writer);
 assertEquals(input.length(), consumed);
 String result = writer.toString();
 assertEquals("Hello \\{0} World!", result);
 }

 @Test
 public void testUnescapeTranslate() {
 String input = "Hello \\{0} World!";
 String result = unescapedTranslator.translate(input);
 assertEquals("Hello {0} World!", result);
 }

 @Test
 public void testUnescapeTranslateWriter() throws IOException {
 String input = "Hello \\{0} World!";
 Writer writer = new StringWriter();
 int consumed = unescapedTranslator.translate(input, 0, writer);
 assertEquals(input.length(), consumed);
 String result = writer.toString();
 assertEquals("Hello {0} World!", result);
 }

 @Test
 public void testUnescapeTranslateWithArguments() {
 String input = "Hello \\{0} World!";
 String arg = "Test";
 String result = unescapedTranslator.translate(input, arg);
 assertEquals("Hello Test World!", result);
 }

 @Test
 public void testUnescapeTranslateWithArgumentsWriter() throws IOException {
 String input = "Hello \\{0} World!";
 String arg = "Test";
 Writer writer = new StringWriter();
 int consumed = unescapedTranslator.translate(input, arg, writer);
 assertEquals(input.length(), consumed);
 String result = writer.toString();
 assertEquals("Hello Test World!", result);
 }
}