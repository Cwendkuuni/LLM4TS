import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateNumericEntity() {
 String input = "&amp;&#x00A9;&#x1F602;&#123;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int length = translator.translate(new StringReader(input), 0, new StringWriter(output));

 assertEquals("&amp;&#xA9;&#x1F602;&#123;", input.subSequence(0, input.length()));
 assertEquals("\u00A9\u1F602\u007B", output.toString().substring(0, length));
 }

 @Test(expected = IOException.class)
 public void testTranslateInvalidEntity() {
 String input = "&invalidEntity;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter output = new StringWriter();
 translator.translate(new StringReader(input), 0, output);
 }

 @Test
 public void testTranslateEmptyString() {
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int length = translator.translate(new StringReader(""), 0, output);
 assertEquals("", "");
 }

 @Test
 public void testTranslateNoEntity() {
 String input = "abcdef";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int length = translator.translate(new StringReader(input), 0, output);
 assertEquals("abcdef", output.toString());
 }

 @Test
 public void testTranslateSingleCharacterEntity() {
 String input = "&amp;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int length = translator.translate(new StringReader(input), 0, output);
 assertEquals("&amp;", input.subSequence(0, input.length()));
 assertEquals("&amp;", output.toString().substring(0, length));
 }

 @Test
 public void testTranslateSingleCharacterEntityWithSemicolon() {
 String input = "&amp;&#123;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int length = translator.translate(new StringReader(input), 0, output);
 assertEquals("&amp;&#123;", input.subSequence(0, input.length()));
 assertEquals("\u007B", output.toString().substring(length - 1, length));
 }

 @Test
 public void testTranslateSingleCharacterEntityWithSemicolonMissing() {
 String input = "&amp;&#123";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int length = translator.translate(new StringReader(input), 0, output);
 assertEquals("&amp;&#123", input.subSequence(0, input.length()));
 assertEquals("\u007B", output.toString().substring(length - 1, length));
 }

 @Test
 public void testTranslateTwoCharacterEntity() {
 String input = "&copy;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int length = translator.translate(new StringReader(input), 0, output);
 assertEquals("\u00A9", output.toString().substring(0, length));
 }

 @Test
 public void testTranslateMultiCharacterEntity() {
 String input = "&oX2160;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int length = translator.translate(new StringReader(input), 0, output);
 assertEquals("\u2160", output.toString().substring(0, length));
 }

 @Test
 public void testTranslateHexEntityWithLeadingZeroes() {
 String input = "&#x007f;&#x007F;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int length = translator.translate(new StringReader(input), 0, output);
 assertEquals("\u007F\u007F", output.toString().substring(0, length));
 }

 @Test
 public void testTranslateHexEntityWithLeadingZerosAndSemicolonMissing() {
 String input = "&#x007f;&#x007F";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int length = translator.translate(new StringReader(input), 0, output);
 assertEquals("\u007F\u007F", output.toString().substring(0, length));
 }

 @Test
 public void testTranslateHexEntityWithLeadingZerosAndSemicolon() {
 String input = "&#x007f;&#x007F;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int length = translator.translate(new StringReader(input), 0, output);
 assertEquals("\u007F\u007F", output.toString().substring(0, length));
 }

 @Test
 public void testTranslateInvalidHexEntityWithLeadingZeros() {
 String input = "&#x007z";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int length = translator.translate(new StringReader(input), 0, output);
 assertEquals("", output.toString());
 }

 @Test
 public void testTranslateMultiCharacterHexEntityWithLeadingZeros() {
 String input = "&#x0061;&#x0062;&#x0063;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int length = translator.translate(new StringReader(input), 0, output);
 assertEquals("\u0061\u0062\u0063", output.toString().substring(0, length));
 }

 @Test
 public void testTranslateMultiCharacterHexEntityWithLeadingZerosAndSemicolonMissing() {
 String input = "&#x0061;&#x0062;&#x0063";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int length = translator.translate(new StringReader(input), 0, output);
 assertEquals("\u0061\u0062\u0063", output.toString().substring(0, length));
 }

 @Test
 public void testTranslateMultiCharacterHexEntityWithLeadingZerosAndSemicolon() {
 String input = "&#x0061;&#x0062;&#x0063";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int length = translator.translate(new StringReader(input), 0, output);
 assertEquals("\u0061\u0062\u0063", output.toString().substring(0, length));
 }

 @Test
 public void testTranslateInvalidMultiCharacterEntity() {
 String input = "&#abcd";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int length = translator.translate(new StringReader(input), 0, output);
 assertEquals("", output.toString());
 }
}