import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateWithBasicNumericEntity() {
 String input = "&num;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int translatedLength = unescaper.translate(new StringReader(input), 0, out);
 String expectedOutput = "\u006E\u0065\u0064"; // 'num' in UTF-16
 String actualOutput = out.toString().substring(0, translatedLength);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testTranslateWithHexNumericEntity() {
 String input = "&#x41;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 int translatedLength = unescaper.translate(new StringReader(input), 0, out);
 char expectedOutput = (char) 0x41; // 'A'
 String actualOutput = out.toString().substring(0, translatedLength);
 assertEquals(String.valueOf(expectedOutput), actualOutput);
 }

 @Test
 public void testTranslateWithLargeNumericEntity() {
 String input = "&#x65536;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 int translatedLength = unescaper.translate(new StringReader(input), 0, out);
 char[] expectedOutput = Character.toChars(0x10000); // '𠀀' in UTF-16
 String actualOutput = out.toString().substring(0, translatedLength);
 assertArrayEquals(expectedOutput, new char[2]); // Output consists of two surrogate pairs
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithMissingSemiColon() {
 String input = "&num";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 int translatedLength = unescaper.translate(new StringReader(input), 0, out);
 fail("Expected IllegalArgumentException, but didn't receive one.");
 }

 @Test
 public void testTranslateWithInvalidNumericEntity() {
 String input = "&invalidEntity;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int translatedLength = unescaper.translate(new StringReader(input), 0, out);
 assertEquals(0, translatedLength);
 }
}