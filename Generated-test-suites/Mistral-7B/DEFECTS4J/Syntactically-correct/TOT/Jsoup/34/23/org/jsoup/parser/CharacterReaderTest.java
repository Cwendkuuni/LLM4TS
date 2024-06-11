import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testSingleCharacterReader() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals("a", reader.consumeAsString());
        assertEquals('a', reader.current());
        assertEquals(1, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeSequenceOfCharacters() {
        CharacterReader reader = new CharacterReader("abcde");
        assertEquals("abcde", reader.consumeToEnd());
        assertEquals('e', reader.current());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals(5, reader.pos());
    }

    @Test
    public void testConsumeSingleChar() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals("a", reader.consumeTo(CharacterReader.EOF));
        assertEquals(CharacterReader.EOF, reader.current());
        assertEquals(1, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeString() {
        CharacterReader reader = new CharacterReader("123abcdefghijklmnopqrstuvwxyz");
        assertEquals("defghijklmnopqrstuvwxyz", reader.consumeTo("abc"));
        assertEquals('f', reader.current());
        assertEquals(13, reader.pos());
    }

    @Test
    public void testConsumeSequenceOfDifferentStrings() {
        CharacterReader reader = new CharacterReader("123abcdefghijklmnopqrstuvwxyz");
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader.consumeToAny('w', 'x', 'y'));
        assertEquals('w', reader.current());
        assertEquals(23, reader.pos());
    }

    @Test
    public void testConsumeEndOfString() {
        CharacterReader reader = new CharacterReader("123abcdefghijklmnopqrstuvwxyz");
        assertEquals("123abcdefghijklmnopqrstuvwxyz", reader.consumeToEnd());
        assertEquals(CharacterReader.EOF, reader.current());
        assertEquals(31, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeLetterSequence());
        assertEquals('a', reader.current());
        assertEquals(2, reader.pos());
        assertFalse(reader.matches('b'));
    }

    @Test
    public void testConsumeLetterThenDigit() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("a1", reader.consumeLetterThenDigitSequence());
        assertEquals('1', reader.current());
        assertEquals(3, reader.pos());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x12345");
        assertEquals("1234", reader.consumeHexSequence());
        assertEquals('4', reader.current());
        assertEquals(7, reader.pos());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123456");
        assertEquals("56", reader.consumeDigitSequence());
        assertEquals('6', reader.current());
        assertEquals(8, reader.pos());
    }

    @Test
    public void testMatching() {
        CharacterReader reader = new CharacterReader("abc123efg");
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches("fg"));
        assertFalse(reader.matches('0'));
        assertFalse(reader.matches("ab"));
        assertFalse(reader.matchesIgnoreCase("AB"));
        assertFalse(reader.matchesAny('d', 'e'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        new CharacterReader(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConsumePastEnd() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consumeToEnd();
        reader.consume();
    }
}