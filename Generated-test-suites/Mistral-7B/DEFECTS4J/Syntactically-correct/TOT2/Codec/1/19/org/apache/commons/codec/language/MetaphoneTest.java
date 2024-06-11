import org.junit.Test;
import org.apache.commons.codec.language.Metaphone;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        String[] testCases = {"apple", "appel", "application", "banana", "Barack Obama", "Bob", "cat", "dog", "elephant", "", null, "äpfel", "Apple!", "antidisestablishmentarianism", "   apple", "apple."};
        String[] expectedCodes = {"A000", "A000", "A042", "A101", "B36361", "B251", "K100", "K100", "E101", "", "", "AE00", "A000", "A00000A000A000", "A000", "A000"};
        String[] actualCodes = new String[testCases.length];

        for (int i = 0; i < testCases.length; i++) {
            String input = testCases[i];
            String output = metaphone.metaphone(input);
            actualCodes[i] = output;
            assertEquals(expectedCodes[i], output);
        }

        // test exception handling
        try {
            Metaphone metaphoneNull = null;
            metaphoneNull.metaphone("");
            fail("Expected EncoderException when null object is passed");
        } catch (EncoderException e) {
            // expected
        }

        try {
            Metaphone metaphoneEmpty = new Metaphone();
            metaphoneEmpty.metaphone(null);
            fail("Expected EncoderException when null string is passed");
        } catch (EncoderException e) {
            // expected
        }
    }

    @Test
    public void testEmptyString() {
        Metaphone metaphone = new Metaphone();
        String input = "";
        String expectedOutput = "";
        String actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testNullInput() {
        Metaphone metaphone = new Metaphone();
        String input = null;
        String expectedOutput = "";
        String actualOutput = "";
        try {
            actualOutput = metaphone.metaphone(input);
        } catch (EncoderException e) {
            actualOutput = e.getMessage();
        }
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testNonASCII() {
        Metaphone metaphone = new Metaphone();
        String input = "äpfel";
        String expectedOutput = "AE00";
        String actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSpecialCharacters() {
        Metaphone metaphone = new Metaphone();
        String input = "Apple!";
        String expectedOutput = "A000";
        String actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLongWords() {
        Metaphone metaphone = new Metaphone();
        String input = "antidisestablishmentarianism";
        String expectedOutput = "A00000A000A000";
        String actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLeadingSpaces() {
        Metaphone metaphone = new Metaphone();
        String input = "   apple";
        String expectedOutput = "A000";
        String actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTrailingPunctuation() {
        Metaphone metaphone = new Metaphone();
        String input = "apple.";
        String expectedOutput = "A000";
        String actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }
}