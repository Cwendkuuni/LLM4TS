import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
        assertThrows(IllegalArgumentException.class, () -> reader.consume());
        assertThrows(IllegalStateException.class, () -> reader.consumeAsString());
        assertThrows(IndexOutOfBoundsException.class, () -> reader.consumeTo('a'));
        assertThrows(IllegalArgumentException.class, () -> reader.consumeTo("ab"));
        assertThrows(IllegalArgumentException.class, () -> reader.consumeToAny('b'));
        assertThrows(IllegalArgumentException.class, () -> reader.consumeToEnd());
        assertThrows(IllegalArgumentException.class, () -> reader.consumeLetterSequence());
        assertThrows(IllegalArgumentException.class, () -> reader.consumeHexSequence());
        assertThrows(IllegalArgumentException.class, () -> reader.consumeDigitSequence());
        assertThrows(IllegalArgumentException.class, () -> reader.matches('a'));
        assertThrows(IllegalArgumentException.class, () -> reader.matches("ab"));
        assertThrows(IllegalArgumentException.class, () -> reader.matchesAny('b'));
        assertThrows(IllegalArgumentException.class, () -> reader.matchesLetter());
        assertThrows(IllegalArgumentException.class, () -> reader.matchesDigit());
        assertThrows(IllegalArgumentException.class, () -> reader.matchConsume("ab"));
        assertThrows(IllegalArgumentException.class, () -> reader.matchConsumeIgnoreCase("ab"));
        assertThrows(IllegalArgumentException.class, () -> reader.containsIgnoreCase("ab"));
        assertEquals("", reader.toString());
    }

    @Test
    public void testSingleCharacterReader() {
        CharacterReader reader = new CharacterReader("A");
        assertFalse(reader.isEmpty());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals("A", reader.consumeAsString());
        assertEquals("A", reader.consumeTo('A'));
        assertEquals("A", reader.consumeTo("A"));
        assertEquals("A", reader.consumeToAny('A'));
        assertEquals("A", reader.consumeToEnd());
        assertEquals("A", reader.consumeLetterSequence());
        assertEquals("A", reader.consumeHexSequence());
        assertEquals("A", reader.consumeDigitSequence());
        assertTrue(reader.matches('A'));
        assertTrue(reader.matches("A"));
        assertTrue(reader.matchesAny('A'));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchConsume("A"));
        assertTrue(reader.matchConsumeIgnoreCase("A"));
        assertFalse(reader.containsIgnoreCase("ab"));
        assertEquals("", reader.toString());
    }

    @Test
    public void testMultipleCharacterReader() {
        CharacterReader reader = new CharacterReader("abc123");
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals("a", reader.consumeAsString());
        assertEquals("a", reader.consumeTo('a'));
        assertEquals("a", reader.consumeTo("a"));
        assertEquals("a", reader.consumeToAny('a'));
        assertEquals("bc", reader.consumeToEnd());
        assertEquals("a", reader.consumeLetterSequence());
        assertEquals("12", reader.consumeHexSequence());
        assertEquals("123", reader.consumeDigitSequence());
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("a"));
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchConsume("a"));
        assertTrue(reader.matchConsumeIgnoreCase("a"));
        assertTrue(reader.containsIgnoreCase("abc"));
        assertEquals("bc", reader.toString());
    }

    @Test
    public void testErrorScenarioReader() {
        CharacterReader reader = new CharacterReader("abcd<e>ef");
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals("a", reader.consumeAsString());
        assertEquals("a", reader.consumeTo('a'));
        assertEquals("a", reader.consumeTo("a"));
        assertEquals("a", reader.consumeToAny('a'));
        assertEquals("bcd<e>ef", reader.consumeToEnd());
        assertEquals("a", reader.consumeLetterSequence());
        assertEquals("", reader.consumeHexSequence());
        assertEquals("ef", reader.consumeDigitSequence());
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("a"));
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchConsume("e"));
        assertFalse(reader.matchConsumeIgnoreCase("e"));
        assertTrue(reader.containsIgnoreCase("abcd<e>ef"));
        assertEquals("bcd<e>ef", reader.toString());
    }

}