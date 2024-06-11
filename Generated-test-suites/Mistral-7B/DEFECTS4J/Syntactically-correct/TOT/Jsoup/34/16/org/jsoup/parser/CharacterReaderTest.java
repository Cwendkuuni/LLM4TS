package org.jsoup.parser.test;

import org.junit.Assert;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @org.junit.Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.nextIndexOf('a'));
        Assert.assertEquals("", reader.consumeToEnd());
    }

    @org.junit.Test
    public void testSingleCharacterReader() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals("", reader.consumeToEnd());
    }

    @org.junit.Test
    public void testMultipleCharacterReader() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals("bcdef", reader.consumeToEnd());
    }

    @org.junit.Test
    public void testMarkAndRewindReader() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('a', reader.current());
    }

    @org.junit.Test
    public void testNextIndexOfSingleCharacterReader() {
        CharacterReader reader = new CharacterReader("abcabc");
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
    }

    @org.junit.Test
    public void testNextIndexOfMultiCharacterReader() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        Assert.assertEquals(1, reader.nextIndexOf("cd"));
        Assert.assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @org.junit.Test
    public void testConsumeToCharacterReader() {
        CharacterReader reader = new CharacterReader("abcabcabc");
        Assert.assertEquals("bc", reader.consumeTo('c'));
        Assert.assertEquals("abc", reader.consumeTo('a'));
        Assert.assertEquals("", reader.consumeTo('z'));
    }

    @org.junit.Test
    public void testConsumeToSequenceReader() {
        CharacterReader reader = new CharacterReader("abcabcabc");
        Assert.assertEquals("bc", reader.consumeTo("bc"));
        Assert.assertEquals("abc", reader.consumeTo("ab"));
        Assert.assertEquals("", reader.consumeTo("xyz"));
    }

    @org.junit.Test
    public void testConsumeToAnyReader() {
        CharacterReader reader = new CharacterReader("1234567890");
        Assert.assertEquals("123", reader.consumeToAny('1', '2'));
        Assert.assertEquals("", reader.consumeToAny('x', 'y'));
    }

    @org.junit.Test
    public void testMatchesCharacterReader() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertFalse(reader.matches('z'));
    }

    @org.junit.Test
    public void testMatchesSequenceReader() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertFalse(reader.matches("cd"));
    }

    @org.junit.Test
    public void testMatchesIgnoreCaseReader() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        Assert.assertTrue(reader.matchesIgnoreCase("ab"));
        Assert.assertFalse(reader.matchesIgnoreCase("CD"));
    }

    @org.junit.Test
    public void testMatchesAnyReader() {
        CharacterReader reader = new CharacterReader("1234567890");
        Assert.assertTrue(reader.matchesAny('1', '2', '3'));
        Assert.assertFalse(reader.matchesAny('x', 'y', 'z'));
    }

    @org.junit.Test
    public void testMatchesLetterReader() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter('0'));
    }

    @org.junit.Test
    public void testMatchesDigitReader() {
        CharacterReader reader = new CharacterReader("1234567890");
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit('a'));
    }

    @org.junit.Test
    public void testMatchConsumeReader() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.matchConsume("ab"));
        Assert.assertFalse(reader.matchConsume("cd"));
    }

    @org.junit.Test
    public void testMatchConsumeIgnoreCaseReader() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("ab"));
        Assert.assertFalse(reader.matchConsumeIgnoreCase("CD"));
    }

    @org.junit.Test
    public void testContainsIgnoreCaseReader() {
        CharacterReader reader = new CharacterReader("
    <html><head><title>Test</title></head><body>Test</body></html>");
        Assert.assertTrue(reader.containsIgnoreCase("/title>"));
        Assert.assertTrue(reader.containsIgnoreCase("/style>"));
        Assert.assertFalse(reader.containsIgnoreCase("/body>"));
    }
}