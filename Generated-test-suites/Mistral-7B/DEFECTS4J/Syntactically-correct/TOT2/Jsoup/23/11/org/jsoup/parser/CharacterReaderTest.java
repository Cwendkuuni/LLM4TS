import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("", reader.toString());
        assertEquals(0, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals("a", reader.consumeAsString());
        assertEquals('e', reader.current());
        assertEquals("def", reader.consumeToEnd());
        reader.mark();
        reader.rewindToMark();
        assertEquals('a', reader.current());
        assertFalse(reader.isEmpty());
        reader.unconsume();
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
        reader.advance();
        assertEquals('c', reader.current());
        reader.advance();
        assertEquals('d', reader.current());
        reader.advance();
        assertEquals('e', reader.current());
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals("", reader.consumeAsString());
        assertEquals(11, reader.pos());
        assertEquals(true, reader.isEmpty());
    }
}