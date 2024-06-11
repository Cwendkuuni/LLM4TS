import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslationWithSingleNumericEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 String input = "This is a text with an entity &amp;#10;";
 Writer outputWriter = new StringWriter();
 try {
 int translatedLength = unescaper.translate(new StringReader(input), 0, outputWriter);
 String translatedText = outputWriter.toString();
 assertEquals("This is a text with an entity \u000A", translatedText);
 assertEquals(5, translatedLength);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException should not be thrown");
 }
 }

 @Test
 public void testTranslationWithMissingSemiColon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 String input = "This is a text with an entity &amp;#10";
 Writer outputWriter = new StringWriter();
 try {
 int translatedLength = unescaper.translate(new StringReader(input), 0, outputWriter);
 fail("IllegalArgumentException should be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
 }
 }

 @Test
 public void testTranslationWithUppercaseHexadecimalEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 String input = "This is a text with an entity &amp;#xA";
 Writer outputWriter = new StringWriter();
 try {
 int translatedLength = unescaper.translate(new StringReader(input), 0, outputWriter);
 String translatedText = outputWriter.toString();
 assertEquals("This is a text with an entity \u0006", translatedText);
 assertEquals(5, translatedLength);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException should not be thrown");
 }
 }

 @Test
 public void testTranslationWithLargeIntegerEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 String input = "This is a text with an entity &amp;#65536";
 Writer outputWriter = new StringWriter();
 try {
 int translatedLength = unescaper.translate(new StringReader(input), 0, outputWriter);
 String translatedText = outputWriter.toString();
 String expectedText = Character.toString((char) 0xFFFD) + Character.toString((char) 0xFFFE);
 assertEquals(expectedText, translatedText);
 assertEquals(4, translatedLength);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException should not be thrown");
 }
 }

 @Test
 public void testTranslationWithErrorIfNoSemiColon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 String input = "This is a text with an entity &amp;#10";
 Writer outputWriter = new StringWriter();
 try {
 int translatedLength = unescaper.translate(new StringReader(input), 0, outputWriter);
 fail("IllegalArgumentException should be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
 }
 }

 @Test
 public void testTranslationWithInvalidEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 String input = "This is a text with an entity &amp;#abc";
 Writer outputWriter = new StringWriter();
 try {
 int translatedLength = unescaper.translate(new StringReader(input), 0, outputWriter);
 fail("Zero should be returned for invalid entity");
 } catch (IOException e) {
 // IOException should not be thrown
 }
 assertEquals(0, translatedLength);
 }
}