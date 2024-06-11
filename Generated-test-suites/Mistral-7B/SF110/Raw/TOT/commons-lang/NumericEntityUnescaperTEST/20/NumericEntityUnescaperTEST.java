import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.EnumSet;

class NumericEntityUnescaperTest {

 @Test
 void testTranslateWithSemiColonRequiredOption() throws IOException {
 CharSequence input = "&amp;#xABCD;";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);
 assertEquals("ABCD", out.toString());
 assertEquals(4, length);
 }

 @Test
 void testTranslateWithSemiColonOptionalOption() throws IOException {
 CharSequence input = "&amp;#xABCD";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);
 assertEquals("ABCD", out.toString());
 assertEquals(3, length);
 }

 @Test
 void testTranslateWithErrorIfNoSemiColonOption() throws IOException {
 CharSequence input = "&amp;#xABCD";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 assertThrows(IllegalArgumentException.class, () -> unescaper.translate(input, 0, new StringWriter()));
 }

 @Test
 void testTranslateWithInvalidHexSequence() throws IOException {
 CharSequence input = "&amp;#xABCDef";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);
 assertEquals(0, length);
 }

 @Test
 void testTranslateWithInvalidDecimalSequence() throws IOException {
 CharSequence input = "&amp;#1234567890123";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);
 assertEquals(0, length);
 }

 @Test
 void testTranslateWithValidDecimalSequence() throws IOException {
 CharSequence input = "&amp;#1234";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);
 assertEquals(4, length);
 assertEquals((char) 1234, out.toString().charAt(0));
 }

 @Test
 void testTranslateWithLeadingAmpersand() throws IOException {
 CharSequence input = "&amp;&amp;#1234";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);
 assertEquals(5, length);
 assertEquals((char) 1234, out.toString().charAt(1));
 }

 @Test
 void testTranslateWithTrailingAmpersand() throws IOException {
 CharSequence input = "&amp;#1234&";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);
 assertEquals(5, length);
 assertEquals((char) 1234, out.toString().charAt(0));
 }
}