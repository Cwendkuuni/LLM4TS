import org.jsoup.parser.CharacterReader;
import org.jsoup.helper.Validate;
import org.junit.Test;
import java.io.StringReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello World", characterReader.toString());
        Validate.notNull(characterReader.reader);
        characterReader.close();
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("Hello World", characterReader.toString());
        Validate.notNull(characterReader.reader);
        characterReader.close();
    }

    @Test
    public void testBufferUpMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertEquals(Character.SIZE, characterReader.bufLength);
        characterReader.consume();
        characterReader.bufferUp();
        assertEquals(Character.SIZE * 2 + 1, characterReader.bufLength);
        characterReader.close();
    }

    @Test
    public void testPosMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.consume(3);
        assertEquals(5, characterReader.pos());
        characterReader.close();
    }

    @Test
    public void testIsEmptyMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertFalse(characterReader.isEmpty());
        characterReader.consume(6);
        assertTrue(characterReader.isEmpty());
        characterReader.close();
    }

    @Test
    public void testCurrentMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.consume();
        assertEquals('l', characterReader.current());
        characterReader.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeMethodAfterEndOfStream() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.consume(6);
        characterReader.consume(); // should throw an exception as this is past the end of the stream
    }

    @Test
    public void testConsumeToMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeTo('l');
        assertEquals("Hello", consumed);
        characterReader.consume();
        assertEquals("World", characterReader.toString());
        characterReader.close();
    }

    @Test
    public void testConsumeToMethodWithEmptyInput() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        String consumed = characterReader.consumeTo('a');
        assertEquals("", consumed);
        characterReader.close();
    }

    @Test
    public void testConsumeToEndMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeToEnd();
        assertEquals("Hello World", consumed);
        characterReader.close();
    }

    @Test
    public void testConsumeLetterSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String sequence = characterReader.consumeLetterSequence();
        assertEquals("Hello", sequence);
        characterReader.consume();
        sequence = characterReader.consumeLetterSequence();
        assertEquals("World", sequence);
        characterReader.close();
    }

    @Test
    public void testConsumeLetterThenDigitSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello123 World456"));
        String sequence = characterReader.consumeLetterThenDigitSequence();
        assertEquals("Hello123", sequence);
        characterReader.consume(4);
        sequence = characterReader.consumeLetterThenDigitSequence();
        assertEquals("World456", sequence);
        characterReader.close();
    }

    @Test
    public void testConsumeHexSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("0x1234 0x5678"));
        String sequence = characterReader.consumeHexSequence();
        assertEquals("1234", sequence);
        characterReader.consume(6);
        sequence = characterReader.consumeHexSequence();
        assertEquals("5678", sequence);
        characterReader.close();
    }

    @Test
    public void testConsumeDigitSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("12345678 90123456"));
        String sequence = characterReader.consumeDigitSequence();
        assertEquals("123456", sequence);
        characterReader.consume(7);
        sequence = characterReader.consumeDigitSequence();
        assertEquals("901234", sequence);
        characterReader.close();
    }

    @Test
    public void testMatchesMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matches('l'));
        assertTrue(characterReader.matches("ll"));
        characterReader.consume();
        assertFalse(characterReader.matches('l'));
        characterReader.close();
    }

    @Test
    public void testMatchesIgnoreCaseMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesIgnoreCase("l"));
        assertTrue(characterReader.matchesIgnoreCase("LL"));
        characterReader.consume();
        assertFalse(characterReader.matchesIgnoreCase("l"));
        characterReader.close();
    }

    @Test
    public void testMatchesAnyMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesAny('l', 'o'));
        assertTrue(characterReader.matchesAny("lo", "lo"));
        characterReader.consume();
        assertFalse(characterReader.matchesAny('l', 'o'));
        characterReader.close();
    }

    @Test
    public void testMatchesAnySortedMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'o'}));
        assertTrue(characterReader.matchesAnySorted(new char[]{'o', 'l'}));
        characterReader.consume();
        assertFalse(characterReader.matchesAnySorted(new char[]{'l', 'o'}));
        characterReader.close();
    }

    @Test
    public void testMatchesLetterMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        assertFalse(characterReader.matchesLetter());
        characterReader.close();
    }

    @Test
    public void testMatchesDigitMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("12345678 90123456"));
        assertTrue(characterReader.matchesDigit());
        characterReader.consume();
        assertTrue(characterReader.matchesDigit());
        characterReader.consume();
        assertFalse(characterReader.matchesDigit());
        characterReader.close();
    }

    @Test
    public void testMatchConsumeMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchConsume("ll"));
        assertEquals(2, characterReader.pos());
        characterReader.close();
    }

    @Test
    public void testMatchConsumeIgnoreCaseMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchConsumeIgnoreCase("LL"));
        assertEquals(2, characterReader.pos());
        characterReader.close();
    }

    @Test
    public void testContainsIgnoreCaseMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><title>Test</title></html>"));
        assertTrue(characterReader.containsIgnoreCase("/title"));
        assertTrue(characterReader.containsIgnoreCase("TITLE"));
        characterReader.close();
    }
}