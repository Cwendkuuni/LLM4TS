import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateXHexEntity() throws IOException {
 String input = "&#[xCA];&#[xAE];Hello World&#[x27];";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 char[] inputChars = input.toCharArray();
 int length = inputChars.length;

 for (int i = 0; i < length; i++) {
 int translated = unescaper.translate(new String(inputChars, 0, i), i, outputWriter);
 outputWriter.write(inputChars, i + translated, length - i - translated);
 }

 String expectedOutput = "ÇÀHello World'";
 String actualOutput = outputWriter.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IOException.class)
 public void testTranslateInvalidEntity() throws IOException {
 String input = "&badEntity;";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 char[] inputChars = input.toCharArray();
 unescaper.translate(new String(inputChars, 0, 0), 0, outputWriter);
 }

 @Test
 public void testTranslateDecimalEntity() throws IOException {
 String input = "&#[128];";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 char[] inputChars = input.toCharArray();
 int length = inputChars.length;

 for (int i = 0; i < length; i++) {
 int translated = unescaper.translate(new String(inputChars, 0, i), i, outputWriter);
 outputWriter.write(inputChars, i + translated, length - i - translated);
 }

 String expectedOutput = "          ";
 String actualOutput = outputWriter.toString();
 assertEquals(expectedOutput, actualOutput);
 }
}