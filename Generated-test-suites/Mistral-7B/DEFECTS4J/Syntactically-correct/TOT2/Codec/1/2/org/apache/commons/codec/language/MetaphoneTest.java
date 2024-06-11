import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Metaphone;
import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

 private final Metaphone metaphone = new Metaphone();

 @Test
 public void testEmptyString() {
 String actual = metaphone.metaphone("");
 String expected = "";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSingleCharacterString() {
 String actual = metaphone.metaphone("A");
 String expected = "A";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMetaphoneEncoding() {
 String actual = metaphone.metaphone("Apple");
 String expected = "APEL";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMetaphoneEncodingWithMultipleWords() {
 String actual = metaphone.metaphone("This is a test");
 String expected = "T2S1 I1 S1 A1 T1";
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = EncoderException.class)
 public void testNullInput() {
 metaphone.encode(null);
 }

 @Test
 public void testMetaphoneEncodingWithSilentG() {
 String actual = metaphone.metaphone("Banana");
 String expected = "BNNG";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMetaphoneEncodingWithSilentFinalH() {
 String actual = metaphone.metaphone("Athens");
 String expected = "ATHN";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMetaphoneEncodingWithInitialX() {
 String actual = metaphone.metaphone("Xylophone");
 String expected = "KSXLFON";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMetaphoneEncodingWithInitialC() {
 String actual = metaphone.metaphone("Cucumber");
 String expected = "KUKMBR";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMetaphoneEncodingWithInitialQu() {
 String actual = metaphone.metaphone("Quercus");
 String expected = "KWERS";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMetaphoneEncodingWithInitialCapitalLetter() {
 String actual = metaphone.metaphone("Apple");
 String expected = "APEL";
 String inputWithCapitalFirstLetter = "Apple";
 inputWithCapitalFirstLetter = inputWithCapitalFirstLetter.toUpperCase();
 Assert.assertEquals(expected, metaphone.metaphone(inputWithCapitalFirstLetter));
 }
}