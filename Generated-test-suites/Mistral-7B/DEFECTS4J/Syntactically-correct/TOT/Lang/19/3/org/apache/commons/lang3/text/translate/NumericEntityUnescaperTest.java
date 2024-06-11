import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testNumericEntityWithHexCode() throws Exception {
 String input = "&#[xAF]";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(new StringReader(input), 0, outputWriter);
 String expectedOutput = "\u00AF";
 assertEquals(expectedOutput.length(), outputWriter.toString().length());
 assertEquals(expectedOutput, outputWriter.toString());
 }

 @Test
 public void testNumericEntityWithDecimalCode() throws Exception {
 String input = "&#[123]";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(new StringReader(input), 0, outputWriter);
 char expectedOutput = (char) 123;
 assertEquals(expectedOutput, outputWriter.toString().charAt(0));
 }

 @Test(expected = IOException.class)
 public void testMalformedNumericEntity() throws Exception {
 String input = "&[xyz]";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 unescaper.translate(new StringReader(input), 0, outputWriter);
 fail("An IOException should be thrown for malformed numeric entities.");
 }

 @Test(expected = IOException.class)
 public void testNumericEntityWithMissingSemicolon() throws Exception {
 String input = "&#[abc";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 unescaper.translate(new StringReader(input), 0, outputWriter);
 fail("An IOException should be thrown for numeric entities without a semicolon.");
 }

 @Test
 public void testNumericEntityWithOutOfRangeValue() throws Exception {
 String input = "&#[65536]";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(new StringReader(input), 0, outputWriter);
 fail("An IOException or IllegalArgumentException should be thrown for values out of the Unicode range.");
 }

 @Test
 public void testNumericEntityWithInvalidHexCode() throws Exception {
 String input = "&#[xz]";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(new StringReader(input), 0, outputWriter);
 fail("An IOException should be thrown for invalid hex codes.");
 }

 @Test
 public void testEmptyInput() throws Exception {
 String input = "";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(new StringReader(input), 0, outputWriter);
 assertEquals(0, length);
 assertEquals("", outputWriter.toString());
 }
}