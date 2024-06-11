import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testSingleCharacterReader() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals("a", reader.consumeAsString());
        assertEquals(-1, reader.nextIndexOf('b'));
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testMultipleCharactersReader() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals("a", reader.consumeAsString());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals("ab", reader.consumeTo('c'));
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals(3, reader.pos());
        assertEquals("abc", reader.consumeTo("def"));
        assertEquals("def", reader.consumeToEnd());
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

    @Test
    public void testScanSpecificChar() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals(2, reader.nextIndexOf('c'));
    }

    @Test
    public void testScanSequence() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals(2, reader.nextIndexOf("bc"));
    }

    @Test
    public void testScanSequenceWithMark() {
        CharacterReader reader = new CharacterReader("abcabc");
        reader.mark();
        reader.advance();
        reader.advance();
        assertEquals(3, reader.nextIndexOf("bc", reader.mark()));
    }

    @Test
    public void testScanHexSequence() {
        CharacterReader reader = new CharacterReader("%#65%6E%63%6F%64%65%64%21");
        assertEquals(0, reader.nextIndexOf("%"));
        assertEquals(5, reader.nextIndexOf("65"));
        assertEquals('A', reader.consumeHexSequence().charAt(0));
        assertEquals('E', reader.consumeHexSequence().charAt(1));
        assertEquals(' ', reader.consume().charValue());
        assertEquals('!', reader.consume().charValue());
    }

    @Test
    public void testScanDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567");
        assertEquals("123456", reader.consumeDigitSequence());
    }

    @Test
    public void testScanLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd1efg");
        assertEquals("abcde", reader.consumeLetterSequence());
    }

    @Test
    public void testScanLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abcd1efg");
        assertEquals("abcd1", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("123abcd123");
        assertTrue(reader.matches('1'));
        assertTrue(reader.matches("ab"));
        assertTrue(reader.matches('d'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("123ABCD123");
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertFalse(reader.matchesIgnoreCase("De"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("123abcd123");
        assertTrue(reader.matchesAny('1', 'a', 'd'));
        assertFalse(reader.matchesAny('e'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("123abcd123");
        assertTrue(reader.matchConsume("ab"));
        assertEquals(2, reader.pos());
        assertFalse(reader.matchConsume("xy"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("123ABCD123");
        assertTrue(reader.matchConsumeIgnoreCase("Ab"));
        assertEquals(2, reader.pos());
        assertFalse(reader.matchConsumeIgnoreCase("xy"));
    }
}