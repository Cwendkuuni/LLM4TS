package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;

/**
 * SoundexTest class to test all methods in Soundex.java class
 */
public class SoundexTest {

 @Test
 public void testUS_ENGLISH_MAPPING_STRING() {
 char[] expectedMapping = {'0', '1', '2', '3', '0', '1', '2', '0', '2', '2', '4', '5', '0', '1', '2', '3', '0'};
 assertEquals("US_ENGLISH_MAPPING_STRING should be equal to expectedMapping", expectedMapping,
 Soundex.US_ENGLISH_MAPPING_STRING.toCharArray());
 }

 @Test
 public void testUS_ENGLISH_MAPPING() {
 char[] expectedMapping = {'0', '1', '2', '3', '0', '1', '2', '0', '2', '2', '4', '5', '0', '1', '2', '3', '0'};
 assertEquals("US_ENGLISH_MAPPING should be equal to expectedMapping", expectedMapping, Soundex.US_ENGLISH.getSoundexMapping());
 }

 @Test(expected = EncoderException.class)
 public void testEncode_nullObject() throws EncoderException {
 Soundex soundex = new Soundex();
 soundex.encode(null);
 }

 @Test
 public void testEncode_emptyString() {
 String expected = "";
 String result = new Soundex().encode("");
 assertEquals("encode() should return empty string for empty string", expected, result);
 }

 @Test
 public void testEncode_singleCharacter() {
 String expected = "K";
 String result = new Soundex().encode("K");
 assertEquals("encode() should return K for single character string", expected, result);
 }

 @Test
 public void testEncode_multipleCharacters() {
 String expected = "K2";
 String result = new Soundex().encode("Kate");
 assertEquals("encode() should return K2 for Kate", expected, result);
 }

 @Test
 public void testEncode_multipleCharactersWithDifferentMappings() {
 String expected = "K23";
 String result = new Soundex().encode("Kate,Patrick");
 assertEquals("encode() should return K23 for Kate,Patrick", expected, result);
 }

 @Test
 public void testEncode_stringWithSpecialCharacters() {
 String expected = "K2";
 String result = new Soundex().encode("Kate,Patrick!");
 assertEquals("encode() should return K2 for Kate,Patrick!", expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMappingCode_invalidCharacter() {
 Soundex soundex = new Soundex();
 soundex.getMappingCode("abc", 3);
 }

 @Test
 public void testGetMappingCode_validCharacter() {
 Soundex soundex = new Soundex();
 char mappingCode = soundex.getMappingCode("abc", 0);
 assertEquals("Mapping code for A should be 1", 1, mappingCode);
 }

 @Test
 public void testDifference_emptyStrings() {
 int expected = 0;
 int result = new Soundex().difference("", "");
 assertEquals("difference() should return 0 for empty strings", expected, result);
 }

 @Test
 public void testDifference_identicalStrings() {
 String str1 = "Kate";
 String str2 = "Kate";
 int expected = 4;
 int result = new Soundex().difference(str1, str2);
 assertEquals("difference() should return 4 for identical strings", expected, result);
 }

 @Test
 public void testDifference_stringsWithDifferentLengths() {
 String str1 = "Kate";
 String str2 = "Patrick";
 int expected = 2;
 int result = new Soundex().difference(str1, str2);
 assertEquals("difference() should return 2 for strings with different length", expected, result);
 }

 @Test
 public void testDifference_stringsWithDifferentCharacters() {
 String str1 = "Kate";
 String str2 = "Patrick";
 int expected = 2;
 int result = new Soundex().difference(str1, str2);
 assertEquals("difference() should return 2 for strings with different characters", expected, result);
 }

 @Test
 public void testDifference_stringsWithSameCharactersButDifferentOrder() {
 String str1 = "ateK";
 String str2 = "Kate";
 int expected = 3;
 int result = new Soundex().difference(str1, str2);
 assertEquals("difference() should return 3 for strings with same characters but different order", expected, result);
 }

 @Test
 public void testDifference_stringsWithSpecialCharacters() {
 String str1 = "Kate,Patrick!";
 String str2 = "Patrick,Kate!";
 int expected = 4;
 int result = new Soundex().difference(str1, str2);
 assertEquals("difference() should return 4 for strings with special characters", expected, result);
 }

 @Test
 public void testEncode_nullString() {
 String expected = null;
 String result = new Soundex().encode(null);
 assertNull("encode() should return null for null string", result);
 }
}