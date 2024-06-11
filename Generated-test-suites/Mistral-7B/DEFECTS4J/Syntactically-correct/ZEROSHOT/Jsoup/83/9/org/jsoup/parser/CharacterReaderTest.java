package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, characterReader.pos());
        Assert.assertTrue(characterReader.isEmpty());
        Assert.assertEquals(EOF, characterReader.current());
        Assert.assertEquals(EOF, characterReader.consume());
    }

    @Test
    public void testCharacterReaderSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("a"));
        Assert.assertEquals(1, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals('a', characterReader.current());
        Assert.assertEquals('a', characterReader.consume());
        Assert.assertEquals(EOF, characterReader.current());
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals(3, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals('a', characterReader.current());
        Assert.assertEquals('a', characterReader.consume());
        Assert.assertEquals('b', characterReader.current());
        Assert.assertEquals('b', characterReader.consume());
        Assert.assertEquals('c', characterReader.current());
        Assert.assertEquals('c', characterReader.consume());
        Assert.assertEquals(EOF, characterReader.current());
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals(3, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.bufferUp();
        Assert.assertEquals(6, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals('d', characterReader.current());
        Assert.assertEquals('d', characterReader.consume());
        Assert.assertEquals('e', characterReader.current());
        Assert.assertEquals('e', characterReader.consume());
        Assert.assertEquals('f', characterReader.current());
        Assert.assertEquals('f', characterReader.consume());
        Assert.assertEquals(EOF, characterReader.current());
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals(3, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.rewindToMark();
        Assert.assertEquals(0, characterReader.pos());
        characterReader.mark();
        characterReader.consume();
        characterReader.advance();
        Assert.assertEquals(2, characterReader.pos());
        characterReader.rewindToMark();
        Assert.assertEquals(1, characterReader.pos());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijk"));
        Assert.assertEquals(-1, characterReader.nextIndexOf('x'));
        Assert.assertEquals(1, characterReader.nextIndexOf('b'));
        Assert.assertEquals(2, characterReader.nextIndexOf('c'));
        Assert.assertEquals(3, characterReader.nextIndexOf('d'));
        Assert.assertEquals(7, characterReader.nextIndexOf('h'));
        Assert.assertEquals(8, characterReader.nextIndexOf('i'));
        Assert.assertEquals(9, characterReader.nextIndexOf('j'));
        Assert.assertEquals(10, characterReader.nextIndexOf('k'));
        Assert.assertEquals(11, characterReader.nextIndexOf('l')); // not present
    }

    @Test
    public void testCharacterReaderNextIndexOfCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijk"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("xyz"));
        Assert.assertEquals(1, characterReader.nextIndexOf("bc"));
        Assert.assertEquals(3, characterReader.nextIndexOf("def"));
        Assert.assertEquals(7, characterReader.nextIndexOf("hij"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijk"));
        String str = characterReader.consumeTo('h');
        Assert.assertEquals("abcdef", str);
        Assert.assertEquals(6, characterReader.pos());
        String str2 = characterReader.consumeTo('i');
        Assert.assertEquals("defghijk", str2);
        Assert.assertEquals(11, characterReader.pos());
        String str3 = characterReader.consumeToEnd();
        Assert.assertEquals("defghijk", str3);
        Assert.assertEquals(11, characterReader.pos());
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijk"));
        String str = characterReader.consumeToAny('h');
        Assert.assertEquals("abcdefg", str);
        Assert.assertEquals(8, characterReader.pos());
        String str2 = characterReader.consumeToAny("ij");
        Assert.assertEquals("hijk", str2);
        Assert.assertEquals(11, characterReader.pos());
        String str3 = characterReader.consumeToAnySorted(new char[]{'i', 'j', 'h'});
        Assert.assertEquals("hijk", str3);
        Assert.assertEquals(11, characterReader.pos());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd&efghij"));
        String str = characterReader.consumeData();
        Assert.assertEquals("abcd", str);
        Assert.assertEquals(5, characterReader.pos());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<title>Test</title>"));
        String str = characterReader.consumeTagName();
        Assert.assertEquals("title", str);
        Assert.assertEquals(6, characterReader.pos());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123"));
        String str = characterReader.consumeLetterSequence();
        Assert.assertEquals("Test", str);
        Assert.assertEquals(4, characterReader.pos());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123"));
        String str = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Test123", str);
        Assert.assertEquals(7, characterReader.pos());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&#x61; &#x62; &#x63;"));
        String str = characterReader.consumeHexSequence();
        Assert.assertEquals("abc", str);
        Assert.assertEquals(9, characterReader.pos());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("12345"));
        String str = characterReader.consumeDigitSequence();
        Assert.assertEquals("12345", str);
        Assert.assertEquals(5, characterReader.pos());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123"));
        Assert.assertTrue(characterReader.matches('T'));
        Assert.assertTrue(characterReader.matches("est"));
        Assert.assertFalse(characterReader.matches('x'));
        Assert.assertFalse(characterReader.matches("Test456"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("test"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("tESt"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("Test456"));
    }

    @Test
    public void testCharacterReaderMatchesAnyChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123"));
        Assert.assertTrue(characterReader.matchesAny('t'));
        Assert.assertTrue(characterReader.matchesAny('e'));
        Assert.assertTrue(characterReader.matchesAny('s'));
        Assert.assertTrue(characterReader.matchesAny('3'));
        Assert.assertFalse(characterReader.matchesAny('x'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123"));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'t', 'e', 's', '3'}));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'s', 't', '3', 'e'}));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'3', 's', 't', 'e'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'x', '3', 't', 's'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123"));
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123"));
        Assert.assertFalse(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit());
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123"));
        String str = characterReader.consumeTo("st");
        Assert.assertEquals("Te", str);
        Assert.assertEquals(2, characterReader.pos());
        String str2 = characterReader.consumeToIgnoreCase("test");
        Assert.assertEquals("st", str2);
        Assert.assertEquals(5, characterReader.pos());
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123"));
        String str = characterReader.toString();
        Assert.assertEquals("Test123", str);
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123"));
        characterReader.reader.close();
        characterReader.consume();
    }

}