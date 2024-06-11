import org.apache.commons.codec.language.SoundexUtils;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

 @Test
 public void testCleanMethod() {
 String input = "Aäéèïíóöúü";
 String expected = "AAEIIIOOUU";
 String result = SoundexUtils.clean(input);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceMethodWithSoundex() throws Exception {
 StringEncoder encoder = new org.apache.commons.codec.language.soundex.Soundex();
 String string1 = "apple";
 String string2 = "application";
 int expected = 3;
 int result = SoundexUtils.difference(encoder, string1, string2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceMethodWithRefinedSoundex() throws Exception {
 StringEncoder encoder = new org.apache.commons.codec.language.soundex.Soundex();
 String string1 = "knight";
 String string2 = "night";
 int expected = 5;
 int result = SoundexUtils.difference(encoder, string1, string2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedMethodWithSoundex() {
 String encodedString1 = "K223121";
 String encodedString2 = "K223100";
 int expected = 3;
 int result = SoundexUtils.differenceEncoded(encodedString1, encodedString2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedMethodWithRefinedSoundex() {
 String encodedString1 = "K2231213111";
 String encodedString2 = "K2231213112";
 int expected = 4;
 int result = SoundexUtils.differenceEncoded(encodedString1, encodedString2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedMethodWithDifferentLengthStrings() {
 String encodedString1 = "K223121";
 String encodedString2 = "K2231213112";
 int expected = 3;
 int result = SoundexUtils.differenceEncoded(encodedString1, encodedString2);
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceMethodWithNullStrings() throws Exception {
 StringEncoder encoder = new org.apache.commons.codec.language.soundex.Soundex();
 String string1 = null;
 String string2 = "application";
 int result = SoundexUtils.difference(encoder, string1, string2);
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceMethodWithNullEncoder() throws Exception {
 String string1 = "apple";
 String string2 = "application";
 int result = SoundexUtils.difference(null, string1, string2);
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceMethodWithNullString1() throws Exception {
 StringEncoder encoder = new org.apache.commons.codec.language.soundex.Soundex();
 String string1 = null;
 String string2 = "application";
 int result = SoundexUtils.difference(encoder, string1, string2);
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceMethodWithNullString2() throws Exception {
 StringEncoder encoder = new org.apache.commons.codec.language.soundex.Soundex();
 String string1 = "apple";
 String string2 = null;
 int result = SoundexUtils.difference(encoder, string1, string2);
 }

 @Test
 public void testCleanMethodWithEmptyString() {
 String input = "";
 String expected = "";
 String result = SoundexUtils.clean(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCleanMethodWithNullInput() {
 String expected = "";
 String result = SoundexUtils.clean(null);
 assertEquals(expected, result);
 }
}