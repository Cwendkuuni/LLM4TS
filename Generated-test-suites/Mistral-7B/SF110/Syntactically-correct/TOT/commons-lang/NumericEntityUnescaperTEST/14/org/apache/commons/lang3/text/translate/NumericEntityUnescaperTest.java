import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateWithNoOptions() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 String input = "&num;&amp;";
 int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
 assertEquals("num&amp;", input.substring(0, input.length() - length));
 assertEquals("num", output.toString());
 }

 @Test
 public void testTranslateWithOptionalSemiColon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 StringBuilder output = new StringBuilder();
 String input = "&num;";
 int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
 assertEquals("num", input.substring(0, input.length() - length));
 assertEquals("num", output.toString());
 }

 @Test
 public void testTranslateWithRequiredSemiColon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 StringBuilder output = new StringBuilder();
 String input = "&num;";
 int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
 assertSame(new IllegalArgumentException("Semi-colon required at end of numeric entity"), unescaper.translate(new StringReader(input), 0, new StringWriter(output)));
 }

 @Test
 public void testTranslateWithErrorIfNoSemiColon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 StringBuilder output = new StringBuilder();
 String input = "&num";
 int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
 assertSame(new IllegalArgumentException("Semi-colon required at end of numeric entity"), unescaper.translate(new StringReader(input), 0, new StringWriter(output)));
 }

 @Test
 public void testTranslateWithHexadecimal() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 String input = "&#x1F600;";
 int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
 assertEquals("", input.substring(0, input.length() - length));
 assertEquals("\uD83C\uDF00", output.toString());
 }

 @Test
 public void testTranslateWithInvalidHexadecimal() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 String input = "&#x1F60Z;";
 int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
 assertEquals("", input.substring(0, input.length() - length));
 assertEquals("", output.toString());
 }

 @Test
 public void testTranslateWithInvalidDecimal() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 String input = "&#XYZ;";
 int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
 assertEquals("", input.substring(0, input.length() - length));
 assertEquals("", output.toString());
 }

 @Test
 public void testTranslateWithValidDecimal() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 String input = "&#1234;";
 int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
 assertEquals("", input.substring(0, input.length() - length));
 assertEquals("1234", output.toString());
 }

 @Test
 public void testTranslateWithLongValue() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 String input = "&#1234567890;";
 int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
 assertEquals("", input.substring(0, input.length() - length));
 assertEquals("1234567890", output.toString());
 }

 @Test
 public void testTranslateWithLongValueWithSemiColon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 StringBuilder output = new StringBuilder();
 String input = "&#1234567890;";
 int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
 assertEquals("", input.substring(0, input.length() - length));
 assertEquals("1234567890", output.toString());
 }

 @Test
 public void testTranslateWithLongValueWithoutSemiColon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 StringBuilder output = new StringBuilder();
 String input = "&#1234567890";
 int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
 assertEquals("1234567890", input.substring(0, input.length() - length));
 assertEquals("1234567890", output.toString());
 }
}