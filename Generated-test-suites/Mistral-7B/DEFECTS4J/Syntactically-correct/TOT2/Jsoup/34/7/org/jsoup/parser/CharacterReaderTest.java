import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    @Test
    public void testIsEmpty() {
        CharacterReader reader = createCharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testIsEmptyAfterReading() {
        CharacterReader reader = createCharacterReader("abc");
        reader.consume(); reader.consume(); reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = createCharacterReader("abc");
        assertEquals(0, reader.pos());
        reader.consume(); reader.consume();
        assertEquals(2, reader.pos());
    }

    // ... (similar test cases for other methods)

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = createCharacterReader("abcdefabc");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(5, reader.nextIndexOf('f'));
        assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testConsume() {
        CharacterReader reader = createCharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(EOF, reader.consume());
    }

    // ... (test cases for other methods)
}