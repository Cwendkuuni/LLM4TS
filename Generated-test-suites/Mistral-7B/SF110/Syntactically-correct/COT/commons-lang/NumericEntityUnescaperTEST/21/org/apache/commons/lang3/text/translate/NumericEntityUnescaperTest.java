import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper;

 @Test
 public void testConstructors() {
 unescaper = new NumericEntityUnescaper();
 unescaper.isSet(OPTION.semiColonRequired);
 unescaper.isSet(OPTION.semiColonOptional);
 unescaper.isSet(OPTION.errorIfNoSemiColon);
 }

 @Test
 public void testIsSet() {
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 assertFalse(unescaper.isSet(OPTION.semiColonOptional));
 assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));

 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.semiColonOptional);
 assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 assertTrue(unescaper.isSet(OPTION.semiColonOptional));
 assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));

 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.semiColonOptional, OPTION.errorIfNoSemiColon);
 assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 assertTrue(unescaper.isSet(OPTION.semiColonOptional));
 assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
 }

 @Test
 public void testTranslateWithSingleNumericEntity() throws IOException {
 char[] input = {'&', '#', '3'};
 Writer out = new StringWriter();
 unescaper.translate(new String(input), 0, out);
 assertEquals("\u0003", out.toString());
 }

 @Test
 public void testTranslateWithSingleNumericEntityHex() throws IOException {
 char[] input = {'&', '#', 'x', '4'};
 Writer out = new StringWriter();
 unescaper.translate(new String(input), 0, out);
 assertEquals("\u0004", out.toString());
 }

 @Test
 public void testTranslateMultipleNumericEntities() throws IOException {
 char[] input = {'&', '#', '3', ';', '&', '#', 'x', 'F', ';'};
 Writer out = new StringWriter();
 unescaper.translate(new String(input), 0, out);
 assertEquals("\u0003;\u000F", out.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithMissingSemiColon() throws IOException {
 char[] input = {'&', '#', '3'};
 Writer out = new StringWriter();
 unescaper.translate(new String(input), 0, out);
 fail("Expected IllegalArgumentException for missing semicolon");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithInvalidNumericEntity() throws IOException {
 char[] input = {'&', '#', 'x', 'a'};
 Writer out = new StringWriter();
 unescaper.translate(new String(input), 0, out);
 fail("Expected IllegalArgumentException for invalid numeric entity");
 }

 @Test
 public void testTranslateWithSemiColonOptional() throws IOException {
 unescaper.isSet(OPTION.semiColonOptional);
 char[] input = {'&', '#', '3'};
 Writer out = new StringWriter();
 unescaper.translate(new String(input), 0, out);
 assertEquals("\u0003", out.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithErrorIfNoSemiColon() throws IOException {
 unescaper.isSet(OPTION.errorIfNoSemiColon);
 char[] input = {'&', '#', '3'};
 Writer out = new StringWriter();
 unescaper.translate(new String(input), 0, out);
 fail("Expected IllegalArgumentException for missing semicolon");
 }

}