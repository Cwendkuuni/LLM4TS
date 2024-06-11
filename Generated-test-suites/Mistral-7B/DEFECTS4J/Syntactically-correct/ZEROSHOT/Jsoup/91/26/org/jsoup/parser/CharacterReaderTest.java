package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader(new StringReader("Hello World!"));
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testIsEmptyNoBufferUp() {
        reader.bufferUp();
        Assert.assertTrue(reader.isEmptyNoBufferUp());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        Assert.assertEquals(1, reader.pos());
    }

    @Test
    public void testConsume() {
        char c = reader.consume();
        Assert.assertEquals('H', c);
        Assert.assertEquals(1, reader.pos());
    }

    @Test
    public void testCurrent() {
        char c = reader.current();
        Assert.assertEquals('H', c);
    }

    @Test
    public void testMatchConsume() {
        Assert.assertTrue(reader.matchConsume("H"));
        Assert.assertEquals(2, reader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.consume();
        reader.consume();
        reader.mark();
        reader.rewindToMark();
        reader.consume();
    }

    @Test
    public void testNextIndexOf() {
        Assert.assertEquals(7, reader.nextIndexOf('W'));
        Assert.assertEquals(6, reader.nextIndexOf('O'));
        Assert.assertEquals(-1, reader.nextIndexOf('X'));
    }

    @Test
    public void testNextIndexOfString() {
        Assert.assertEquals(6, reader.nextIndexOf("World"));
        Assert.assertEquals(0, reader.nextIndexOf("Hello"));
        Assert.assertEquals(-1, reader.nextIndexOf("Xyz"));
    }

    @Test
    public void testConsumeTo() {
        String str = reader.consumeTo(' ');
        Assert.assertEquals("Hello", str);
        Assert.assertEquals(6, reader.pos());
    }

    @Test
    public void testConsumeToAny() {
        String str = reader.consumeToAny('d', 'e');
        Assert.assertEquals("llo", str);
        Assert.assertEquals(8, reader.pos());
    }

    @Test
    public void testConsumeToAnySorted() {
        String str = reader.consumeToAnySorted('e', 'd');
        Assert.assertEquals("llo", str);
        Assert.assertEquals(8, reader.pos());
    }

    @Test
    public void testConsumeData() {
        String str = reader.consumeData();
        Assert.assertEquals("Hello World!", str);
        Assert.assertEquals(13, reader.pos());
    }

    @Test
    public void testConsumeLetterSequence() {
        String str = reader.consumeLetterSequence();
        Assert.assertEquals("llo", str);
        Assert.assertEquals(8, reader.pos());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String str = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("llo1", str);
        Assert.assertEquals(10, reader.pos());
    }

    @Test
    public void testConsumeHexSequence() {
        String str = reader.consumeHexSequence();
        Assert.assertEquals("World", str);
        Assert.assertEquals(11, reader.pos());
    }

    @Test
    public void testConsumeDigitSequence() {
        String str = reader.consumeDigitSequence();
        Assert.assertEquals("1", str);
        Assert.assertEquals(12, reader.pos());
    }

    @Test
    public void testMatchIgnoreCase() {
        Assert.assertTrue(reader.matchesIgnoreCase("world"));
        Assert.assertEquals(13, reader.pos());
    }

    @Test
    public void testMatchesAny() {
        Assert.assertTrue(reader.matchesAny(new char[]{'l', 'o'}));
        Assert.assertEquals(8, reader.pos());
    }

    @Test
    public void testMatchesAnySorted() {
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'d', 'e'}));
        Assert.assertEquals(8, reader.pos());
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertEquals('H', reader.current());
        Assert.assertEquals(2, reader.pos());
    }

    @Test
    public void testMatchesDigit() {
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertEquals('1', reader.current());
        Assert.assertEquals(12, reader.pos());
    }

    @Test
    public void testToString() {
        String str = reader.toString();
        Assert.assertEquals("Hello World!", str);
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        String str1 = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.pos() - reader.bufPos);
        Assert.assertEquals("World!", str1);
        Assert.assertEquals(13, reader.pos());

        String str2 = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.pos() - reader.bufPos);
        Assert.assertEquals("World!", str2);
        Assert.assertEquals(13, reader.pos());

        String str3 = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.pos() - reader.bufPos);
        Assert.assertEquals("World!", str3);
        Assert.assertEquals(13, reader.pos());
    }
}