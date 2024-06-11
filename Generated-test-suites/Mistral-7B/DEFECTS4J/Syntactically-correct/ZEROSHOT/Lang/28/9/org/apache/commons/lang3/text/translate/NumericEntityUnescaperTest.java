import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private final CharSequenceTranslator unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateSingleHex() throws IOException {
 final String input = "&#[xA9];";
 int expectedOutputLength = 1;
 int outputLength = unescaper.translate(new String(input), 0, new StringBuilder()).length();
 char expectedOutput = (char) 0xC5;
 char output = unescaper.translate(new String(input), 0, new StringBuilder()).charAt(0);

 assertEquals(expectedOutputLength, outputLength);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testTranslateSingleDecimal() throws IOException {
 final String input = "&#[128];";
 int expectedOutputLength = 1;
 int outputLength = unescaper.translate(new String(input), 0, new StringBuilder()).length();
 char expectedOutput = (char) 0x80;
 char output = unescaper.translate(new String(input), 0, new StringBuilder()).charAt(0);

 assertEquals(expectedOutputLength, outputLength);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testTranslateMultipleEntities() throws IOException {
 final String input = "&#[x41];&#[161];&#[x1F4];";
 int expectedOutputLength = 3;
 int outputLength = unescaper.translate(new String(input), 0, new StringBuilder()).length();
 char expectedOutput1 = (char) 0x41;
 char expectedOutput2 = (char) 0xAF;
 char expectedOutput3 = (char) 0xF0;
 char output1 = unescaper.translate(new String(input), 0, new StringBuilder()).charAt(0);
 char output2 = unescaper.translate(new String(input), 0, new StringBuilder()).charAt(1);
 char output3 = unescaper.translate(new String(input), 0, new StringBuilder()).charAt(2);

 assertEquals(expectedOutputLength, outputLength);
 assertEquals(expectedOutput1, output1);
 assertEquals(expectedOutput2, output2);
 assertEquals(expectedOutput3, output3);
 }

 @Test
 public void testTranslateEntitiesWithSpaces() throws IOException {
 final String input = "&#[x41]; & & #[161]; & & #[x1F4];";
 int expectedOutputLength = 3;
 int outputLength = unescaper.translate(new String(input), 0, new StringBuilder()).length();
 char expectedOutput1 = (char) 0x41;
 char expectedOutput2 = (char) 0xAF;
 char expectedOutput3 = (char) 0xF0;
 char output1 = unescaper.translate(new String(input), 0, new StringBuilder()).charAt(0);
 char output2 = unescaper.translate(new String(input), 0, new StringBuilder()).charAt(2);
 char output3 = unescaper.translate(new String(input), 0, new StringBuilder()).charAt(4);

 assertEquals(expectedOutputLength, outputLength);
 assertEquals(expectedOutput1, output1);
 assertEquals(expectedOutput2, output2);
 assertEquals(expectedOutput3, output3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateInvalidEntity() throws IOException {
 final String input = "&#[xyz];";
 unescaper.translate(new String(input), 0, new StringBuilder());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testTranslateIndexOutOfBounds() throws IOException {
 final String input = "&#[x41];";
 unescaper.translate(new String(input), 3, new StringBuilder());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNullInput() {
 try {
 unescaper.translate(null, 0, new StringBuilder());
 } catch (IOException e) {
 // expected
 }
 }
}