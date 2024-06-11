import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test case for pos() method
    }

    @Test
    public void testPosAtBeginning() {
        // Test case for pos() when at the beginning
    }

    @Test
    public void testPosAtEnd() {
        // Test case for pos() when at the end
    }

    @Test
    public void testPosInMiddle() {
        // Test case for pos() when in the middle
    }

    @Test
    public void testIsEmpty() {
        // Test case for isEmpty() method
    }

    @Test
    public void testCurrent() {
        // Test case for current() method
    }

    @Test
    public void testConsume() {
        // Test case for consume() method
    }

    @Test
    public void testUnconsume() {
        // Test case for unconsume() method
    }

    @Test
    public void testAdvance() {
        // Test case for advance() method
    }

    @Test
    public void testMark() {
        // Test case for mark() method
    }

    @Test
    public void testRewindToMark() {
        // Test case for rewindToMark() method
    }

    @Test
    public void testConsumeAsString() {
        // Test case for consumeAsString() method
    }

    @Test
    public void testConsumeTo() {
        // Test case for consumeTo() method
    }

    @Test
    public void testConsumeToEnd() {
        // Test case for consumeToEnd() method
    }

    @Test
    public void testConsumeLetterSequence() {
        // Test case for consumeLetterSequence() method
    }

    @Test
    public void testConsumeHexSequence() {
        // Test case for consumeHexSequence() method
    }

    @Test
    public void testConsumeDigitSequence() {
        // Test case for consumeDigitSequence() method
    }

    @Test
    public void testMatches() {
        // Test case for matches() method
    }

    @Test
    public void testMatchesIgnoreCase() {
        // Test case for matchesIgnoreCase() method
    }

    @Test
    public void testMatchesAny() {
        // Test case for matchesAny() method
    }

    @Test
    public void testMatchesLetter() {
        // Test case for matchesLetter() method
    }

    @Test
    public void testMatchesDigit() {
        // Test case for matchesDigit() method
    }

    @Test
    public void testMatchConsume() {
        // Test case for matchConsume() method
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        // Test case for matchConsumeIgnoreCase() method
    }

    @Test
    public void testContainsIgnoreCase() {
        // Test case for containsIgnoreCase() method
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInputIsNull() {
        // Test case for input being null
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInput() {
        // Test case for empty input
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInputWithInvalidCharacters() {
        // Test case for input containing invalid characters
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConsumeToEndWithEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        String consumed = reader.consumeToEnd();
        assertEquals("", consumed);

        reader.consumeToEnd();
        reader.consumeToEnd();

        try {
            reader.consumeToEnd();
        } catch (IndexOutOfBoundsException e) {
            // Test passed
            assertEquals("No more input to consume", e.getMessage());
        }
    }
}