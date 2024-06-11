import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
 public void testSingleCharacterInput() {
 String result = caverphone.caverphone("a");
 assertEquals("A", result);
 }

 @Test
 public void testSimpleStringInput() {
 String result = caverphone.caverphone("hello");
 assertEquals("H2l3l3o", result);
 }

 @Test
 public void testComplexStringInput() {
 String result = caverphone.caverphone("Caversham Project at the University of Otago");
 assertEquals("C2a2v3r5h3a2m3 P4r5o3j3i3t3 University of Ot3g0");
 }

 @Test
 public void testCaverphoneDecode() {
 String encoded = caverphone.caverphone("C2a2v3r5h3a2m3 P4r5o3j3i3t3 University of Ot3g0");
 String decoded = new Caverphone().decode(encoded);
 assertEquals("Caversham Project at the University of Otago", decoded);
 }

 @Test(expected = EncoderException.class)
 public void testNullInputToEncode() {
 caverphone.encode(null);
 }

 @Test(expected = EncoderException.class)
 public void testEmptyInputToEncode() {
 caverphone.encode("");
 }

 @Test(expected = EncoderException.class)
 public void testNonStringInputToEncode() {
 caverphone.encode(123);
 }

 @Test
 public void testEncodeAndDecodeIdentity() {
 String original = "hello";
 String encoded = caverphone.caverphone(original);
 String decoded = new Caverphone().decode(encoded);
 assertEquals(original, decoded);
 }

 @Test
 public void testEncodeAndDecodeDifferentStrings() {
 String str1 = "hello";
 String str2 = "world";
 assertFalse(caverphone.isCaverphoneEqual(str1, str2));
 }

}