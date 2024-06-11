import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        Assertions.assertTrue(reader.isEmpty());
        Assertions.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testReaderWithSingleCharacter() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        Assertions.assertFalse(reader.isEmpty());
        Assertions.assertEquals('a', reader.current());
    }

    @Test
    public void testReaderWithMultipleCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        String consumed = reader.consumeTo('b');
        Assertions.assertEquals("a", consumed);
        Assertions.assertEquals('b', reader.current());
    }

    @Test
    public void testReaderWithSequenceOfCharacters() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeTo("def");
        Assertions.assertEquals("abc", consumed);
        Assertions.assertEquals('d', reader.current());
    }

    @Test
    public void testReaderWithEOF() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consumeTo('z');
        reader.consumeToEnd();
        Assertions.assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderWithNegativeIndex() {
        CharacterReader reader = new CharacterReader("abc");
        Assertions.assertThrows(IllegalArgumentException.class, () -> reader.nextIndexOf(-1));
    }

    @Test
    public void testReaderWithEmptySequence() {
        CharacterReader reader = new CharacterReader("abc");
        Assertions.assertEquals(-1, reader.nextIndexOf(""));
    }

    @Test
    public void testReaderWithNullSequence() {
        CharacterReader reader = new CharacterReader("abc");
        Assertions.assertThrows(NullPointerException.class, () -> reader.nextIndexOf(null));
    }

    @Test
    public void testReaderWithConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijk");
        String consumed = reader.consumeLetterSequence();
        Assertions.assertEquals("abc", consumed);
    }

    @Test
    public void testReaderWithConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        String consumed = reader.consumeLetterThenDigitSequence();
        Assertions.assertEquals("abc1", consumed);
    }

    @Test
    public void testReaderWithConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0xABCDEF");
        String consumed = reader.consumeHexSequence();
        Assertions.assertEquals("ABCDEF", consumed);
    }

    @Test
    public void testReaderWithConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        String consumed = reader.consumeDigitSequence();
        Assertions.assertEquals("123456", consumed);
    }

    @Test
    public void testReaderWithMatches() {
        CharacterReader reader = new CharacterReader("abcdefghijk");
        Assertions.assertTrue(reader.matches('e'));
        Assertions.assertTrue(reader.matches("ghi"));
    }

    @Test
    public void testReaderWithMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEFGHIJK");
        Assertions.assertTrue(reader.matchesIgnoreCase("ghi"));
    }

    @Test
    public void testReaderWithMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdefghijk");
        Assertions.assertTrue(reader.matchesAny('a', 'e', 'i'));
    }

    @Test
    public void testReaderWithMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdefghijk");
        String consumed = reader.matchConsume("def");
        Assertions.assertEquals("def", consumed);
    }

    @Test
    public void testReaderWithMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEFGHIJK");
        String consumed = reader.matchConsumeIgnoreCase("GHI");
        Assertions.assertEquals("GHI", consumed);
    }

    @Test
    public void testReaderWithContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("</title><script><style>");
        Assertions.assertTrue(reader.containsIgnoreCase("/title"));
        Assertions.assertTrue(reader.containsIgnoreCase("<script"));
        Assertions.assertTrue(reader.containsIgnoreCase("<style"));
    }
}