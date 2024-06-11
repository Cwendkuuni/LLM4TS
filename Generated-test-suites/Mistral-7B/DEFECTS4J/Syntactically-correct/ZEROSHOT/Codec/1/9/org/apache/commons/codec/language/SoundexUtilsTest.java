import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

 @Test
 public void testClean() {
 String input = "A an Apple a day keeps Doctor away!";
 String expected = "A A A A A A A A";
 String result = SoundexUtils.clean(input);
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceNegative() {
 String encoderClass = null;
 String s1 = "apple";
 String s2 = "banana";
 SoundexUtils.difference(encoderClass, s1, s2);
 }

 @Test
 public void testDifferenceSoundex() {
 String encoderClass = SoundexUtils.class.getPackage().getName() + ".Soundex";
 String s1 = "Apple";
 String s2 = "Apple";
 int expected = 4;
 int result = SoundexUtils.difference(new Soundex(), s1, s2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceSoundexDifferent() {
 String encoderClass = SoundexUtils.class.getPackage().getName() + ".Soundex";
 String s1 = "Apple";
 String s2 = "Orange";
 int expected = 0;
 int result = SoundexUtils.difference(new Soundex(), s1, s2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceRefinedSoundex() {
 String encoderClass = SoundexUtils.class.getPackage().getName() + ".RefinedSoundex";
 String s1 = "Apple";
 String s2 = "Orange";
 int expected = 1;
 int result = SoundexUtils.difference(new RefinedSoundex(), s1, s2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceRefinedSoundexSame() {
 String encoderClass = SoundexUtils.class.getPackage().getName() + ".RefinedSoundex";
 String s1 = "Apple";
 String s2 = "Apple";
 int expected = 4;
 int result = SoundexUtils.difference(new RefinedSoundex(), s1, s2);
 assertEquals(expected, result);
 }

}