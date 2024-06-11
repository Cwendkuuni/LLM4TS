
import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        String input = "This is a test string";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input, reader.input);
        assertEquals(input.length(), reader.length);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.isEmpty());
        reader.advance();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals(0, reader.pos());
        reader.advance();
        reader.advance();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testCurrentAndConsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matches('l'));
        assertTrue(reader.matches("ld"));
        assertFalse(reader.matches('x'));
    }

    // Test edge cases and exception handling scenarios here...

}
