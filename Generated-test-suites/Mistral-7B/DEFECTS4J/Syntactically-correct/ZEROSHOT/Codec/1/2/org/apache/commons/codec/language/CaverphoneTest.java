import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

 private Caverphone caverphoneEncoder = new Caverphone();

 @Test
 public void testEmptyString() {
 String emptyString = "";
 String expectedResult = "1111111111";
 try {
 String result = caverphoneEncoder.encode(emptyString);
 assertEquals(expectedResult, result);
 } catch (EncoderException e) {
 throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
 }
 }

 @Test
 public void testNullString() {
 String nullString = null;
 try {
 String result = caverphoneEncoder.encode(nullString);
 } catch (EncoderException e) {
 assertEquals("Parameter supplied to Caverphone encode is null", e.getMessage());
 }
 }

 @Test
 public void testLowerCase() {
 String lowerCaseString = "hello world";
 String expectedResult = "hElLoWoRlD";
 String result = caverphoneEncoder.encode(lowerCaseString);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testRemoveNonAlphabets() {
 String nonAlphabetString = "1hello2world3!";
 String expectedResult = "helloworld";
 String result = caverphoneEncoder.encode(nonAlphabetString);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testRemoveFinalE() {
 String finalEString = "enough";
 String expectedResult = "enou2f";
 String result = caverphoneEncoder.encode(finalEString);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testStartOptions() {
 String coughString = "cough";
 String expectedResult = "cou2f";
 String result = caverphoneEncoder.encode(coughString);
 assertEquals(expectedResult, result);

 String roughString = "rough";
 expectedResult = "rou2f";
 result = caverphoneEncoder.encode(roughString);
 assertEquals(expectedResult, result);

 String toughString = "tough";
 expectedResult = "tou2f";
 result = caverphoneEncoder.encode(toughString);
 assertEquals(expectedResult, result);

 String enoughString = "enough";
 expectedResult = "enou2f"; // Specification error, it should be "enou2f" not "enou2f"
 result = caverphoneEncoder.encode(enoughString);
 assertEquals(expectedResult, result);

 String troughString = "trough";
 expectedResult = "trou2f"; // Specification error, it should be "trou2f" not "trou2f"
 result = caverphoneEncoder.encode(troughString);
 assertEquals(expectedResult, result);

 String gnString = "gn";
 expectedResult = "2n";
 result = caverphoneEncoder.encode(gnString);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testReplacements() {
 String cqString = "caqueta";
 String expectedResult = "caqu2qeta";
 String result = caverphoneEncoder.encode(cqString);
 assertEquals(expectedResult, result);

 String ciString = "citrus";
 expectedResult = "si2t2rus";
 result = caverphoneEncoder.encode(ciString);
 assertEquals(expectedResult, result);

 String ceString = "centre";
 expectedResult = "se2ente";
 result = caverphoneEncoder.encode(ceString);
 assertEquals(expectedResult, result);

 String cyString = "cyclone";
 expectedResult = "sy2clone";
 result = caverphoneEncoder.encode(cyString);
 assertEquals(expectedResult, result);

 String tchString = "atchison";
 expectedResult = "2chison";
 result = caverphoneEncoder.encode(tchString);
 assertEquals(expectedResult, result);

 String cString = "cat";
 expectedResult = "k";
 result = caverphoneEncoder.encode(cString);
 assertEquals(expectedResult, result);

 String qString = "queen";
 expectedResult = "k";
 result = caverphoneEncoder.encode(qString);
 assertEquals(expectedResult, result);

 String xString = "xylophone";
 expectedResult = "k";
 result = caverphoneEncoder.encode(xString);
 assertEquals(expectedResult, result);

 String vString = "value";
 expectedResult = "f";
 result = caverphoneEncoder.encode(vString);
 assertEquals(expectedResult, result);

 String dgString = "doughnut";
 expectedResult = "2g";
 result = caverphoneEncoder.encode(dgString);
 assertEquals(expectedResult, result);

 String tioString = "tion";
 expectedResult = "sio";
 result = caverphoneEncoder.encode(tioString);
 assertEquals(expectedResult, result);

 String tiaString = "tian";
 expectedResult = "sia";
 result = caverphoneEncoder.encode(tiaString);
 assertEquals(expectedResult, result);

 String dString = "dog";
 expectedResult = "t";
 result = caverphoneEncoder.encode(dString);
 assertEquals(expectedResult, result);

 String phString = "phone";
 expectedResult = "fh";
 result = caverphoneEncoder.encode(phString);
 assertEquals(expectedResult, result);

 String bString = "bottle";
 expectedResult = "p";
 result = caverphoneEncoder.encode(bString);
 assertEquals(expectedResult, result);

 String shString = "shoe";
 expectedResult = "s2";
 result = caverphoneEncoder.encode(shString);
 assertEquals(expectedResult, result);

 String zString = "zebra";
 expectedResult = "s";
 result = caverphoneEncoder.encode(zString);
 assertEquals(expectedResult, result);

 String w3String = "willow";
 expectedResult = "W3";
 result = caverphoneEncoder.encode(w3String);
 assertEquals(expectedResult, result);

 // Uncomment the following lines to test the removed 1.0 replacements
 //"why", "Why" and "wy", "Wy"

 String wh3String = "whale";
 expectedResult = "Wh3";
 result = caverphoneEncoder.encode(wh3String);
 assertEquals(expectedResult, result);

 String wString = "waffle";
 expectedResult = "W";
 result = caverphoneEncoder.encode(wString);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testRemovals() {
 String twoString = "two";
 String expectedResult = "";
 String result = caverphoneEncoder.encode(twoString);
 assertEquals(expectedResult, result);

 String threeString = "three";
 expectedResult = "A";
 result = caverphoneEncoder.encode(threeString);
 assertEquals(expectedResult, result);

 String threeStringEnd = "threed";
 expectedResult = "A";
 result = caverphoneEncoder.encode(threeStringEnd);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testPutTenOnesOnEnd() {
 String stringWithLengthSix = "apple";
 String expectedResult = "apple11111111";
 String result = caverphoneEncoder.encode(stringWithLengthSix);
 assertEquals(expectedResult, result.substring(0, 10));
 }

 @Test
 public void testTakeFirstSixCharactersAsCode() {
 String longString = "caverphonealgorithmisamazing";
 String expectedResult = "cavepho2algo3itama";
 String result = caverphoneEncoder.encode(longString);
 assertEquals(expectedResult.substring(0, 6), result.substring(0, 6));
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "caverphonealgorithmisamazing";
 String str2 = "caverphonealgorithmisamazing";
 boolean expectedResult = true;
 boolean result = caverphoneEncoder.isCaverphoneEqual(str1, str2);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsCaverphoneEqualDifferentStrings() {
 String str1 = "caverphonealgorithmisamazing";
 String str2 = "caverphonealgorithmisnotamazing";
 boolean expectedResult = false;
 boolean result = caverphoneEncoder.isCaverphoneEqual(str1, str2);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsCaverphoneEqualNullString() {
 String str1 = "caverphonealgorithmisamazing";
 String str2 = null;
 boolean expectedResult = false;
 boolean result = caverphoneEncoder.isCaverphoneEqual(str1, str2);
 assertEquals(expectedResult, result);
 }
}