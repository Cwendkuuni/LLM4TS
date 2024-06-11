import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.encoder.Caverphone;

public class CaverphoneTest {

 private final StringEncoder caverphoneEncoder = new Caverphone();

 @Test
 public void testEmptyString() {
 String expected = "1111111111";
 String input = "";
 String result = caverphoneEncoder.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testNullString() {
 String expected = "1111111111";
 String input = null;
 try {
 String result = caverphoneEncoder.encode(input);
 fail("Expected EncoderException for null input");
 } catch (EncoderException e) {
 // expected
 }
 assertEquals(expected, caverphoneEncoder.encode(""));
 }

 @Test
 public void testSingleLowerCaseLetter() {
 String expected = "A";
 String input = "a";
 String result = caverphoneEncoder.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testSingleUpperCaseLetter() {
 String expected = "1";
 String input = "A";
 String result = caverphoneEncoder.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testStringWithSpecialCharacters() {
 String expected = "1111111111";
 String input = "!@#$%^&*()_+-=[]{}|;:,.<>?";
 String result = caverphoneEncoder.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testStringWithMultipleSpecialCharacters() {
 String expected = "1111111111";
 String input = "!@#$%^&*()_+-=[]{}|;:,.<>?";
 String result = caverphoneEncoder.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testStringWithMultipleLetters() {
 String expected = "AS";
 String input = "hello";
 String result = caverphoneEncoder.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneEquals() {
 String str1 = "hello";
 String str2 = "hello";
 boolean expected = true;
 boolean result = caverphoneEncoder.isCaverphoneEqual(str1, str2);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneNotEqual() {
 String str1 = "hello";
 String str2 = "world";
 boolean expected = false;
 boolean result = caverphoneEncoder.isCaverphoneEqual(str1, str2);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneEncodeAndDecode() {
 String str = "hello";
 String encoded = caverphoneEncoder.encode(str);
 String decoded = caverphoneEncoder.decode(encoded);
 boolean expected = str.equals(decoded);
 boolean result = true;
 assertEquals(expected, result);
 }

 @Test (expected = EncoderException.class)
 public void testNullInputExpection() {
 StringEncoder encoder = new Caverphone();
 encoder.encode(null);
 }
}