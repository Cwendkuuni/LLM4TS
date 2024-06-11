import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.Encoder;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

 @Test
 public void testCleanMethod() {
 String input = "A an Apple a day keeps the Doctor away!";
 String expectedOutput = "A A A A A A A A A";
 String actualOutput = SoundexUtils.clean(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDifferenceMethodWithSoundexEncoder() throws EncoderException {
 Encoder encoder = new Soundex();
 String str1 = "Apple";
 String str2 = "Orange";
 int expectedOutput = 0;
 int actualOutput = SoundexUtils.difference(encoder, str1, str2);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDifferenceMethodWithSoundexEncodedStrings() {
 String es1 = "A123";
 String es2 = "B456";
 int expectedOutput = 0;
 int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDifferenceMethodWithSoundexEncodedStringsSame() {
 String es1 = "A123";
 String es2 = "A123";
 int expectedOutput = 4;
 int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDifferenceMethodWithSoundexEncodedStringsDifferentButSimilar() {
 String es1 = "A123";
 String es2 = "A213";
 int expectedOutput = 3;
 int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = DecoderException.class)
 public void testDifferenceMethodWithNullString() {
 Encoder encoder = new Soundex();
 String str1 = null;
 String str2 = "Orange";
 int actualOutput = SoundexUtils.difference(encoder, str1, str2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDifferenceMethodWithNullEncoder() {
 String str1 = "Apple";
 String str2 = "Orange";
 int actualOutput = SoundexUtils.difference(null, str1, str2);
 }

 @Test(expected = NullPointerException.class)
 public void testDifferenceMethodWithNullString1() {
 Encoder encoder = new Soundex();
 String str1 = null;
 String str2 = "Orange";
 int actualOutput = SoundexUtils.difference(encoder, str1, str2);
 }

 @Test(expected = NullPointerException.class)
 public void testDifferenceMethodWithNullString2() {
 Encoder encoder = new Soundex();
 String str1 = "Apple";
 String str2 = null;
 int actualOutput = SoundexUtils.difference(encoder, str1, str2);
 }

}