import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

 private static final Caverphone caverphoneInstance = new Caverphone();

 @Test
 public void testNullInput() {
  String expected = "1111111111";
  String actual = caverphoneInstance.caverphone(null);
  Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEmptyInput() {
  String expected = "1111111111";
  String actual = caverphoneInstance.caverphone("");
  Assert.assertEquals(expected, actual);
 }

 @Test
 public void testLowercaseInput() {
  String input = "hello world";
  String expected = "he2lo2 w3rd";
  String actual = caverphoneInstance.caverphone(input);
  Assert.assertEquals(expected, actual);
 }

 @Test
 public void testStringWithSpecialCharacters() {
  String input = "Hello World!@#$%^&*()_+=";
  String expected = "He2lo2 W3rd!@#$%^&*()_+=";
  String actual = caverphoneInstance.caverphone(input);
  Assert.assertEquals(expected, actual);
 }

 @Test(expected = EncoderException.class)
 public void testNonStringInput() {
  Object nonStringInput = 123;
  caverphoneInstance.encode(nonStringInput);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidCaverphoneCode() {
  String invalidCaverphoneCode = "invalidCaverphoneCode";
  StringDecoder decoder = new StringDecoder();
  decoder.decode(invalidCaverphoneCode);
 }

 @Test
 public void testDecodeValidCaverphoneCode() {
  String caverphoneCode = "A21T2P2S2K2F2M2N2L3W3R32";
  String expected = "Apple";
  String actual = new StringDecoder().decode(caverphoneCode);
  Assert.assertEquals(expected, actual);
 }

 @Test
 public void testDecodeValidCaverphoneCodeWithLeadingZeros() {
  String caverphoneCode = "A101T102P102S102K102F102M102N102L3W3R32";
  String expected = "Apple";
  String actual = new StringDecoder().decode(caverphoneCode);
  Assert.assertEquals(expected, actual);
 }

 @Test
 public void testDecodeValidCaverphoneCodeWithShortLength() {
  String caverphoneCode = "A1T1P1S1K1F1M1N1L3W3R2";
  String expected = "Apple";
  String actual = new StringDecoder().decode(caverphoneCode);
  Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneEqual() {
  String str1 = "Hello World!";
  String str2 = "Hello World!";
  boolean expected = true;
  boolean actual = caverphoneInstance.isCaverphoneEqual(str1, str2);
  Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneNotEqual() {
  String str1 = "Hello World!";
  String str2 = "Hello World!1";
  boolean expected = false;
  boolean actual = caverphoneInstance.isCaverphoneEqual(str1, str2);
  Assert.assertEquals(expected, actual);
 }
}