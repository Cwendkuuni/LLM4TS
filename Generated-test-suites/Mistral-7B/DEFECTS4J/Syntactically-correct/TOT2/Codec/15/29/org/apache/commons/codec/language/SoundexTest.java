package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class SoundexTest {

 private final Soundex soundex = new Soundex();

 @Test
 public void testUSEnglishSoundex() throws EncoderException {
 String input = "Apple";
 String expectedOutput = "A231";
 String output = soundex.soundex(input);
 Assert.assertEquals(expectedOutput, output);

 input = "Banana";
 expectedOutput = "B162";
 output = soundex.soundex(input);
 Assert.assertEquals(expectedOutput, output);

 input = "Cat";
 expectedOutput = "K132";
 output = soundex.soundex(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testCustomSoundexMapping() throws EncoderException {
 char[] customMapping = {'1', '1', '2', '2', '3', '3', '4', '4', '5', '5', '6', '6', '7', '7', '0', '0', '0', '0'};
 Soundex customSoundex = new Soundex(customMapping);
 String input = "Apple";
 String expectedOutput = "A231";
 String output = customSoundex.soundex(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test(expected = EncoderException.class)
 public void testIllegalArgumentException() throws EncoderException {
 Soundex soundex = new Soundex();
 String input = "apple"; // lowercase 'a'
 String output = soundex.soundex(input);
 }

 @Test(expected = EncoderException.class)
 public void testNullInput() throws EncoderException {
 Soundex soundex = new Soundex();
 String output = soundex.soundex(null);
 }

 @Test
 public void testEmptyInput() throws EncoderException {
 Soundex soundex = new Soundex();
 String output = soundex.soundex("");
 Assert.assertEquals("", output);
 }

 @Test
 public void testSoundexDifference() throws EncoderException {
 String input1 = "Apple";
 String input2 = "Orange";
 int difference = soundex.difference(input1, input2);
 Assert.assertEquals(1, difference);
 }

 @Test
 public void testSoundexDifferenceSame() throws EncoderException {
 String input1 = "Apple";
 String input2 = "Apple";
 int difference = soundex.difference(input1, input2);
 Assert.assertEquals(4, difference);
 }
}