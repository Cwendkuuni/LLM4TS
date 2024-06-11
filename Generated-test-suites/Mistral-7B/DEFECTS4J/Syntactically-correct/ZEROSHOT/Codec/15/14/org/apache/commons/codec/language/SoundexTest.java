package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING_STRING;
import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING;
import static org.apache.commons.codec.language.Soundex.soundex;

public class SoundexTest {

 Soundex soundexInstance = new Soundex();
 Soundex usEnglishSoundexInstance = Soundex.US_ENGLISH;

 @Test
 public void testSoundexWithEmptyString() {
 String emptyString = "";
 String expectedEmptyString = "";
 String result = soundexInstance.soundex(emptyString);
 Assert.assertEquals(expectedEmptyString, result);
 }

 @Test
 public void testSoundexWithSingleCharacterString() {
 char character = 'A';
 String singleCharacterString = Character.toString(character);
 String expectedSingleCharacterString = Character.toString(US_ENGLISH_MAPPING[character - 'A']);
 String result = soundexInstance.soundex(singleCharacterString);
 Assert.assertEquals(expectedSingleCharacterString, result);
 }

 @Test
 public void testSoundexWithMultipleCharactersString() {
 String multipleCharactersString = "Apple";
 String expectedMultipleCharactersString = "A1";
 String result = soundexInstance.soundex(multipleCharactersString);
 Assert.assertEquals(expectedMultipleCharactersString, result);
 }

 @Test
 public void testSoundexWithMultipleSameCharactersString() {
 String multipleSameCharactersString = "AAAA";
 String expectedMultipleSameCharactersString = "A0";
 String result = soundexInstance.soundex(multipleSameCharactersString);
 Assert.assertEquals(expectedMultipleSameCharactersString, result);
 }

 @Test
 public void testSoundexWithInternationalString() {
 String internationalString = "Schrödinger";
 String expectedInternationalString = "S321";
 String result = usEnglishSoundexInstance.soundex(internationalString);
 Assert.assertEquals(expectedInternationalString, result);
 }

 @Test
 public void testDifferenceWithSameStrings() {
 String string1 = "Apple";
 String string2 = "Apple";
 int difference = 0;
 try {
 difference = soundexInstance.difference(string1, string2);
 } catch (EncoderException e) {
 // This should not happen
 }
 Assert.assertEquals(4, difference);
 }

 @Test
 public void testDifferenceWithDifferentStrings() {
 String string1 = "Apple";
 String string2 = "Banana";
 int difference = 0;
 try {
 difference = soundexInstance.difference(string1, string2);
 } catch (EncoderException e) {
 // This should not happen
 }
 Assert.assertEquals(0, difference);
 }

 @Test
 public void testDifferenceWithDifferentLengthStrings() {
 String string1 = "Apple";
 String string2 = "BananaApple";
 int difference = 0;
 try {
 difference = soundexInstance.difference(string1, string2);
 } catch (EncoderException e) {
 // This should not happen
 }
 Assert.assertEquals(1, difference);
 }
}