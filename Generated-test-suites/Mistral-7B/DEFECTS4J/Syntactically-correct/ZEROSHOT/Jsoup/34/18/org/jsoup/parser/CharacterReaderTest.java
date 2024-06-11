package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderNonEmpty() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('b', reader.current());
    }

    @Test
    public void testCharacterReaderUnconsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderAdvance() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.advance();
        Assert.assertEquals('b', reader.current());
    }

    @Test
    public void testCharacterReaderMark() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeAsString();
        Assert.assertEquals("abc", consumed);
        Assert.assertEquals(3, reader.pos());
    }

    @Test
    public void testCharacterReaderNextIndexOfSingleChar() {
        CharacterReader reader = new CharacterReader("abcabc");
        int index = reader.nextIndexOf('a');
        Assert.assertEquals(0, index);

        index = reader.nextIndexOf('b');
        Assert.assertEquals(3, index);

        index = reader.nextIndexOf('c');
        Assert.assertEquals(1, index);

        index = reader.nextIndexOf('x');
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testCharacterReaderNextIndexOfMultiChar() {
        CharacterReader reader = new CharacterReader("abcabcabcaab");
        String seq = "abc";
        int index = reader.nextIndexOf(seq);
        Assert.assertEquals(0, index);

        index = reader.nextIndexOf(seq, 4);
        Assert.assertEquals(7, index);

        index = reader.nextIndexOf("def");
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testCharacterReaderConsumeToSingleChar() {
        CharacterReader reader = new CharacterReader("abcabc");
        String consumed = reader.consumeTo('a');
        Assert.assertEquals("a", consumed);
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('b', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToMultiChar() {
        CharacterReader reader = new CharacterReader("abcabcabcaab");
        String consumed = reader.consumeTo("abc");
        Assert.assertEquals("abc", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("12345abcdef");
        String consumed = reader.consumeToAny('1', '3');
        Assert.assertEquals("13", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals('2', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("def", consumed);
        Assert.assertEquals(6, reader.pos());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefg");
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("abc", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        String consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("abc123", consumed);
        Assert.assertEquals(6, reader.pos());
        Assert.assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcdef0x12345f");
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals("0x12345", consumed);
        Assert.assertEquals(7, reader.pos());
        Assert.assertEquals('f', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals("123456", consumed);
        Assert.assertEquals(9, reader.pos());
        Assert.assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderMatchesSingleChar() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.matches('e'));
        Assert.assertFalse(reader.matches('x'));
    }

    @Test
    public void testCharacterReaderMatchesMultiChar() {
        CharacterReader reader = new CharacterReader("abcabc");
        String seq = "ab";
        Assert.assertTrue(reader.matches(seq));
        Assert.assertFalse(reader.matches("def"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCabc");
        String seq = "ab";
        Assert.assertTrue(reader.matchesIgnoreCase(seq));
        Assert.assertFalse(reader.matchesIgnoreCase("DEF"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("12345abcdef");
        char[] chars = {'1', '4', 'f'};
        Assert.assertTrue(reader.matchesAny(chars));
        Assert.assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("abc123def");
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsumeSingleChar() {
        CharacterReader reader = new CharacterReader("abcabc");
        reader.matchConsume('a');
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('b', reader.current());
    }

    @Test
    public void testCharacterReaderMatchConsumeMultiChar() {
        CharacterReader reader = new CharacterReader("abcabcabcaab");
        reader.matchConsume("abc");
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCabc");
        reader.matchConsumeIgnoreCase("ab");
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body>");
        Assert.assertTrue(reader.containsIgnoreCase("title"));
        Assert.assertTrue(reader.containsIgnoreCase("HEAD"));
        Assert.assertFalse(reader.containsIgnoreCase("BODY"));
    }
}