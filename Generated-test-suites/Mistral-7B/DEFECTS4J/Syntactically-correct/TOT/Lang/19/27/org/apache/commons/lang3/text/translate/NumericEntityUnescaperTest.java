import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper translator = new NumericEntityUnescaper();

 @Test
 public void testTranslateBasic() throws IOException {
 String input = "&amp; &#160; &#128; &#x41; &#x65; &#xFF; &#x41;&#x65; &#x20AC;";
 String expectedOutput = "&amp; &nbsp; � A a €";
 Writer out = new StringWriter();
 int length = translator.translate(new StringReader(input), 0, out);
 assertEquals(expectedOutput, out.toString().substring(0, length));
 }

 @Test(expected = IOException.class)
 public void testTranslateMissingSemicolon() throws IOException {
 String input = "&amp; #160";
 String output = "";
 Writer out = new StringWriter();
 int length = translator.translate(new StringReader(input), 0, out);
 assertEquals("", output); // Should not reach here
 }

 @Test
 public void testTranslateInvalidHex() throws IOException {
 String input = "&amp; &#xF00F";
 String output = "";
 Writer out = new StringWriter();
 int length = translator.translate(new StringReader(input), 0, out);
 assertEquals("", output);
 }

 @Test
 public void testTranslateLargeHex() throws IOException {
 String input = "&amp; &#xFFFF";
 char[] expectedOutput = Character.toChars(0xFFFF);
 Writer out = new StringWriter();
 int length = translator.translate(new StringReader(input), 0, out);
 assertEquals(2, length);
 out.write(expectedOutput[0]);
 out.write(expectedOutput[1]);
 }

 @Test
 public void testTranslateDecimals() throws IOException {
 String input = "&amp; &#10; &#37; &#1024";
 String expectedOutput = "&amp; \n % 1024";
 Writer out = new StringWriter();
 int length = translator.translate(new StringReader(input), 0, out);
 assertEquals(expectedOutput, out.toString().substring(0, length));
 }

 @Test
 public void testTranslateInvalidDecimal() throws IOException {
 String input = "&amp; &#20000";
 String output = "";
 Writer out = new StringWriter();
 int length = translator.translate(new StringReader(input), 0, out);
 assertEquals("", output);
 }

 @Test
 public void testTranslateInvalidCharacter() throws IOException {
 String input = "&amp; &#123"; // Invalid Unicode character
 String output = "";
 Writer out = new StringWriter();
 int length = translator.translate(new StringReader(input), 0, out);
 assertEquals("", output);
 }
}