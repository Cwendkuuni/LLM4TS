package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderInstance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        Assert.assertNotNull(characterReader);
    }

    @Test
    public void testCharacterReaderMaxBufferLen() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"), 10);
        Assert.assertEquals("10", Integer.toString(characterReader.charBuf.length));
    }

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderNotEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.bufferUp();
        Assert.assertEquals('t', characterReader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.bufferUp();
        characterReader.reader.mark(1);
        characterReader.reader.skip(-1);
        characterReader.consume();
        characterReader.reader.close();
        characterReader.consume();
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.bufferUp();
        characterReader.consume();
        Assert.assertEquals('t', characterReader.current());
    }

    @Test
    public void testCharacterReaderRewindToMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.bufferUp();
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('t', characterReader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        Assert.assertEquals(-1, characterReader.nextIndexOf('z'));
        Assert.assertEquals(1, characterReader.nextIndexOf('t'));
    }

    @Test
    public void testCharacterReaderNextIndexOfSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("zx"));
        Assert.assertEquals(0, characterReader.nextIndexOf("est"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        Assert.assertEquals("est", characterReader.consumeTo('s'));
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        Assert.assertEquals("test", characterReader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        Assert.assertEquals("est", characterReader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test123"));
        Assert.assertEquals("test123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test#0xaF#"));
        Assert.assertEquals("test#0xaF#", characterReader.consumeHexSequence());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test123"));
        Assert.assertEquals("123", characterReader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        Assert.assertTrue(characterReader.matches('t'));
    }

    @Test
    public void testCharacterReaderMatchesSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        Assert.assertTrue(characterReader.matches("es"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("test"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        Assert.assertTrue(characterReader.matchesAny('s', 't'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'s', 't'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        Assert.assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test123"));
        Assert.assertTrue(characterReader.matchesDigit());
    }
}