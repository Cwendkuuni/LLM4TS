import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

 private StringEncoder encoder = new Caverphone();
 private StringDecoder decoder = new Caverphone();

 @Test
 public void testEncodeValidStrings() {
 String input = "The quick brown fox jumps over the lazy dog";
 String expected = "TqBkBrFxJmSpLgDyOvTh";
 String output = encoder.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testEncodeEmptyString() {
 String input = "";
 String expected = "1111111111";
 String output = encoder.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testEncodeNullString() {
 String input = null;
 try {
 String output = encoder.encode(input);
 fail("EncoderException expected");
 } catch (EncoderException e) {
 // expected
 }
 }

 @Test
 public void testEncodeSpecialCharacters() {
 String input = "!@#$%^&*()_+-={}[]|\\:\"<>,.?/";
 String expected = "1111111111";
 String output = encoder.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testEncodeUpperCaseString() {
 String input = "HELLO WORLD";
 String expected = "HlLwOrDl";
 String output = encoder.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testDecodeValidStrings() {
 String encoded = "TqBkBrFxJmSpLgDyOvTh";
 String decoded = decoder.decode(encoded);
 String input = "The quick brown fox jumps over the lazy dog";
 assertEquals(input, decoded);
 }

 @Test
 public void testDecodeEmptyString() {
 String encoded = "";
 String decoded = decoder.decode(encoded);
 String input = "";
 assertEquals(input, decoded);
 }

 @Test
 public void testDecodeNullString() {
 String encoded = null;
 try {
 String decoded = decoder.decode(encoded);
 fail("DecoderException expected");
 } catch (DecoderException e) {
 // expected
 }
 }

 @Test
 public void testDecodeInvalidString() {
 String encoded = "Invalid";
 try {
 String decoded = decoder.decode(encoded);
 fail("DecoderException expected");
 } catch (DecoderException e) {
 // expected
 }
 }
}