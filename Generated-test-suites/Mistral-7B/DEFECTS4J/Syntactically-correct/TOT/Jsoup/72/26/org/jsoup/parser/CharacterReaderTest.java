package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());
    }

    @Test
    public void testSingleCharacterReader() {
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
    public void testMultipleCharactersReader() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
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
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('d', reader.current());
        Assert.assertEquals('d', reader.consume());
        Assert.assertEquals(4, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("ab") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
        reader.consume();
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testNextIndexOfCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abababc"));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
        Assert.assertEquals(3, reader.nextIndexOf('a'));
        Assert.assertEquals(6, reader.nextIndexOf('b'));
    }

    @Test
    public void testNextIndexOfString() {
        CharacterReader reader = new CharacterReader(new StringReader("abababc"));
        Assert.assertEquals(-1, reader.nextIndexOf("xyz"));
        Assert.assertEquals(3, reader.nextIndexOf("ab"));
        Assert.assertEquals(6, reader.nextIndexOf("bc"));
    }

    @Test
    public void testConsumeToCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("", reader.consumeTo(EOF));
        Assert.assertEquals("a", reader.consumeTo('b'));
        Assert.assertEquals("ab", reader.consumeTo('c'));
        Assert.assertEquals("abcd", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("", reader.consumeTo("xyz"));
        Assert.assertEquals("ab", reader.consumeTo("cd"));
        Assert.assertEquals("abcd", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAnyCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ab"));
        Assert.assertEquals("", reader.consumeToAny(EOF));
        Assert.assertEquals("a", reader.consumeToAny(
                'a'));
        Assert.assertEquals("ab", reader.consumeToAny(
                'b'));
        Assert.assertEquals("ab", reader.consumeToAny(new char[]{'a', 'b'}));
    }

    @Test
    public void testConsumeToAnySortedCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ab"));
        Assert.assertEquals("", reader.consumeToAnySorted(new char[]{EOF, 'b'}));
        Assert.assertEquals("a", reader.consumeToAnySorted(new char[]{'a'}));
        Assert.assertEquals("ab", reader.consumeToAnySorted(new char[]{'a', 'b'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<ab"));
        Assert.assertEquals("", reader.consumeData());
        Assert.assertEquals("&", reader.consumeData());
        Assert.assertEquals("&<", reader.consumeData());
        Assert.assertEquals("&<ab", reader.consumeToEnd());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("\t<title>Test\n<style>"));
        Assert.assertEquals("", reader.consumeTagName());
        Assert.assertEquals("\t<title>", reader.consumeTagName());
        Assert.assertEquals("\t<title>\n<style>", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("", reader.consumeToEnd());
        Assert.assertEquals("abcd", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertEquals("", reader.consumeLetterSequence());
        Assert.assertEquals("ab", reader.consumeLetterSequence());
        Assert.assertEquals("abcd", reader.consumeLetterSequence());
        Assert.assertEquals("abcdefg", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ab1234defg"));
        Assert.assertEquals("", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("a", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("ab", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("ab1", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("ab12", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("ab123", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("ab1234", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("ab1234defg", reader.consumeToEnd());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ab%cdefg"));
        Assert.assertEquals("", reader.consumeHexSequence());
        Assert.assertEquals("ab", reader.consumeHexSequence());
        Assert.assertEquals("ab%c", reader.consumeHexSequence());
        Assert.assertEquals("ab%cdefg", reader.consumeToEnd());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ab123defg"));
        Assert.assertEquals("", reader.consumeDigitSequence());
        Assert.assertEquals("1", reader.consumeDigitSequence());
        Assert.assertEquals("12", reader.consumeDigitSequence());
        Assert.assertEquals("123", reader.consumeDigitSequence());
        Assert.assertEquals("123defg", reader.consumeToEnd());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertTrue(reader.matches('c'));
        Assert.assertTrue(reader.matches('d'));
        Assert.assertFalse(reader.matches(EOF));
        Assert.assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matches("a"));
        Assert.assertTrue(reader.matches("b"));
        Assert.assertTrue(reader.matches("c"));
        Assert.assertTrue(reader.matches("d"));
        Assert.assertFalse(reader.matches("e"));
        Assert.assertFalse(reader.matches("ef"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesIgnoreCase("a"));
        Assert.assertTrue(reader.matchesIgnoreCase("b"));
        Assert.assertTrue(reader.matchesIgnoreCase("c"));
        Assert.assertTrue(reader.matchesIgnoreCase("d"));
        Assert.assertFalse(reader.matchesIgnoreCase("e"));
        Assert.assertTrue(reader.matchesIgnoreCase("d")); // case insensitive match
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c', 'd', EOF));
        Assert.assertFalse(reader.matchesAny('e', 'f'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesAnySorted('a', 'b', 'c', 'd'));
        Assert.assertTrue(reader.matchesAnySorted('a', 'd', 'c', 'b'));
        Assert.assertFalse(reader.matchesAnySorted('e', 'f'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter(EOF));
        Assert.assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("ab123cd"));
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit(EOF));
        Assert.assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsumeString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchConsume("a"));
        Assert.assertTrue(reader.matchConsume("b"));
        Assert.assertTrue(reader.matchConsume("c"));
        Assert.assertTrue(reader.matchConsume("d"));
        Assert.assertFalse(reader.matchConsume("e"));
        Assert.assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("a"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("b"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("d"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("c"));
        Assert.assertFalse(reader.matchConsumeIgnoreCase("e"));
        Assert.assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><title>Test</title>"));
        Assert.assertTrue(reader.containsIgnoreCase("<title>"));
        Assert.assertTrue(reader.containsIgnoreCase("title>"));
        Assert.assertFalse(reader.containsIgnoreCase("xyz"));
    }
}