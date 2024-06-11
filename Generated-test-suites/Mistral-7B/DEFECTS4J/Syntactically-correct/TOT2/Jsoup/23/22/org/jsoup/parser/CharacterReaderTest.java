import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.input);
        assertEquals(0, reader.pos);
        assertEquals(false, reader.isEmpty());
        assertEquals('H', reader.current());
        assertEquals('H', reader.consume());
        assertEquals(1, reader.pos);
        assertEquals('e', reader.current());
        // ... test other basic methods
    }

    // ... test cases for other methods
}