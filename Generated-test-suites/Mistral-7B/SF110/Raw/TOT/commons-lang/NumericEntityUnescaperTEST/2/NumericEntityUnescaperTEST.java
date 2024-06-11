import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateWithBasicNumericEntity() {
 String input = "&amp;#160;"; // ASCII character ' ' (space)
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 CharSequence charSequence = () -> input;
 int length = unescaper.translate(charSequence, 0, output);
 assertEquals(1, length);
 assertEquals(" ", output.toString());
 }

 @Test
 public void testTranslateWithHexNumericEntity() {
 String input = "&amp;#x1F44D;"; // Unicode character ' penalty sign' (U+203F)
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 CharSequence charSequence = () -> input;
 int length = unescaper.translate(charSequence, 0, output);
 assertEquals(3, length);
 assertEquals("\uD83E\uDD1E", output.toString());
 }

 @Test
 public void testTranslateWithOptionalSemiColon() {
 String input = "&amp;#160;"; // ASCII character ' ' (space)
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 StringBuilder output = new StringBuilder();
 CharSequence charSequence = () -> input;
 int length = unescaper.translate(charSequence, 0, output);
 assertEquals(1, length);
 assertEquals(" ", output.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithNoSemiColon() {
 String input = "&amp;#160;"; // ASCII character ' ' (space)
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 StringBuilder output = new StringBuilder();
 CharSequence charSequence = () -> input;
 int length = unescaper.translate(charSequence, 0, output);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testTranslateWithInvalidHexNumericEntity() {
 String input = "&amp;#xzzz;"; // Invalid hexadecimal value
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 CharSequence charSequence = () -> input;
 int length = unescaper.translate(charSequence, 0, output);
 assertEquals(0, length);
 }

 @Test
 public void testTranslateWithInvalidDecNumericEntity() {
 String input = "&amp;#abc;"; // Invalid decimal value
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 CharSequence charSequence = () -> input;
 int length = unescaper.translate(charSequence, 0, output);
 assertEquals(0, length);
 }
}