package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeSingleCharacter() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        char c = reader.consume();
        Assert.assertEquals('a', c);
    }

    @Test
    public void testCharacterReaderConsumeMultipleCharacters() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        char c1 = reader.consume();
        char c2 = reader.consume();
        char c3 = reader.consume();
        char c4 = reader.consume();
        Assert.assertEquals('a', c1);
        Assert.assertEquals('b', c2);
        Assert.assertEquals('c', c3);
        Assert.assertEquals('d', c4);
    }

    @Test
    public void testCharacterReaderUnconsume() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        reader.unconsume();
        char c = reader.current();
        Assert.assertEquals('a', c);
    }

    @Test
    public void testCharacterReaderMarkRewind() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        char c = reader.current();
        Assert.assertEquals('a', c);
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        String s = reader.consumeAsString();
        Assert.assertEquals("abc", s);
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        String input = "helloworld";
        CharacterReader reader = new CharacterReader(input);
        int index = reader.nextIndexOf('l');
        Assert.assertEquals(5, index);
    }

    @Test
    public void testCharacterReaderNextIndexOfString() {
        String input = "helloworld";
        CharacterReader reader = new CharacterReader(input);
        int index = reader.nextIndexOf("ll");
        Assert.assertEquals(6, index);
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        String input = "hello world!";
        CharacterReader reader = new CharacterReader(input);
        String s = reader.consumeTo(' ');
        Assert.assertEquals("hello ", s);
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        String input = "hello world!";
        CharacterReader reader = new CharacterReader(input);
        String s = reader.consumeTo(" world!");
        Assert.assertEquals("hello world!", s);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        String input = "abcd1234efg";
        CharacterReader reader = new CharacterReader(input);
        String s = reader.consumeToAny(new char[]{'b', 'c'});
        Assert.assertEquals("bc", s);
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        String input = "abcd1234efg";
        CharacterReader reader = new CharacterReader(input);
        String s = reader.consumeToAnySorted(new char[]{'b', 'c', 'd'});
        Assert.assertEquals("bc", s);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        String input = "&amp;<tag/>&lt;tag&gt;null";
        CharacterReader reader = new CharacterReader(input);
        String s = reader.consumeData();
        Assert.assertEquals("&amp;<tag/>&lt;tag&gt;null", s);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        String input = "<tag attribute=\"value\" other=\"attr2\"/>";
        CharacterReader reader = new CharacterReader(input);
        String s = reader.consumeTagName();
        Assert.assertEquals("tag", s);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        String input = "hello world!";
        CharacterReader reader = new CharacterReader(input);
        String s = reader.consumeToEnd();
        Assert.assertEquals("hello world!", s);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        String s = reader.consumeLetterSequence();
        Assert.assertEquals("abc", s);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        String input = "abc123def456";
        CharacterReader reader = new CharacterReader(input);
        String s = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("abc123", s);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        String input = "0x1a 0x2b 0x3c";
        CharacterReader reader = new CharacterReader(input);
        String s = reader.consumeHexSequence();
        Assert.assertEquals("0x1a 0x2b 0x3c", s);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        String input = "12345678";
        CharacterReader reader = new CharacterReader(input);
        String s = reader.consumeDigitSequence();
        Assert.assertEquals("123456", s);
    }

    @Test
    public void testCharacterReaderMatches() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        Assert.assertTrue(reader.matches('d'));
        Assert.assertTrue(reader.matches("d"));
        Assert.assertFalse(reader.matches('e'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        Assert.assertTrue(reader.matchesIgnoreCase("d"));
        Assert.assertTrue(reader.matchesIgnoreCase("D"));
        Assert.assertFalse(reader.matchesIgnoreCase("e"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        Assert.assertTrue(reader.matchesAny('c', 'd'));
        Assert.assertTrue(reader.matchesAny(new char[]{'c', 'd'}));
        Assert.assertFalse(reader.matchesAny('e'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'c', 'd'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'e', 'd'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter('3'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        String input = "123456";
        CharacterReader reader = new CharacterReader(input);
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testCharacterReaderToString() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        String s = reader.toString();
        Assert.assertEquals("abcd", s);
    }
}