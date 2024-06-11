package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.nextIndexOf('a'));
        Assert.assertEquals(-1, reader.nextIndexOf("ab"));
    }

    @Test
    public void testSingleCharacterReader() {
        CharacterReader reader = new CharacterReader("a");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.nextIndexOf('a'));
        Assert.assertEquals(-1, reader.nextIndexOf('b'));
        Assert.assertEquals("a", reader.consumeAsString());
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testMultipleCharacterReader() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.nextIndexOf('a'));
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(2, reader.nextIndexOf('c'));
        Assert.assertEquals("ab", reader.consumeTo('b'));
        Assert.assertEquals("bc", reader.consumeToEnd());
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeIndexReading() {
        CharacterReader reader = new CharacterReader("abc");
        reader.rewindToMark();
        reader.consume(-1);
    }

    @Test
    public void testScanSingleCharacterSequence() {
        CharacterReader reader = new CharacterReader("ababc");
        Assert.assertEquals(1, reader.nextIndexOf('a'));
        Assert.assertEquals(3, reader.nextIndexOf('a', 1));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testScanCharacterSequence() {
        CharacterReader reader = new CharacterReader("ababc");
        Assert.assertEquals("ab", reader.consumeTo('b'));
        Assert.assertEquals("bc", reader.consumeToEnd());
        Assert.assertEquals("ab", reader.consumeTo('b'));
        Assert.assertEquals("", reader.consumeTo('z'));
    }

    @Test
    public void testScanAnyCharacterSequence() {
        CharacterReader reader = new CharacterReader("abxcdab");
        Assert.assertEquals("x", reader.consumeToAny('x'));
        Assert.assertEquals("cd", reader.consumeToAny('d'));
    }

    @Test
    public void testScanStringSequenceIgnoreCase() {
        CharacterReader reader = new CharacterReader("abABCaab");
        Assert.assertTrue(reader.containsIgnoreCase("ab"));
        Assert.assertTrue(reader.containsIgnoreCase("AB"));
        Assert.assertTrue(reader.containsIgnoreCase("aB"));
        Assert.assertFalse(reader.containsIgnoreCase("xyz"));
    }
}