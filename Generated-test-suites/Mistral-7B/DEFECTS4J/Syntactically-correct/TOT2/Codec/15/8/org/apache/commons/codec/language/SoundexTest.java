package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SoundexTest {

 Soundex soundex = new Soundex();

 @Test
 public void testDefaultSoundexEncoding() {
 String input = "Apple";
 String expectedOutput = "A113";
 String actualOutput = soundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCustomSoundexEncoding() {
 String input = "Apple";
 char[] customMapping = {"A", "1", "1", "3"};
 Soundex customSoundex = new Soundex(customMapping);
 String expectedOutput = "A113";
 String actualOutput = customSoundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSoundexEncodingWithEmptyString() {
 String input = "";
 String expectedOutput = "";
 String actualOutput = soundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSoundexEncodingWithNullString() {
 assertThrows(EncoderException.class, () -> soundex.encode(null));
 }

 @Test
 public void testSoundexEncodingWithMultipleWords() {
 String input = "The quick brown fox jumps over the lazy dog";
 String expectedOutput = "T221 K112 B211 F111 X022 O111 M221 P111 S111 V111 G111 T221";
 String actualOutput = soundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSoundexEncodingWithMultipleWordsAndCustomMapping() {
 String input = "The quick brown fox jumps over the lazy dog";
 char[] customMapping = {"T", "T", "K", "K", "B", "B", "F", "F", "X", "X", "O", "O", "M", "M", "P", "P", "S", "S", "V", "V", "G", "G"};
 Soundex customSoundex = new Soundex(customMapping);
 String expectedOutput = "T221 K112 B211 F111 X022 O111 M221 P111 S111 V111 G111 T221";
 String actualOutput = customSoundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSoundexEncodingWithInvalidCharacter() {
 char invalidChar = '✖';
 assertThrows(IllegalArgumentException.class, () -> soundex.map(invalidChar));
 }

 @Test(expected = EncoderException.class)
 public void testSoundexEncodingWithInvalidInput() {
 assertThrows(EncoderException.class, () -> soundex.encode(null));
 }

}