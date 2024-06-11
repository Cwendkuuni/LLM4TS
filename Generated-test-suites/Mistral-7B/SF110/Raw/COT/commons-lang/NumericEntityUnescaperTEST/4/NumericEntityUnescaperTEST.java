import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper;

 @Test
 public void testNumericEntityUnescaperConstructorWithOptions() {
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
 assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
 }

 @Test
 public void testNumericEntityUnescaperConstructorWithoutOptions() {
 unescaper = new NumericEntityUnescaper();
 assertFalse(unescaper.isSet(OPTION.semiColonRequired));
 assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));
 }

 @Test
 public void testIsSet() {
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
 assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
 unescaper = new NumericEntityUnescaper();
 assertFalse(unescaper.isSet(OPTION.semiColonRequired));
 assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));
 }

 @Test
 public void testTranslateWithValidNumericEntities() throws IOException {
 char[] input = {'&', '#', '0', ';'};
 Writer out = new StringWriter();
 int length = unescaper.translate(new String(input), 0, out);
 assertEquals(4, length);
 assertEquals("0", out.toString());

 input = new char[]{'&', '#', 'x', '3', 'f'};
 out = new StringWriter();
 length = unescaper.translate(new String(input), 0, out);
 assertEquals(6, length);
 assertEquals("ƒ", out.toString());
 }

 @Test
 public void testTranslateWithInvalidNumericEntity() throws IOException {
 char[] input = {'&', '#', 'x', 'a'}; // invalid hex character 'a'
 Writer out = new StringWriter();
 int length = unescaper.translate(new String(input), 0, out);
 assertEquals(0, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithMissingSemiColon() throws IOException {
 char[] input = {'&', '#', '0'};
 Writer out = new StringWriter();
 unescaper.translate(new String(input), 0, out);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithSetErrorIfNoSemiColon() throws IOException {
 unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 char[] input = {'&', '#', '0'};
 Writer out = new StringWriter();
 unescaper.translate(new String(input), 0, out);
 }

}