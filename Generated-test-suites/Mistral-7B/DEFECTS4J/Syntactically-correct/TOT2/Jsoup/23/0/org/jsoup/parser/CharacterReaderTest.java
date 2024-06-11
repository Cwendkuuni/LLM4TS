import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderInitialization() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.input);
    }

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testNonEmptyReader() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertFalse(reader.isEmpty());
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.consume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsumeSubstring() {
        CharacterReader reader = new CharacterReader("Hello World");
        String consumed = reader.consumeTo('l');
        assertEquals("ello", consumed);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        String consumed = reader.consumeToEnd();
        assertEquals("World", consumed);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcde");
        assertTrue(reader.matches('e'));
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader reader = new CharacterReader("abcde");
        String sequence = "bc";
        assertTrue(reader.matches(sequence));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcde");
        String sequence = "Bc";
        assertTrue(reader.matchesIgnoreCase(sequence));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcde");
        char[] sequence = {'a', 'c'};
        assertTrue(reader.matchesAny(sequence));
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcde");
        String sequence = "ab";
        assertEquals("ab", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x1234");
        String sequence = "12";
        assertEquals("12", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234");
        String sequence = "12";
        assertEquals("12", reader.consumeDigitSequence());
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("World", reader.toString());
    }

}