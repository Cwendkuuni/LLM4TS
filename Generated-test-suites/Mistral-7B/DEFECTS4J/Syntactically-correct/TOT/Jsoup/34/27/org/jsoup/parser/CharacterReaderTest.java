import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.nextIndexOf('a'));
        Assert.assertEquals(-1, reader.nextIndexOf("ab"));
    }

    @Test
    public void testSingleCharacterReader() {
        CharacterReader reader = new CharacterReader("a");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals(-1, reader.nextIndexOf('b'));
        Assert.assertEquals("a", reader.consumeAsString());
    }

    @Test
    public void testScanForCharacter() {
        CharacterReader reader = new CharacterReader("hello world");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('h', reader.current());
        Assert.assertEquals('h', reader.consume());
        Assert.assertEquals('e', reader.current());
        Assert.assertEquals(2, reader.pos());
        Assert.assertEquals(1, reader.nextIndexOf('l'));
        Assert.assertEquals("hello", reader.consumeTo('l'));
    }

    @Test(expected = RuntimeException.class)
    public void testScanBeyondEndOfString() {
        CharacterReader reader = new CharacterReader("hello");
        reader.consumeTo('z');
    }

    @Test
    public void testScanString() {
        CharacterReader reader = new CharacterReader("hello world");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('h', reader.current());
        Assert.assertEquals("hello", reader.consumeTo("llo"));
        Assert.assertEquals(6, reader.pos());
        Assert.assertEquals("world", reader.consumeToEnd());
    }

    @Test
    public void testScanMultipleCharacters() {
        CharacterReader reader = new CharacterReader("hello world");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals("hell", reader.consumeTo('l'));
        Assert.assertEquals("world", reader.consumeToEnd());
    }

    @Test
    public void testScanWhitespace() {
        CharacterReader reader = new CharacterReader(" hello world ");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals(' ', reader.current());
        reader.consume();
        Assert.assertEquals('h', reader.current());
        Assert.assertEquals("hello", reader.consumeTo(' '));
        Assert.assertEquals("world", reader.consumeToEnd());
    }

    @Test
    public void testScanLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefg");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals("abcdefg", reader.consumeLetterSequence());
    }

    @Test
    public void testScanLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals("abc", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testScanHexSequence() {
        CharacterReader reader = new CharacterReader("0x1a2b3c");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals("0x1a2b3c", reader.consumeHexSequence());
    }

    @Test
    public void testScanDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals("1234567890", reader.consumeDigitSequence());
    }

    @Test
    public void testMatchCharacter() {
        CharacterReader reader = new CharacterReader("hello world");
        Assert.assertTrue(reader.matches('h'));
        Assert.assertTrue(reader.matches("hl"));
        Assert.assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchString() {
        CharacterReader reader = new CharacterReader("hello world");
        Assert.assertTrue(reader.matches("hello"));
        Assert.assertFalse(reader.matches("worldhello"));
    }

    @Test
    public void testMatchIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(reader.matchesIgnoreCase("hello"));
        Assert.assertTrue(reader.matchesIgnoreCase("HELLO"));
        Assert.assertFalse(reader.matchesIgnoreCase("worldhello"));
    }

    @Test
    public void testMatchAny() {
        CharacterReader reader = new CharacterReader("hello world");
        Assert.assertTrue(reader.matchesAny('l', 'o'));
        Assert.assertFalse(reader.matchesAny('z', 'o'));
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('h', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.advance();
        Assert.assertEquals('h', reader.current());
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('h', reader.current());
    }
}