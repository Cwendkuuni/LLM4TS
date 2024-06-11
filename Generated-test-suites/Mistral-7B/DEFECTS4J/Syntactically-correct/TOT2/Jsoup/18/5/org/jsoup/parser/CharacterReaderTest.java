import org.junit.Test;
import org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.isEmpty());
        assertEquals(input, reader.toString());
    }

    // Test cases for other methods here...
}