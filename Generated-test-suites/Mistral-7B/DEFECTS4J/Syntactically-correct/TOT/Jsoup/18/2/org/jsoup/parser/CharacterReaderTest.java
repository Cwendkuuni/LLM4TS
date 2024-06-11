package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.current());
        Assert.assertEquals(-1, reader.consume());
        Assert.assertEquals(-1, reader.pos());
    }

    @Test
    public void testBasicConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('b', reader.current());
        // consume b
        reader.consume();
        // check pos and current
        Assert.assertEquals(2, reader.pos());
        Assert.assertEquals('c', reader.current());
    }

    @Test
    public void testConsumeString() {
        CharacterReader reader = new CharacterReader("abcdef");
        String expected = "abc";
        String consumed = reader.consumeAsString();
        Assert.assertEquals(expected, consumed);
        Assert.assertEquals(3, reader.pos());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeBeyondEnd() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consumeToEnd(); // consume all
        reader.consume(); // consume one more
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        String expected = "abcd";
        Assert.assertEquals(expected, reader.consumeTo('d'));
        Assert.assertEquals(7, reader.pos());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        String expected = "abcd";
        String consumed = reader.consumeTo("d");
        Assert.assertEquals(expected, consumed);
        Assert.assertEquals(7, reader.pos());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        String expected = "abcd";
        Assert.assertEquals(expected, reader.consumeToAny('c', 'd'));
        Assert.assertEquals(7, reader.pos());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        String expected = "abcd";
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals(expected, consumed);
        Assert.assertEquals(5, reader.pos());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0123456789ABCDEFabcdef");
        String expected = "0123";
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals(expected, consumed);
        Assert.assertEquals(3, reader.pos());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345678901234567890");
        String expected = "123";
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals(expected, consumed);
        Assert.assertEquals(3, reader.pos());
    }

    @Test
    public void testConsumeIgnoringCase() {
        CharacterReader reader = new CharacterReader("AbCdEfGhIjKlMnOpQrStUvWxYz");
        String expected = "AbCd";
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals(expected, consumed);
        Assert.assertEquals(3, reader.pos());
    }

    @Test
    public void testMatching() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertFalse(reader.matches('z'));
        Assert.assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchingAndConsuming() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        Assert.assertTrue(reader.matchConsume('a'));
        Assert.assertEquals("bc", reader.toString());
        Assert.assertFalse(reader.matchConsume('x'));
        Assert.assertEquals("cd", reader.toString());
    }

    @Test
    public void testMatchingIgnoringCase() {
        CharacterReader reader = new CharacterReader("ABcdEfGhIjKlMnOpQrSTuVwXyZ");
        Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
        Assert.assertTrue(reader.matchesIgnoreCase("cd"));
        Assert.assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchingAny() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        Assert.assertTrue(reader.matchesAny('a', 'b'));
        Assert.assertTrue(reader.matchesAny('b', 'd'));
        Assert.assertFalse(reader.matchesAny('z', 'x'));
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.advance();
        reader.consume(); // consume 'a'
        reader.rewindToMark(); // go back to mark
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('a', reader.current());
    }
}