import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

 private Soundex soundex = new Soundex();

 @Test
 public void testDefaultSoundexEncoding() throws EncoderException {
 String original = "Apple";
 String expected = "A113";
 String result = soundex.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testCustomSoundexEncoding() throws EncoderException {
 Soundex customSoundex = new Soundex(new String("1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray());
 String original = "Apple";
 String expected = "A113";
 String result = customSoundex.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodingNullString() {
 String original = null;
 try {
 String result = soundex.encode(original);
 fail("Should have thrown EncoderException for null String input");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testEncodingEmptyString() {
 String original = "";
 String expected = "";
 String result = soundex.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodingSingleCharacterString() throws EncoderException {
 String original = "A";
 String expected = "A000";
 String result = soundex.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodingMultipleSameCharacterString() throws EncoderException {
 String original = "AAA";
 String expected = "A111";
 String result = soundex.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodingStringWithDigits() throws EncoderException {
 String original = "Apple123";
 String expected = "A113";
 String result = soundex.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodingStringWithSpecialCharacters() throws EncoderException {
 String original = "Apple$#@%";
 String expected = "A113";
 String result = soundex.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodingStringWithMultipleCodeGroups() throws EncoderException {
 String original = "AppleApple";
 String expected = "A113A113";
 String result = soundex.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodingStringWithDoubleLetterAtTheBeginning() throws EncoderException {
 String original = "AppleEe";
 String expected = "A113E0";
 String result = soundex.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodingStringWithDoubleLetterInTheMiddle() throws EncoderException {
 String original = "AppleEpple";
 String expected = "A113E113";
 String result = soundex.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodingStringWithDoubleLetterAtTheEnd() throws EncoderException {
 String original = "AppleEe";
 String expected = "A113E0";
 String result = soundex.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodingStringWithDoubleDigitsAtTheBeginning() throws EncoderException {
 String original = "11Apple";
 String expected = "11A111";
 String result = soundex.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodingStringWithDoubleDigitsInTheMiddle() throws EncoderException {
 String original = "Apple11Apple";
 String expected = "A11311A113";
 String result = soundex.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodingStringWithDoubleDigitsAtTheEnd() throws EncoderException {
 String original = "Apple11";
 String expected = "A11311";
 String result = soundex.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodingStringWithMixedSpecialCharacters() throws EncoderException {
 String original = "Apple$#@%&Apple$#@%&";
 String expected = "A113#0#A113#0#";
 String result = soundex.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodingStringWithMixedSpecialAndDigits() throws EncoderException {
 String original = "Apple$#@%123Apple$#@%123";
 String expected = "A113#0#123A113#0#123";
 String result = soundex.encode(original);
 assertEquals(expected, result);
 }

 @Test (expected = EncoderException.class)
 public void testEncodingStringWithInvalidCharacter() throws EncoderException {
 String original = "Apple!";
 String result = soundex.encode(original);
 fail("Should have thrown EncoderException for invalid character !");
 }

}