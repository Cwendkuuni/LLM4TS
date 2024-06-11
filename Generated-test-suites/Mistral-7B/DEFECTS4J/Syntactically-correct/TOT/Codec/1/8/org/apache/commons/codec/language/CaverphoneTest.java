import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final StringEncoder caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
 String input = "";
 String expected = "1111111111";
 String actual = caverphone.encode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testNullString() {
 assertThrows(EncoderException.class, () -> caverphone.encode(null));
 }

 @Test
 public void testSingleLetterString() {
 String input = "a";
 String expected = "A";
 String actual = caverphone.encode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testSingleLetterUpperString() {
 String input = "A";
 String expected = "A";
 String actual = caverphone.encode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testMultipleLettersString() {
 String input = "hello";
 String expected = "H2L2O";
 String actual = caverphone.encode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testMultipleLettersUpperString() {
 String input = "HELLO";
 String expected = "H2L2O";
 String actual = caverphone.encode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testMultipleDigitsString() {
 String input = "12345";
 String expected = "12345";
 String actual = caverphone.encode(input);
 assertNotEquals(expected, actual);
 throw new AssertionError("Expected caverphone encoding of digits to be different from original string.");
 }

 @Test
 public void testMultipleVowelsString() {
 String input = "eieio";
 String expected = "A2A2A2A";
 String actual = caverphone.encode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testMultipleConsontantsString() {
 String input = "bcdfghjklmnp";
 String expected = "B2C2D2F2G2H2J2K2L2M2N2P";
 String actual = caverphone.encode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testMultipleVowelsAndConsontantsString() {
 String input = "eatme";
 String expected = "A2E2A2T2M2E";
 String actual = caverphone.encode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithSpecialChars() {
 String input = "applesauce!";
 String expected = "A2P2L2E2S2A2U2C2E";
 String actual = caverphone.encode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithSpace() {
 String input = "kangaroo";
 String expected = "K2A2N2G2A2R2O2";
 String actual = caverphone.encode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithStartOptions() {
 String input = "enough cough";
 String expected = "E3N3O2U2F2";
 String actual = caverphone.encode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithHandledReplacements() {
 String input = "action tch";
 String expected = "A2C2I2O2N2";
 String actual = caverphone.encode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithHandledRemovals() {
 String input = "two three";
 String expected = "T2";
 String actual = caverphone.encode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithHandledStartOptionsAndReplacements() {
 String input = "tough enough trough";
 String expected = "T2O2U2F2";
 String actual = caverphone.encode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithHandledStartOptionsAndReplacementsAndRemovals() {
 String input = "enough tough enough trough";
 String expected = "E3N3O2U2F2";
 String actual = caverphone.encode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testEncodeNullObject() {
 assertThrows(EncoderException.class, () -> caverphone.encode(null));
 }

 @Test
 public void testEncodeNonStringObject() {
 assertThrows(EncoderException.class, () -> caverphone.encode(123));
 }

 @Test
 public void testDecodeEmptyString() {
 String input = "";
 String expected = "";
 String actual = caverphone.decode(caverphone.encode(input));
 assertEquals(expected, actual);
 }

 @Test
 public void testDecodeSingleLetterString() {
 String input = "A";
 String expected = "a";
 String actual = caverphone.decode(caverphone.encode(input));
 assertEquals(expected, actual);
 }

 @Test
 public void testDecodeMultipleLettersString() {
 String input = "H2L2O";
 String expected = "hello";
 String actual = caverphone.decode(caverphone.encode(input));
 assertEquals(expected, actual);
 }

 @Test
 public void testDecodeStringWithErrorScenario1() {
 String input = "12345";
 String expected = "";
 String actual = caverphone.decode(caverphone.encode(input));
 assertEquals(expected, actual);
 }

 @Test
 public void testDecodeStringWithErrorScenario2() {
 String input = "543210";
 String expected = "";
 String actual = caverphone.decode(caverphone.encode(input));
 assertEquals(expected, actual);
 }

 @Test
 public void testDecodeStringWithErrorScenario3() {
 String input = "123456111111";
 String expected = "12345";
 String actual = caverphone.decode(caverphone.encode(input));
 assertEquals(expected, actual);
 }

 @Test
 public void testDecodeStringWithErrorScenario4() {
 String input = "12345611111112";
 String expected = "123456";
 String actual = caverphone.decode(caverphone.encode(input));
 assertEquals(expected, actual);
 }

 @Test
 public void testDecodeStringWithErrorScenario5() {
 String input = "123456111111123";
 String expected = "12345611111112";
 String actual = caverphone.decode(caverphone.encode(input));
 assertEquals(expected, actual);
 }

 @Test
 public void testDecodeStringWithErrorScenario6() {
 String input = "A2B2C2D2E2F2G2H2I2J2K2L2M2N2O2P2";
 String expected = "abcdefghijklmnop";
 String actual = caverphone.decode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testDecodeStringWithErrorScenario7() {
 String input = "A2B2C2D2E2F2G2H2I2J2K2L2M2N2O2Q2";
 String expected = "abcdefghijklmnopq";
 String actual = caverphone.decode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsCaverphoneEqualEmptyStrings() {
 String string1 = "";
 String string2 = "";
 boolean expected = true;
 boolean actual = caverphone.isCaverphoneEqual(string1, string2);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsCaverphoneEqualIdenticalStrings() {
 String string1 = "hello";
 String string2 = "hello";
 boolean expected = true;
 boolean actual = caverphone.isCaverphoneEqual(string1, string2);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsCaverphoneEqualNonIdenticalStrings() {
 String string1 = "hello";
 String string2 = "world";
 boolean expected = false;
 boolean actual = caverphone.isCaverphoneEqual(string1, string2);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsCaverphoneEqualNullStrings() {
 String string1 = null;
 String string2 = null;
 boolean expected = true;
 boolean actual = caverphone.isCaverphoneEqual(string1, string2);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsCaverphoneEqualNullAndNonNullStrings() {
 String string1 = "";
 String string2 = null;
 boolean expected = false;
 boolean actual = caverphone.isCaverphoneEqual(string1, string2);
 assertEquals(expected, actual);
 }
}