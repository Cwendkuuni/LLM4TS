import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Metaphone;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

 Metaphone metaphone = new Metaphone();

 @Test
 public void testMetaphoneEmptyString() {
 String encodedEmpty = metaphone.metaphone("");
 assertEquals("", encodedEmpty);
 }

 @Test
 public void testMetaphoneSingleCharacter() {
 String input = "A";
 String encoded = metaphone.metaphone(input);
 assertEquals(input.toUpperCase(), encoded);
 }

 @Test
 public void testMetaphoneTwoCharacters() {
 String input = "Apple";
 String expected = "APP";
 String encoded = metaphone.metaphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testMetaphoneThreeCharacters() {
 String input = "Banana";
 String expected = "BAN";
 String encoded = metaphone.metaphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testMetaphoneFourCharacters() {
 String input = "Coconut";
 String expected = "COC";
 String encoded = metaphone.metaphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testMetaphoneFiveCharacters() {
 String input = "Encodable";
 String expected = "ENC";
 String encoded = metaphone.metaphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testMetaphoneSixCharacters() {
 String input = "MetaphoneTest";
 String expected = "MTA";
 String encoded = metaphone.metaphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testMetaphoneSevenCharacters() {
 String input = "ApacheSoftwareFoundation";
 String expected = "APC";
 String encoded = metaphone.metaphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testMetaphoneSpecialCases() {
 String input1 = "Knighthood";
 String expected1 = "NHT";
 String encoded1 = metaphone.metaphone(input1);
 assertEquals(expected1, encoded1);

 String input2 = "Quiet";
 String expected2 = "KET";
 String encoded2 = metaphone.metaphone(input2);
 assertEquals(expected2, encoded2);

 String input3 = "Aardvark";
 String expected3 = "AAV";
 String encoded3 = metaphone.metaphone(input3);
 assertEquals(expected3, encoded3);

 String input4 = "Aluminium";
 String expected4 = "ALM";
 String encoded4 = metaphone.metaphone(input4);
 assertEquals(expected4, encoded4);
 }

 @Test(expected = EncoderException.class)
 public void testMetaphoneInvalidInput() {
 String input = "1234abcd";
 metaphone.metaphone(input);
 fail("EncoderException should have been thrown");
 }

 @Test
 public void testMetaphoneIdenticalStrings() {
 String input1 = "Apple";
 String input2 = "Apple";
 String expected = "APP";
 String encoded1 = metaphone.metaphone(input1);
 String encoded2 = metaphone.metaphone(input2);
 assertTrue(metaphone.isMetaphoneEqual(input1, input2));
 assertEquals(expected, encoded1);
 assertEquals(expected, encoded2);
 }

 @Test
 public void testMetaphoneNonIdenticalStrings() {
 String input1 = "Apple";
 String input2 = "Orange";
 String expected1 = "APP";
 String expected2 = "ORN";
 String encoded1 = metaphone.metaphone(input1);
 String encoded2 = metaphone.metaphone(input2);
 assertFalse(metaphone.isMetaphoneEqual(input1, input2));
 assertNotEquals(expected1, expected2);
 assertNotEquals(encoded1, encoded2);
 }

 @Test
 public void testMetaphoneMaxCodeLen() {
 String input = "ApacheSoftwareFoundation";
 String expected = "APC";
 Metaphone metaphoneWithMaxLen5 = new Metaphone();
 metaphoneWithMaxLen5.setMaxCodeLen(5);
 String encoded = metaphoneWithMaxLen5.metaphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testMetaphoneMaxCodeLenLongerThanInput() {
 String input = "MetaphoneTest";
 String encoded = metaphone.metaphone(input);
 String expected = "MTA";
 assertEquals(expected, encoded);
 Metaphone metaphoneWithMaxLenMoreThanFour = new Metaphone();
 metaphoneWithMaxLenMoreThanFour.setMaxCodeLen(5);
 String encodedWithMaxLenMoreThanFour = metaphoneWithMaxLenMoreThanFour.metaphone(input);
 assertEquals(expected, encodedWithMaxLenMoreThanFour);
 }
}