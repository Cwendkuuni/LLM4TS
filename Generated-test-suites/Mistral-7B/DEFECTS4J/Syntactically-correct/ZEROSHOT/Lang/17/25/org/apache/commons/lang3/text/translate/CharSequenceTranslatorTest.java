import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.IdentityTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

 @Test
 public void testIdentityTranslator() {
 CharSequenceTranslator translator = new IdentityTranslator();
 String input = "Hello World!";
 String expected = "Hello World!";
 String output = translator.translate(input);
 assertEquals(expected, output);
 }

 @Test
 public void testAggregateTranslator() {
 CharSequenceTranslator identityTranslator = new IdentityTranslator();
 CharSequenceTranslator hexTranslator = (cs, out) -> {
 for (int i = 0; i < cs.length(); i++) {
 int codePoint = Character.codePointAt(cs, i);
 String hex = CharSequenceTranslator.hex(codePoint);
 out.write(hex);
 out.write(" ");
 }
 };
 CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[] { identityTranslator, hexTranslator });
 String input = "Hello World!";
 String expected = "Hello World! HELLO_20_48_65_20_4C_20_6F_20_6D_65_20_74_68_69_73_20_6C_69_6E_65_20_77_6F_72_6C_64";
 String output = translator.translate(input);
 assertEquals(expected, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullWriter() {
 CharSequenceTranslator translator = new IdentityTranslator();
 CharSequence input = "Hello World!";
 Writer out = null;
 int len = translator.translate(input, 0, out);
 fail("Should throw IllegalArgumentException for null Writer");
 }

 @Test
 public void testTranslateWithWriter() throws IOException {
 CharSequenceTranslator translator = new IdentityTranslator();
 CharSequence input = "Hello World!";
 StringWriter writer = new StringWriter();
 int len = translator.translate(input, 0, writer);
 String output = writer.toString();
 String expected = "Hello World!";
 assertEquals(expected, output);
 assertEquals(input.length() * 2, len);
 }

 @Test
 public void testTranslateWithStringWriter() throws IOException {
 CharSequenceTranslator translator = new IdentityTranslator();
 CharSequence input = "Hello World!";
 StringWriter writer = new StringWriter();
 translator.translate(input, writer);
 String output = writer.toString();
 String expected = "Hello World!";
 assertEquals(expected, output);
 }

 @Test
 public void testTranslateWithSurrogatePair() throws IOException {
 CharSequenceTranslator translator = new IdentityTranslator();
 CharSequence input = "💩";
 StringWriter writer = new StringWriter();
 int len = translator.translate(input, 0, writer);
 String output = writer.toString();
 assertEquals(2, len);
 assertEquals("\uD83C\uDFF0", output);
 }
}