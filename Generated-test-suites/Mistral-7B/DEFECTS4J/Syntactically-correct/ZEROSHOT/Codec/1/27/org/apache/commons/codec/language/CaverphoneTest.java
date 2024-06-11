import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

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
 public void testCaverphoneEncoding() {
 // Test simple case
 String input = "hello";
 String expected = "h3l3l3o";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);

 // Test cases with replacements
 String input1 = "Caversham";
 String expected1 = "C2v3rsh4m";
 String result1 = caverphone.caverphone(input1);
 assertEquals(expected1, result1);

 String input2 = "Caversham Project";
 String expected2 = "C2v3rsh4mp R2j3ekt";
 String result2 = caverphone.caverphone(input2);
 assertEquals(expected2, result2);

 String input3 = "Caverphone 2.0 algorithm";
 String expected3 = "C2v3rph3n3 2.0 2.l3g0r1th3m";
 String result3 = caverphone.caverphone(input3);
 assertEquals(expected3, result3);

 // Test cases with removals
 String input4 = "applesauce";
 String expected4 = "p3l3s";
 String result4 = caverphone.caverphone(input4);
 assertEquals(expected4, result4);

 String input5 = "caversham project test";
 String expected5 = "c2v3rsh4m p3j33t";
 String result5 = caverphone.caverphone(input5);
 assertEquals(expected5, result5);

 String input6 = "the quick brown fox jumps over the lazy dog";
 String expected6 = "th3 q3u1ck br3wn f3x j5m2s 0v3r th3 l4z3 d0g";
 String result6 = caverphone.caverphone(input6);
 assertEquals(expected6, result6);
 }

 @Test(expected = EncoderException.class)
 public void testNonStringInput() {
 caverphone.encode(123);
 }

 @Test
 public void testEncodeAndDecode() {
 String input = "hello";
 String encoded = caverphone.encode(input);
 String decoded = caverphone.decode(encoded);
 assertEquals(input, decoded);
 }

 @Test
 public void testDecodeNullInput() {
 assertNull(caverphone.decode(null));
 }

 @Test
 public void testDecodeEmptyInput() {
 assertNull(caverphone.decode(""));
 }

 @Test
 public void testDecodeInvalidInput() {
 String invalidInput = "111111111111111111";
 assertFalse(caverphone.isCaverphoneEqual("hello", invalidInput));
 assertNull(caverphone.decode(invalidInput));
 }

 @Test
 public void testDecodeValidInput() {
 String input = "h3l3l3o";
 String decoded = caverphone.decode(input);
 assertEquals("hello", decoded);
 }
}