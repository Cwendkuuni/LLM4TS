import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));
        assertEquals("Test string", reader.toString());
    }

    @Test
    public void testCharacterReaderPos() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));
        assertEquals(0, reader.pos());

        reader.advance();
        reader.advance();
        reader.advance();

        assertEquals(3, reader.pos());
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));
        Assert.assertFalse(reader.isEmpty());

        reader.bufferUp();
        reader.unconsume();
        reader.unconsume();
        reader.unconsume();

        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));

        assertEquals('T', reader.current());
        reader.consume();
        assertEquals('e', reader.current());

        reader.unconsume();
        assertEquals('T', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string")) {
            @Override
            public void bufferUp() {
                throw new IOException("Test IOException");
            }
        };

        reader.consume();
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));

        String result = reader.consumeTo('s');
        assertEquals("Test ", result);
    }

    @Test
    public void testCharacterReaderEmptyConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader(""));

        String result = reader.consumeTo('s');
        assertEquals("", result);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));

        String result = reader.consumeToEnd();
        assertEquals("Test string", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("TestLetterSequence"));

        String result = reader.consumeLetterSequence();
        assertEquals("TestLetterSequence", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("TestLetterThenDigitSequence123"));

        String result = reader.consumeLetterThenDigitSequence();
        assertEquals("TestLetterThenDigitSequence", result);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test#HexSequence%"));

        String result = reader.consumeHexSequence();
        assertEquals("#HexSequence%", result);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test123DigitSequence"));

        String result = reader.consumeDigitSequence();
        assertEquals("Test123DigitSequence", result);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("TestString"));

        assertTrue(reader.matches('T'));
        assertTrue(reader.matches("est"));
        assertFalse(reader.matches('@'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("TestString"));

        assertTrue(reader.matchesIgnoreCase("test"));
        assertFalse(reader.matchesIgnoreCase("@"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("TestString"));

        assertTrue(reader.matchesAny('T'));
        assertTrue(reader.matchesAny('e'));
        assertTrue(reader.matchesAny('s'));
        assertFalse(reader.matchesAny('@'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("TestString"));

        assertTrue(reader.matchesAnySorted(new char[]{'T', 'e', 's'}));
        assertFalse(reader.matchesAnySorted(new char[]{'A', 'B', 'C'}));
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("TestString"));

        assertEquals(4, reader.nextIndexOf('t'));
        assertEquals(-1, reader.nextIndexOf('Z'));
    }

    @Test
    public void testCharacterReaderNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("TestStringTestString"));

        assertEquals(4, reader.nextIndexOf("Test"));
        assertEquals(11, reader.nextIndexOf("Test"));
        assertEquals(-1, reader.nextIndexOf("TestTest"));
    }
}