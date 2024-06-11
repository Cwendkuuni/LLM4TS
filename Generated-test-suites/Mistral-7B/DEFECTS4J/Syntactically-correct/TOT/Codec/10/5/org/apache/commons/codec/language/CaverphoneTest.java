import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final StringEncoder caverphoneEncoder = new Caverphone();

 @Test
 public void testEmptyString() {
 String input = "";
 String expectedOutput = "1111111111";
 String output = caverphoneEncoder.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testSingleCharacterString() {
 String input = "a";
 String expectedOutput = "a";
 String output = caverphoneEncoder.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testLowercaseString() {
 String input = "hello";
 String expectedOutput = "h2l2o";
 String output = caverphoneEncoder.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUppercaseString() {
 String input = "HELLO";
 String expectedOutput = "h2l2o";
 String output = caverphoneEncoder.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = EncoderException.class)
 public void testNullString() {
 String input = null;
 String expectedOutput = "1111111111";
 String output = caverphoneEncoder.encode(input);
 fail("Expected EncoderException, but got " + output);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeNullOutput() {
 String input = "test";
 String output = null;
 String decodedOutput = caverphoneEncoder.decode(output);
 fail("Expected DecoderException, but got " + decodedOutput);
 }

 @Test
 public void testDecodeEmptyOutput() {
 String input = "test";
 String output = "";
 String decodedOutput = caverphoneEncoder.decode(output);
 assertEquals("", decodedOutput);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidOutput() {
 String input = "test";
 String output = "invalid";
 String decodedOutput = caverphoneEncoder.decode(output);
 fail("Expected DecoderException, but got " + decodedOutput);
 }

 @Test
 public void testDecodeValidOutput() {
 String input = "test";
 String output = "t1t1";
 String decodedOutput = caverphoneEncoder.decode(output);
 assertEquals(input, decodedOutput);
 }

 @Test
 public void testDecodeSpecialCases() {
 String input = "Caversham Project at the University of Otago";
 String output = "C2va2s2h2a2 m2t2 h2i2 t2h2 U2t2 h2o2 g2o2";
 String decodedOutput = caverphoneEncoder.decode(output);
 assertEquals(input, decodedOutput);
 }

 @Test
 public void testDecodeWithDifferentCase() {
 String input = "Caversham Project at the University of Otago";
 String output = "C2va2s2h2a2 m2t2 h2i2 t2h2 U2t2 h2o2 g2o2";
 String decodedOutput = caverphoneEncoder.decode(output.toUpperCase());
 assertEquals(input, decodedOutput);
 }

 @Test
 public void testDecodeWithExtraCharacters() {
 String input = "Caversham Project at the University of Otago";
 String output = "C2va2s2h2a2 m2t2 h2i2 t2h2 U2t2 h2o2 g2o2X";
 String decodedOutput = caverphoneEncoder.decode(output);
 assertEquals(input, decodedOutput);
 }

 @Test
 public void testDecodeWithoutLeadingZeros() { // 1.0 only
 String input = "test";
 String output = "t1t1";
 String decodedOutput = caverphoneEncoder.decode(output.substring(0, 6));
 assertEquals(input, decodedOutput);
 }
}