package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Random;

public class SoundexTest {

    @Test
    public void testDefaultSoundexInstance() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple, Banana, Cat, Dog, Elephant";
        String expectedOutput = "A133, A1211, K131, D224, E113";

        StringBuilder outputBuilder = new StringBuilder();
        String[] inputs = input.split(", ");
        for (String inputStr : inputs) {
            String soundexCode = soundex.encode(inputStr);
            outputBuilder.append(soundexCode).append(" ");
        }

        String output = outputBuilder.toString().trim();
        assertEquals(expectedOutput, output);
    }

    @Test(expected = EncoderException.class)
    public void testInvalidInputSoundexInstance() {
        Soundex soundex = new Soundex(new char[0]);
        String input = "Apple, Banana, Cat, Dog, Elephant";
        String[] inputs = input.split(", ");

        for (String inputStr : inputs) {
            soundex.encode(inputStr);
        }
    }

    @Test
    public void testCustomSoundexInstance() {
        String mapping = "0122012230120112401255012623010202"; // Dutch mapping
        Soundex soundex = new Soundex(mapping);
        String input = "Apple, Banana, Cat, Dog, Elephant";
        String expectedOutput = "A133, A121, K131, D220, E113";

        StringBuilder outputBuilder = new StringBuilder();
        String[] inputs = input.split(", ");
        for (String inputStr : inputs) {
            String soundexCode = soundex.encode(inputStr);
            outputBuilder.append(soundexCode).append(" ");
        }

        String output = outputBuilder.toString().trim();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEmptyStringInputSoundexInstance() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "";
        String expectedOutput = "";

        String soundexCode = soundex.encode(input);
        assertEquals(expectedOutput, soundexCode);
    }

    @Test
    public void testSingleCharacterInputSoundexInstance() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "A";
        String expectedOutput = "A000";

        String soundexCode = soundex.encode(input);
        assertEquals(expectedOutput, soundexCode);
    }

    @Test
    public void testMultipleSimilarStringsSoundexInstance() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple, Apples, Applebee, Applejack";
        String expectedOutput = "A133, A133, K131, K131";

        StringBuilder outputBuilder = new StringBuilder();
        String[] inputs = input.split(", ");
        for (String inputStr : inputs) {
            String soundexCode = soundex.encode(inputStr);
            outputBuilder.append(soundexCode).append(" ");
        }

        String output = outputBuilder.toString().trim();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testDifferentLettersWithSameSoundexCodeSoundexInstance() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple, April, Orange, Umbrella";
        String expectedOutput = "A133, A133, O351, U165";

        StringBuilder outputBuilder = new StringBuilder();
        String[] inputs = input.split(", ");
        for (String inputStr : inputs) {
            String soundexCode = soundex.encode(inputStr);
            outputBuilder.append(soundexCode).append(" ");
        }

        String output = outputBuilder.toString().trim();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUpperCaseLetterSoundexInstance() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple, ApplE, Applebee, Applejack";
        String expectedOutput = "A133, A133, K131, K131";

        StringBuilder outputBuilder = new StringBuilder();
        String[] inputs = input.split(", ");
        for (String inputStr : inputs) {
            String soundexCode = soundex.encode(inputStr);
            outputBuilder.append(soundexCode).append(" ");
        }

        String output = outputBuilder.toString().trim();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testLowerCaseLetterSoundexInstance() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple, apple, Applebee, applejack";
        String expectedOutput = "A133, A133, K131, K131";

        StringBuilder outputBuilder = new StringBuilder();
        String[] inputs = input.split(", ");
        for (String inputStr : inputs) {
            String soundexCode = soundex.encode(inputStr);
            outputBuilder.append(soundexCode).append(" ");
        }

        String output = outputBuilder.toString().trim();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testMultibyteCharacterSoundexInstance() {
        Soundex soundex = new Soundex(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'Å', 'Ä', 'Ö', 'Ä', 'Ö', 'Å', 'Ä', 'Ö', 'Å', 'Ä', 'Ö', 'Å', 'Ä', 'Ö', 'Å', 'Ä', 'Ö', 'Å', 'Ä', 'Ö', 'Å', 'Ä', 'Ö', 'Å', 'Ä', 'Ö', 'Å', 'Ä', 'Ö', 'Å'});
        String input = "Apple, Äpple, Applebee, Äppeljack";
        String expectedOutput = "A133, A133, K131, K131";

        StringBuilder outputBuilder = new StringBuilder();
        String[] inputs = input.split(", ");
        for (String inputStr : inputs) {
            String soundexCode = soundex.encode(inputStr);
            outputBuilder.append(soundexCode).append(" ");
        }

        String output = outputBuilder.toString().trim();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testRandomStringsSoundexInstance() {
        Soundex soundex = Soundex.US_ENGLISH;
        StringBuilder inputBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            char randomChar;
            String inputStr = "";
            for (int j = 0; j < new Random().nextInt(20) + 1; j++) {
                randomChar = (char) (random.nextInt(26) + 'A');
                inputStr += randomChar;
            }
            inputBuilder.append(inputStr).append(", ");
        }
        String input = inputBuilder.toString().trim();

        StringBuilder outputBuilder = new StringBuilder();
        String[] inputs = input.split(", ");
        for (String inputStr : inputs) {
            String soundexCode = soundex.encode(inputStr);
            outputBuilder.append(soundexCode).append(" ");
        }

        String output = outputBuilder.toString().trim();
        System.out.println("Input: " + input);
        System.out.println("Output: " + output);
        assertTrue(output.length() > 0);
    }
}