import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private static final String NUMERIC_ENTITY = "&amp;#160; &#x2013; &#xFFFD; &amp;#0065; &amp;#x1F4A9; &amp;#123456; &amp;#xFFFE; &amp;#65536; &amp;#x12345678; &amp;#x7FFF;";

 @Test
 public void testTranslateWithDefaultOptions() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 String input = "This is a test &" + NUMERIC_ENTITY + ";";
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("This is a test " + NUMERIC_ENTITY + ";", input.substring(0, input.length() - 1));
 assertEquals(input.length() + NUMERIC_ENTITY.length() - 1, length);
 assertEquals(input, out.toString());
 }

 @Test
 public void testTranslateWithSemiColonRequiredOption() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 String input = "This is a test &" + NUMERIC_ENTITY;
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 fail("Expected IllegalArgumentException: Semi-colon required at end of numeric entity");
 }

 @Test
 public void testTranslateWithSemiColonOptionalOption() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 String input = "This is a test &" + NUMERIC_ENTITY;
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("This is a test " + NUMERIC_ENTITY, out.toString());
 }

 @Test
 public void testTranslateWithErrorIfNoSemiColonOption() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 String input = "This is a test &" + NUMERIC_ENTITY;
 Writer out = new StringWriter();
 try {
 unescaper.translate(new StringReader(input), 0, out);
 fail("Expected IllegalArgumentException: Semi-colon required at end of numeric entity");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testTranslateInvalidNumericEntity() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 String input = "This is a test &%#;";
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("This is a test &%#;", input);
 assertEquals(input.length(), length);
 assertEquals("", out.toString());
 }

 @Test
 public void testTranslateLongNumericEntity() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 String input = "This is a test &#x12345678;";
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("This is a test \u12345678", input);
 assertEquals(input.length() + 4, length);
 assertEquals("\u12345678", out.toString());
 }

 @Test
 public void testTranslateHexNumericEntity() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 String input = "This is a test &#x1F4A9;";
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("This is a test 𠮷", input);
 assertEquals(input.length() + 4, length);
 assertEquals("𠮷", out.toString());
 }
}