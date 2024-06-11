package org.jsoup.parser;

import org.jsoup.UncheckedIOException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertEquals("Hello", characterReader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello"));
        Assert.assertEquals("Hello", characterReader.consumeToEnd());
    }

    @Test
    public void testEmptyCharacterReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderCurrent() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsume() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertEquals('H', characterReader.consume());
        Assert.assertEquals('e', characterReader.consume());
        Assert.assertEquals('l', characterReader.consume());
        Assert.assertEquals('l', characterReader.consume());
        Assert.assertEquals('o', characterReader.consume());
        Assert.assertEquals(CharacterReader.EOF, characterReader.consume());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        ByteArrayReader reader = new ByteArrayReader(new byte[1]);
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.consume();
        reader.close();
        characterReader.consume();
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        int position = characterReader.pos();
        characterReader.mark();
        characterReader.consumeTo('l');
        int markPosition = characterReader.pos();
        characterReader.rewindToMark();
        Assert.assertEquals(position, characterReader.pos());
        Assert.assertEquals('l', characterReader.current());
        Assert.assertEquals(markPosition, characterReader.pos());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertEquals(1, characterReader.nextIndexOf('e'));
        Assert.assertEquals(5, characterReader.nextIndexOf('o'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testCharacterReaderNextIndexOfWithSequence() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertEquals(0, characterReader.nextIndexOf("he"));
        Assert.assertEquals(3, characterReader.nextIndexOf("ll"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("zz"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        String consumed = characterReader.consumeTo('l');
        Assert.assertEquals("He", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToWithSequence() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        String consumed = characterReader.consumeTo("ll");
        Assert.assertEquals("Hell", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        String consumed = characterReader.consumeToAny('l');
        Assert.assertEquals("He", consumed);
        consumed = characterReader.consumeToAny("lo");
        Assert.assertEquals("Hello", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        String consumed = characterReader.consumeToAnySorted('l', 'o');
        Assert.assertEquals("He", consumed);
        consumed = characterReader.consumeToAnySorted("lo");
        Assert.assertEquals("Hello", consumed);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'&', '>', 'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        String consumed = characterReader.consumeData();
        Assert.assertEquals("Hello", consumed);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'<', 'h', 'e', 'l', 'l', 'o', '>', ' '});
        CharacterReader characterReader = new CharacterReader(reader);
        String consumed = characterReader.consumeTagName();
        Assert.assertEquals("hell", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("Hello", consumed);
    }

    @Test
    public void testCharacterReaderToString() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        String consumed = characterReader.toString();
        Assert.assertEquals("Hello", consumed);
    }

    @Test
    public void testCharacterReaderCacheString() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        String consumed = characterReader.consumeTo("ll");
        String cached = characterReader.cacheString(characterReader.charBuf, characterReader.stringCache, 0, consumed.length());
        Assert.assertEquals(consumed, cached);
    }

    @Test
    public void testCharacterReaderRangeEquals() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        String consumed = characterReader.consumeTo("ll");
        Assert.assertTrue(characterReader.rangeEquals(consumed));
    }

    @Test
    public void testCharacterReaderMatches() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertTrue(characterReader.matches('l'));
        Assert.assertTrue(characterReader.matches("ll"));
        Assert.assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertTrue(characterReader.matchesIgnoreCase("ll"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("ll"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("zz"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertTrue(characterReader.matchesAny('l'));
        Assert.assertTrue(characterReader.matchesAny('o'));
        Assert.assertFalse(characterReader.matchesAny('z'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertTrue(characterReader.matchesAnySorted('l'));
        Assert.assertTrue(characterReader.matchesAnySorted('o'));
        Assert.assertFalse(characterReader.matchesAnySorted('z'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter(' '));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        ByteArrayReader reader = new ByteArrayReader(new byte[]{'H', 'e', 'l', 'l', 'o'});
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertFalse(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit());
        Assert.assertTrue(characterReader.matchesDigit('0'));
    }
}