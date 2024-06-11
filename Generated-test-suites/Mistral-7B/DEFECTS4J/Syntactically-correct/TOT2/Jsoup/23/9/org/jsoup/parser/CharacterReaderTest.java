import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(0, reader.pos());
    }
}