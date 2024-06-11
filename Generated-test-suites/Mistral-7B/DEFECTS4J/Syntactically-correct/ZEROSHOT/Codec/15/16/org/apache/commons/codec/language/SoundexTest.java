import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

 @Test
 public void testUS_ENGLISH_MAPPING() {
 char[] usEnglishMapping = Soundex.US_ENGLISH_MAPPING;
 assertEquals('0', usEnglishMapping[0]);
 assertEquals('1', usEnglishMapping[1]);
 assertEquals('2', usEnglishMapping[2]);
 assertEquals('0', usEnglishMapping[3]);
 // ... continue checking the rest of the mapping array
 }

 @Test
 public void testUS_ENGLISH_Instance() {
 Soundex usEnglish = Soundex.US_ENGLISH;
 assertEquals(4, usEnglish.maxLength);
 // You can test the methods of the Soundex class here
 }

 @Test
 public void testCustomMapping_String() {
 Soundex customMapping = new Soundex("123456789012");
 assertEquals(10, customMapping.maxLength);
 // Test methods of the Soundex object "customMapping" here
 }

 @Test
 public void testCustomMapping_CharArray() {
 char[] customMapping = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 Soundex customMappingObj = new Soundex(customMapping);
 assertEquals(10, customMappingObj.maxLength);
 // Test methods of the Soundex object "customMappingObj" here
 }

 @Test(expected = EncoderException.class)
 public void testEncode_NonStringObject() {
 Soundex soundex = new Soundex();
 Object nonStringObj = new Object();
 String encodedString = soundex.encode(nonStringObj);
 fail("EncoderException expected but not thrown");
 }

 @Test
 public void testDifference() throws EncoderException {
 Soundex soundex = new Soundex();
 String str1 = "apple";
 String str2 = "appel";
 int difference = soundex.difference(str1, str2);
 assertEquals(3, difference);
 }

 @Test
 public void testSoundex() {
 Soundex soundex = new Soundex();
 String str1 = "apple";
 String soundexCode1 = soundex.soundex(str1);
 String str2 = "application";
 String soundexCode2 = soundex.soundex(str2);
 assertEquals("appl", soundexCode1);
 assertEquals("appl", soundexCode2);
 }

}