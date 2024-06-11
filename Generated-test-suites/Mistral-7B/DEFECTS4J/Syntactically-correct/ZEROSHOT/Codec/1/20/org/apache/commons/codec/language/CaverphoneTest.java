package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testNullString() {
 try {
 caverphone.caverphone(null);
 } catch (EncoderException e) {
 assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
 }
 }

 @Test
 public void testSingleCharacter() {
 String result = caverphone.caverphone("a");
 assertEquals("a", result.substring(0, 1));
 assertEquals("2", result.substring(1, 10));
 }

 @Test
 public void testMultipleCharacters() {
 String result = caverphone.caverphone("hello");
 assertEquals("h3l2l2o2", result);
 }

 @Test
 public void testCaverphoneEquals() {
 String str1 = "hello";
 String str2 = "hello";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(true, result);
 }

 @Test
 public void testCaverphoneNotEqual() {
 String str1 = "hello";
 String str2 = "world";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(false, result);
 }

 @Test
 public void testVariousCaverphoneStrings() {
 String[] caverphoneStrings = new String[]{"Caversham", "Project", "at", "the", "University", "of", "Otago.", "Caverphone", "2.0", "algorithm", "created", "the", "Caversham", "Project", "at", "the", "University", "of", "Otago.", " Apache", " Software", "Foundation", "Licensed", "to", "the", "Apache", "Software", "Foundation", "under", "one", "or", "more", "contributor", "license", "agreements. ", "See", "the", "NOTICE", "file", "distributed", "with", "this", "work", "for", "additional", "information", "regarding", "copyright", "ownership.", " The", "ASF", "licenses", "this", "file", "to", "You", "under", "the", "Apache", "License,", "Version", "2.0", "(the", "\"License\";", "you", "may", "not", "use", "this", "file", "except", "in", "compliance", "with", "the", "License.", " You", "may", "obtain", "a", "copy", "of", "the", "License", "at", "http://www.apache.org/licenses/LICENSE-2.0", " "};
 String expectedCaverphone = "CavershamProjec2atUniver3ityofOtago.Caverphone2.0algorithmcreated3CavershamProjec3atUniver3ityofOtago.ApacheSoftwareFoundationLicens3to3ASFlicens3toYouunderApacheLicenseVersion2.0(the\"Licens3;youmaynotusethisfileexceptincompliancewiththeLicens3.YoumayobtainacopyoftheLicens3athttp://www.apache.org/licenses/LICENSE-2.0";
 for (String str : caverphoneStrings) {
 String result = caverphone.caverphone(str);
 assertEquals(expectedCaverphone.substring(0, str.length()), result.substring(0, str.length()));
 assertEquals(expectedCaverphone, caverphone.caverphone(str));
 }
 }

}