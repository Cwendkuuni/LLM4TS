import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexTest {

 private Soundex soundexInstance;

 @Test
 public void testDefaultSoundexInstance() {
 soundexInstance = Soundex.US_ENGLISH;
 String inputString1 = "Apple";
 String inputString2 = "Application";
 String expectedSoundexCode1 = "A120";
 String expectedSoundexCode2 = "A120";

 String soundexCode1 = soundexInstance.soundex(inputString1);
 String soundexCode2 = soundexInstance.soundex(inputString2);

 assertEquals(expectedSoundexCode1, soundexCode1);
 assertEquals(expectedSoundexCode2, soundexCode2);
 }

 @Test(expected = EncoderException.class)
 public void testInvalidInputException() {
 soundexInstance = new Soundex(null);
 String inputString = null;

 try {
 soundexInstance.soundex(inputString);
 } catch (EncoderException e) {
 // Expected exception
 }
 }

 @Test
 public void testEmptyInput() {
 soundexInstance = new Soundex();
 String inputString = "";
 String expectedSoundexCode = "";

 String soundexCode = soundexInstance.soundex(inputString);

 assertEquals(expectedSoundexCode, soundexCode);
 }

 @Test
 public void testSingleCharacterInput() {
 soundexInstance = new Soundex();
 String inputString = "A";
 String expectedSoundexCode = "A000";

 String soundexCode = soundexInstance.soundex(inputString);

 assertEquals(expectedSoundexCode, soundexCode);
 }

 @Test
 public void testMultipleCodeGroupCharacters() {
 soundexInstance = new Soundex();
 String inputString1 = "Battery";
 String inputString2 = "Batteries";
 String expectedSoundexCode1 = "B320";
 String expectedSoundexCode2 = "B320";

 String soundexCode1 = soundexInstance.soundex(inputString1);
 String soundexCode2 = soundexInstance.soundex(inputString2);

 assertEquals(expectedSoundexCode1, soundexCode1);
 assertEquals(expectedSoundexCode2, soundexCode2);
 }

 @Test
 public void testMultipleCharactersWithDifferentCodes() {
 soundexInstance = new Soundex();
 String inputString1 = "Cake";
 String inputString2 = "Dog";
 String expectedSoundexCode1 = "K100";
 String expectedSoundexCode2 = "D230";

 String soundexCode1 = soundexInstance.soundex(inputString1);
 String soundexCode2 = soundexInstance.soundex(inputString2);

 assertEquals(expectedSoundexCode1, soundexCode1);
 assertEquals(expectedSoundexCode2, soundexCode2);
 }

 @Test
 public void testHWRule() {
 soundexInstance = new Soundex();
 String inputString1 = "Happy";
 String inputString2 = "Happy Hour";
 String expectedSoundexCode1 = "H100";
 String expectedSoundexCode2 = "H100";

 String soundexCode1 = soundexInstance.soundex(inputString1);
 String soundexCode2 = soundexInstance.soundex(inputString2);

 assertEquals(expectedSoundexCode1, soundexCode1);
 assertEquals(expectedSoundexCode2, soundexCode2);
 }

 @Test
 public void testCustomMapping() {
 soundexInstance = new Soundex(new char[]{'B', '1', 'C', '2', 'D', '3', 'E', '4', 'F', '5', 'G', '6', 'H', '7', 'I', '8', 'J', '9', 'K', '10', 'L', '11', 'M', '12', 'N', '13', 'O', '14', 'P', '15', 'Q', '16', 'R', '17', 'S', '18', 'T', '19', 'U', '20', 'V', '21', 'W', '22', 'X', '23', 'Y', '24', 'Z', '25'});
 String inputString1 = "Apple";
 String inputString2 = "Application";
 String expectedSoundexCode1 = "C120";
 String expectedSoundexCode2 = "C120";

 String soundexCode1 = soundexInstance.soundex(inputString1);
 String soundexCode2 = soundexInstance.soundex(inputString2);

 assertEquals(expectedSoundexCode1, soundexCode1);
 assertEquals(expectedSoundexCode2, soundexCode2);
 }

}