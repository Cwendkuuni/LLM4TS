import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

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
 public void testLowercaseInput() {
 String input = "hello";
 String result = caverphone.caverphone(input);
 String expected = "h3l3l3o";
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveNonAlphabeticalCharacters() {
 String input = "Hello1, World!";
 String result = caverphone.caverphone(input);
 String expected = "hllo2wrd";
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveFinalE() {
 String input = "Stole the pen from the table.";
 String result = caverphone.caverphone(input);
 String expected = "stl3 th3 p3n fr3m th3 tab3l.";
 assertEquals(expected, result);
 }

 @Test
 public void testHandleSpecificStrings() {
 String input = "Cough Enough Trough Gnough";
 String result = caverphone.caverphone(input);
 String expected = "cou2f enou2f trou2f m2";
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceSequences() {
 String input = "Cucumber Cucumber Cucumber";
 String result = caverphone.caverphone(input);
 String expected = "2q2q2q";
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceIndividualLetters() {
 String input = "Ceasar";
 String result = caverphone.caverphone(input);
 String expected = "seasr";
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveDuplicates() {
 String input = "The quick brown fox jumps over the lazy dog";
 String result = caverphone.caverphone(input);
 String expected = "th3 qck brwn fx jmps vr th3 lzy dg";
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveLastThreeCharacters() {
 String input = "The quick brown fox jumps over the lazy dog.";
 String result = caverphone.caverphone(input);
 String expected = "th3 qck brwn fx jmps vr th3 lzy";
 assertEquals(expected, result.substring(0, result.length() - 3));
 }

 @Test
 public void testAddThree1sAtEnd() {
 String input = "The quick brown fox jumps over the lazy dog.";
 String result = caverphone.caverphone(input);
 String expected = "th3 qck brwn fx jmps vr th3 lzy";
 String resultWith1s = caverphone.caverphone(expected + "111111" + "1111");
 String expectedWith1s = "th3 qck brwn fx jmps vr th3 lzy 1111111111";
 assertEquals(expectedWith1s, resultWith1s);
 }

 @Test
 public void testReturnFirstTenCharacters() {
 String input = "The quick brown fox jumps over the lazy dog.";
 String result = caverphone.caverphone(input);
 String expected = "th3 qck brwn fx jmps vr th3 lzy";
 String expectedSubstring = expected.substring(0, 10);
 assertEquals(expectedSubstring, result.substring(0, 10));
 }

 @Test
 public void testDecodeAndEncodeEqual() throws EncoderException, DecoderException {
 String input = "The quick brown fox jumps over the lazy dog.";
 String encoded = caverphone.encode(input);
 String decoded = new StringDecoder().decode(encoded.getBytes(), "Caverphone");
 assertEquals(input, decoded);
 }

 @Test (expected = EncoderException.class)
 public void testEncodeNonStringInput() {
 caverphone.encode(123);
 }

}