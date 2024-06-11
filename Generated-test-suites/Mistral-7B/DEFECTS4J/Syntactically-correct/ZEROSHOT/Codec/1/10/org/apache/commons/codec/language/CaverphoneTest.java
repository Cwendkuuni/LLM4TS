import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

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
 public void testLowerCaseInput() {
 String input = "Hello World";
 String result = caverphone.caverphone(input);
 String expected = "H2h2o2W2r2l2d";
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveNonAZ() {
 String input = "1234Hello5World6!@#";
 String result = caverphone.caverphone(input);
 String expected = "H2h2o2W2r2l2d";
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveFinalE() {
 String input = "enough";
 String result = caverphone.caverphone(input);
 String expected = "enou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testHandleStartOptions() {
 String input = "CoughCake";
 String result = caverphone.caverphone(input);
 String expected = "cou2fcak";
 assertEquals(expected, result);
 }

 @Test
 public void testHandleReplacements() {
 String input = "Quiet Cello Cycles";
 String result = caverphone.caverphone(input);
 String expected = "qiiet s2ell s2yklz";
 assertEquals(expected, result);
 }

 @Test
 public void testHandleRemovals() {
 String input = "Two Three Two Three Two Three";
 String result = caverphone.caverphone(input);
 String expected = "222";
 assertEquals(expected, result);
 }

 @Test
 public void testHandleAdditionalRules() {
 String input = "Why not Why not Why not Why not Why not Why not Why not";
 String result = caverphone.caverphone(input);
 String expected = "Wh3n3t3 Wh3n3t3 Wh3n3t3 Wh3n3t3 Wh3n3t3 Wh3n3t3 Wh3n3t3 Wh3n3t3";
 assertEquals(expected, result);
 }

 @Test (expected = DecoderException.class)
 public void testEncodeNonStringObject() {
 caverphone.encode(123L);
 }

 @Test
 public void testEncodeDifferentStrings() {
 String str1 = "Hello World";
 String str2 = "Hello Universe";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertFalse(result);
 }

 @Test
 public void testEncodeSameStrings() {
 String str1 = "Hello World";
 String str2 = "Hello World";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertTrue(result);
 }

}