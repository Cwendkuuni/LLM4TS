import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

public class SoundexTest {

 private Soundex soundex;

 @Before
 public void setUp() {
 soundex = new Soundex();
 }

 @Test
 public void testEncodeBasic() {
 String input = "Apple";
 String expectedOutput = "A123";
 String output = soundex.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeEmptyString() {
 String input = "";
 String expectedOutput = "";
 String output = soundex.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeNull() {
 assertThrows(EncoderException.class, () -> soundex.encode(null));
 }

 @Test
 public void testEncodeLowercase() {
 String input = "apple";
 String expectedOutput = "A123";
 String output = soundex.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeWithSpecialChars() {
 String input = "Apple!";
 String expectedOutput = "A123";
 String output = soundex.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodeUnmappedChar() {
 String input = "Apple#";
 String output = soundex.encode(input);
 }

 @Test
 public void testDifferenceBasic() {
 String s1 = "Apple";
 String s2 = "Orange";
 int expectedOutput = 0;
 int output = soundex.difference(s1, s2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testDifferenceSameString() {
 String s1 = "Apple";
 String s2 = "Apple";
 int expectedOutput = 4;
 int output = soundex.difference(s1, s2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testDifferenceSubstring() {
 String s1 = "Apple";
 String s2 = "AppleApple";
 int expectedOutput = 4;
 int output = soundex.difference(s1, s2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testDifferenceEmptyStrings() {
 String s1 = "";
 String s2 = "";
 int expectedOutput = 4;
 int output = soundex.difference(s1, s2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testDifferenceNullString() {
 String s1 = null;
 String s2 = "Apple";
 int expectedOutput = 0;
 int output = soundex.difference(s1, s2);
 assertEquals(expectedOutput, output);
 }
}