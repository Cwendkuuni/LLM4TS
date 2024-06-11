import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateWithValidNumericEntity() {
 String input = "&amp;#123;";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("123", out.toString());
 assertEquals(3, length);
 }

 @Test
 public void testTranslateWithValidHexNumericEntity() {
 String input = "&amp;#x1F4";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("\u007F", out.toString());
 assertEquals(3, length);
 }

 @Test
 public void testTranslateWithValidLargeNumericEntity() {
 String input = "&amp;#65533";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("\ufffd", out.toString());
 assertEquals(3, length);
 }

 @Test
 public void testTranslateWithMissingSemiColon() {
 String input = "&amp;#123";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 StringWriter out = new StringWriter();
 try {
 int length = unescaper.translate(new StringReader(input), 0, out);
 fail("Expected IllegalArgumentException when semi-colon is missing");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testTranslateWithInvalidNumericEntity() {
 String input = "&amp;#xyz";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 @Test
 public void testTranslateWithNegativeNumericEntity() {
 String input = "&amp;#-123";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 @Test
 public void testTranslateWithLargeHexNumericEntity() {
 String input = "&amp;#xFFFF";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 @Test
 public void testTranslateWithInvalidHexNumericEntity() {
 String input = "&amp;#xz";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 @Test
 public void testIsSetWithValidOption() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
 assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
 }

 @Test
 public void testIsSetWithInvalidOption() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
 assertFalse(unescaper.isSet(OPTION.semiColonOptional));
 }
}