package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Locale;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderBasic() throws IOException {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // Test initial state
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());

        // Consume all characters
        for (int i = 0; i < input.length(); i++) {
            char c = reader.consume();
            Assert.assertNotEquals(EOF, c);
            Assert.assertEquals(i + 1, reader.pos());
        }

        // Test end-of-stream
        Assert.assertEquals(input.length(), reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderAdvance() throws IOException {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // Advance past all characters
        for (int i = 0; i < input.length(); i++) {
            reader.advance();
        }

        // Test end-of-stream
        Assert.assertEquals(input.length(), reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() throws IOException {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();

        Assert.assertEquals(1, reader.pos());
    }

    @Test
    public void testCharacterReaderCurrent() throws IOException {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        Assert.assertEquals('a', reader.current());
        Assert.assertEquals(0, reader.pos());
        reader.advance();
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals(1, reader.pos());
    }

    @Test
    public void testCharacterReaderConsume() throws IOException {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals(1, reader.pos());
    }

    @Test
    public void testCharacterReaderUnconsume() throws IOException {
        String input = "abc";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        reader.consume();
        reader.unconsume();

        Assert.assertEquals('a', reader.current());
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderNextIndexOf() throws IOException {
        String input = "abcdabcdabcd";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // Simple char test
        Assert.assertEquals(4, reader.nextIndexOf('d'));

        // Char sequence test
        Assert.assertEquals(4, reader.nextIndexOf("d"));

        // Test non-existent char sequence
        Assert.assertEquals(-1, reader.nextIndexOf("xyz"));

        // Test empty char sequence
        Assert.assertEquals(-1, reader.nextIndexOf(""));
    }

    @Test
    public void testCharacterReaderConsumeTo() throws IOException {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        String result = reader.consumeTo('f');
        Assert.assertEquals("abcde", result);

        String result2 = reader.consumeTo('g');
        Assert.assertEquals("", result2);
    }

    @Test
    public void testCharacterReaderConsumeToAny() throws IOException {
        String input = "abcdabcdabcd";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        String result = reader.consumeToAny(new char[]{ 'a', 'b' });
        Assert.assertEquals("ab", result);
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() throws IOException {
        String input = "abcdabcdabcd";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        String result = reader.consumeToAnySorted(new char[]{'d', 'c'});
        Assert.assertEquals("cd", result);
    }

    @Test
    public void testCharacterReaderConsumeData() throws IOException {
        String input = "abc&def<null>";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        String result = reader.consumeData();
        Assert.assertEquals("abc", result);
    }

    @Test
    public void testCharacterReaderConsumeTagName() throws IOException {
        String input = "<tab>\t<br>\r<hr>\f<title>My Title</title>";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        String result = reader.consumeTagName();
        Assert.assertEquals("title", result);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() throws IOException {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        String result = reader.consumeToEnd();
        Assert.assertEquals("abcdef", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() throws IOException {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        String result = reader.consumeLetterSequence();
        Assert.assertEquals("abc", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() throws IOException {
        String input = "abc123def456ghi";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        String result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("abc123", result);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() throws IOException {
        String input = "abcdefabcdef0x10hexabcdef";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        String result = reader.consumeHexSequence();
        Assert.assertEquals("abcdef", result);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() throws IOException {
        String input = "1234567890123456";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        String result = reader.consumeDigitSequence();
        Assert.assertEquals("123456", result);
    }

    @Test
    public void testCharacterReaderMatches() throws IOException {
        String input = "abcdabcdabcd";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        Assert.assertTrue(reader.matches('d'));
        Assert.assertTrue(reader.matches("d"));
        Assert.assertFalse(reader.matches('x'));
        Assert.assertFalse(reader.matches("x"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() throws IOException {
        String input = "ABCDABCDABCD";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        Assert.assertTrue(reader.matchesIgnoreCase("d"));
        Assert.assertTrue(reader.matchesIgnoreCase("D"));
        Assert.assertFalse(reader.matchesIgnoreCase("x"));
        Assert.assertFalse(reader.matchesIgnoreCase("X"));
    }

    @Test
    public void testCharacterReaderMatchesAny() throws IOException {
        String input = "abcdabcdabcd";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        Assert.assertTrue(reader.matchesAny('a', 'b'));
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c'));
        Assert.assertFalse(reader.matchesAny('x'));
        Assert.assertFalse(reader.matchesAny('x', 'y'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() throws IOException {
        String input = "abcdabcdabcd";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        Assert.assertTrue(reader.matchesAnySorted(new char[]{'d', 'c'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'d', 'c', 'b'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'x'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'x', 'y'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() throws IOException {
        String input = "abcdabcdabcd";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() throws IOException {
        String input = "1234567890123456";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testCharacterReaderMatchConsume() throws IOException {
        String input = "abcdabcdabcd";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        Assert.assertTrue(reader.matchConsume("d"));
        Assert.assertEquals(4, reader.pos());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() throws IOException {
        String input = "ABCDABCDABCD";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        Assert.assertTrue(reader.matchConsumeIgnoreCase("d"));
        Assert.assertEquals(4, reader.pos());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() throws IOException {
        String input = "<title>My Title</title>";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("TITLE"));
        Assert.assertFalse(reader.containsIgnoreCase("/header"));
        Assert.assertFalse(reader.containsIgnoreCase("HEADER"));
    }
}