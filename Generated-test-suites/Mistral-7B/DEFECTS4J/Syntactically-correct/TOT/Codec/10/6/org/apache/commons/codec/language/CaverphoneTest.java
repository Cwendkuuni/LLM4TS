import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.encoder.Caverphone;

public class CaverphoneTest {

 private StringEncoder encoder = new Caverphone();

 @Test
 public void testEmptyString() {
 String input = "";
 String expected = "1111111111";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testNullString() {
 assertThrows(NullPointerException.class, () -> encoder.encode(null));
 }

 @Test
 public void testLowerCaseString() {
 String input = "Hello World";
 String expected = "H2l2o2 w2r2d";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testUpperCaseString() {
 String input = "HELLO WORLD";
 String expected = "H2l2o2 w2r2d";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testEmptyStringAfterRemovals() {
 String input = "Caversham Project";
 String expected = "C2v2r2 h2m2 p3o3j3 t3p3 i3c3";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testCaverphoneWithSpecialChars() {
 String input = "C@versh@m Pr0ject";
 String expected = "C@2v2@h2m2 p3o3j3 t3p3 i3c3";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testCaverphoneWithDigits() {
 String input = "12345";
 String expected = "12345";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testCaverphoneWithSpace() {
 String input = "Caverphone Algorithm";
 String expected = "C2v2p2 h3l3g3o2 t3 A2l3g3o2 r3i2t3m3";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testCaverphoneWithStartOptions() {
 String input = "Cough Caverphone";
 String expected = "c2u2f2 c2v2f";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testCaverphoneWithEndOptions() {
 String input = "enough Enough";
 String expected = "en2f2 E3n2f2";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testCaverphoneWithReplacements() {
 String input = "Caterpillar";
 String expected = "c2t2e2 t2p2p2 l2a2r2";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testCaverphoneWithMultipleReplacements() {
 String input = "Caterpillar Caterpillar";
 String expected = "c2t2e2 t2p2p2 l2a2r2 t2p2p2 l2a2r2";
 assertEquals(expected, encoder.encode(input));
 }

 @Test
 public void testCaverphoneWithSameInputAndDifferentOrder() {
 String input1 = "Caterpillar";
 String input2 = "pillarCater";
 String expected = "c2t2e2 t2p2p2 l2a2r2";
 assertEquals(expected, encoder.encode(input1));
 assertEquals(expected, encoder.encode(input2));
 }

 @Test
 public void testCaverphoneWithErrorScenarios() {
 String input = null;
 assertThrows(NullPointerException.class, () -> encoder.encode(input));

 input = " ";
 assertThrows(IllegalArgumentException.class, () -> encoder.encode(input));
 }

 @Test
 public void testCaverphoneWithTwoStrings() {
 String input1 = "Caterpillar";
 String input2 = "Butterfly";
 assertFalse(encoder.isCaverphoneEqual(input1, input2));
 }

 @Test
 public void testCaverphoneWithTwoEqualStrings() {
 String input1 = "Caterpillar";
 String input2 = "Caterpillar";
 assertTrue(encoder.isCaverphoneEqual(input1, input2));
 }

}