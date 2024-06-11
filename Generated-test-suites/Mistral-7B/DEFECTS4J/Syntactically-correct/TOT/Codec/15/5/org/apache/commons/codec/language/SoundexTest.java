import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for Soundex implementation
 */
public class SoundexTest {

    /**
     * Test default US English encoding
     */
    @Test
    public void testDefaultEncoding() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple, Banana, Cat, Dog, Elephant";
        String expected = "A123, A123, K160, D222, E130";
        String output = "";

        for (String word : input.split(", ")) {
            try {
                output += soundex.encode(word) + ", ";
            } catch (EncoderException e) {
                //Should not happen since input is valid
            }
        }

        assertEquals(expected, output.substring(0, output.length() - 2));
    }

    /**
     * Test custom encoding with international mapping
     */
    @Test
    public void testCustomEncoding() {
        String mapping = "123012101123010201010202";
        Soundex soundex = new Soundex(mapping.toCharArray());
        String input = "Apple, Banana, Katze, Hund, Elefant";
        String expected = "A123, A123, K160, H120, E130";
        String output = "";

        for (String word : input.split(", ")) {
            try {
                output += soundex.encode(word) + ", ";
            } catch (EncoderException e) {
                //Should not happen since input is valid
            }
        }

        assertEquals(expected, output.substring(0, output.length() - 2));
    }

    /**
     * Test encoding of empty and null strings
     */
    @Test
    public void testEmptyAndNullStrings() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "";
        String expected = "";
        String output = soundex.encode(input);
        assertEquals(expected, output);

        input = null;
        try {
            output = soundex.encode(input);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            //Expected
        }
    }

    /**
     * Test encoding of non-alphabetic characters
     */
    @Test
    public void testNonAlphabeticCharacters() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple%^&, Banana, Cat#$%, Dog*^%&, Elephant!@#$%";
        String expected = "A123, A123, K160, D222, E130";
        String output = "";

        for (String word : input.split(", ")) {
            try {
                output += soundex.encode(word) + ", ";
            } catch (EncoderException e) {
                //Should not happen since input is valid
            }
        }

        assertEquals(expected, output.substring(0, output.length() - 2));
    }

    /**
     * Test encoding of multi-byte characters
     */
    @Test
    public void testMultibyteCharacters() {
        String mapping = "123012101123010201010202";
        Soundex soundex = new Soundex(mapping.toCharArray());
        String input = "Apple, Banane, Katze, Hund, Elefant";
        String expected = "A123, A123, K160, H120, E130";
        String output = "";

        for (String word : input.split(", ")) {
            try {
                output += soundex.encode(word) + ", ";
            } catch (EncoderException e) {
                //Should not happen since input is valid
            }
        }

        assertEquals(expected, output.substring(0, output.length() - 2));
    }

    /**
     * Test difference method
     */
    @Test
    public void testDifference() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input1 = "Apple, Banana, Cat, Dog, Elephant";
        String input2 = "Apple, Banana, Cat, Dog, Elephant";
        int expected = 4;
        int actual;

        try {
            actual = soundex.difference(input1, input2);
        } catch (EncoderException e) {
            fail("Expected no exception when comparing identical strings");
        }

        assertEquals(expected, actual);

        input1 = "Apple, Banana, Cat, Dog, Elephant";
        input2 = "Apple, Banana, Dog, Dog, Elephant";
        expected = 3;

        try {
            actual = soundex.difference(input1, input2);
        } catch (EncoderException e) {
            fail("Expected no exception when comparing identical strings");
        }

        assertEquals(expected, actual);

        input1 = "Apple, Banana, Cat, Dog, Elephant";
        input2 = "Apple, Banana, Cat, Dog, Monkey";
        expected = 1;

        try {
            actual = soundex.difference(input1, input2);
        } catch (EncoderException e) {
            fail("Expected no exception when comparing valid strings");
        }

        assertEquals(expected, actual);

        input1 = "Apple, Banana, Cat, Dog, Elephant";
        input2 = null;
        try {
            actual = soundex.difference(input1, input2);
            fail("Expected EncoderException when comparing a string with null");
        } catch (EncoderException e) {
            //Expected
        }
    }

    /**
     * Test difference method with invalid input
     */
    @Test(expected = EncoderException.class)
    public void testDifferenceInvalidInput() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input1 = null;
        String input2 = "Apple, Banana, Cat, Dog, Elephant";
        soundex.difference(input1, input2);
    }

    /**
     * Test difference method with empty input
     */
    @Test
    public void testDifferenceEmptyInput() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input1 = "";
        String input2 = "Apple, Banana, Cat, Dog, Elephant";
        int expected = 0;
        int actual;

        try {
            actual = soundex.difference(input1, input2);
        } catch (EncoderException e) {
            fail("Expected no exception when comparing empty string with valid string");
        }

        assertEquals(expected, actual);
    }
}