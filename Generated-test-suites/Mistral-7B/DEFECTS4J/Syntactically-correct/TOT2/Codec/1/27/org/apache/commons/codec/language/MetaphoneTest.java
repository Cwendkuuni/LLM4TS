import org.apache.commons.codec.decode.Hex;
import org.apache.commons.codec.encode.Base64;
import org.junit.Test;
import org.junit.Assert.*;

public class MetaphoneTest {

 private Metaphone metaphone = new Metaphone();

 @Test
 public void testEmptyString() {
 String input = "";
 String expectedOutput = "";
 String output = metaphone.metaphone(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testSingleCharacter() {
 String input = "A";
 String expectedOutput = "A";
 String output = metaphone.metaphone(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testTwoCharacters() {
 String input = "AB";
 String expectedOutput = "B";
 String output = metaphone.metaphone(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testInitializeCases() {
 String input = "AKN";
 String expectedOutput = "K";
 String output = metaphone.metaphone(input);
 assertEquals(expectedOutput, output);

 input = "AET";
 expectedOutput = "A";
 output = metaphone.metaphone(input);
 assertEquals(expectedOutput, output);

 input = "APP";
 expectedOutput = "P";
 output = metaphone.metaphone(input);
 assertEquals(expectedOutput, output);

 input = "KNIGHT";
 expectedOutput = "N";
 output = metaphone.metaphone(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testInitialX() {
 String input = "X" ;
 String expectedOutput = "S";
 String output = metaphone.metaphone(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testInitialDoubleLetter() {
 String input = "FF";
 String expectedOutput = "F";
 String output = metaphone.metaphone(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testInitialCapitalLetter() {
 String input = "TheQuickBrownFox";
 String expectedOutput = "TQBFRX";
 String output = metaphone.metaphone(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = EncoderException.class)
 public void testNullInput() {
 Metaphone metaphone = new Metaphone();
 String input = null;
 metaphone.metaphone(input);
 }

 @Test(expected = EncoderException.class)
 public void testInputWithSpecialCharacters() {
 String input = "Test@123#$%^&*()_+={}[];:\"'<>,.?/~`| ";
 String expectedOutput = "Error";
 String output = metaphone.metaphone(input);
 Assert.assertNotEquals(expectedOutput, output);
 }

 @Test
 public void testInputWithNumbers() {
 String input = "Test123";
 String expectedOutput = "T123";
 String output = metaphone.metaphone(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testInputWithMultipleWords() {
 String input = "The Quick Brown Fox Jumps";
 String expectedOutput = "TQBFJMPS";
 String output = metaphone.metaphone(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeDecode() {
 String input = "The Quick Brown Fox Jumps";
 String expectedOutput = "TQBFJMPS";
 String encodedOutput = Base64.encodeBase64String(Hex.encodeHex(metaphone.encode(input).getBytes()));
 String decodedOutput = new String(Hex.decodeHex(Base64.decodeBase64(encodedOutput)));
 assertEquals(expectedOutput, decodedOutput);
 }

 @Test
 public void testMetaphoneEqual() {
 String input1 = "The Quick Brown Fox Jumps";
 String input2 = "The quick brown fox jumps";
 boolean expectedOutput = true;
 boolean output = metaphone.isMetaphoneEqual(input1, input2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testMetaphoneNotEqual() {
 String input1 = "The Quick Brown Fox Jumps";
 String input2 = "The Quick Brown Dog Jumps";
 boolean expectedOutput = false;
 boolean output = metaphone.isMetaphoneEqual(input1, input2);
 assertEquals(expectedOutput, output);
 }
}