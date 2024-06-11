import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexTest {

 @Test
 public void testUS_ENGLISH_Soundex() throws EncoderException {
 Soundex soundex = Soundex.US_ENGLISH;
 String input1 = "Apple";
 String expected1 = "A120";
 String input2 = "Application";
 String expected2 = "A120";
 String input3 = "Orange";
 String expected3 = "O150";
 String input4 = "Banana";
 String expected4 = "B250";
 String input5 = "Caramel";
 String expected5 = "C310";
 String input6 = "Elephant";
 String expected6 = "E120";
 String input7 = "India";
 String expected7 = "I425";
 String input8 = "Indigo";
 String expected8 = "I321";

 assertEquals(expected1, soundex.encode(input1));
 assertEquals(expected2, soundex.encode(input2));
 assertEquals(expected3, soundex.encode(input3));
 assertEquals(expected4, soundex.encode(input4));
 assertEquals(expected5, soundex.encode(input5));
 assertEquals(expected6, soundex.encode(input6));
 assertEquals(expected7, soundex.encode(input7));
 assertEquals(expected8, soundex.encode(input8));
 }

 @Test
 public void testCustomSoundex() {
 Soundex soundex = new Soundex(new String("1010101010101010101010101010101010101010101010101010101010101010101010101").toCharArray());
 String input1 = "Apple";
 String expected1 = "A120";
 String input2 = "Application";
 String expected2 = "A120";
 String input3 = "Orange";
 String expected3 = "O150";
 String input4 = "Banana";
 String expected4 = "B250";
 String input5 = "Caramel";
 String expected5 = "C310";
 String input6 = "Elephant";
 String expected6 = "E120";
 String input7 = "India";
 String expected7 = "I425";
 String input8 = "Indigo";
 String expected8 = "I321";

 assertEquals(expected1, soundex.encode(input1));
 assertEquals(expected2, soundex.encode(input2));
 assertEquals(expected3, soundex.encode(input3));
 assertEquals(expected4, soundex.encode(input4));
 assertEquals(expected5, soundex.encode(input5));
 assertEquals(expected6, soundex.encode(input6));
 assertEquals(expected7, soundex.encode(input7));
 assertEquals(expected8, soundex.encode(input8));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNonAlphabetCharacter() {
 Soundex soundex = Soundex.US_ENGLISH;
 char invalidChar = '!';
 String input = Character.toString(invalidChar);
 soundex.encode(input);
 }

 @Test(expected = EncoderException.class)
 public void testNullInput() {
 Soundex soundex = Soundex.US_ENGLISH;
 String input = null;
 soundex.encode(input);
 }
}