package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

public class SoundexTest {

 @Test
 public void testUSEnglishSoundex() throws EncoderException {
 String input1 = "Apple";
 String expected1 = "A122";
 String input2 = "Banana";
 String expected2 = "B122";
 String input3 = "Coconut";
 String expected3 = "K321"; // soundex mapping for 'C' and 'K' is the same

 Soundex soundex = Soundex.US_ENGLISH;
 String actual1 = soundex.encode(input1);
 String actual2 = soundex.encode(input2);
 String actual3 = soundex.encode(input3);

 assertEquals(expected1, actual1);
 assertEquals(expected2, actual2);
 assertEquals(expected3, actual3);
 }

 @Test(expected = EncoderException.class)
 public void testInvalidInputSoundex() throws EncoderException {
 Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING); // create instance with US_ENGLISH_MAPPING
 String input = "\u0061\u0304"; // an invalid input with diacritic 'À'
 String expectedErrorMessage = "The character is not mapped: ";

 try {
 soundex.encode(input);
 fail("EncoderException should be thrown");
 } catch (EncoderException e) {
 String actualErrorMessage = e.getMessage();
 assertTrue(actualErrorMessage.startsWith(expectedErrorMessage));
 }
 }

 @Test
 public void testCustomSoundexMapping() throws EncoderException {
 char[] customMapping = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", '0', '0', '0', '0', '0', '0', '0', '0'};
 Soundex customSoundex = new Soundex(customMapping);
 String input1 = "Apple";
 String expected1 = "A122";
 String input2 = "Banana";
 String expected2 = "B122";
 String input3 = "Coconut";
 String expected3 = "K321";

 String actual1 = customSoundex.encode(input1);
 String actual2 = customSoundex.encode(input2);
 String actual3 = customSoundex.encode(input3);

 assertEquals(expected1, actual1);
 assertEquals(expected2, actual2);
 assertEquals(expected3, actual3);
 }

 @Test
 public void testSoundexDifference() throws EncoderException {
 Soundex soundex = Soundex.US_ENGLISH;
 String str1 = "Apple";
 String str2 = "Apricot";
 int expected = 2; // 2 similar characters in Soundex codes

 int actual = soundex.difference(str1, str2);

 assertEquals(expected, actual);
 }

 @Test
 public void testSoundexEncodeNullInput() {
 Soundex soundex = Soundex.US_ENGLISH;
 String input = null;

 try {
 String actual = soundex.encode(input);
 fail("EncoderException should be thrown");
 } catch (EncoderException e) {
 // expected exception
 }
 }
}