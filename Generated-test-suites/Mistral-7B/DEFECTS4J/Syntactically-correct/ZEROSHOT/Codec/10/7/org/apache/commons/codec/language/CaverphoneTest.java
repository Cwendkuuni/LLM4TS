import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testNullInput() {
 String result = caverphone.caverphone(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testEmptyInput() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testBasicCaverphone() {
 String input = "abcdefghijklmnopqrstuvwxyz";
 String result = caverphone.caverphone(input);
 String expected = "2mTK2S2F2L2P2K2F2T2W2K2S2N2J2C2G2K2R2T2M2F2T2S2P2K";
 assertEquals(expected, result);
 }

 @Test
 public void testStartOption() {
 String input = "tough";
 String result = caverphone.caverphone(input);
 String expected = "tou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testMultipleStartOptions() {
 String input = "coughenough";
 String result = caverphone.caverphone(input);
 String expected = "cou2fenou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testReplacements() {
 String input = "actionable";
 String result = caverphone.caverphone(input);
 String expected = "act2n2l2e";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesReplacementsWithLongerInputs() {
 String input = "philosophically";
 String result = caverphone.caverphone(input);
 String expected = "phil2soph2ic2aly";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testNonStringInput() {
 caverphone.caverphone(123);
 }

 @Test(expected = EncoderException.class)
 public void testInputWithSpecialChars() {
 caverphone.caverphone("@#$%^&*()_+=-[]\\{}|;:,.<>");
 }

 @Test(expected = EncoderException.class)
 public void testInputWithControlChars() {
 String input = "\u0001\u0002\u0003\u0004";
 caverphone.caverphone(input);
 }

 @Test
 public void testDecode() {
 String encoded = "2mTK2S2F2L2P2K2F2T2W2K2S2N2J2C2G2K2R2T2M2F2T2S2P2K";
 String decoded = caverphone.decode(Hex.decodeHex(encoded.getBytes()));
 assertEquals("abcdefghijklmnopqrstuvwxyz", decoded);
 }

 @Test
 public void testIdenticalCaverphones() {
 String str1 = "thisisatest";
 String str2 = "thisisatest";
 assertTrue(caverphone.isCaverphoneEqual(str1, str2));
 }

 @Test
 public void testDifferentCaverphones() {
 String str1 = "thisisatest";
 String str2 = "thisisnotatest";
 assertFalse(caverphone.isCaverphoneEqual(str1, str2));
 }

}