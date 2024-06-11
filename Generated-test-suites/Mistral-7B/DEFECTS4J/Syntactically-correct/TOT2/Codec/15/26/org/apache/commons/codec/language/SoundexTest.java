import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

 Soundex soundex = new Soundex(); // default US_ENGLISH instance

 @Test
 public void testEncodeString() {
 String input = "Apple";
 String expected = "A113";
 String output = soundex.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testEncodeNullString() {
 assertNull(soundex.encode(null));
 }

 @Test
 public void testEncodeEmptyString() {
 String output = soundex.encode("");
 assertEquals("", output);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNonStringObject() {
 soundex.encode(123);
 }

 @Test
 public void testDifferenceTwoIdenticalStrings() {
 String s1 = "Apple";
 String s2 = "Apple";
 int difference = soundex.difference(s1, s2);
 assertEquals(4, difference);
 }

 @Test
 public void testDifferenceTwoDifferentStringsWithSameSoundexCode() {
 String s1 = "Apple";
 String s2 = "Apricot";
 int difference = soundex.difference(s1, s2);
 assertEquals(3, difference);
 }

 @Test
 public void testDifferenceTwoDifferentStringsWithDifferentSoundexCode() {
 String s1 = "Apple";
 String s2 = "Banana";
 int difference = soundex.difference(s1, s2);
 assertEquals(0, difference);
 }

 @Test
 public void testCustomSoundexMapping() {
 Soundex customSoundex = new Soundex(new String("12345").toCharArray());
 String input = "Apple";
 String expected = "1134";
 String output = customSoundex.encode(input);
 assertEquals(expected, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMappingCodeWithNonAlphabeticCharacter() {
 String str = "Apple#";
 int index = 3;
 char mappedChar = soundex.getMappingCode(str, index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMappingCodeWithNegativeIndex() {
 String str = "Apple";
 int index = -1;
 char mappedChar = soundex.getMappingCode(str, index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMapCharacterOutOfBound() {
 Soundex soundexObj = new Soundex(new char[]{'A', 'B', 'C', 'D', 'E'});
 char ch = 'Z';
 char mappedChar = soundexObj.map(ch);
 }

 @Test
 public void testSoundexWithMultipleConsecutiveSameLetters() {
 String input = "Appple";
 String expected = "A113";
 String output = soundex.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testSoundexWithMultipleConsecutiveDifferentLetters() {
 String input = "ApplBle";
 String expected = "A113B";
 String output = soundex.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testSoundexWithMultipleConsecutiveSameLettersAndDifferentLetters() {
 String input = "AppplBllle";
 String expected = "A113B3";
 String output = soundex.encode(input);
 assertEquals(expected, output);
 }

}