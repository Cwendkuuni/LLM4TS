import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

 @Test
 public void testClean() {
 String input = "aBc DeF 123!@#";
 String expected = "ABC";
 String result = SoundexUtils.clean(input);
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceThrowsExceptionWithNullInput() {
 SoundexUtils.difference(null, null, null);
 }

 @Test
 public void testDifferenceWithNullOutput() {
 String s1 = "apple";
 String s2 = null;
 String encoder = new Soundex(); // Assuming Soundex is a valid encoder class
 int expected = 0;
 int result = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceWithEmptyStrings() {
 String s1 = "";
 String s2 = "";
 String encoder = new Soundex(); // Assuming Soundex is a valid encoder class
 int expected = 4; // Both empty strings have the same Soundex code
 int result = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceWithIdenticalStrings() {
 String s1 = "Apple";
 String s2 = "Apple";
 String encoder = new Soundex(); // Assuming Soundex is a valid encoder class
 int expected = 4;
 int result = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceWithDifferentStrings() {
 String s1 = "Apple";
 String s2 = "Banana";
 String encoder = new Soundex(); // Assuming Soundex is a valid encoder class
 int expected = 0;
 int result = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceWithSoundexEncodedStrings() {
 String s1Encoded = "A123";
 String s2Encoded = "A123";
 int expected = 4;
 int result = SoundexUtils.differenceEncoded(s1Encoded, s2Encoded);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceWithRefinedSoundexEncodedStrings() {
 String s1Encoded = "A1234";
 String s2Encoded = "A12345";
 int expected = 4; // Both strings have the same Soundex code but different refined Soundex codes
 int result = SoundexUtils.differenceEncoded(s1Encoded, s2Encoded);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceWithLongStrings() {
 String s1 = "The quick brown fox jumps over the lazy dog";
 String s2 = "The quick red fox jumps over the lazy dog";
 String encoder = new Soundex(); // Assuming Soundex is a valid encoder class
 int expected = 3; // The number of identical Soundex characters in the strings
 int result = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(expected, result);
 }

}