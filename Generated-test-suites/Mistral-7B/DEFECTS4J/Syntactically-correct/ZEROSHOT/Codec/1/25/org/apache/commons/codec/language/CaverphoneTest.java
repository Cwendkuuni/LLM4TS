import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.codec.language.Caverphone;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testNullString() {
 String result = caverphone.caverphone(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testEmptyString() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testLowerCase() {
 String result = caverphone.caverphone("hello world");
 String expected = "h2e2l2o2w2r2d2";
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveNonAlphabets() {
 String result = caverphone.caverphone("1hello world2");
 String expected = "h2e2l2o2w2r2d2";
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveFinalE() {
 String result = caverphone.caverphone("enough");
 String expected = "enou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesStartOptions() {
 String result = caverphone.caverphone("coughtough");
 String expected = "cou2fou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesReplacements() {
 String result = caverphone.caverphone("cthqcytch");
 String expected = "2qsi2ch";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesRemovals() {
 String result = caverphone.caverphone("12321");
 String expected = "";
 assertEquals(expected, result);
 }

 @Test
 public void testReturnsFirstSixChars() {
 String result = caverphone.caverphone("thequickbrownfoxjumpsoverthelazydog");
 String expected = "th3q1c1k1b1r2ow3n1j1mp3s3o3v3th3l4z1d0g";
 assertEquals(expected.substring(0, 6), result.substring(0, 6));
 }

 @Test (expected = EncoderException.class)
 public void testNonStringObject() {
 caverphone.encode(new Object());
 }
}