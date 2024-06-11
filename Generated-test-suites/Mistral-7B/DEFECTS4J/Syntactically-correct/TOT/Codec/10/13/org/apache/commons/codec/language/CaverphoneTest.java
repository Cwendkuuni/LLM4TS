import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.strings.StringUtils;
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
 public void testNullString() {
 assertThrows(DecoderException.class, () -> caverphoneEncoder.encode(null));
 }

 @Test
 public void testLowercaseString() {
 String input = "hello world";
 String expectedOutput = "h2l2o2l2w2o2r2d2";
 String output = caverphoneEncoder.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStringWithSpecialCharacters() {
 String input = "Hello, world!";
 String expectedOutput = "H2l2o2l2w2o2r2d2!";
 String output = caverphoneEncoder.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = DecoderException.class)
 public void testStringWithFinalE() {
 String input = "enough";
 String expectedOutput = "enou2f";
 String output = caverphoneEncoder.encode(input);
 assertNotEquals(expectedOutput, output);
 }

 @Test(expected = DecoderException.class)
 public void testStringWithInitialCough() {
 String input = "cough";
 String expectedOutput = "cou2f";
 String output = caverphoneEncoder.encode(input);
 assertNotEquals(expectedOutput, output);
 }

 @Test(expected = DecoderException.class)
 public void testStringWithInitialRough() {
 String input = "rough";
 String expectedOutput = "rou2f";
 String output = caverphoneEncoder.encode(input);
 assertNotEquals(expectedOutput, output);
 }

 @Test(expected = DecoderException.class)
 public void testStringWithInitialTough() {
 String input = "tough";
 String expectedOutput = "tou2f";
 String output = caverphoneEncoder.encode(input);
 assertNotEquals(expectedOutput, output);
 }

 @Test(expected = DecoderException.class)
 public void testStringWithInitialTrough() {
 String input = "trough";
 String expectedOutput = "trou2f";
 String output = caverphoneEncoder.encode(input);
 assertNotEquals(expectedOutput, output);
 }

 @Test(expected = DecoderException.class)
 public void testStringWithInitialGn() {
 String input = "gn";
 String expectedOutput = "2n";
 String output = caverphoneEncoder.encode(input);
 assertNotEquals(expectedOutput, output);
 }

 @Test
 public void testStringWithMultipleReplacements() {
 String input = "action tchat";
 String expectedOutput = "act2ion t2chat";
 String output = caverphoneEncoder.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStringWithMultipleHandlingOptions() {
 String input = "hello world goodbye";
 String expectedOutput = "h2l2o2l2w2o2r2d2g3d3h3";
 String output = caverphoneEncoder.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStringWithSingleCharacter() {
 String input = "a";
 String expectedOutput = "A";
 String output = caverphoneEncoder.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStringWithMultipleHandlingOptionsAndSpecialCharacters() {
 String input = "action, tchat!";
 String expectedOutput = "act2ion, t2chat!";
 String output = caverphoneEncoder.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStringWithMultipleHandlingOptionsAndMultipleSpecialCharacters() {
 String input = "hello, world! goodbye, and welcome.";
 String expectedOutput = "h2l2o2l2w2o2r2d2g3d3h3, a2n3d, w2e2l2c3e.";
 String output = caverphoneEncoder.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEqualsStrings() {
 String str1 = "hello world";
 String str2 = "hello world";
 assertTrue(caverphoneEncoder.isCaverphoneEqual(str1, str2));
 }

 @Test
 public void testNotEqualsStrings() {
 String str1 = "hello world";
 String str2 = "hello world1";
 assertFalse(caverphoneEncoder.isCaverphoneEqual(str1, str2));
 }

 @Test
 public void testLengthStrings() {
 String str1 = "hello world";
 String str2 = "helloworld";
 assertFalse(caverphoneEncoder.isCaverphoneEqual(str1, str2));
 }

 @Test
 public void testNullStringComparison() {
 String str1 = "";
 String str2 = null;
 assertFalse(caverphoneEncoder.isCaverphoneEqual(str1, str2));
 }

 @Test
 public void testEmptyStringComparison() {
 String str1 = "";
 String str2 = "";
 assertTrue(caverphoneEncoder.isCaverphoneEqual(str1, str2));
 }

 @Test
 public void testStringWithSpecialCharacterComparison() {
 String str1 = "hello world!";
 String str2 = "hello world!";
 assertTrue(caverphoneEncoder.isCaverphoneEqual(str1, str2));
 }

 @Test
 public void testDifferentStrings() {
 String str1 = "hello world";
 String str2 = "goodbye world";
 assertFalse(caverphoneEncoder.isCaverphoneEqual(str1, str2));
 }
}