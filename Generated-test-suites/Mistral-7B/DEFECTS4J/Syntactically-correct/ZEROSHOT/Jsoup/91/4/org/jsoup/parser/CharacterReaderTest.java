package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        // pos() should return 0 initially
        Assert.assertEquals(0, reader.pos());

        // consume should return the first character
        Assert.assertEquals('H', reader.consume());
        Assert.assertEquals(1, reader.pos());

        // consume should return the next character
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals(2, reader.pos());

        // consume should return the next character
        Assert.assertEquals('l', reader.consume());
        Assert.assertEquals(3, reader.pos());

        // consumeTo should return the substring until the specified character
        String str = reader.consumeTo('l');
        Assert.assertEquals("He", str);
        Assert.assertEquals(3, reader.pos());

        // consumeTo should return the whole string when the character is not present
        str = reader.consumeTo('z');
        Assert.assertEquals("Hello World", str);
        Assert.assertEquals(11, reader.pos());

        // isEmpty should return false when there are characters left
        Assert.assertFalse(reader.isEmpty());

        // consume all characters
        while (!reader.isEmpty()) {
            reader.consume();
        }

        // isEmpty should return true when all characters have been consumed
        Assert.assertTrue(reader.isEmpty());

        // mark and rewind should work correctly
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('H', reader.current());

        // nextIndexOf should return the index of the first occurrence of a character
        Assert.assertEquals(0, reader.nextIndexOf('H'));
        Assert.assertEquals(7, reader.nextIndexOf('o'));

        // nextIndexOf should return -1 when the character is not present
        Assert.assertEquals(-1, reader.nextIndexOf('x'));

        // nextIndexOf should return the index of a sequence of characters
        Assert.assertEquals(4, reader.nextIndexOf("ll"));

        // nextIndexOf should return -1 when the sequence of characters is not present
        Assert.assertEquals(-1, reader.nextIndexOf("zz"));

        // consumeToAny should return the substring until the first occurrence of any of the specified characters
        str = reader.consumeToAny('l', 'o');
        Assert.assertEquals("He", str);
        Assert.assertEquals(3, reader.pos());

        // consumeToAny should return the whole string when all characters have been consumed
        str = reader.consumeToAny('a', 'z');
        Assert.assertEquals("Hello World", str);
        Assert.assertEquals(11, reader.pos());

        // consumeData should return the substring until the first occurrence of a special character
        str = reader.consumeData();
        Assert.assertEquals("Hello World", str);
        Assert.assertEquals(11, reader.pos());

        // consumeTagName should return the tag name
        reader = new CharacterReader(new StringReader("<html><body><h1>Hello</h1></body></html>"));
        str = reader.consumeTagName();
        Assert.assertEquals("html", str);
        Assert.assertEquals(4, reader.pos());

        // consumeToEnd should return the rest of the string
        str = reader.consumeToEnd();
        Assert.assertEquals("<body><h1>Hello</h1></body></html>", str);
        Assert.assertEquals(20, reader.pos());

        // consumeLetterSequence should return a letter sequence
        str = reader.consumeLetterSequence();
        Assert.assertEquals("llo", str);
        Assert.assertEquals(6, reader.pos());

        // consumeLetterThenDigitSequence should return a letter followed by a digit sequence
        str = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("World", str);
        Assert.assertEquals(7, reader.pos());

        // consumeHexSequence should return a hex sequence
        str = reader.consumeHexSequence();
        Assert.assertEquals("World", str);
        Assert.assertEquals(7, reader.pos());

        // consumeDigitSequence should return a digit sequence
        str = reader.consumeDigitSequence();
        Assert.assertEquals("1", str);
        Assert.assertEquals(1, reader.pos());

        // matches should return true if the current character matches the specified character
        Assert.assertTrue(reader.matches('l'));

        // matches should return false if the current character does not match the specified character
        Assert.assertFalse(reader.matches('x'));

        // matches should return false if there are no more characters left
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        Assert.assertFalse(reader.matches('l'));

        // matches should return true if the current sequence of characters matches the specified sequence
        Assert.assertTrue(reader.matches("llo"));

        // matches should return false if the current sequence of characters does not match the specified sequence
        Assert.assertFalse(reader.matches("llz"));

        // matchesIgnoreCase should return true if the current sequence of characters matches the specified sequence ignoring case
        reader.mark();
        reader.consume(); // consume 'H'
        reader.consume(); // consume 'e'
        reader.consume(); // consume 'l'
        reader.consume(); // consume 'l'
        reader.consume(); // consume 'o'
        reader.rewindToMark();
        Assert.assertTrue(reader.matchesIgnoreCase("hell"));

        // matchesAny should return true if the current character matches any of the specified characters
        Assert.assertTrue(reader.matchesAny('l', 'o'));

        // matchesAny should return false if the current character does not match any of the specified characters
        reader.consume(); // consume 'l'
        Assert.assertFalse(reader.matchesAny('x', 'z'));

        // matchesAny should return true if the current sequence of characters matches any of the specified characters
        reader.mark();
        reader.consume(); // consume 'H'
        reader.consume(); // consume 'e'
        reader.consume(); // consume 'l'
        reader.consume(); // consume 'l'
        reader.consume(); // consume 'o'
        reader.rewindToMark();
        Assert.assertTrue(reader.matchesAnySorted('l', 'o'));

        // matchesLetter should return true if the current character is a letter
        Assert.assertTrue(reader.matchesLetter());

        // matchesLetter should return false if the current character is not a letter
        Assert.assertFalse(reader.matchesDigit());

        // matchConsume should consume the current sequence of characters if it matches the specified sequence
        reader.mark();
        reader.consume(); // consume 'H'
        reader.consume(); // consume 'e'
        reader.consume(); // consume 'l'
        reader.consume(); // consume 'l'
        reader.consume(); // consume 'o'
        Assert.assertEquals("hell", reader.consumedString());
        Assert.assertEquals(5, reader.pos());
        reader.rewindToMark();

        // matchConsume should return false if the current sequence of characters does not match the specified sequence
        Assert.assertFalse(reader.matchConsume("llz"));

        // containsIgnoreCase should return true if the specified sequence of characters is present in the string ignoring case
        reader = new CharacterReader(new StringReader("<html><head><title>Hello World</title></head><body>"));
        Assert.assertTrue(reader.containsIgnoreCase("hello"));

        // containsIgnoreCase should return false if the specified sequence of characters is not present in the string ignoring case
        Assert.assertFalse(reader.containsIgnoreCase("hello2"));

        // test binary detection
        reader = new CharacterReader(new StringReader("\u0000Hello World\u0000"));
        Assert.assertTrue(reader.isBinary());

        // test toString
        Assert.assertEquals("Hello World", reader.toString());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.bufferUp();
        reader.reader.close();
        reader.consume();
    }
}