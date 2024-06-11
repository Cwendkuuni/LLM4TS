package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmptyInput() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.nextIndexOf('a'));
    }

    @Test
    public void testCharacterReaderSimpleInput() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('H', reader.current());
        Assert.assertEquals('H', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('e', reader.current());
        Assert.assertEquals("ello Worl", reader.consumeTo('l'));
        Assert.assertEquals("ld", reader.consumeToEnd());
        reader.rewindToMark();
        Assert.assertEquals("Hello", reader.consumeLetterSequence());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello") {
            @Override
            public int read(char[] c, int off, int len) throws IOException {
                throw new IOException();
            }
        });
        reader.consume();
    }

    @Test
    public void testCharacterReaderAdvanceAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(0, reader.pos());
        reader.advance();
        Assert.assertEquals(1, reader.pos());
        reader.rewindToMark();
        Assert.assertEquals(0, reader.pos());

        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        reader.advance();
        Assert.assertEquals(2, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello", reader.consumeTo(' '));
        Assert.assertEquals("ld", reader.consumeToEnd());
        reader.rewindToMark();
        Assert.assertEquals("World", reader.consumeToEnd());
        reader.rewindToMark();
        Assert.assertEquals("Hello", reader.consumeTo(' '));
        reader.advance();
        Assert.assertEquals("ld", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello<World>"));
        Assert.assertEquals("Hello<", reader.consumeToAny(' '));
        Assert.assertEquals("ld<World>", reader.consumeToAnySorted(new char[]{'<', 'l'}));
        Assert.assertEquals("World", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt; &gt; &amp; &nbsp;"));
        Assert.assertEquals("&lt; &gt; &amp; ", reader.consumeData());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><body><h1>Hello World</h1></body></html>"));
        Assert.assertEquals("<html><body><h1>", reader.consumeTagName());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcabc"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertFalse(reader.matches("c"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcABC"));
        Assert.assertTrue(reader.matchesIgnoreCase("A"));
        Assert.assertTrue(reader.matchesIgnoreCase("ab"));
        Assert.assertFalse(reader.matchesIgnoreCase("d"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matchesAny('a'));
        Assert.assertTrue(reader.matchesAny('b'));
        Assert.assertTrue(reader.matchesAny('c'));
        Assert.assertFalse(reader.matchesAny('d'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'d', 'c'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matchesLetter());
        reader.advance();
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("1234"));
        Assert.assertTrue(reader.matchesDigit());
        reader.advance();
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("ababab"));
        Assert.assertEquals("ab", reader.matchConsume("ab"));
        Assert.assertEquals("bab", reader.matchConsume("ba"));
        Assert.assertEquals("b", reader.matchConsume("b"));
        Assert.assertEquals("", reader.matchConsume("c"));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCaBc"));
        Assert.assertEquals("Ab", reader.matchConsumeIgnoreCase("Ab"));
        Assert.assertEquals("Bc", reader.matchConsumeIgnoreCase("Bc"));
        Assert.assertEquals("", reader.matchConsumeIgnoreCase("d"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><body><h1>Hello World</h1></body></html>"));
        Assert.assertTrue(reader.containsIgnoreCase("/html"));
        Assert.assertTrue(reader.containsIgnoreCase("/body"));
        Assert.assertTrue(reader.containsIgnoreCase("h1"));
        Assert.assertFalse(reader.containsIgnoreCase("/foo"));
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", reader.toString());
    }
}