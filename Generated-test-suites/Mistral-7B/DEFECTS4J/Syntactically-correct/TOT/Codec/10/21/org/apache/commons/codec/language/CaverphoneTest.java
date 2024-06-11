import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphoneEncoder = new Caverphone();

 @Test
 public void testEmptyString() {
 String emptyString = "";
 String expected = "1111111111";
 String actual = caverphoneEncoder.encode(emptyString);
 assertEquals(expected, actual);
 }

 @Test
 public void testNullString() {
 String nullString = null;
 try {
 String actual = caverphoneEncoder.encode(nullString);
 fail("Expected EncoderException for null input");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testSingleCharacterString() {
 String singleCharacter = "a";
 String expected = "A";
 String actual = caverphoneEncoder.encode(singleCharacter);
 assertEquals(expected, actual);
 }

 @Test
 public void testMultipleCharactersString() {
 String multipleCharacters = "abcdefghijklmnopqrstuvwxyz";
 String expected = "2A2T2F2S2L2P2K2F2M2N2W3R3L32";
 String actual = caverphoneEncoder.encode(multipleCharacters);
 assertEquals(expected, actual);
 }

 @Test
 public void testCaseInsensitive() {
 String caseInsensitive = "AbCdEfGhIjKlMnOpQrStUvWxYz";
 String expected = "2A2T2F2S2L2P2K2F2M2N2W3R3L32";
 String actual = caverphoneEncoder.encode(caseInsensitive);
 assertEquals(expected, actual);
 }

 @Test
 public void testSpecialCharacters() {
 String specialCharacters = "!@#$%^&*()_+-=[]{}|;:,.<>?";
 String expected = "";
 try {
 String actual = caverphoneEncoder.encode(specialCharacters);
 fail("Expected EncoderException for special characters input");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testEncodeDecode() {
 String encoded = "2A2T2F2S2L2P2K2F2M2N2W3R3L32";
 String decoded = caverphoneEncoder.decode(Hex.decodeHex(encoded.substring(0, 24).toCharArray()));
 String expected = "abcdefghijklmnopqrstuvwxyz";
 assertEquals(expected, decoded);
 }

 @Test(expected = EncoderException.class)
 public void testInvalidInput() {
 String invalidInput = "abcde";
 try {
 String actual = caverphoneEncoder.encode(invalidInput);
 fail("Expected EncoderException for invalid input");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "abcdefghijklmnopqrstuvwxyz";
 String str2 = "abcdefghijklmnopqrstuvwxyz";
 String str3 = "abcdefghijklmnoqrstuvwxyz";
 assertTrue(caverphoneEncoder.isCaverphoneEqual(str1, str2));
 assertTrue(caverphoneEncoder.isCaverphoneEqual(str1, str3));
 assertFalse(caverphoneEncoder.isCaverphoneEqual(str1, "abcdefghijklmnopqrstuvwxyzX"));
 }

}