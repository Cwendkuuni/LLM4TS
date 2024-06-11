import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

 private final StringEncoder caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
 String input = "";
 String expected = "1111111111";
 try {
 String output = caverphone.encode(input);
 assertEquals(expected, output);
 } catch (EncoderException e) {
 // This should not be thrown for an empty string
 }
 }

 @Test
 public void testNullString() {
 String input = null;
 try {
 String output = caverphone.encode(input);
 fail("NullPointerException expected");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testSingleLetter() {
 String input = "a";
 String expected = "a";
 String output = caverphone.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testAllLetters() {
 String input = "abcdefghijklmnopqrstuvwxyz";
 String expected = "2222222222222222222222222222";
 String output = caverphone.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testMiddleCase() {
 String input = "ABcDeFgHiJkLmNoPqRsTuVwXyZ";
 String expected = "A2A2A2A2A2A2A2A2A2A2A2A2A2";
 String output = caverphone.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testCaverphoneEqual() {
 String input1 = "abcdefghijklmnopqrstuvwxyz";
 String input2 = "abcdefghijklmnopqrstuvwxyz";
 String expected = "2222222222222222222222222222";
 String output1 = caverphone.encode(input1);
 String output2 = caverphone.encode(input2);
 assertEquals(expected, output1);
 assertEquals(expected, output2);
 assertEquals(caverphone.isCaverphoneEqual(input1, input2), true);
 }

 @Test
 public void testCaverphoneNotEqual() {
 String input1 = "abcdefghijklmnopqrstuvwxyz";
 String input2 = "AbCdEfGhIjKlMnOpQrStUvWxYz";
 String expected = "2222222222222222222222222222";
 String output1 = caverphone.encode(input1);
 String output2 = caverphone.encode(input2);
 assertEquals(expected, output1);
 assertEquals(expected, output2);
 assertEquals(caverphone.isCaverphoneEqual(input1, input2), false);
 }

 @Test
 public void testEncodeException() {
 String input = "12345";
 try {
 caverphone.encode(input);
 fail("EncoderException expected");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testDecodeInvalidInput() {
 String input = "123456";
 try {
 caverphone.decode(input);
 fail("DecoderException expected");
 } catch (DecoderException e) {
 // Expected
 }
 }

 @Test
 public void testDecodeEmptyInput() {
 String input = "";
 try {
 caverphone.decode(input);
 fail("DecoderException expected");
 } catch (DecoderException e) {
 String expected = "1111111111";
 String output = caverphone.encode(expected);
 assertEquals(output, input);
 }
 }

 @Test
 public void testDecodeInvalidLength() {
 String input = "11111111111";
 try {
 caverphone.decode(input);
 fail("DecoderException expected");
 } catch (DecoderException e) {
 // Expected
 }
 }

 @Test
 public void testDecodeValidInput() {
 String input = "2222222222222222222222222222";
 String expected = "abcdefghijklmnopqrstuvwxyz";
 String output = caverphone.decode(input);
 assertEquals(expected, output);
 }
}