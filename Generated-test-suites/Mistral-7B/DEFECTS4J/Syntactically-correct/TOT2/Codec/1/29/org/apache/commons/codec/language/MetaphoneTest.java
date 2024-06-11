import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class MetaphoneTest {

 private StringEncoder encoder = new Metaphone();

 @Test
 public void testEmptyString() {
 String encoded = encoder.encode("");
 assertEquals("", encoded);
 }

 @Test
 public void testSingleCharacter() {
 String encoded = encoder.encode("A");
 assertEquals("A", encoded);
 }

 @Test
 public void testSimpleWord() {
 String encoded = encoder.encode("apple");
 assertEquals("APEL", encoded);
 }

 @Test
 public void testComplexWord() {
 String encoded = encoder.encode("metaphone");
 assertEquals("MF01N", encoded);
 }

 @Test(expected = EncoderException.class)
 public void testInvalidCharacter() {
 String encoded = encoder.encode("Apple1");
 fail("Expected EncoderException thrown for invalid character");
 }

 @Test
 public void testMaxCodeLength() {
 String encoded = encoder.encode("Metaphonealgorithmisafastandefficient algorithmforapproximatematchingstrings");
 assertEquals("M001F1N001A01L001G001O01T01S001I01F01A01L01G01O01T01S", encoded);
 }

 @Test
 public void testMetaphoneEquality() {
 String str1 = "cat";
 String str2 = "hat";
 boolean result = encoder.isMetaphoneEqual(str1, str2);
 assertFalse(result); // different metaphones

 str1 = "cat";
 str2 = "cat";
 result = encoder.isMetaphoneEqual(str1, str2);
 assertTrue(result); // same metaphone
 }

 @Test
 public void testSetMaxCodeLen() {
 Metaphone encoder = new Metaphone();
 encoder.setMaxCodeLen(3);
 String encoded = encoder.encode("Metaphonealgorithmisafastandefficient algorithmforapproximatematchingstrings");
 assertEquals("M001F1N001A01L001G001O01T01S", encoded);
 }

}