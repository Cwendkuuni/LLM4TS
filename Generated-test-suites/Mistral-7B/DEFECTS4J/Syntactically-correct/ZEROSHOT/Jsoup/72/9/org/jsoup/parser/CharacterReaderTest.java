package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.io.StringReader;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Test constructor
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"), 10);
        Assert.assertEquals(10, reader.charBuf.length);
        Assert.assertNotNull(reader.reader);

        // Test isEmpty
        Assert.assertFalse(reader.isEmpty());
        reader.advance();
        reader.advance();
        Assert.assertTrue(reader.isEmpty());

        // Test pos
        Assert.assertEquals(0, reader.pos());
        reader.advance();
        reader.advance();
        Assert.assertEquals(2, reader.pos());

        // Test consume
        Assert.assertEquals('H', reader.consume());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals(EOF, reader.consume());

        // Test nextIndexOf
        Assert.assertEquals(6, reader.nextIndexOf('W'));
        Assert.assertEquals(-1, reader.nextIndexOf('X'));

        // Test consumeTo
        Assert.assertEquals("Hello", reader.consumeTo(' '));
        Assert.assertEquals("World", reader.consumeToEnd());

        // Test consumeToAny
        Assert.assertEquals("lloWorld", reader.consumeToAny('l'));
        Assert.assertEquals("ld", reader.consumeToAnySorted(new char[]{'l', 'd'}));

        // Test consumeData
        Assert.assertEquals("Hello", reader.consumeData());

        // Test consumeTagName
        Assert.assertEquals("Hello", reader.consumeTagName());

        // Test consumeToEnd
        Assert.assertEquals("Hello World", reader.consumeToEnd());

        // Test matches
        Assert.assertTrue(reader.matches('e'));
        Assert.assertFalse(reader.matches(EOF));

        // Test matchesIgnoreCase
        reader.mark();
        reader.advance();
        Assert.assertTrue(reader.matchesIgnoreCase("LO"));
        reader.rewindToMark();

        // Test matchesAny
        Assert.assertTrue(reader.matchesAny(new char[]{'l', 'e'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'x', 'l'}));

        // Test matchesLetter
        Assert.assertTrue(reader.matchesLetter());

        // Test matchesDigit
        reader.advance();
        reader.advance();
        Assert.assertTrue(reader.matchesDigit());

        // Test matchConsume
        Assert.assertEquals("World", reader.matchConsume("ld"));

        // Test matchConsumeIgnoreCase
        Assert.assertEquals("ld", reader.matchConsumeIgnoreCase("LD"));

        // Test containsIgnoreCase
        reader.mark();
        reader.advance();
        reader.advance();
        Assert.assertTrue(reader.containsIgnoreCase("ld"));
        reader.rewindToMark();

        // Test toString
        Assert.assertEquals("World", reader.toString());
    }
}