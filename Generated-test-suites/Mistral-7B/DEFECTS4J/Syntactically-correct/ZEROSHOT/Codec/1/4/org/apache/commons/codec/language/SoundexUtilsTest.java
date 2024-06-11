import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

 @Test
 public void testClean() {
 String input = "aBc1DeF2Gh! iJkLmNoPqRsTuVwXyZ";
 String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
 String result = SoundexUtils.clean(input);
 assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullInput() {
 String input = null;
 String result = SoundexUtils.clean(input);
 // NullPointerException should be thrown
 }

 @Test
 public void testDifferenceNullInput() {
 String encoderType = "Soundex";
 String input1 = null;
 String input2 = "TestString";
 try {
 int result = SoundexUtils.difference(new Soundex(), input1, input2);
 fail("NullPointerException expected");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceEncoderException() {
 String encoderType = "InvalidEncoder";
 String input1 = "TestString1";
 String input2 = "TestString2";
 SoundexUtils soundexUtils = new SoundexUtils();
 try {
 int result = soundexUtils.difference(new InvalidEncoder(), input1, input2);
 fail("EncoderException expected");
 } catch (EncoderException e) {
 // expected
 }
 }

 @Test
 public void testDifferenceEmptyStrings() {
 String encoderType = "Soundex";
 String input1 = "";
 String input2 = "";
 int result = SoundexUtils.difference(new Soundex(), input1, input2);
 assertEquals(4, result);
 }

 @Test
 public void testDifferenceIdenticalStrings() {
 String encoderType = "Soundex";
 String input1 = "Apple";
 String input2 = "Apple";
 int result = SoundexUtils.difference(new Soundex(), input1, input2);
 assertEquals(4, result);
 }

 @Test
 public void testDifferenceSoundexStrings() {
 String encoderType = "Soundex";
 String input1 = "Apple";
 String input2 = "Orange";
 int result = SoundexUtils.difference(new Soundex(), input1, input2);
 assertEquals(2, result);
 }

 @Test
 public void testDifferenceRefinedSoundexStrings() {
 String encoderType = "RefinedSoundex";
 String input1 = "Apple";
 String input2 = "Orange";
 int result = SoundexUtils.difference(new RefinedSoundex(), input1, input2);
 assertEquals(3, result);
 }

 @Test
 public void testDifferenceSoundexAndRefinedSoundexStrings() {
 String encoderTypeSoundex = "Soundex";
 String encoderTypeRefinedSoundex = "RefinedSoundex";
 String input1 = "Apple";
 String input2 = "Orange";
 int resultSoundex = SoundexUtils.difference(new Soundex(), input1, input2);
 int resultRefinedSoundex = SoundexUtils.difference(new RefinedSoundex(), input1, input2);
 assertEquals(2, resultSoundex);
 assertEquals(3, resultRefinedSoundex);
 }
}