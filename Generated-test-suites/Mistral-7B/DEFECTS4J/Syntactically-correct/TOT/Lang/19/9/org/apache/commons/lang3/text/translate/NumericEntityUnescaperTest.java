import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateValidNumericEntities() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp; &lt; &gt; &apos; &quot; &amp;#128; &amp;#X61; &amp;#x1F4A9;");

 Writer out = new StringBuilder();
 unescaper.translate(input, 0, out);

 String expectedOutput = "& amp; < > ' &quot; ¶ a 🦋";
 assertEquals(expectedOutput, out.toString());
 }

 @Test
 public void testTranslateValidNumericEntitiesWithSemicolon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp;#1234; &amp;#X65536;");

 Writer out = new StringBuilder();
 unescaper.translate(input, 0, out);

 String expectedOutput = "𝄞 🔝";
 assertEquals(expectedOutput, out.toString());
 }

 @Test(expected = IOException.class)
 public void testTranslateMalformedNumericEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp;&#x; &amp;#abc; &amp;#1234567890");

 Writer out = new StringBuilder();
 unescaper.translate(input, 0, out);
 }

 @Test
 public void testTranslateValidNumericEntityOutOfRange() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp;#65536; &amp;#x110000;");

 Writer out = new StringBuilder();
 unescaper.translate(input, 0, out);

 String expectedOutput = "🔝 😱";
 assertEquals(expectedOutput, out.toString());
 }

 @Test
 public void testTranslateInvalidNumericEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp;#abc;");

 Writer out = new StringBuilder();
 unescaper.translate(input, 0, out);

 String expectedOutput = "&amp;#abc;";
 assertEquals(expectedOutput, out.toString());
 }

 @Test
 public void testTranslateInvalidNumericEntityWithSemicolon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp;#xabc;");

 Writer out = new StringBuilder();
 unescaper.translate(input, 0, out);

 String expectedOutput = "&amp;#xabc;";
 assertEquals(expectedOutput, out.toString());
 }
}