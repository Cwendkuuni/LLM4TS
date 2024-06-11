package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Soundex class
 */
public class SoundexTest {

 /**
 * Test constructor Soundex()
 */
 @Test
 public void testSoundexDefaultConstructor() {
 String soundexCode = new Soundex().encode("Apple");
 assertEquals("APPL", soundexCode);
 }

 /**
 * Test constructor Soundex(char[])
 */
 @Test
 public void testSoundexMappingConstructor() {
 char[] usEnglishMapping = Soundex.US_ENGLISH_MAPPING;
 Soundex soundexInstance = new Soundex(usEnglishMapping);
 String soundexCode = soundexInstance.encode("Apple");
 assertEquals("APPL", soundexCode);
 }

 /**
 * Test constructor Soundex(String)
 */
 @Test
 public void testSoundexMappingStringConstructor() {
 Soundex soundexInstance = new Soundex(Soundex.US_ENGLISH_MAPPING_STRING);
 String soundexCode = soundexInstance.encode("Apple");
 assertEquals("APPL", soundexCode);
 }

 /**
 * Test method difference
 */
 @Test
 public void testDifference() throws EncoderException {
 String str1 = "Apple";
 String str2 = "Orange";
 Soundex soundexInstance = new Soundex();
 int difference = soundexInstance.difference(str1, str2);
 assertEquals(0, difference);

 str1 = "Apple";
 str2 = "Apple";
 difference = soundexInstance.difference(str1, str2);
 assertEquals(4, difference);

 str1 = "Apple";
 str2 = "Application";
 difference = soundexInstance.difference(str1, str2);
 assertEquals(3, difference);
 }

 /**
 * Test method encode
 */
 @Test
 public void testEncode() throws EncoderException {
 Soundex soundexInstance = new Soundex();
 String soundexCode = soundexInstance.encode("Apple");
 assertEquals("APPL", soundexCode);

 soundexInstance = new Soundex(Soundex.US_ENGLISH_MAPPING);
 soundexCode = soundexInstance.encode("Apple");
 assertEquals("APPL", soundexCode);

 soundexInstance = new Soundex(Soundex.US_ENGLISH_MAPPING_STRING);
 soundexCode = soundexInstance.encode("Apple");
 assertEquals("APPL", soundexCode);
 }

 /**
 * Test method getMappingCode
 * @throws IllegalArgumentException
 */
 @Test(expected = IllegalArgumentException.class)
 public void testGetMappingCodeIllegalArgumentException() {
 Soundex soundexInstance = new Soundex();
 soundexInstance.getMappingCode(null, 0);
 }

 @Test
 public void testGetMappingCode() {
 Soundex soundexInstance = new Soundex();
 char last = soundexInstance.getMappingCode("Apple", 0);
 assertEquals('A', last);

 last = soundexInstance.getMappingCode("Apple", 1);
 assertEquals('P', last);

 last = soundexInstance.getMappingCode("Apple", 2);
 assertEquals('P', last);

 last = soundexInstance.getMappingCode("Apple", 3);
 assertEquals('L', last);
 }

 /**
 * Test method getSoundexMapping
 */
 @Test
 public void testGetSoundexMapping() {
 Soundex soundexInstance = new Soundex();
 char[] soundexMapping = soundexInstance.getSoundexMapping();
 assertEquals(26, soundexMapping.length);
 }

 /**
 * Test method map
 * @throws IllegalArgumentException
 */
 @Test(expected = IllegalArgumentException.class)
 public void testMapIllegalArgumentException() {
 Soundex soundexInstance = new Soundex();
 soundexInstance.map((char) 0);
 }

 @Test
 public void testMap() {
 Soundex soundexInstance = new Soundex();
 char mapped = soundexInstance.map('A');
 assertEquals('A', mapped);

 mapped = soundexInstance.map('B');
 assertEquals('B', mapped);

 mapped = soundexInstance.map('C');
 assertEquals('K', mapped);
 }
}