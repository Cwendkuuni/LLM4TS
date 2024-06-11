import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper;

 @Before
 public void setup() {
 unescaper = new NumericEntityUnescaper();
 }

 /**
 * Test translating a numeric entity with a decimal value.
 */
 @Test
 public void testDecimalEntity() {
 String input = "&#10;";
 String expectedOutput = "\n";
 try {
 char[] output = new char[10];
 int length = unescaper.translate(new String(input), 0, new StringWriter(output).getBuffer());
 Assert.assertEquals(expectedOutput, new String(output, 0, length));
 } catch (IOException e) {
 // This should not be thrown, as we're using a StringWriter
 }
 }

 /**
 * Test translating a numeric entity with a hexadecimal value.
 */
 @Test
 public void testHexEntity() {
 String input = "&#[x41];";
 String expectedOutput = "A";
 try {
 char[] output = new char[10];
 int length = unescaper.translate(new String(input), 0, new StringWriter(output).getBuffer());
 Assert.assertEquals(expectedOutput, new String(output, 0, length));
 } catch (IOException e) {
 // This should not be thrown, as we're using a StringWriter
 }
 }

 /**
 * Test translating an entity with an invalid number format.
 */
 @Test(expected = NumberFormatException.class)
 public void testInvalidEntity() {
 String input = "&#[abc];";
 unescaper.translate(new String(input), 0, new StringWriter(new char[10]).getBuffer());
 }

 /**
 * Test translating an entity with an entity inside the entity.
 */
 @Test
 public void testNestedEntities() {
 String input = "&#[x26];#32;";
 String expectedOutput = "&";
 try {
 char[] output = new char[20];
 int length = unescaper.translate(new String(input), 0, new StringWriter(output).getBuffer());
 Assert.assertEquals(expectedOutput, new String(output, 0, length));
 } catch (IOException e) {
 // This should not be thrown, as we're using a StringWriter
 }
 }

 /**
 * Test translating an empty string.
 */
 @Test
 public void testEmptyString() {
 String input = "";
 String expectedOutput = "";
 try {
 char[] output = new char[10];
 int length = unescaper.translate(new String(input), 0, new StringWriter(output).getBuffer());
 Assert.assertEquals(expectedOutput, new String(output, 0, length));
 } catch (IOException e) {
 // This should not be thrown, as we're using a StringWriter
 }
 }
}