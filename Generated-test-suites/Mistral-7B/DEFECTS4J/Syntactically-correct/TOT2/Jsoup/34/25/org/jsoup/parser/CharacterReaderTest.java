import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testEmpty() {
        CharacterReader reader1 = new CharacterReader("test");
        assertTrue(reader1.isEmpty());

        CharacterReader reader2 = new CharacterReader("");
        assertTrue(reader2.isEmpty());

        CharacterReader reader3 = new CharacterReader("Characters");
        assertFalse(reader3.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertEquals(0, reader.pos());
    }

    // Test cases for current(), consume(), unconsume(), mark(), and rewindToMark() methods go here

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Hello World!");
        String consumed = reader.consumeAsString();
        assertEquals("Hello", consumed);
    }

    // Test cases for nextIndexOf(char), nextIndexOf(CharSequence), consumeTo(char), consumeTo(String), consumeToAny(char[]), consumeToEnd(), consumeLetterSequence(), consumeLetterThenDigitSequence(), consumeHexSequence(), and consumeDigitSequence() methods go here

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertTrue(reader.matches('H'));
        assertTrue(reader.matches("Hello"));
        assertFalse(reader.matches('Z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertTrue(reader.matchesIgnoreCase("Hello"));
        assertFalse(reader.matchesIgnoreCase("World"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertTrue(reader.matchesAny('H', 'o'));
        assertFalse(reader.matchesAny('Z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("1234abcdABCD");
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertTrue(reader.matchConsume("ll"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertTrue(reader.matchConsumeIgnoreCase("HeLlO"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head></html>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("HEAD"));
        assertFalse(reader.containsIgnoreCase("footer"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        new CharacterReader(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        new CharacterReader(123);
    }

    @Test
    public void testIndexOutOfBounds() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        reader.pos = reader.length;
        reader.unconsume();
        reader.pos++;
        reader.consume();

        int index = reader.nextIndexOf('F');
        assertEquals(-1, index);

        // Test for reading past the end of the input
        reader.pos++;
        index = reader.nextIndexOf('F');
        assertEquals(-1, index);
    }
}