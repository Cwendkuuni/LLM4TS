import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals("Test String", reader.input);
        assertEquals(14, reader.length);
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals(0, reader.pos());
        reader.advance();
        assertEquals(1, reader.pos());
        reader.mark();
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        reader = new CharacterReader("Test String");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals('T', reader.current());
        reader.advance();
        assertEquals('e', reader.current());
        reader.unconsume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals('T', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals(' ', reader.consume()); // empty character
        assertEquals(EOF, reader.consume()); // end of input
    }

    // Add test cases for remaining methods here

    @Test
    public void testEdgeCasesAndExceptions() {
        CharacterReader reader = new CharacterReader("");
        // Add test cases for edge cases and exceptions here
    }
}