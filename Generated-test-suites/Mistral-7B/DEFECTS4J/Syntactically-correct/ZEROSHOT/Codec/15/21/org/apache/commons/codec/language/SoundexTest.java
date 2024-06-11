import org.apache.commons.codec.language.Soundex;
import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

 @Test
 public void testSoundexUSEnglish() {
 String originalStrings[] = { "apple", "appel", "apfel", "app", "about", "aboot", "above", "applied" };
 String expectedSoundexCodes[] = { "A111", "A111", "A111", "A111", "A001", "A001", "A000", "A112" };

 Soundex soundexInstance = Soundex.US_ENGLISH;

 for (int i = 0; i < originalStrings.length; i++) {
 String soundexCode = soundexInstance.soundex(originalStrings[i]);
 Assert.assertEquals(expectedSoundexCodes[i], soundexCode);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCharacter() {
 Soundex soundexInstance = new Soundex(Soundex.US_ENGLISH_MAPPING);
 String invalidString = "!apple";
 String soundexCode = "";
 try {
 soundexCode = soundexInstance.soundex(invalidString);
 } catch (EncoderException e) {
 // EncoderException is thrown for Objects that are not Strings, ignore it
 }
 Assert.assertEquals("", soundexCode);
 }

 @Test
 public void testSameStringsDifference() {
 String string1 = "Apple";
 String string2 = "Apple";
 int difference = new Soundex(Soundex.US_ENGLISH_MAPPING).difference(string1, string2);
 Assert.assertEquals(4, difference);
 }

 @Test
 public void testDifferentStringsDifference() {
 String string1 = "Apple";
 String string2 = "Banana";
 int difference = new Soundex(Soundex.US_ENGLISH_MAPPING).difference(string1, string2);
 Assert.assertEquals(0, difference);
 }
}