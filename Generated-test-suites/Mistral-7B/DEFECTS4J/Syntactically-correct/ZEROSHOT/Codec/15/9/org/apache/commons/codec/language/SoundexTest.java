import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexTest {

 @Test
 public void testSoundexUSEnglish() {
 Soundex soundex = Soundex.US_ENGLISH;
 String input1 = "Apple";
 String input2 = "Application";
 String input3 = "Algorithm";
 String input4 = "Always";
 String input5 = "Anna";
 String input6 = "Banana";

 String expected1 = "A320";
 String expected2 = "A1611320";
 String expected3 = "A2101300";
 String expected4 = "A111";
 String expected5 = "A123";
 String expected6 = "A120";

 String output1 = soundex.soundex(input1);
 String output2 = soundex.soundex(input2);
 String output3 = soundex.soundex(input3);
 String output4 = soundex.soundex(input4);
 String output5 = soundex.soundex(input5);
 String output6 = soundex.soundex(input6);

 assertEquals(expected1, output1);
 assertEquals(expected2, output2);
 assertEquals(expected3, output3);
 assertEquals(expected4, output4);
 assertEquals(expected5, output5);
 assertEquals(expected6, output6);
 }

 @Test
 public void testSoundexCustomMapping() {
 char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
 Soundex soundex = new Soundex(customMapping);

 String input1 = "Apple";
 String expected1 = soundex.soundex(input1); // This test will fail as the custom mapping does not match the default mapping

 assertEquals("Test with custom mapping should fail", null, expected1);
 }
}