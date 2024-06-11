import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateWithDefaultOptions() {
 CharSequence input = "&num;Hello &#123;abc&num; World!";
 Writer output = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 unescaper.translate(input, 0, output);
 String expected = "&num;Hello &#x61;&#x62;&#x63;abc&num; World!\n";
 assertEquals(expected, output.toString());
 }

 @Test
 public void testTranslateWithSemiColonRequiredOption() {
 CharSequence input = "&num;Hello &#123;abc&num; World!";
 Writer output = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 unescaper.translate(input, 0, output);
 String expected = "&num;Hello &#x61;&#x62;&#x63;abc&num; World!\n";
 assertEquals(expected, output.toString());

 CharSequence inputWithMissingSemiColon = "&num;Hello &#123;abc&num; World!";
 Writer outputWithMissingSemiColon = new StringWriter();
 try {
 unescaper.translate(inputWithMissingSemiColon, 0, outputWithMissingSemiColon);
 fail("Expected IllegalArgumentException with 'Semi-colon required at end of numeric entity' message");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testTranslateWithSemiColonOptionalOption() {
 CharSequence input = "&num;Hello &#123;abc&num; World!";
 Writer output = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 unescaper.translate(input, 0, output);
 String expected = "&num;Hello &#x61;&#x62;&#x63;abcWorld!\n";
 assertEquals(expected, output.toString());
 }

 @Test
 public void testTranslateWithErrorIfNoSemiColonOption() {
 CharSequence input = "&num;Hello &#123;abc&num; World!";
 Writer output = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 unescaper.translate(input, 0, output);
 String expected = "&num;Hello &#x61;&#x62;&#x63;abc&num; World!\n";
 assertEquals(expected, output.toString());

 CharSequence inputWithMissingSemiColon = "&num;Hello &#123;abc&num; World!";
 Writer outputWithMissingSemiColon = new StringWriter();
 try {
 unescaper.translate(inputWithMissingSemiColon, 0, outputWithMissingSemiColon);
 fail("Expected IllegalArgumentException with 'Semi-colon required at end of numeric entity' message");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testTranslateWithHexValue() {
 CharSequence input = "&num;Hello &#x0061;&#x0062;&#x0063;World!";
 Writer output = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 unescaper.translate(input, 0, output);
 String expected = "&num;Hello &#x61;&#x62;&#x63;World!\n";
 assertEquals(expected, output.toString());
 }

 @Test
 public void testTranslateWithInvalidHexValue() {
 CharSequence input = "&num;Hello &#x1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
 Writer output = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 try {
 unescaper.translate(input, 0, output);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testTranslateWithNumberGreaterThan65535() {
 CharSequence input = "&num;Hello &#x10000;World!";
 Writer output = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 unescaper.translate(input, 0, output);
 String expected = "&num;Hello &#10000;\nWorld!\n";
 assertEquals(expected, output.toString());
 }
}