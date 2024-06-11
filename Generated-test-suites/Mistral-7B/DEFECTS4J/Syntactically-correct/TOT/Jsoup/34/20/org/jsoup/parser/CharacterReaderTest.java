import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());
    }

    @Test
    public void testBasicConsumption() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('b', reader.consume());
        // Consume past the end of the input
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertEquals(EOF, reader.consume());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abab");
        reader.mark();
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('a', reader.consume());
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeSubstring() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeTo('c');
        Assert.assertEquals("ab", consumed);
        Assert.assertEquals('d', reader.current());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConsumeBeyondEnd() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consumeTo('d');
    }

    @Test
    public void testNextIndexOfChar() {
        CharacterReader reader = new CharacterReader("abcdefabc");
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfString() {
        CharacterReader reader = new CharacterReader("ababcdabcdabcd");
        Assert.assertEquals(1, reader.nextIndexOf("ab"));
        Assert.assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("ab123def456");
        String consumed = reader.consumeToAny('1', '2');
        Assert.assertEquals("b12", consumed);
        Assert.assertEquals('f', reader.current());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("cd"));
        Assert.assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbCdEfGhIj");
        Assert.assertTrue(reader.matchesIgnoreCase("ab"));
        Assert.assertTrue(reader.matchesIgnoreCase("ef"));
        Assert.assertFalse(reader.matchesIgnoreCase("xy"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c'));
        Assert.assertFalse(reader.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        Assert.assertTrue(reader.containsIgnoreCase("ab"));
        Assert.assertTrue(reader.containsIgnoreCase("cd"));
        Assert.assertFalse(reader.containsIgnoreCase("xy"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        new CharacterReader(null);
    }
}