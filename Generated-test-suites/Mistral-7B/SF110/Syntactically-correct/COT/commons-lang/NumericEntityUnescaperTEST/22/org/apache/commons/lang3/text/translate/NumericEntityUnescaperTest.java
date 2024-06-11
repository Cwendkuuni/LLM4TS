import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper;

 @Test
 public void testNumericEntityUnescaperInstance() {
 unescaper = new NumericEntityUnescaper();
 assertNotNull(unescaper);
 }

 @Test
 public void testNumericEntityUnescaperWithOptions() {
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 assertNotNull(unescaper);
 assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 assertFalse(unescaper.isSet(OPTION.semiColonOptional));
 assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));
 }

 @Test
 public void testNumericEntityUnescaperWithNoOptions() {
 unescaper = new NumericEntityUnescaper();
 assertNotNull(unescaper);
 assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 }

 @Test
 public void testTranslateBasic() throws IOException {
 String input = "&num;";
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("num", out.toString());
 assertEquals(3, length);
 }

 @Test
 public void testTranslateNumericEntity() throws IOException {
 String input = "&num;";
 Writer out = new StringWriter();
 unescaper.translate(new StringReader(input), 0, out);
 assertEquals("num", out.toString());
 }

 @Test
 public void testTranslateNumericEntityWithSemiColon() throws IOException {
 String input = "&num;";
 Writer out = new StringWriter();
 unescaper.translate(new StringReader(input), 0, out);
 assertEquals("num", out.toString());
 String output = out.toString();
 unescaper.translate(new StringReader(output + ";"), 0, out);
 assertEquals("num;", out.toString());
 }

 @Test
 public void testTranslateHexNumericEntity() throws IOException {
 String input = "&#x41;";
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("A", out.toString());
 assertEquals(2, length);
 }

 @Test
 public void testTranslateHexNumericEntityWithSemiColon() throws IOException {
 String input = "&#x41;";
 Writer out = new StringWriter();
 unescaper.translate(new StringReader(input), 0, out);
 assertEquals("A", out.toString());
 String output = out.toString();
 unescaper.translate(new StringReader(output + ";"), 0, out);
 assertEquals("A;", out.toString());
 }

 @Test
 public void testTranslateInvalidNumericEntity() throws IOException {
 String input = "&#xinvalid;";
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 @Test
 public void testTranslateInvalidNumericEntityWithSemiColon() throws IOException {
 String input = "&#xinvalid;";
 Writer out = new StringWriter();
 unescaper.translate(new StringReader(input), 0, out);
 assertEquals("", out.toString());
 String output = out.toString();
 unescaper.translate(new StringReader(output + ";"), 0, out);
 assertEquals("", out.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateMissingSemiColon() throws IOException {
 String input = "&num;";
 Writer out = new StringWriter();
 unescaper.translate(new StringReader(input), 0, out);
 assertEquals("num", out.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateMissingSemiColonWithOptions() throws IOException {
 unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 String input = "&num;";
 Writer out = new StringWriter();
 unescaper.translate(new StringReader(input), 0, out);
 assertEquals("num", out.toString());
 }

}