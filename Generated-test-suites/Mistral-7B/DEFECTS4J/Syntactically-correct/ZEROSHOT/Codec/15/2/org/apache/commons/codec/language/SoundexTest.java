package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING_STRING;
import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING;

public class SoundexTest {

 Soundex soundexInstance;

 @BeforeClass
 public static void setupClass() {
 soundexInstance = new Soundex();
 }

 @Test
 public void testSoundexConstructorWithDefaultMapping() {
 char[] soundexMappingDefault = US_ENGLISH_MAPPING;
 Soundex soundexDefault = new Soundex();
 assertArrayEquals(soundexMappingDefault, soundexDefault.getSoundexMapping());
 }

 @Test
 public void testSoundexConstructorWithUS_ENGLISH_MAPPING() {
 Soundex soundexWithUSMapping = new Soundex(US_ENGLISH_MAPPING);
 char[] soundexMappingUS = US_ENGLISH_MAPPING;
 assertArrayEquals(soundexMappingUS, soundexWithUSMapping.getSoundexMapping());
 }

 @Test
 public void testSoundexConstructorWithCustomMapping() {
 String customMapping = "123123213122123";
 Soundex soundexCustom = new Soundex(customMapping.toCharArray());
 char[] soundexMappingCustom = customMapping.toCharArray();
 assertArrayEquals(soundexMappingCustom, soundexCustom.getSoundexMapping());
 }

 @Test
 public void testSoundexDifference() throws EncoderException {
 String str1 = "Smith";
 String str2 = "Smyth";
 int difference = soundexInstance.difference(str1, str2);
 assertEquals(3, difference);
 }

 @Test
 public void testSoundexEncode() {
 String str = "John";
 String encodedStr = soundexInstance.encode(str);
 String expected = "J261";
 assertEquals(expected, encodedStr);
 }

 @Test
 public void testSoundexEncodeWithNullInput() {
 String nullInput = null;
 try {
 String encodedNullInput = soundexInstance.encode(nullInput);
 fail("Expected EncoderException for null input");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testSoundexEncodeWithNonStringInput() {
 Object nonStringInput = 123;
 try {
 String encodedNonStringInput = (String) soundexInstance.encode(nonStringInput);
 fail("Expected EncoderException for non-String input");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testSoundexSoundex() {
 String str = "John";
 String encodedStr = soundexInstance.soundex(str);
 String expected = "J261";
 assertEquals(expected, encodedStr);
 }

 @Test
 public void testSoundexSoundexWithEmptyInput() {
 String emptyInput = "";
 String encodedEmptyInput = soundexInstance.soundex(emptyInput);
 assertEquals("", encodedEmptyInput);
 }

}