import org.apache.commons.codec.language.SoundexUtils;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

 private final StringEncoder soundexEncoder = new org.apache.commons.codec.language.Soundex();

 @Test
 public void testClean() {
 String input = "The quick Brown Fox jumps over the lazy dog.";
 String expectedOutput = "The quick Brown Fox jumps over the lazy dog".toUpperCase();
 String actualOutput = SoundexUtils.clean(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceWithNullEncoder() {
 String s1 = "Apple";
 String s2 = "Banana";
 SoundexUtils.difference(null, s1, s2);
 }

 @Test(expected = EncoderException.class)
 @Test(expected = NullPointerException.class)
 public void testDifferenceWithNullStrings() {
 String s1 = null;
 String s2 = "Banana";
 SoundexUtils.difference(soundexEncoder, s1, s2);
 s1 = "Apple";
 s2 = null;
 SoundexUtils.difference(soundexEncoder, s1, s2);
 }

 @Test
 public void testDifferenceWithEmptyStrings() {
 String s1 = "";
 String s2 = "";
 int actualOutput = SoundexUtils.differenceEncoded(soundexEncoder.encode(s1), soundexEncoder.encode(s2));
 int expectedOutput = 4;
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDifferenceWithIdenticalStrings() {
 String s1 = "Apple";
 String s2 = "Apple";
 int actualOutput = SoundexUtils.differenceEncoded(soundexEncoder.encode(s1), soundexEncoder.encode(s2));
 int expectedOutput = 4;
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDifferenceWithSoundexIdenticalStrings() {
 String s1 = "Apple";
 String s2 = "Aple";
 int actualOutput = SoundexUtils.differenceEncoded(soundexEncoder.encode(s1), soundexEncoder.encode(s2));
 int expectedOutput = 3;
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDifferenceWithSoundexDifferentStrings() {
 String s1 = "Apple";
 String s2 = "Apricot";
 int actualOutput = SoundexUtils.differenceEncoded(soundexEncoder.encode(s1), soundexEncoder.encode(s2));
 int expectedOutput = 0;
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDifferenceWithRefinedSoundexIdenticalStrings() {
 String s1 = "Apple";
 String s2 = "Apple";
 int actualOutput = SoundexUtils.differenceEncoded(soundexEncoder.encode(s1, '0'), soundexEncoder.encode(s2, '0'));
 int expectedOutput = 4;
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDifferenceWithRefinedSoundexDifferentStrings() {
 String s1 = "Apple";
 String s2 = "Apricot";
 int actualOutput = SoundexUtils.differenceEncoded(soundexEncoder.encode(s1, '0'), soundexEncoder.encode(s2, '0'));
 int expectedOutput = 2;
 assertEquals(expectedOutput, actualOutput);
 }

}