import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateNumericEntities() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 // Test numeric entities with semicolon
 String input1 = "&amp;#160;&#161;&#35;&#xA9;&#x1F449;&#128137;";
 String expectedOutput1 = "  á€€‰ acknowledgement";

 String output = "";
 try {
 unescaper.translate(new StringCharSequence(input1), 0, new StringWriter(output));
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expectedOutput1, output);

 // Test numeric entities without semicolon
 String input2 = "&amp;#128137";
 String expectedOutput2 = "‰";

 String output2 = "";
 try {
 unescaper.translate(new StringCharSequence(input2), 0, new StringWriter(output2));
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expectedOutput2, output2);

 // Test hexadecimal numeric entities
 String input3 = "&amp;#x1F449;";
 String expectedOutput3 = "‰";

 String output3 = "";
 try {
 unescaper.translate(new StringCharSequence(input3), 0, new StringWriter(output3));
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expectedOutput3, output3);

 // Test invalid numeric entities
 String input4 = "&amp;#abcd;";
 String expectedOutput4 = "";
 String output4 = "";
 try {
 unescaper.translate(new StringCharSequence(input4), 0, new StringWriter(output4));
 } catch (IOException e) {
 e.printStackTrace();
 } catch (NumberFormatException nfe) {
 expectedOutput4 = "FAIL: abcd[" + 3 + ":" + (3 + 3) + "]";
 }
 assertEquals(expectedOutput4, output4);
 }
}