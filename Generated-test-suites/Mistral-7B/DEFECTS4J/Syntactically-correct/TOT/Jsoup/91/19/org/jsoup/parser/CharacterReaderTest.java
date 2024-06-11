package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume(); // This should throw an IOException
    }

    @Test
    public void testCharacterReaderMark() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        Assert.assertEquals(0, reader.pos());
        reader.advance();
        reader.advance();
        Assert.assertEquals(2, reader.pos());
        reader.rewindToMark();
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String result = reader.consumeTo('c');
        Assert.assertEquals("ab", result);
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String result = reader.consumeToEnd();
        Assert.assertEquals("abcd", result);
        Assert.assertEquals(4, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String result = reader.consumeToAny(',', '.');
        Assert.assertEquals("abcd", result);
        Assert.assertEquals(4, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());

        reader = new CharacterReader(new StringReader("1,2,3,4"));
        result = reader.consumeToAny(',');
        Assert.assertEquals("1,", result);
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('2', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("1,2,3,4"));
        String result = reader.consumeToAnySorted(new char[]{'2', '3'});
        Assert.assertEquals("2,3", result);
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('4', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;foo&gt;"));
        String result = reader.consumeData();
        Assert.assertEquals("&lt;foo&gt;", result);
        Assert.assertEquals(6, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag name=\"foo\"></tag>"));
        String result = reader.consumeTagName();
        Assert.assertEquals("tag", result);
        Assert.assertEquals(6, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMatch() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("abc"));
        Assert.assertFalse(reader.matches("bcd"));
        Assert.assertFalse(reader.matches("efg"));
        Assert.assertFalse(reader.matches('z'));
        Assert.assertFalse(reader.matches(null));
    }

    @Test
    public void testCharacterReaderMatchIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEF"));
        Assert.assertTrue(reader.matchesIgnoreCase("abc"));
        Assert.assertTrue(reader.matchesIgnoreCase("ABC"));
        Assert.assertFalse(reader.matchesIgnoreCase("bcd"));
        Assert.assertFalse(reader.matchesIgnoreCase("efg"));
        Assert.assertFalse(reader.matchesIgnoreCase('z'));
        Assert.assertFalse(reader.matchesIgnoreCase(null));
    }

    @Test
    public void testCharacterReaderMatchAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesAny('a'));
        Assert.assertTrue(reader.matchesAny('b'));
        Assert.assertTrue(reader.matchesAny('c'));
        Assert.assertTrue(reader.matchesAny('d'));
        Assert.assertTrue(reader.matchesAny('e'));
        Assert.assertTrue(reader.matchesAny('f'));
        Assert.assertFalse(reader.matchesAny('g'));
        Assert.assertFalse(reader.matchesAny('h'));
        Assert.assertFalse(reader.matchesAny(null));
    }

    @Test
    public void testCharacterReaderMatchAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'c', 'b', 'a'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'e', 'f', 'd'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'f', 'e', 'd'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'g', 'h'}));
        Assert.assertFalse(reader.matchesAnySorted(null));
    }

    @Test
    public void testCharacterReaderMatchLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter('a'));
        Assert.assertTrue(reader.matchesLetter('b'));
        Assert.assertTrue(reader.matchesLetter('c'));
        Assert.assertTrue(reader.matchesLetter('d'));
        Assert.assertTrue(reader.matchesLetter('e'));
        Assert.assertTrue(reader.matchesLetter('f'));
        Assert.assertFalse(reader.matchesLetter('g'));
        Assert.assertFalse(reader.matchesLetter('h'));
    }

    @Test
    public void testCharacterReaderMatchDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit('0'));
        Assert.assertTrue(reader.matchesDigit('1'));
        Assert.assertTrue(reader.matchesDigit('2'));
        Assert.assertTrue(reader.matchesDigit('3'));
        Assert.assertTrue(reader.matchesDigit('4'));
        Assert.assertTrue(reader.matchesDigit('5'));
        Assert.assertTrue(reader.matchesDigit('6'));
        Assert.assertFalse(reader.matchesDigit('7'));
        Assert.assertFalse(reader.matchesDigit('h'));
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals(-1, reader.nextIndexOf('g'));
        Assert.assertEquals(1, reader.nextIndexOf('a'));
        Assert.assertEquals(2, reader.nextIndexOf('b'));
        Assert.assertEquals(3, reader.nextIndexOf('c'));
        Assert.assertEquals(4, reader.nextIndexOf('d'));
        Assert.assertEquals(5, reader.nextIndexOf('e'));
        Assert.assertEquals(6, reader.nextIndexOf('f'));
        Assert.assertEquals(1, reader.nextIndexOf("ab"));
        Assert.assertEquals(3, reader.nextIndexOf("cd"));
        Assert.assertEquals(5, reader.nextIndexOf("ef"));
        Assert.assertEquals(0, reader.nextIndexOf(""));
        Assert.assertEquals(0, reader.nextIndexOf(null));
    }

    @Test
    public void testCharacterReaderNextIndexOfNull() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals(-1, reader.nextIndexOf(null));
        reader.bufferUp();
        reader.mark();
        reader.consumeToEnd();
        reader.rewindToMark();
        Assert.assertEquals(0, reader.nextIndexOf(null));
    }
}