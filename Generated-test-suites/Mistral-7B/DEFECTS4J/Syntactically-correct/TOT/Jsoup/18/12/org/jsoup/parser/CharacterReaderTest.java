import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.current());
    }

    @Test
    public void testSimpleConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.consume());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('d', reader.current());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        Assert.assertEquals('a', reader.current());
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumePastEnd() {
        CharacterReader reader = new CharacterReader("abc");
        while (reader.hasNext()) {
            reader.consume();
        }
        reader.consume();
    }

    @Test
    public void testConsumeSubstring() {
        CharacterReader reader = new CharacterReader("123456");
        String substring = reader.consumeTo("3");
        Assert.assertEquals("12", substring);
    }

    @Test
    public void testConsumeString() {
        CharacterReader reader = new CharacterReader("123456");
        String substring = reader.consumeTo("five"); // should not match
        Assert.assertEquals("12345", substring);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdABCefg123");
        String sequence = reader.consumeLetterSequence();
        Assert.assertEquals("abc", sequence);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("1a2b3c4dEfF0G");
        String sequence = reader.consumeHexSequence();
        Assert.assertEquals("1a2b3c4d", sequence);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        String sequence = reader.consumeDigitSequence();
        Assert.assertEquals("1234567890", sequence);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abCDEF");
        Assert.assertTrue(reader.matchesIgnoreCase("aB"));
        Assert.assertFalse(reader.matchesIgnoreCase("z"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc123");
        Assert.assertTrue(reader.matchesAny('a', '1'));
        Assert.assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head>");
        Assert.assertTrue(reader.containsIgnoreCase("title"));
        Assert.assertTrue(reader.containsIgnoreCase("HEAD"));
        Assert.assertFalse(reader.containsIgnoreCase("FOOT"));
    }
}