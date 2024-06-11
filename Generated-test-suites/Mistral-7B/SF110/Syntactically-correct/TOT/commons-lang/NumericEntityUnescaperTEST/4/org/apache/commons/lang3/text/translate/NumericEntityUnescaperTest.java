import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslate_ValidNumericEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 String input = "&amp;#160;&#163; &Heta; &X7F &amp;#xFF01;&#x00A9;&#x03B4;&#x041C;&#x1F465;";
 Writer out = new StringWriter();
 int translatedLength = unescaper.translate(new StringReader(input), 0, out);
 String expectedOutput = "  ĥ X fö ág ĳ ð Є Л ᵇ é";
 String actualOutput = out.toString();
 assertEquals(expectedOutput, actualOutput.substring(0, translatedLength));
 }

 @Test
 public void testTranslate_InvalidInput() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 String input = "&amp;#abc";
 Writer out = new StringWriter();
 int translatedLength = unescaper.translate(new StringReader(input), 0, out);
 assertEquals(0, translatedLength);
 assertThrows(IllegalArgumentException.class, () -> unescaper.translate(new StringReader(input), 0, out));
 }

 @Test
 public void testTranslate_ValidHexNumericEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 String input = "&amp;#x1F465";
 Writer out = new StringWriter();
 int translatedLength = unescaper.translate(new StringReader(input), 0, out);
 String expectedOutput = "🇵";
 String actualOutput = out.toString();
 assertEquals(expectedOutput, actualOutput.substring(0, translatedLength));
 }

 @Test
 public void testTranslate_EmptyInput() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 String input = "";
 Writer out = new StringWriter();
 int translatedLength = unescaper.translate(new StringReader(input), 0, out);
 assertEquals(0, translatedLength);
 }

 @Test
 public void testTranslate_ValidNumericEntityWithSemiColon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 String input = "&amp;#160;&#163; &Heta; &X7F &amp;#xFF01;&#x00A9;&#x03B4;&#x041C;&#x1F465; &";
 Writer out = new StringWriter();
 int translatedLength = unescaper.translate(new StringReader(input), 0, out);
 String expectedOutput = "  ĥ X fö ág ĳ ð  Є Л ᵇ é ";
 String actualOutput = out.toString();
 assertEquals(expectedOutput, actualOutput.substring(0, translatedLength));
 }

 @Test
 public void testTranslate_ErrorIfNoSemiColon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 String input = "&amp;#160;&#163; &Heta; &X7F &amp;#xFF01";
 Writer out = new StringWriter();
 int translatedLength = unescaper.translate(new StringReader(input), 0, out);
 assertThrows(IllegalArgumentException.class, () -> unescaper.translate(new StringReader(input), 0, out));
 }
}