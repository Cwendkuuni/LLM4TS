import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private static final String ENTITY_WITH_SEMI_COLON = "&amp;#x1F600;";
 private static final String ENTITY_WITHOUT_SEMI_COLON = "&amp;#x1F600";
 private static final String INVALID_ENTITY = "&amp;#abc123;";
 private static final String INVALID_HEX_ENTITY = "&amp;#xinvalidHex;";
 private static final String EMPTY_INPUT = "";
 private static final String VALID_DECIMAL_ENTITY = "&amp;#123;";
 private static final String VALID_HEX_ENTITY = "&amp;#x78";

 @Test
 public void testTranslate_ValidDecimalEntityWithSemiColon() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 String expectedOutput = String.valueOf((char) 123);
 String input = "&amp;#123;";
 Writer out = new StringWriter();
 int actualLength = unescaper.translate(new StringReader(input), 0, out);
 String actualOutput = out.toString();
 assertEquals(expectedOutput, actualOutput.substring(0, actualLength));
 }

 @Test
 public void testTranslate_ValidHexEntityWithSemiColon() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 String expectedOutput = "µ";
 String input = "&amp;#x03bc;";
 Writer out = new StringWriter();
 int actualLength = unescaper.translate(new StringReader(input), 0, out);
 String actualOutput = out.toString();
 assertEquals(expectedOutput, actualOutput.substring(0, actualLength));
 }

 @Test
 public void testTranslate_ValidDecimalEntityWithoutSemiColon() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 String expectedOutput = String.valueOf((char) 123);
 String input = "&amp;#123";
 Writer out = new StringWriter();
 int actualLength = unescaper.translate(new StringReader(input), 0, out);
 String actualOutput = out.toString();
 assertEquals(expectedOutput, actualOutput.substring(0, actualLength));
 }

 @Test
 public void testTranslate_ValidHexEntityWithoutSemiColon() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 String expectedOutput = "µ";
 String input = "&amp;#x03bc";
 Writer out = new StringWriter();
 int actualLength = unescaper.translate(new StringReader(input), 0, out);
 String actualOutput = out.toString();
 assertEquals(expectedOutput, actualOutput.substring(0, actualLength));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_InvalidEntity() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 String input = INVALID_ENTITY;
 unescaper.translate(new StringReader(input), 0, new StringWriter());
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidHexEntity() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 String input = INVALID_HEX_ENTITY;
 unescaper.translate(new StringReader(input), 0, new StringWriter());
 }

 @Test
 public void testTranslate_EmptyInput() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 String input = EMPTY_INPUT;
 Writer out = new StringWriter();
 int actualLength = unescaper.translate(new StringReader(input), 0, out);
 String actualOutput = out.toString();
 assertEquals("", actualOutput);
 }

 @Test
 public void testTranslate_ValidDecimalEntityWithSemiColon_ErrorIfNoSemiColon() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 String input = VALID_DECIMAL_ENTITY;
 assertThrows(IllegalArgumentException.class, () -> {
 unescaper.translate(new StringReader(input), 0, new StringWriter());
 });
 }

 @Test
 public void testTranslate_ValidDecimalEntityWithoutSemiColon_SemiColonRequired() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 String input = VALID_DECIMAL_ENTITY;
 assertThrows(IllegalArgumentException.class, () -> {
 unescaper.translate(new StringReader(input), 0, new StringWriter());
 });
 }

 @Test
 public void testTranslate_ValidHexEntityWithSemiColon_SemiColonOptional() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 String input = VALID_HEX_ENTITY + ";";
 Writer out = new StringWriter();
 int actualLength = unescaper.translate(new StringReader(input), 0, out);
 String actualOutput = out.toString();
 assertEquals(expectedOutput, actualOutput.substring(0, actualLength));
 }

 @Test
 public void testTranslate_ValidHexEntityWithoutSemiColon_SemiColonOptional() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 String input = VALID_HEX_ENTITY;
 Writer out = new StringWriter();
 int actualLength = unescaper.translate(new StringReader(input), 0, out);
 String actualOutput = out.toString();
 assertEquals(expectedOutput, actualOutput.substring(0, actualLength));
 }
}