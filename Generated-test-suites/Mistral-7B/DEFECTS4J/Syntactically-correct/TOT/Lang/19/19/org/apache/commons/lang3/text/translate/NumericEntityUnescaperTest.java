import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateNumericEntity() {
 String input = "&amp;#160;&#x2019;&#x2018;&#48;&#128;&#163;&#7104;&#8203;&#8288;&#8211;&#8482;&#11044;&#11053;&#11056;&#11057;&#12288;";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringWriter writer = new StringWriter();

 // Test normal entities
 int length = unescaper.translate(new StringReader(input), 0, writer);
 assertEquals(" ‘’⁴℔℞⁻Å␠␤␺␻␼␽−ℙ⅃⅄ⅅ", writer.toString().substring(0, length));

 // Test entities with semicolon
 input = "&amp;#160;";
 length = unescaper.translate(new StringReader(input), 0, writer);
 assertEquals(" ", writer.toString().substring(0, length));

 // Test entities with only x in hex
 input = "&amp;#x41;";
 length = unescaper.translate(new StringReader(input), 0, writer);
 assertEquals("A", writer.toString().substring(0, length));

 // Test entities with invalid format
 input = "&amp;#xabcd;";
 try {
 length = unescaper.translate(new StringReader(input), 0, writer);
 } catch (NumberFormatException e) {
 // Expected
 }
 }
}