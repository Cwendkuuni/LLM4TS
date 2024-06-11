import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testNullString() {
 try {
 caverphone.caverphone(null);
 fail("Expected EncoderException to be thrown");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testSingleCharacter() {
 String result = caverphone.caverphone("a");
 assertEquals("a", result.substring(0, 1));
 assertEquals("A1111111111", result);
 }

 @Test
 public void testSimpleString() {
 String result = caverphone.caverphone("example");
 assertEquals("E2x1m1p1l1e1", result);
 }

 @Test
 public void testComplexString() {
 String result = caverphone.caverphone("Caverphone algorithm");
 assertEquals("C2a1v1e1r1p1h1o1n1e1", result);
 }

 @Test
 public void testComplexStringWithDigits() {
 String result = caverphone.caverphone("The quick brown fox jumps over the lazy dog 12345");
 assertEquals("Th3 q1u1c1k1 b1r0wn f1o1x j1u1m1p1s 0v3r 1h2z1y d1o3g 12345", result);
 }

 @Test
 public void testStringWithSpecialCharacters() {
 String result = caverphone.caverphone("&*()_+%$@!?$#%^&*()_+%$@!");
 assertEquals("&*()_+%$@!?$#%^&*()_+%$@!", result);
 }

 @Test
 public void testStringWithMultipleSpaces() {
 String result = caverphone.caverphone("  The quick brown fox  jumps over the lazy dog 12345  ");
 assertEquals(" Th3 q1u1c1k1 b1r0wn f1o1x j1u1m1p1s 0v3r 1h2z1y d1o3g 12345 ", result);
 }

 @Test
 public void testEncodeDecode() throws DecoderException {
 String encoded = caverphone.encode("example");
 String decoded = caverphone.decode(encoded);
 assertEquals("example", decoded);
 }

 @Test
 public void testEncodeDecodeWithNull() throws DecoderException {
 try {
 caverphone.decode(null);
 fail("Expected DecoderException to be thrown");
 } catch (DecoderException e) {
 // Expected
 }
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "example";
 String str2 = "example";
 String str3 = "exampl3";
 assertTrue(caverphone.isCaverphoneEqual(str1, str1));
 assertTrue(caverphone.isCaverphoneEqual(str1, str2));
 assertFalse(caverphone.isCaverphoneEqual(str1, str3));
 }

}