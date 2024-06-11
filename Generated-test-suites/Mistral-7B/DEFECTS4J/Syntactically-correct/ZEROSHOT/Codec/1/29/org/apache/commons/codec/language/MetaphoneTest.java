import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MetaphoneTest {

 private Metaphone metaphone = new Metaphone();

 @Test
 public void testMetaphoneEmptyString() {
 String encoded = metaphone.metaphone("");
 assertEquals("", encoded);
 }

 @Test
 public void testMetaphoneSingleCharacter() {
 String encoded = metaphone.metaphone("A");
 assertEquals("A", encoded);
 }

 @Test
 public void testMetaphoneKN() {
 String encoded = metaphone.metaphone("KNIGHT");
 assertEquals("K1T", encoded);
 }

 @Test
 public void testMetaphoneAE() {
 String encoded = metaphone.metaphone("APPLE");
 assertEquals("A2P", encoded);
 }

 @Test
 public void testMetaphoneWR() {
 String encoded = metaphone.metaphone("WRAP");
 assertEquals("W1P", encoded);
 }

 @Test
 public void testMetaphoneWH() {
 String encoded = metaphone.metaphone("WHISKEY");
 assertEquals("W1S", encoded);
 }

 @Test
 public void testMetaphoneX() {
 String encoded = metaphone.metaphone("XEROX");
 assertEquals("S0X", encoded);
 }

 @Test
 public void testMetaphoneInitialK() {
 String encoded = metaphone.metaphone("KANGAROO");
 assertEquals("K1NG", encoded);
 }

 @Test
 public void testMetaphoneSilentG() {
 String encoded = metaphone.metaphone("BAGGAGE");
 assertEquals("B1GG", encoded);
 }

 @Test
 public void testMetaphoneSilentCH() {
 String encoded = metaphone.metaphone("CHURCH");
 assertEquals("K1RK", encoded);
 }

 @Test
 public void testMetaphoneSilentTH() {
 String encoded = metaphone.metaphone("THEME");
 assertEquals("TH0M", encoded);
 }

 @Test
 public void testMetaphoneSilentDGE() {
 String encoded = metaphone.metaphone("DIGEST");
 assertEquals("J1ST", encoded);
 }

 @Test
 public void testMetaphoneSilentDGI() {
 String encoded = metaphone.metaphone("DIGIT");
 assertEquals("J1T", encoded);
 }

 @Test
 public void testMetaphoneSilentDGY() {
 String encoded = metaphone.metaphone("DIGITAL");
 assertEquals("J1T", encoded);
 }

 @Test
 public void testMetaphoneDGEWithNextLetter() {
 String encoded = metaphone.metaphone("BEDGE");
 assertEquals("J1D", encoded);
 }

 @Test
 public void testMetaphoneJ() {
 String encoded = metaphone.metaphone("JUMBLE");
 assertEquals("J1M", encoded);
 }

 @Test
 public void testMetaphoneQ() {
 String encoded = metaphone.metaphone("QUICK");
 assertEquals("K1K", encoded);
 }

 @Test
 public void testMetaphoneS() {
 String encoded = metaphone.metaphone("SILENT");
 assertEquals("S0N", encoded);
 }

 @Test
 public void testMetaphoneT() {
 String encoded = metaphone.metaphone("THISISATEST");
 assertEquals("T1S", encoded);
 }

 @Test(expected = EncoderException.class)
 public void testNonStringParameter() {
 Metaphone metaphone = new Metaphone();
 metaphone.encode(123);
 }

 @Test
 public void testIsMetaphoneEqualEmptyStrings() {
 boolean result = metaphone.isMetaphoneEqual("", "");
 assertEquals(true, result);
 }

 @Test
 public void testIsMetaphoneEqualIdenticalStrings() {
 String str1 = "ABCDEFG";
 String str2 = "ABCDEFG";
 boolean result = metaphone.isMetaphoneEqual(str1, str2);
 assertEquals(true, result);
 }

 @Test
 public void testIsMetaphoneEqualDifferentStrings() {
 String str1 = "ABCDEFG";
 String str2 = "HIJKLMN";
 boolean result = metaphone.isMetaphoneEqual(str1, str2);
 assertEquals(false, result);
 }

 @Test
 public void testIsMetaphoneEqualSameCode() {
 String str1 = "ABCDEFG";
 String str2 = "ABCDEFG";
 String code1 = metaphone.metaphone(str1);
 String code2 = metaphone.metaphone(str2);
 boolean result = metaphone.isMetaphoneEqual(str1, str2);
 assertEquals(true, result);
 assertEquals(code1, code2);
 }

 @Test
 public void testIsMetaphoneEqualDifferentCodes() {
 String str1 = "ABCDEFG";
 String str2 = "HIJKLMN";
 String code1 = metaphone.metaphone(str1);
 String code2 = metaphone.metaphone(str2);
 boolean result = metaphone.isMetaphoneEqual(str1, str2);
 assertEquals(false, result);
 assertNotEquals(code1, code2);
 }

 @Test
 public void testGetMaxCodeLen() {
 int maxCodeLen = metaphone.getMaxCodeLen();
 assertEquals(4, maxCodeLen);
 }

 @Test
 public void testSetMaxCodeLen() {
 Metaphone metaphone = new Metaphone();
 int newMaxCodeLen = 5;
 metaphone.setMaxCodeLen(newMaxCodeLen);
 assertEquals(5, metaphone.getMaxCodeLen());
 }

}