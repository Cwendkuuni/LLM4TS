import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testUSEnglishSoundex() throws EncoderException {
        Soundex soundex = Soundex.US_ENGLISH;
        String input1 = "Apple";
        String expectedOutput1 = "A113";
        String output1 = soundex.soundex(input1);
        Assert.assertEquals(expectedOutput1, output1);

        String input2 = "Banana";
        String expectedOutput2 = "B110";
        String output2 = soundex.soundex(input2);
        Assert.assertEquals(expectedOutput2, output2);

        String input3 = "Cat";
        String expectedOutput3 = "K113";
        String output3 = soundex.soundex(input3);
        Assert.assertEquals(expectedOutput3, output3);

        String input4 = "Dog";
        String expectedOutput4 = "D114";
        String output4 = soundex.soundex(input4);
        Assert.assertEquals(expectedOutput4, output4);

        String input5 = "Elephant";
        String expectedOutput5 = "E111";
        String output5 = soundex.soundex(input5);
        Assert.assertEquals(expectedOutput5, output5);
    }

    @Test(expected = EncoderException.class)
    public void testNegativeScenarioSoundex() throws EncoderException {
        Soundex soundex = new Soundex(new char[]{'A', 'B'}); // invalid mapping
        String input = "Apple";
        String expectedExceptionMessage = "The mapping array is not valid";
        soundex.soundex(input);
        Assert.fail(expectedExceptionMessage);
    }

    @Test(expected = EncoderException.class)
    public void testNullScenarioSoundex() throws EncoderException {
        Soundex soundex = new Soundex();
        String input = null;
        String expectedExceptionMessage = "Parameter supplied to Soundex encode is not of type java.lang.String";
        soundex.encode(input);
        Assert.fail(expectedExceptionMessage);
    }
}