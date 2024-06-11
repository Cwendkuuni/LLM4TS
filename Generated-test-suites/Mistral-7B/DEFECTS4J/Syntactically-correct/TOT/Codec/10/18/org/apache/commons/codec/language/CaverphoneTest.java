import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final StringEncoder encoder = new Caverphone();

 @Test
 public void testEmptyString() {
 String input = "";
 String expected = "1111111111";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testNullString() {
 assertNull(encoder.encode(null));
 }

 @Test
 public void testLowercaseString() {
 String input = "hello world";
 String expected = "h2l2o2l2o2w2r2d2";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testStringWithDigits() {
 String input = "hello123 world";
 String expected = "h2l2o2l2o2w2r2d23";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testEmptyStringAfterHandlingStartOptions() {
 String input = "tough enought";
 String expected = "tou2f enou2f";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testStringWithUpperCaseLetters() {
 String input = "HELLO WORLD";
 String expected = "H2L2O2L2O2W2R2D2";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testStringWithSpecialCharacters() {
 String input = "Hello, World!";
 String expected = "H2L2O2L2O2W2R2D2!";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testHandlingReplacements() {
 String input = "Caversham Project";
 String expected = "C3v3rsh3mPr3j3ct";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testHandlingRemovals() {
 String input = "Caverphone is awesome!";
 String expected = "Cv3rph3n3 IS AW3S3M3";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testHandlingStartOptionsAndReplacements() {
 String input = "Caverphone Algorithm";
 String expected = "C3v3rsh3mAlgoR3th3m";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testHandlingStartOptionsAndReplacementsWithEmptyString() {
 String input = "Algorithm for Caverphone";
 String expected = "AlgoR3th3mF0rC3v3rsh3m";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "Caverphone Algorithm";
 String str2 = "Algorithm for Caverphone";
 String caverphone1 = encoder.encode(str1);
 String caverphone2 = encoder.encode(str2);
 assertTrue(encoder.isCaverphoneEqual(str1, str2));
 assertTrue(caverphone1.equals(caverphone2));
 }

 @Test(expected = EncoderException.class)
 public void testNonStringInput() {
 encoder.encode(42);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeNull() {
 encoder.decode(null);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeEmptyString() {
 String decoded = encoder.decode("");
 fail("Decoding of empty string should throw DecoderException");
 }

 @Test
 public void testDecodeInvalidCaverphone() {
 String encoded = "111111111110"; // invalid
 String decoded = encoder.decode(encoded);
 assertNull(decoded);
 }

 @Test
 public void testDecodeValidCaverphone() {
 String encoded = "H2L2O2L2O2W2R2D2!";
 String decoded = encoder.decode(encoded);
 assertEquals("Hello, World!", decoded);
 }

}