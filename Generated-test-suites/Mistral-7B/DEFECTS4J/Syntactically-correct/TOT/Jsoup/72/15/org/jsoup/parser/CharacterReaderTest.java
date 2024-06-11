package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Testing CharacterReader class.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Test default constructor with String input
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", reader.toString());

        // Test advanced constructor with Reader input
        Reader readerInput = new StringReader("Java is a programming language");
        CharacterReader readerAdvanced = new CharacterReader(readerInput, 1024);
        Assert.assertEquals("Java is a programming language", readerAdvanced.toString());

        // Test methods: pos(), isEmpty(), current(), consume(), unconsume(), advance(), mark(), rewindToMark()
        readerAdvanced.mark();
        readerAdvanced.advance();
        readerAdvanced.unconsume();
        readerAdvance.consume();
        readerAdvanced.advance();
        readerAdvanced.mark();
        readerAdvanced.rewindToMark();

        Assert.assertEquals(6, readerAdvanced.pos());
        Assert.assertFalse(readerAdvanced.isEmpty());
        Assert.assertEquals('a', readerAdvanced.current());
        Assert.assertEquals('a', readerAdvanced.consume());
        Assert.assertEquals('i', readerAdvanced.consume());
        Assert.assertEquals('s', readerAdvanced.consume());
        Assert.assertEquals(' ', readerAdvanced.consume());

        // Test methods: nextIndexOf(), nextIndexOf(CharSequence)
        readerAdvanced.reset();
        readerAdvanced.mark();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();

        Assert.assertEquals(13, readerAdvanced.nextIndexOf('s'));
        Assert.assertEquals(17, readerAdvanced.nextIndexOf("is a"));

        // Test methods: consumeTo(), consumeTo(CharSequence), consumeToAny(), consumeToAnySorted(), consumeData(), consumeTagName()
        readerAdvanced.reset();
        readerAdvanced.mark();

        String consumed = readerAdvanced.consumeTo('a');
        Assert.assertEquals("Java is a ", consumed);

        consumed = readerAdvanced.consumeTo("Java");
        Assert.assertEquals("Java is a programming language", consumed);

        consumed = readerAdvanced.consumeToAny(new char[]{'a', 'z'});
        Assert.assertEquals("Java is a programming language", consumed);

        consumed = readerAdvanced.consumeToAnySorted(new char[]{'z', 'a'});
        Assert.assertEquals("Java is a programming language", consumed);

        consumed = readerAdvanced.consumeData();
        Assert.assertEquals("Java is a programming language", consumed);

        consumed = readerAdvanced.consumeTagName();
        Assert.assertEquals("Java", consumed);

        // Test methods: consumeToEnd(), consumeLetterSequence(), consumeLetterThenDigitSequence(), consumeHexSequence(), consumeDigitSequence()
        readerAdvanced.reset();
        readerAdvanced.mark();

        consumed = readerAdvanced.consumeToEnd();
        Assert.assertEquals("Java is a programming language", consumed);

        consumed = readerAdvanced.consumeLetterSequence();
        Assert.assertEquals("Java", consumed);

        consumed = readerAdvanced.consumeLetterThenDigitSequence();
        Assert.assertEquals("Java is a programming language", consumed);

        consumed = readerAdvanced.consumeHexSequence();
        Assert.assertEquals("Java is a programming language", consumed);

        consumed = readerAdvanced.consumeDigitSequence();
        Assert.assertEquals("Java is a programming language", consumed);

        // Test methods: matches(), matches(CharSequence), matchesIgnoreCase(), matchesAny(), matchesAnySorted()
        readerAdvanced.reset();
        readerAdvanced.mark();

        Assert.assertTrue(readerAdvanced.matches('a'));
        Assert.assertTrue(readerAdvanced.matches("Java"));
        Assert.assertTrue(readerAdvanced.matchesIgnoreCase("is a programming language"));
        Assert.assertTrue(readerAdvanced.matchesAny(new char[]{'a', 'z'}));
        Assert.assertTrue(readerAdvanced.matchesAnySorted(new char[]{'z', 'a'}));

        // Test methods: matchesLetter(), matchesDigit()
        Assert.assertTrue(readerAdvanced.matchesLetter());
        Assert.assertTrue(readerAdvanced.matchesDigit());

        // Test methods: matchConsume(), matchConsumeIgnoreCase()
        readerAdvanced.reset();
        readerAdvanced.mark();

        Assert.assertTrue(readerAdvanced.matchConsume("Java"));
        readerAdvanced.mark();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();
        readerAdvanced.advance();

        Assert.assertTrue(readerAdvanced.matchConsumeIgnoreCase("is a programming language"));

        // Test toString()
        Assert.assertEquals("Java is a programming language", readerAdvanced.toString());

        // Test cacheString() method
        readerAdvanced.reset();
        readerAdvanced.mark();

        String str1 = readerAdvanced.consumeLetterSequence();
        String str2 = readerAdvanced.consumeLetterSequence();

        Assert.assertEquals(str1, str2);

        readerAdvanced.reset();
        readerAdvanced.mark();

        String str3 = readerAdvanced.consumeLetterSequence();
        String str4 = readerAdvanced.consumeLetterSequence();

        Assert.assertEquals(str3, str4);

        // Test containsIgnoreCase()
        readerAdvanced.reset();
        readerAdvanced.mark();

        Assert.assertTrue(readerAdvanced.containsIgnoreCase("/title"));
        Assert.assertTrue(readerAdvanced.containsIgnoreCase("/STYLE"));

        // Test edge cases and error scenarios
        readerAdvanced.reset();
        readerAdvanced.mark();

        try {
            readerAdvanced.consumeTo((char) 0);
            Assert.fail();
        } catch (Exception e) {
            // expected
        }

        readerAdvanced.reset();
        readerAdvanced.mark();

        try {
            readerAdvanced.consumeToAny(null);
            Assert.fail();
        } catch (Exception e) {
            // expected
        }

        readerAdvanced.reset();
        readerAdvanced.mark();

        try {
            readerAdvanced.consumeToAny(new char[0]);
            Assert.fail();
        } catch (Exception e) {
            // expected
        }

        readerAdvanced.reset();
        readerAdvanced.mark();

        try {
            readerAdvanced.matchConsume(null);
            Assert.fail();
        } catch (Exception e) {
            // expected
        }

        readerAdvanced.reset();
        readerAdvanced.mark();

        try {
            readerAdvanced.matchConsumeIgnoreCase(null);
            Assert.fail();
        } catch (Exception e) {
            // expected
        }

        readerAdvanced.reset();
        readerAdvanced.mark();

        try {
            readerAdvanced.containsIgnoreCase(null);
            Assert.fail();
        } catch (Exception e) {
            // expected
        }
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader characterReader = new CharacterReader(new IOException("Test IOException"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        CharacterReader characterReader = new CharacterReader(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        characterReader.consume(); // consume the 'd' character
        characterReader.mark(); // mark the current position
        characterReader.rewindToMark(); // rewind to the marked position
        characterReader.consume(); // try to consume the 'd' character again
    }

    @Test
    public void testHashCollision() {
        CharacterReader characterReader = new CharacterReader("testHashCollision");
        String str1 = characterReader.consumeLetterSequence();
        String str2 = characterReader.consumeLetterSequence();

        Assert.assertNotEquals(str1, str2);

        characterReader.reset();
        String str3 = characterReader.consumeLetterSequence();
        String str4 = characterReader.consumeLetterSequence();

        Assert.assertEquals(str1, str3);
        Assert.assertEquals(str1, str4);
    }

    @Test
    public void testReaderLimit() {
        StringReader reader = new StringReader("");
        CharacterReader characterReader = new CharacterReader(reader, 1);

        Assert.assertEquals(0, characterReader.pos());
        Assert.assertTrue(characterReader.isEmpty());
        Assert.assertEquals(0, characterReader.nextIndexOf('\n'));
        Assert.assertEquals(0, characterReader.nextIndexOf(" "));
    }
}