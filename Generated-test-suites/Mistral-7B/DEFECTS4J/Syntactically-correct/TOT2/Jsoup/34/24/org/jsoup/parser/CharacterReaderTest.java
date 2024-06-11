import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.isEmpty());
    }
}