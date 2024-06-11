import org.apache.commons.codec.language.Soundex;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Soundex.java
 */
public class SoundexTest {

 @Test
 public void testSoundexUSEnglish() {
 Soundex soundex = Soundex.US_ENGLISH;
 String[] input = { "apple", "appel", "application", "approval" };
 String[] expected = { "A100", "A111", "A120", "A130" };
 for (int i = 0; i < input.length; i++) {
 String result = soundex.encode(input[i]);
 Assert.assertEquals(expected[i], result);
 }
 }

 @Test
 public void testSoundexCustomMapping() {
 char[] customMapping = { '0', '1', '1', '2', '0', '2', '2', '3', '1', '1', '1', '1', '1', '0', '0', '0' };
 Soundex soundex = new Soundex(customMapping);
 String[] input = { "apple", "appel", "application", "approval" };
 String[] expected = { "A111", "A111", "A220", "A130" }; // different mapping results in different codes
 for (int i = 0; i < input.length; i++) {
 String result = soundex.encode(input[i]);
 Assert.assertEquals(expected[i], result);
 }
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNonString() {
 Soundex soundex = new Soundex();
 Object nonString = new Object();
 soundex.encode(nonString);
 }

 @Test
 public void testDifference() throws EncoderException {
 Soundex soundex = Soundex.US_ENGLISH;
 String[] input1 = { "apple", "appel" };
 String[] input2 = { "application", "approval" };
 int[] expected = { 1, 0 };
 for (int i = 0; i < input1.length; i++) {
 int result = soundex.difference(input1[i], input2[0]);
 Assert.assertEquals(expected[i], result);
 }
 }

 @Test
 public void testDifferenceSameString() throws EncoderException {
 Soundex soundex = Soundex.US_ENGLISH;
 String input = "apple";
 int result = soundex.difference(input, input);
 Assert.assertEquals(4, result);
 }
}