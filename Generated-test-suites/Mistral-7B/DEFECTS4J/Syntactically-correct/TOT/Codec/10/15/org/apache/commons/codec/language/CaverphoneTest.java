import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testNullInput() {
 String input = null;
 String expected = "1111111111";
 String encoded = caverphone.encode(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testEmptyInput() {
 String input = "";
 String expected = "1111111111";
 String encoded = caverphone.encode(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testSingleLowercaseLetter() {
 String input = "a";
 String expected = "a";
 String encoded = caverphone.encode(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testSingleUppercaseLetter() {
 String input = "A";
 String expected = "A";
 String encoded = caverphone.encode(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testSingleDigit() {
 String input = "1";
 String expected = "1";
 String encoded = caverphone.encode(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testMultipleLowercaseLetters() {
 String input = "abc";
 String expected = "ababab";
 String encoded = caverphone.encode(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testMultipleUppercaseLetters() {
 String input = "ABC";
 String expected = "ABABAB";
 String encoded = caverphone.encode(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testMultipleDigits() {
 String input = "123";
 String expected = "121212";
 String encoded = caverphone.encode(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testLetterAndDigit() {
 String input = "a1b";
 String expected = "a1a1b1a1b";
 String encoded = caverphone.encode(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testWhitespace() {
 String input = " one two three ";
 String expected = "onetwothree";
 String encoded = caverphone.encode(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testPunctuation() {
 String input = "hello!";
 String expected = "hellohello";
 String encoded = caverphone.encode(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testSpecialChars() {
 String input = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
 String expected = "!@#$%^&*()_+-=[]{}|;':\".,./<>?!@#$%^&*()_+-=[]{}|;:\".,./<>?!@#$%^&*()_+-=[]{}|";
 String encoded = caverphone.encode(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testStringWithInvalidReplacements() {
 String input = "xqx";
 String expected = "k2k";
 String encoded = caverphone.encode(input);
 assertEquals(expected, encoded);
 }

 @Test(expected = EncoderException.class)
 public void testNonStringInput() {
 Integer input = 1;
 caverphone.encode(input);
 }

 @Test(expected = EncoderException.class)
 public void testNullOutput() {
 String encoded = null;
 assertThrows(EncoderException.class, () -> caverphone.decode(encoded));
 }

 @Test
 public void testDecodeAndEncodeIdentity() {
 String input = "abcd1234efgh";
 String encoded = caverphone.encode(input);
 String decoded = caverphone.decode(encoded);
 assertEquals(input, decoded);
 }

 @Test
 public void testDecodeInvalidCaverphone() {
 String encoded = "111111111112";
 assertNull(caverphone.decode(encoded));
 }

 @Test
 public void testDecodeEmptyCaverphone() {
 String encoded = "1111111111";
 String decoded = caverphone.decode(encoded);
 assertEquals("", decoded);
 }

 @Test
 public void testDecodeCaverphoneWithInvalidLength() {
 String encoded = "11111111111";
 assertThrows(DecoderException.class, () -> caverphone.decode("111111111112"));
 }

 @Test
 public void testDecodeCaverphoneWithInvalidCharacters() {
 String encoded = "111111111112";
 assertThrows(DecoderException.class, () -> caverphone.decode(encoded));
 }

 @Test
 public void testDecodeCaverphoneWithInvalidSequence() {
 String encoded = "11111111111R";
 assertThrows(DecoderException.class, () -> caverphone.decode(encoded));
 }

 @Test
 public void testDecodeCaverphoneWithWhitespace() {
 String encoded = "1111111111 onetwothree";
 assertThrows(DecoderException.class, () -> caverphone.decode(encoded));
 }

 @Test
 public void testDecodeCaverphoneWithPunctuation() {
 String encoded = "1111111111!@#$%^&*()_+-=[]{}|;':\",./<>?!@#$%^&*()_+-=[]{}|;:\".,./<>?!@#$%^&*()_+-=[]{}|";
 assertThrows(DecoderException.class, () -> caverphone.decode(encoded));
 }

 @Test
 public void testDecodeCaverphoneWithNonPrintableCharacters() {
 String encoded = "1111111111\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u0008\u0009\u000a\u000b\u000c\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018";
 assertThrows(DecoderException.class, () -> caverphone.decode(encoded));
 }

 @Test
 public void testDecodeCaverphoneWithValidSequenceShort() {
 String encoded = "111111";
 String decoded = caverphone.decode(encoded);
 assertEquals("", decoded);
 }

 @Test
 public void testDecodeCaverphoneWithValidSequenceLong() {
 String encoded = "1111111111111111";
 String decoded = caverphone.decode(encoded);
 assertEquals("", decoded);
 }

 @Test
 public void testDecodeCaverphoneWithValidSequenceLongerThanMaxSupported() {
 String encoded = "1111111111111111111111111111111111";
 assertThrows(DecoderException.class, () -> caverphone.decode(encoded));
 }

 @Test
 public void testDecodeCaverphoneWithValidSequenceMixedCase() {
 String encoded = "1111111111AbAbAbAb";
 String decoded = caverphone.decode(encoded);
 assertEquals("AbAbAbAb", decoded);
 }

 @Test
 public void testDecodeCaverphoneWithValidSequenceWithDigits() {
 String encoded = "1111111111123456";
 String decoded = caverphone.decode(encoded);
 assertEquals("23456", decoded);
 }

 @Test
 public void testDecodeCaverphoneWithValidSequenceWithDuplicateSequence() {
 String encoded = "1111111111111111";
 String decoded = caverphone.decode(encoded);
 assertEquals("", decoded);
 }

 @Test
 public void testToString() {
 String caverphoneInstance = new Caverphone().toString();
 assertEquals("Caverphone", caverphoneInstance);
 }
}