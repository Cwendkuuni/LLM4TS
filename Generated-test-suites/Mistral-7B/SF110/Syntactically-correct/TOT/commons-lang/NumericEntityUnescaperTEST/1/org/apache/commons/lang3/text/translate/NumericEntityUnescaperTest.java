import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateWithNoOptions() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 char[] input = {'&', '#', '0', ';'};
 Writer output = new StringWriter();
 int length = unescaper.translate(new String(input), 0, output);
 String expectedOutput = "\u0026\u00230\u003B";
 assertEquals(expectedOutput.length(), length);
 assertEquals(expectedOutput, output.toString());
 }

 @Test
 public void testTranslateWithSemiColonRequiredOption() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 char[] input = {'&', '#', '0'};
 Writer output = new StringWriter();
 try {
 unescaper.translate(new String(input), 0, output);
 fail("Expected IllegalArgumentException for missing semicolon");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }

 input = new char[]{'&', '#', '0', ';'};
 length = unescaper.translate(new String(input), 0, output);
 expectedOutput = "\u0026\u00230\u003B";
 assertEquals(expectedOutput.length(), length);
 assertEquals(expectedOutput, output.toString());
 }

 @Test
 public void testTranslateWithSemiColonOptionalOption() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 char[] input = {'&', '#', '0'};
 Writer output = new StringWriter();
 int length = unescaper.translate(new String(input), 0, output);
 String expectedOutput = "\u0026\u00230";
 assertEquals(expectedOutput.length(), length);
 assertEquals(expectedOutput, output.toString());
 }

 @Test
 public void testTranslateWithErrorIfNoSemiColonOption() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 char[] input = {'&', '#', '0'};
 Writer output = new StringWriter();
 try {
 unescaper.translate(new String(input), 0, output);
 fail("Expected IllegalArgumentException for missing semicolon");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }

 char[] inputWithSemicolon = {'&', '#', '0', ';'};
 String expectedOutputWithSemicolon = "\u0026\u00230\u003B";
 length = unescaper.translate(new String(inputWithSemicolon), 0, output);
 assertEquals(expectedOutputWithSemicolon.length(), length);
 assertEquals(expectedOutputWithSemicolon, output.toString());
 }

 @Test
 public void testTranslateWithInvalidOption() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.INVALID);
 char[] input = {'&', '#', '0', ';'};
 Writer output = new StringWriter();
 int length = unescaper.translate(new String(input), 0, output);
 String expectedOutput = "\u0026\u00230\u003B";
 assertEquals(expectedOutput.length(), length);
 assertEquals(expectedOutput, output.toString());
 }

 @Test
 public void testTranslateWithHexNumber() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 char[] input = {'&', '#', 'x', '3', 'F', ';'};
 Writer output = new StringWriter();
 int length = unescaper.translate(new String(input), 0, output);
 int hexValue = 0x3F;
 String expectedOutput = Character.toString((char) hexValue);
 assertEquals(expectedOutput.length(), length);
 assertEquals(expectedOutput, output.toString());
 }

 @Test
 public void testTranslateWithInvalidHexNumber() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 char[] input = {'&', '#', 'x', '3', 'G', ';'};
 Writer output = new StringWriter();
 int length = unescaper.translate(new String(input), 0, output);
 assertEquals(0, length);
 assertEquals("", output.toString());
 }

 @Test
 public void testTranslateWithLargeNumber() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 char[] input = {'&', '#', 'x', '7', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F'};
 Writer output = new StringWriter();
 int length = unescaper.translate(new String(input), 0, output);
 int largeValue = 0x10FFFF;
 String expectedOutput = Character.toString((char) largeValue);
 assertEquals(expectedOutput.length(), length);
 assertEquals(expectedOutput, output.toString());
 }
}