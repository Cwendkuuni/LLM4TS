import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Metaphone;
import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

 private final Metaphone metaphone = new Metaphone();

 @Test
 public void testEmptyString() {
 String input = "";
 String expectedOutput = "";
 try {
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 } catch (EncoderException e) {
 // empty string is a valid input
 }
 }

 @Test
 public void testSingleCharacterString() {
 String input = "A";
 String expectedOutput = "A";
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testBasicString() {
 String input = "apple";
 String expectedOutput = "APEL";
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test(expected = EncoderException.class)
 public void testInvalidInput() {
 String input = "apple123";
 metaphone.encode(input); // should throw an exception
 }

 @Test
 public void testInitialExceptions() {
 // Test initial exceptions
 String input = "KNapple";
 String expectedOutput = "NAPP";
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);

 input = "appAle";
 expectedOutput = "APEL";
 output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);

 input = "appWhale";
 expectedOutput = "WHAL";
 output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);

 input = "apExple";
 expectedOutput = "APEX";
 output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testDigits() {
 String input = "1234apple";
 String expectedOutput = "APEL";
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testDgeDgiDgy() {
 String input = "dgedog";
 String expectedOutput = "JJGT";
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testGh() {
 String input = "bugh";
 String expectedOutput = "BHG";
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);

 input = "bough";
 expectedOutput = "BHG";
 output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);

 input = "bought";
 expectedOutput = "BHT";
 output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);

 input = "boughten";
 expectedOutput = "BHTN";
 output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testTh() {
 String input = "thought";
 String expectedOutput = "T00T";
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testSh() {
 String input = "shoe";
 String expectedOutput = "S00E";
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testCh() {
 String input = "church";
 String expectedOutput = "KURCH";
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testCi() {
 String input = "city";
 String expectedOutput = "SITY";
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testCiWithPrecedingS() {
 String input = "scity";
 String expectedOutput = "SITY";
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testCea() {
 String input = "ceiling";
 String expectedOutput = "SILG";
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testCeaWithPrecedingS() {
 String input = "sceling";
 String expectedOutput = "SILG";
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test(expected = EncoderException.class)
 public void testInvalidInputWithDigits() {
 String input = "1apple";
 metaphone.encode(input); // should throw an exception
 }

 @Test(expected = EncoderException.class)
 public void testInvalidInputWithSpecialChars() {
 String input = "apple#";
 metaphone.encode(input); // should throw an exception
 }

 @Test
 public void testIsMetaphoneEqual() {
 String input1 = "apple";
 String input2 = "apple";
 String input3 = "aple";
 String expectedOutput = true;
 boolean output = metaphone.isMetaphoneEqual(input1, input2);
 Assert.assertEquals(expectedOutput, output);
 Assert.assertFalse(metaphone.isMetaphoneEqual(input1, input3));
 }

 @Test
 public void testSetMaxCodeLen() {
 Metaphone metaphone = new Metaphone();
 int maxCodeLen = 5;
 metaphone.setMaxCodeLen(maxCodeLen);
 String input = "apple";
 String expectedOutput = "APEL";
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 Assert.assertEquals(maxCodeLen, metaphone.getMaxCodeLen());
 }
}