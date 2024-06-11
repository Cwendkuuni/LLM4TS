package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

 private Soundex soundex = new Soundex();

 @Test
 public void testUSEnglishMapping() {
 char[] usEnglishMapping = Soundex.US_ENGLISH_MAPPING;
 assertEquals('0', usEnglishMapping[0]);
 assertEquals('1', usEnglishMapping[1]);
 assertEquals('2', usEnglishMapping[2]);
 // ... continue for all letters in the alphabet
 }

 @Test
 public void testDefaultEncoding() throws EncoderException {
 String input1 = "Apple";
 String input2 = "Orange";
 String expectedOutput1 = "A120";
 String expectedOutput2 = "O150";
 String output1 = soundex.encode(input1);
 String output2 = soundex.encode(input2);
 assertEquals(expectedOutput1, output1);
 assertEquals(expectedOutput2, output2);
 }

 @Test
 public void testCustomEncoding() throws EncoderException {
 char[] customMapping = {'0', '1', '2', '3', '4'}; // custom mapping
 Soundex customSoundex = new Soundex(customMapping);
 String input = "Example";
 String expectedOutput = "E110";
 String output = customSoundex.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testDifference() throws EncoderException {
 String input1 = "Apple";
 String input2 = "Apple";
 int expectedOutput = 4;
 int output = soundex.difference(input1, input2);
 assertEquals(expectedOutput, output);

 input1 = "Apple";
 input2 = "Orange";
 expectedOutput = 0;
 output = soundex.difference(input1, input2);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = EncoderException.class)
 public void testInvalidEncodingInput() throws EncoderException {
 Soundex soundex = new Soundex();
 String input = "Apple!";
 soundex.encode(input);
 fail("Expected EncoderException but it didn't occur.");
 }

 @Test
 public void testNormalizeInput() {
 String input = "Apple";
 String expectedOutput = "APPLE";
 String output = soundex.normalize(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testNormalizeInputWithDigits() {
 String input = "Apple123";
 String expectedOutput = "APPLE";
 String output = soundex.normalize(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testNormalizeInputWithSpecialCharacters() {
 String input = "Apple!";
 String expectedOutput = "APPLE";
 String output = soundex.normalize(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testNormalizeInputWithMultipleSpecialCharacters() {
 String input = "Apple!%^&*()";
 String expectedOutput = "APPLE";
 String output = soundex.normalize(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testNormalizeInputWithMultipleWords() {
 String input = "Apple Orange";
 String expectedOutput = "APPLE ORANGE";
 String output = soundex.normalize(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testNormalizeInputWithMultipleWordsAndSpecialCharacters() {
 String input = "Apple Orange!%^&*()";
 String expectedOutput = "APPLE ORANGE";
 String output = soundex.normalize(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testNormalizeInputWithEmptyString() {
 String input = "";
 String expectedOutput = "";
 String output = soundex.normalize(input);
 assertEquals(expectedOutput, output);
 }
}