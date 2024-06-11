package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest class to test the CharacterReader class.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Test empty constructor
        CharacterReader readerEmpty = new CharacterReader(null);
        Assert.assertFalse(readerEmpty.isEmpty());
        Assert.assertEquals(-1, readerEmpty.pos());

        // Test constructor with a String
        CharacterReader readerString = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(readerString.isEmpty());
        Assert.assertEquals(0, readerString.pos());
        Assert.assertEquals('H', readerString.current());

        // Test constructor with a Reader
        CharacterReader readerReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(readerReader.isEmpty());
        Assert.assertEquals(0, readerReader.pos());
        Assert.assertEquals('H', readerReader.current());

        // Test bufferUp()
        readerReader.bufferUp();
        Assert.assertEquals(1, readerReader.pos());
        Assert.assertEquals('H', readerReader.current());

        // Test pos()
        readerReader.advance();
        Assert.assertEquals(2, readerReader.pos());
        Assert.assertEquals('H', readerReader.current());

        // Test isEmpty()
        readerReader.bufferUp();
        Assert.assertFalse(readerReader.isEmpty());

        // Test consume()
        readerReader.consume();
        Assert.assertEquals(1, readerReader.pos());
        Assert.assertEquals('H', readerReader.current());

        // Test unconsume()
        readerReader.unconsume();
        Assert.assertEquals(2, readerReader.pos());
        Assert.assertEquals('H', readerReader.current());

        // Test advance()
        readerReader.advance();
        Assert.assertEquals(3, readerReader.pos());
        Assert.assertEquals('E', readerReader.current());

        // Test mark() and rewindToMark()
        readerReader.mark();
        readerReader.advance();
        readerReader.rewindToMark();
        Assert.assertEquals(3, readerReader.pos());
        Assert.assertEquals('H', readerReader.current());

        // Test nextIndexOf() with single character
        readerReader.bufferUp();
        Assert.assertEquals(0, readerReader.nextIndexOf('H'));

        // Test nextIndexOf() with multiple characters
        readerReader.bufferUp(6);
        Assert.assertEquals(5, readerReader.nextIndexOf("llo"));

        // Test nextIndexOf() with empty sequence
        Assert.assertEquals(-1, readerReader.nextIndexOf(""));

        // Test consumeTo() with single character
        String str = readerReader.consumeTo('E');
        Assert.assertEquals("Hello", str);
        Assert.assertEquals(5, readerReader.pos());

        // Test consumeTo() with multiple characters
        str = readerReader.consumeTo("llo");
        Assert.assertEquals("Hello", str);
        Assert.assertEquals(8, readerReader.pos());

        // Test consumeToAny()
        str = readerReader.consumeToAny(new char[]{'E', 'l'});
        Assert.assertEquals("Hello", str);
        Assert.assertEquals(8, readerReader.pos());

        // Test consumeToAnySorted()
        str = readerReader.consumeToAnySorted(new char[]{'l', 'E'});
        Assert.assertEquals("Hello", str);
        Assert.assertEquals(8, readerReader.pos());

        // Test consumeData()
        str = readerReader.consumeData();
        Assert.assertEquals("Hello World", str);
        Assert.assertEquals(13, readerReader.pos());

        // Test consumeTagName()
        str = readerReader.consumeTagName();
        Assert.assertEquals("Hello", str);
        Assert.assertEquals(8, readerReader.pos());

        // Test consumeToEnd()
        str = readerReader.consumeToEnd();
        Assert.assertEquals("Hello World", str);
        Assert.assertEquals(13, readerReader.pos());

        // Test consumeLetterSequence()
        str = readerReader.consumeLetterSequence();
        Assert.assertEquals("Hello", str);
        Assert.assertEquals(8, readerReader.pos());

        // Test consumeLetterThenDigitSequence()
        str = readerReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("HelloWorld", str);
        Assert.assertEquals(13, readerReader.pos());

        // Test consumeHexSequence()
        str = readerReader.consumeHexSequence();
        Assert.assertEquals("HelloWorld", str);
        Assert.assertEquals(13, readerReader.pos());

        // Test consumeDigitSequence()
        str = readerReader.consumeDigitSequence();
        Assert.assertEquals("1234567890", str);
        Assert.assertEquals(14, readerReader.pos());

        // Test matches() with single character
        readerReader.bufferUp();
        Assert.assertTrue(readerReader.matches('H'));

        // Test matches() with multiple characters
        readerReader.bufferUp(5);
        Assert.assertTrue(readerReader.matches("llo"));

        // Test matchesIgnoreCase() with single character
        Assert.assertTrue(readerReader.matchesIgnoreCase("H"));

        // Test matchesIgnoreCase() with multiple characters
        Assert.assertTrue(readerReader.matchesIgnoreCase("llo"));

        // Test matchesAny() with single character
        Assert.assertTrue(readerReader.matchesAny('H'));

        // Test matchesAny() with multiple characters
        readerReader.bufferUp(5);
        Assert.assertTrue(readerReader.matchesAny(new char[]{'E', 'l'}));

        // Test matchesAnySorted() with single character
        Assert.assertTrue(readerReader.matchesAnySorted('H'));

        // Test matchesAnySorted() with multiple characters
        readerReader.bufferUp(5);
        Assert.assertTrue(readerReader.matchesAnySorted(new char[]{'l', 'E'}));

        // Test matchesLetter()
        Assert.assertTrue(readerReader.matchesLetter());

        // Test matchesDigit()
        readerReader.bufferUp();
        Assert.assertTrue(readerReader.matchesDigit());

        // Test matchConsume() with single character
        readerReader.bufferUp();
        Assert.assertEquals("Hello", readerReader.consumeTo("llo"));
        Assert.assertEquals(8, readerReader.pos());

        // Test matchConsume() with multiple characters
        readerReader.bufferUp(4);
        Assert.assertEquals("llo", readerReader.consumeTo("llo"));
        Assert.assertEquals(7, readerReader.pos());

        // Test matchConsumeIgnoreCase() with single character
        readerReader.bufferUp();
        Assert.assertEquals("Hello", readerReader.consumeToIgnoreCase("llo"));
        Assert.assertEquals(8, readerReader.pos());

        // Test matchConsumeIgnoreCase() with multiple characters
        readerReader.bufferUp(4);
        Assert.assertEquals("llo", readerReader.consumeToIgnoreCase("llo"));
        Assert.assertEquals(7, readerReader.pos());

        // Test containsIgnoreCase()
        Assert.assertTrue(readerReader.containsIgnoreCase("llo"));

        // Test toString()
        Assert.assertEquals("Hello World", readerReader.toString());

        // Test cacheString()
        String cacheString = readerReader.cacheString(charBuf, stringCache, 0, 13);
        Assert.assertEquals("Hello World", cacheString);

        // Test rangeEquals()
        Assert.assertTrue(readerReader.rangeEquals(0, 13, "Hello World"));

        // Test CharacterReader with maxBufferLen
        CharacterReader readerMax = new CharacterReader(new StringReader("A" + new String(new char[maxBufferLen])), maxBufferLen);
        Assert.assertEquals(1, readerMax.pos());
        Assert.assertEquals('A', readerMax.current());

        readerMax.bufferUp();
        Assert.assertEquals(maxBufferLen + 1, readerMax.bufLength);
        Assert.assertEquals(maxBufferLen + 1, readerMax.pos());
        Assert.assertEquals('A', readerMax.current());
    }

    private char[] charBuf;
    private String[] stringCache;

    @Before
    public void setUp() {
        charBuf = new char[128];
        stringCache = new String[512];
    }
}