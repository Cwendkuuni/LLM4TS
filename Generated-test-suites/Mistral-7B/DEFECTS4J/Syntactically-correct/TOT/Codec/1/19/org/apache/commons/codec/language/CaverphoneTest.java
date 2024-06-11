import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final StringEncoder encoder = new Caverphone();

 @Test
 public void testEmptyString() throws DecoderException {
 String input = "";
 String expectedOutput = "1111111111";
 assertEquals(expectedOutput, encoder.encode(input));
 }

 @Test
 public void testNullString() throws DecoderException {
 String input = null;
 try {
 encoder.encode(input);
 fail("Should have thrown an exception");
 } catch (EncoderException e) {
 // Expected exception
 }
 }

 @Test
 public void testSimpleString() throws DecoderException {
 String input = "hello";
 String expectedOutput = "hAhA2l2l2o2o";
 assertEquals(expectedOutput, encoder.encode(input));
 }

 @Test
 public void testComplexString() throws DecoderException {
 String input = "The quick brown fox jumps over the lazy dog.";
 String expectedOutput = "Th3 q1ck br0wn f0x jm2p3s 0v3r th3 l4zy d0g.";
 assertEquals(expectedOutput, encoder.encode(input));
 }

 @Test(expected = DecoderException.class)
 public void testInvalidInput() throws DecoderException {
 String input = "12345";
 encoder.encode(input);
 fail("Should have thrown an exception");
 }

 @Test(expected = DecoderException.class)
 public void testInvalidInputWithSpecialChars() throws DecoderException {
 String input = "!@#$%^&*()_+={}[]|\\:;\"'<>,.?/~`";
 encoder.encode(input);
 fail("Should have thrown an exception");
 }

 @Test(expected = DecoderException.class)
 public void testInvalidInputWithUpperCase() throws DecoderException {
 String input = "HELLO";
 encoder.encode(input);
 fail("Should have thrown an exception");
 }

 @Test
 public void testEqualsStrings() {
 String str1 = "The quick brown fox jumps over the lazy dog.";
 String str2 = "The quick brown fox jumps over the lazy dog.";
 assertTrue(encoder.isCaverphoneEqual(str1, str2));
 }

 @Test
 public void testNotEqualsStrings() {
 String str1 = "The quick brown fox jumps over the lazy dog.";
 String str2 = "The quick brown fox jumps over the cat.";
 assertFalse(encoder.isCaverphoneEqual(str1, str2));
 }

}