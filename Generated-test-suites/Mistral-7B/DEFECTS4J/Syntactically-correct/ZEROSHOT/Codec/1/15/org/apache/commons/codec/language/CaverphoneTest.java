import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testNullString() {
 try {
 String result = caverphone.caverphone(null);
 fail("Expected EncoderException for null String");
 } catch (EncoderException e) {
 // expected
 }
 }

 @Test
 public void testLowerCase() {
 String result = caverphone.caverphone("hello");
 assertEquals("he2o3l3o", result);
 }

 @Test
 public void testRemoveNonLetters() {
 String result = caverphone.caverphone("123Hello45World!");
 assertEquals("he2o3l3o", result);
 }

 @Test
 public void testRemoveFinalE() {
 String result = caverphone.caverphone("enough");
 assertEquals("enou2f", result);
 }

 @Test
 public void testStartsWith() {
 String result = caverphone.caverphone("coughenough");
 assertEquals("cou2fenou2f", result);
 }

 @Test
 public void testReplacements() {
 String result = caverphone.caverphone("ecstasy");
 assertEquals("e2s2t2a2s2t2y", result);
 }

 @Test
 public void testMultipleReplacements() {
 String result = caverphone.caverphone("coughingecstasytrough");
 assertEquals("cou2fingecou2f2ghenecou2f2gh3trou2f", result);
 }

 @Test
 public void testHandleRemovals() {
 String result = caverphone.caverphone("ph2ng3gh3");
 assertEquals("ph2ng3", result);
 }

 @Test
 public void testEmptyCode() {
 String result = caverphone.caverphone("2");
 assertEquals("", result);
 }

 @Test
 public void testFinalCode() {
 String result = caverphone.caverphone("3");
 assertEquals("A", result);
 }

 @Test
 public void testEncodeObject() {
 try {
 Object result = caverphone.encode(123);
 fail("Expected EncoderException for non-String object");
 } catch (EncoderException e) {
 // expected
 }
 }

 @Test
 public void testEncodeString() {
 String result = (String) caverphone.encode("hello");
 assertEquals("he2o3l3o", result);
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "he2o3l3o";
 String str2 = "he2o3l3o";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertTrue(result);
 }

 @Test
 public void testIsCaverphoneNotEqual() {
 String str1 = "he2o3l3o";
 String str2 = "he2o3l3p";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertFalse(result);
 }

}