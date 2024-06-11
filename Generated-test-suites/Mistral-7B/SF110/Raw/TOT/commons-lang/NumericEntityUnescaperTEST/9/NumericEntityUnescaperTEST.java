import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateNumericEntity() {
 CharSequence input = new StringBuilder("&amp;#160; &#x20AC; &#032; &amp;#x1F44D;&#x1F44E; &amp;#65536; &amp;#xFFFD; &amp;#x1F600;&#x1F601;").toString();
 Writer out = new StringWriter();

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
 int translatedLength = unescaper.translate(input, 0, out, 0, input.length());
 String expectedOutput = "  €   &amp; ÀÁ;  première deuxièmeprime ฬอ ";
 assertEquals(expectedOutput, out.toString().substring(0, translatedLength));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateMissingSemiColon() {
 CharSequence input = new StringBuilder("&amp;#160; &#x20AC; &#032; &amp;#x1F44D;&#x1F44E; &amp;#65536;").toString();
 Writer out = new StringWriter();

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 int translatedLength = unescaper.translate(input, 0, out, 0, input.length());
 fail("Expected IllegalArgumentException but no exception was thrown.");
 }

 @Test
 public void testTranslateHexEntity() {
 CharSequence input = new StringBuilder("&amp;#x20AC;").toString();
 Writer out = new StringWriter();

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 int translatedLength = unescaper.translate(input, 0, out, 0, input.length());
 String expectedOutput = "€";
 assertEquals(expectedOutput, out.toString().substring(0, translatedLength));
 }

 @Test
 public void testTranslateLargeEntity() {
 CharSequence input = new StringBuilder("&amp;#xFFFF;").toString();
 Writer out = new StringWriter();

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 int translatedLength = unescaper.translate(input, 0, out, 0, input.length());
 char[] expectedChars = Character.toChars(0xFFFF);
 assertArrayEquals(expectedChars, new char[2]);
 assertEquals(expectedChars[0], out.toString().charAt(0));
 assertEquals(expectedChars[1], out.toString().charAt(1));
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidEntity() {
 CharSequence input = new StringBuilder("&amp;#abc;").toString();
 Writer out = new StringWriter();

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 int translatedLength = unescaper.translate(input, 0, out, 0, input.length());
 fail("Expected NumberFormatException but no exception was thrown.");
 }

 @Test
 public void testTranslateEmptyInput() {
 CharSequence input = "";
 Writer out = new StringWriter();

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 int translatedLength = unescaper.translate(input, 0, out, 0, input.length());
 assertEquals("", out.toString().substring(0, translatedLength));
 }
}