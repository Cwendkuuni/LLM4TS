import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.UncheckedIOException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * CharacterReaderTest.java - Test cases for CharacterReader class.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmptyInput() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderBasicInput() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("Hello World".getBytes())));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
        reader.consume();
        reader.advance();
        Assert.assertEquals('l', reader.current());
        // Testing consumeToEnd()
        String str = reader.consumeToEnd();
        Assert.assertEquals("Hello World", str);
        // Testing isEmpty() after consumeToEnd()
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("Hello World".getBytes()), "UTF-8")); // Wrong encoding
        reader.consume();
    }

    @Test
    public void testCharacterReaderMarkUnconsume() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("Hello World".getBytes())));
        reader.mark();
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('H', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOfSingleChar() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("Hello World".getBytes())));
        Assert.assertEquals(5, reader.nextIndexOf('o'));
    }

    @Test
    public void testCharacterReaderNextIndexOfMultiChar() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("Hello World".getBytes())));
        Assert.assertEquals(-1, reader.nextIndexOf("Hello"));
        Assert.assertEquals(6, reader.nextIndexOf("ld"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("Hello World".getBytes())));
        Assert.assertEquals("World", reader.consumeTo(' '));
        Assert.assertEquals("World", reader.consumeToAny(new char[]{' ', 'l'}));
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("Hello World".getBytes())));
        Assert.assertEquals("Hello World", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("123AbCdEfG123".getBytes())));
        Assert.assertEquals("AbCdEfG", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("123AbCdEfG123".getBytes())));
        Assert.assertEquals("AbCdEfG123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("0x123ABCdEfG123".getBytes())));
        Assert.assertEquals("123ABCdEf", reader.consumeHexSequence());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("1234567890123".getBytes())));
        Assert.assertEquals("01234567890", reader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReaderMatchSingleChar() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("Hello World".getBytes())));
        Assert.assertTrue(reader.matches('o'));
    }

    @Test
    public void testCharacterReaderMatchString() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("Hello World".getBytes())));
        Assert.assertTrue(reader.matches("ll"));
    }

    @Test
    public void testCharacterReaderMatchIgnoreCase() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("Hello World".getBytes())));
        Assert.assertTrue(reader.matchesIgnoreCase("ll"));
    }

    @Test
    public void testCharacterReaderMatchAnySingleChar() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("123AbCdEfG123".getBytes())));
        Assert.assertTrue(reader.matchesAny('1'));
    }

    @Test
    public void testCharacterReaderMatchAnySorted() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("123AbCdEfG123".getBytes())));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'1', '2'}));
    }

    @Test
    public void testCharacterReaderMatchLetter() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("123AbCdEfG123".getBytes())));
        Assert.assertTrue(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchDigit() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("123AbCdEfG123".getBytes())));
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsumeSingleChar() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("Hello World".getBytes())));
        Assert.assertTrue(reader.matchConsume('o'));
    }

    @Test
    public void testCharacterReaderMatchConsumeStringIgnoreCase() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("<title>Hello World</title>".getBytes())));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("<title>"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("Title"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("<title>Hello World</title>".getBytes())));
        Assert.assertTrue(reader.containsIgnoreCase("<title>"));
        Assert.assertTrue(reader.containsIgnoreCase("Title"));
    }
}