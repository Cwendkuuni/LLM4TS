import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import java.io.StringReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithInvalidReader() {
        try {
            CharacterReader characterReader = new CharacterReader(null);
        } catch (NullPointerException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderNotEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals('H', characterReader.current());
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testCharacterReaderCurrentAfterEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        characterReader.consumeToEnd();
        characterReader.current();
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals('H', characterReader.consume());
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testCharacterReaderUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testCharacterReaderAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testCharacterReaderMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOfSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals(0, characterReader.nextIndexOf('H'));
    }

    @Test
    public void testCharacterReaderNextIndexOfMultiChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals(4, characterReader.nextIndexOf("ll"));
    }

    @Test
    public void testCharacterReaderNextIndexOfEmptyString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals(-1, characterReader.nextIndexOf(""));
    }

    @Test
    public void testCharacterReaderConsumeToChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello", characterReader.consumeTo('l'));
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello", characterReader.consumeTo("ll"));
    }

    @Test
    public void testCharacterReaderConsumeToAnySingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("World", characterReader.consumeToAny(','));
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("World", characterReader.consumeToAnySorted(new char[]{'l', 'o'}));
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&lt;script&gt;alert('Hello World')&lt;/script&gt;"));
        assertEquals("<script>", characterReader.consumeData());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Hello World</title></head><body>"));
        assertEquals("html", characterReader.consumeTagName());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Hello World</title></head><body>"));
        assertEquals("<head><title>Hello World</title></head><body>", characterReader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd"));
        assertEquals("abcd", characterReader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ab12cd"));
        assertEquals("ab12cd", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&#x1234;"));
        assertEquals("&#x1234;", characterReader.consumeHexSequence());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("1234"));
        assertEquals("1234", characterReader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReaderMatchesSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdef"));
        assertTrue(characterReader.matches('a'));
    }

    @Test
    public void testCharacterReaderMatchesString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdef"));
        assertTrue(characterReader.matches("ab"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdef"));
        assertTrue(characterReader.matchesIgnoreCase("Ab"));
    }

    @Test
    public void testCharacterReaderMatchesAnySingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdef"));
        assertTrue(characterReader.matchesAny('\n'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdef"));
        assertTrue(characterReader.matchesAnySorted(new char[]{'b', 'c'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdef"));
        assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("12345"));
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsumeSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdef"));
        characterReader.matchConsume('a');
        assertEquals("bcdef", characterReader.toString());
    }

    @Test
    public void testCharacterReaderMatchConsumeString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdabcd"));
        characterReader.matchConsume("ab");
        assertEquals("cdcd", characterReader.toString());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Hello World</title></head><body>"));
        assertTrue(characterReader.containsIgnoreCase("/title"));
        assertTrue(characterReader.containsIgnoreCase("/TITLE"));
    }

}