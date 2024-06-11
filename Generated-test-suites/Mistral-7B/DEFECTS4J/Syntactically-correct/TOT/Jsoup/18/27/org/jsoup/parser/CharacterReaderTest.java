package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(CharacterReader.EOF, reader.current());
        Assert.assertThrows(IllegalArgumentException.class, () -> reader.consume());
    }

    @Test
    public void testSimpleConsumption() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals('d', reader.current());
        Assert.assertEquals('d', reader.consume());
        Assert.assertEquals('e', reader.current());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals('f', reader.current());
        Assert.assertEquals('f', reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
    }

    @Test
    public void testConsumeSubstring() {
        CharacterReader reader = new CharacterReader("hello world");
        Assert.assertEquals("hello", reader.consumeTo(' '));
        Assert.assertEquals(' ', reader.current());
        Assert.assertEquals("world", reader.consumeToEnd());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeSubstringWithEmptySequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("", reader.consumeToAny());
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeSubstringWithMismatch() {
        CharacterReader reader = new CharacterReader("abcd1234");
        Assert.assertEquals("1234", reader.consumeTo("123")); // will match on '1' but not '3' and '3' is not consumed
        Assert.assertEquals('4', reader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abracadabra");
        Assert.assertEquals("abra", reader.consumeLetterSequence());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals('a', reader.consumeLetterSequence().charAt(0));
        Assert.assertEquals('b', reader.consumeLetterSequence().charAt(1));
        Assert.assertEquals('r', reader.consumeLetterSequence().charAt(2));
        Assert.assertEquals('a', reader.consumeLetterSequence().charAt(3));
        Assert.assertEquals('', reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ab12cd34ef");
        Assert.assertEquals("12", reader.consumeHexSequence());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals("34", reader.consumeHexSequence());
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("ab123cd4ef");
        Assert.assertEquals("123", reader.consumeDigitSequence());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals("4", reader.consumeDigitSequence());
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abracadabra");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertFalse(reader.matches('c'));
        Assert.assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABRACADABRA");
        Assert.assertTrue(reader.matchesIgnoreCase("a"));
        Assert.assertTrue(reader.matchesIgnoreCase("b"));
        Assert.assertFalse(reader.matchesIgnoreCase('c'));
        Assert.assertTrue(reader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("123abcd123");
        Assert.assertTrue(reader.matchesAny('1', '2', '3'));
        Assert.assertTrue(reader.matchesAny('1', 'a', 'd'));
        Assert.assertFalse(reader.matchesAny('e', 'f', 'z'));
        Assert.assertFalse(reader.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("
        <html><head><title>The Dancing
        </title></head><body>The quick
        <style>
        </body></html>");
        Assert.assertTrue(reader.containsIgnoreCase("<title>"));
        Assert.assertTrue(reader.containsIgnoreCase("<style>"));
    }
}