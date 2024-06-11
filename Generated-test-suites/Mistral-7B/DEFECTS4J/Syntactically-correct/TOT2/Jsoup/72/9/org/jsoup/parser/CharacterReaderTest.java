package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertEquals("Hello World!", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals("Hello World!", characterReader.toString());
    }

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals(0, characterReader.pos());

        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());

        characterReader.advance();
        characterReader.advance();
        Assert.assertEquals(3, characterReader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!")) {
            @Override
            void bufferUp() {
                throw new IOException();
            }
        };
        characterReader.consume();
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals('H', characterReader.consume());
        Assert.assertEquals('e', characterReader.consume());
        // consume past EOF
        Assert.assertEquals(CharacterReader.EOF, characterReader.consume());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals(6, characterReader.nextIndexOf(' '));
        Assert.assertEquals(-1, characterReader.nextIndexOf('Z'));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String consumed = characterReader.consumeTo(' ');
        Assert.assertEquals("Hello", consumed);

        consumed = characterReader.consumeTo("ld!");
        Assert.assertEquals("Hello World!", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String consumed = characterReader.consumeToAny(new char[]{'l', 'd'});
        Assert.assertEquals("Hello World!", consumed);

        consumed = characterReader.consumeToAny(new char[]{'h', 'W'});
        Assert.assertEquals("Hello", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("Hello World!", consumed);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String consumed = characterReader.consumeLetterSequence();
        Assert.assertEquals("Hello", consumed);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello123World!"));
        String consumed = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Hello123", consumed);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("0x1a"));
        String consumed = characterReader.consumeHexSequence();
        Assert.assertEquals("0x1a", consumed);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("123"));
        String consumed = characterReader.consumeDigitSequence();
        Assert.assertEquals("123", consumed);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matches('l'));
        Assert.assertTrue(characterReader.matches("ld!"));
        Assert.assertFalse(characterReader.matches('Z'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("ld!"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("hello"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesAny('l', 'd'));
        Assert.assertFalse(characterReader.matchesAny('Z'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'd'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'Z'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter('Z'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("123"));
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit('Z'));
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchConsume("ld!"));
        Assert.assertEquals(6, characterReader.pos());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("ld!"));
        Assert.assertEquals(6, characterReader.pos());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Hello World</title></head><body>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/title>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/head>"));
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals("Hello World!", characterReader.toString());
    }
}