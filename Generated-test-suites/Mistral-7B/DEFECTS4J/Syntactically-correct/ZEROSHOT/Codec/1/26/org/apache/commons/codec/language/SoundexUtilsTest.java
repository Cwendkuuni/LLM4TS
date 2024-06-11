import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SoundexUtilsTest {

 @Test
 public void testClean() {
 String input = "A an Apple a day keeps the Doctor away!";
 String expected = "A A A A A A A A A A";
 String result = SoundexUtils.clean(input);
 assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullInput() {
 String input = null;
 SoundexUtils.clean(input);
 }

 @Test
 public void testCleanEmptyInput() {
 String input = "";
 String expected = "";
 String result = SoundexUtils.clean(input);
 assertEquals(expected, result);
 }

 @Test
 public void testDifference() throws EncoderException {
 String encoderType = "Soundex";
 String s1 = "Apple";
 String s2 = "Orange";
 String encoder = new SoundexEncoder();
 int difference = SoundexUtils.difference(encoder, s1, s2);
 int expected = 0;
 assertEquals(expected, difference);
 }

 @Test
 public void testDifferenceEncoded() {
 String es1 = "A123";
 String es2 = "A234";
 int difference = SoundexUtils.differenceEncoded(es1, es2);
 int expected = 1;
 assertEquals(expected, difference);
 }

 @Test
 public void testDifferenceSoundexIdentical() {
 String es1 = "A123";
 String es2 = "A123";
 int difference = SoundexUtils.differenceEncoded(es1, es2);
 int expected = 4;
 assertEquals(expected, difference);
 }

 @Test
 public void testDifferenceSoundexRefinedIdentical() {
 String es1 = "A12345";
 String es2 = "A12345";
 int difference = SoundexUtils.differenceEncoded(es1, es2);
 int expected = 5;
 assertEquals(expected, difference);
 }

 @Test
 public void testDifferenceSoundexDifferent() {
 String es1 = "A123";
 String es2 = "B456";
 int difference = SoundexUtils.differenceEncoded(es1, es2);
 int expected = 0;
 assertEquals(expected, difference);
 }

 @Test(expected = NullPointerException.class)
 public void testDifferenceNullInput1() {
 String es1 = null;
 String es2 = "Apple";
 int difference = SoundexUtils.differenceEncoded(es1, es2);
 }

 @Test(expected = NullPointerException.class)
 public void testDifferenceNullInput2() {
 String es1 = "Apple";
 String es2 = null;
 int difference = SoundexUtils.differenceEncoded(es1, es2);
 }

 @Test
 public void testDifferenceEmptyInput1() {
 String es1 = "";
 String es2 = "Apple";
 int difference = SoundexUtils.differenceEncoded(es1, es2);
 int expected = 0;
 assertEquals(expected, difference);
 }

 @Test
 public void testDifferenceEmptyInput2() {
 String es1 = "Apple";
 String es2 = "" ;
 int difference = SoundexUtils.differenceEncoded(es1, es2);
 int expected = 0;
 assertEquals(expected, difference);
 }

}