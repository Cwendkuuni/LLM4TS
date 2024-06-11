import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.test.CharSequenceTestUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 @Test
 public void testNumericEntityUnescaper() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 // Test single character entities
 CharSequence input1 = "&#128;";
 int length1 = unescaper.translate(input1, 0, CharSequenceTestUtils.newWriterToString());
 assertEquals("ဠ", input1.subSequence(0, input1.length()).toString());
 assertEquals(2, length1);

 CharSequence input2 = "&amp;";
 int length2 = unescaper.translate(input2, 0, CharSequenceTestUtils.newWriterToString());
 assertEquals("&", input2.subSequence(0, input2.length()).toString());
 assertEquals(1, length2);

 // Test entities with semicolon
 CharSequence input3 = "&#x41;";
 int length3 = unescaper.translate(input3, 0, CharSequenceTestUtils.newWriterToString());
 assertEquals("A", input3.subSequence(0, input3.length()).toString());
 assertEquals(1, length3);

 CharSequence input4 = "&#[xCA]FF;";
 int length4 = unescaper.translate(input4, 0, CharSequenceTestUtils.newWriterToString());
 assertEquals("\uCAFF", input4.subSequence(0, input4.length()).toString());
 assertEquals(3, length4);

 // Test entities with multiple characters
 CharSequence input5 = "&#[x1F000]&\u00A9;";
 int length5 = unescaper.translate(input5, 0, CharSequenceTestUtils.newWriterToString());
 assertEquals("\uD83C\uDD10\u00A9", input5.subSequence(0, input5.length()).toString());
 assertEquals(5, length5);
 }

 @Test(expected = IOException.class)
 public void testNumericEntityUnescaperWithInvalidEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 CharSequence input = "&#xabcd"; // This entity value is not an integer
 unescaper.translate(input, 0, CharSequenceTestUtils.newWriterToString());
 }

 @Test(expected = NumberFormatException.class)
 public void testNumericEntityUnescaperWithInvalidHexValue() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 CharSequence input = "&#[xz]"; // z is not a hexadecimal digit
 unescaper.translate(input, 0, CharSequenceTestUtils.newWriterToString());
 }
}