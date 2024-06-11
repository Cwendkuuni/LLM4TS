import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class CaverphoneTest {

 private final Caverphone caverphoneEncoder = new Caverphone();

 @Test
 public void testEmptyString() {
 String caverphoneCode = caverphoneEncoder.caverphone("");
 assertEquals("1111111111", caverphoneCode);
 }

 @Test
 public void testNullString() {
 assertThrows(DecoderException.class, () -> caverphoneEncoder.caverphone(null));
 }

 @Test
 public void testSingleLetterString() {
 String caverphoneCode = caverphoneEncoder.caverphone("a");
 assertEquals("A", caverphoneCode);
 }

 @Test
 public void testAllLowercaseString() {
 String caverphoneCode = caverphoneEncoder.caverphone("hello");
 assertEquals("h2l2o2l2o", caverphoneCode);
 }

 @Test
 public void testAllUppercaseString() {
 String caverphoneCode = caverphoneEncoder.caverphone("HELLO");
 assertEquals("H2L2O2L2O", caverphoneCode);
 }

 @Test
 public void testStringWithDigits() {
 String caverphoneCode = caverphoneEncoder.caverphone("hello123");
 assertEquals("h2l2o2l2o123", caverphoneCode);
 }

 @Test
 public void testStringWithSpecialCharacters() {
 String caverphoneCode = caverphoneEncoder.caverphone("hello@world!");
 assertEquals("h2l2o2l2oAw1d3l1!", caverphoneCode);
 }

 @Test
 public void testStringWithEmptySpace() {
 String caverphoneCode = caverphoneEncoder.caverphone("hello world");
 assertEquals("h2l2o2l2o20w2l2d", caverphoneCode);
 }

 @Test
 public void testStringWithMultipleSpaces() {
 String caverphoneCode = caverphoneEncoder.caverphone("hello world hello world");
 assertEquals("h2l2o2l2o20w2l2d20w2l2d", caverphoneCode);
 }

 @Test
 public void testStringWithLeadingAndTrailingEmptySpaces() {
 String caverphoneCode = caverphoneEncoder.caverphone(" hello world ");
 assertEquals("h2l2o2l2o20w2l2d", caverphoneCode);
 }

 @Test
 public void testStringWithLeadingAndTrailingSpecialChars() {
 String caverphoneCode = caverphoneEncoder.caverphone("@hello world@");
 assertEquals("Aw1h2l2o2l2o20w2l2d", caverphoneCode);
 }

 @Test
 public void testCaverphoneEqualStrings() {
 String str1 = "hello world";
 String str2 = "hello world";
 assertEquals(caverphoneEncoder.caverphone(str1), caverphoneEncoder.caverphone(str2));
 }

 @Test
 public void testCaverphoneNotEqualStrings() {
 String str1 = "hello world";
 String str2 = "hello universe";
 assertFalse(caverphoneEncoder.isCaverphoneEqual(str1, str2));
 }

 @Test
 public void testEncodeInvalidObject() {
 assertThrows(EncoderException.class, () -> caverphoneEncoder.encode(123));
 }

 @Test
 public void testEncodeInvalidString() {
 assertThrows(DecoderException.class, () -> caverphoneEncoder.caverphone(null));
 }

 @Test
 public void testEncodeInvalidStringWithLengthZero() {
 String emptyString = "";
 assertThrows(DecoderException.class, () -> caverphoneEncoder.caverphone(emptyString));
 }

 @Test
 public void testEncodeInvalidStringWithLengthOne() {
 String stringWithSingleCharacter = "a";
 String encodedString = caverphoneEncoder.encode(stringWithSingleCharacter);
 assertEquals("A", encodedString);
 }

 @Test
 public void testEncodeString() {
 String stringToEncode = "hello world";
 String encodedString = caverphoneEncoder.encode(stringToEncode);
 assertEquals(caverphoneEncoder.caverphone(stringToEncode), encodedString);
 }

 @Test
 public void testEncodeStringWithLengthOneGreater() {
 String stringToEncode = "hello world hello world";
 String encodedString = caverphoneEncoder.encode(stringToEncode);
 assertEquals(caverphoneEncoder.caverphone(stringToEncode), encodedString);
 }

 @Test
 public void testEncodeStringWithLeadingEmptySpaces() {
 String stringToEncode = " hello world ";
 String encodedString = caverphoneEncoder.encode(stringToEncode);
 assertEquals(caverphoneEncoder.caverphone(" hello world "), encodedString);
 }

 @Test
 public void testEncodeStringWithLeadingAndTrailingSpecialChars() { {String stringToEncode = "@hello world@"; String encodedString = caverphoneEncoder.encode(stringToEncode); assertEquals(caverphoneEncoder.caverphone("@hello world@"), encodedString); } }

 @Test
 public void testEncodeStringWithDigits() { String stringToEncode = "hello123world"; String encodedString = caverphoneEncoder.encode(stringToEncode); assertEquals(caverphoneEncoder.caverphone("hello123world"), encodedString); }

 @Test
 public void testEncodeStringWithSpecialChars() { String stringToEncode = "hello@world!"; String encodedString = caverphoneEncoder.encode(stringToEncode); assertEquals(caverphoneEncoder.caverphone("hello@world!"), encodedString); } }