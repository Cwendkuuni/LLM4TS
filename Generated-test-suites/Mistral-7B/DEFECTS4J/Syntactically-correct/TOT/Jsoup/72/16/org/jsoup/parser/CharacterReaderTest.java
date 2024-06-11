package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * CharacterReaderTest class for testing CharacterReader functionalities.
 */
public class CharacterReaderTest {

    /**
     * Testing constructor with Reader input.
     */
    @Test
    public void testCharacterReaderWithReaderInput() {
        String testInput = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(testInput));
        Assert.assertEquals("Hello World!", characterReader.toString());
        characterReader.consumeToAnySorted('l', 'o');
        Assert.assertEquals("World!", characterReader.toString());
        characterReader.rewindToMark();
        characterReader.consumeLetterSequence();
        Assert.assertEquals("ello", characterReader.toString());
    }

    /**
     * Testing constructor with String input.
     */
    @Test
    public void testCharacterReaderWithStringInput() {
        String testInput = "Hello World!";
        CharacterReader characterReader = new CharacterReader(testInput);
        Assert.assertEquals("Hello World!", characterReader.toString());
        characterReader.consumeToAnySorted('l', 'o');
        Assert.assertEquals("World!", characterReader.toString());
        characterReader.rewindToMark();
        characterReader.consumeLetterSequence();
        Assert.assertEquals("ello", characterReader.toString());
    }

    /**
     * Testing isEmpty method.
     */
    @Test
    public void testIsEmpty() {
        CharacterReader characterReaderEmpty = new CharacterReader(new StringReader(""));
        CharacterReader characterReaderNonEmpty = new CharacterReader(new StringReader("Hello!"));

        Assert.assertTrue(characterReaderEmpty.isEmpty());
        Assert.assertFalse(characterReaderNonEmpty.isEmpty());
    }

    /**
     * Testing pos method.
     */
    @Test
    public void testPos() {
        String testInput = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(testInput));

        Assert.assertEquals(0, characterReader.pos());
        characterReader.consume();
        Assert.assertEquals(1, characterReader.pos());
        characterReader.consume(3);
        Assert.assertEquals(4, characterReader.pos());
    }

    /**
     * Testing consume method.
     */
    @Test
    public void testConsume() {
        String testInput = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(testInput));

        Assert.assertEquals('H', characterReader.consume());
        Assert.assertEquals('e', characterReader.consume());
        Assert.assertEquals('l', characterReader.consume());
        Assert.assertEquals('l', characterReader.consume());
        Assert.assertEquals('o', characterReader.consume());
        Assert.assertEquals(' ', characterReader.consume());
        Assert.assertEquals('W', characterReader.consume());
        Assert.assertEquals('o', characterReader.consume());
        Assert.assertEquals('r', characterReader.consume());
        Assert.assertEquals('l', characterReader.consume());
        Assert.assertEquals('d', characterReader.consume());
        Assert.assertEquals('!', characterReader.consume());
        Assert.assertTrue(characterReader.isEmpty());
    }

    /**
     * Testing mark and rewindToMark methods.
     */
    @Test
    public void testMarkAndRewindToMark() throws IOException {
        String testInput = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(testInput));

        characterReader.mark();
        Assert.assertEquals('H', characterReader.consume());
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());

        characterReader.consume();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
    }

    /**
     * Testing nextIndexOf method with single character.
     */
    @Test
    public void testNextIndexOfSingleCharacter() {
        String testInput = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(testInput));

        Assert.assertEquals(6, characterReader.nextIndexOf(' '));
        Assert.assertEquals(13, characterReader.nextIndexOf('!'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('Z'));
    }

    /**
     * Testing nextIndexOf method with CharSequence.
     */
    @Test
    public void testNextIndexOfCharSequence() {
        String testInput = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(testInput));

        Assert.assertEquals(6, characterReader.nextIndexOf(" "));
        Assert.assertEquals(13, characterReader.nextIndexOf("!"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("Zoo"));
    }

    /**
     * Testing consumeTo
     */
    @Test
    public void testConsumeTo() {
        String testInput = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader characterReader = new CharacterReader(new StringReader(testInput));

        Assert.assertEquals("a", characterReader.consumeTo('a').toString());
        Assert.assertEquals("bcde", characterReader.consumeTo("bcde").toString());
        Assert.assertEquals("fghijklmnopqrstuvwxyz", characterReader.consumeToEnd().toString());
    }

    /**
     * Testing consumeToAny
     */
    @Test
    public void testConsumeToAny() {
        String testInput = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        CharacterReader characterReader = new CharacterReader(new StringReader(testInput));

        Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", characterReader.consumeToAnySorted('a', 'z', 'A', 'Z').toString());
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", characterReader.consumeToAnySorted(new char[]{'a', 'z', 'A', 'Z'}).toString());
    }

    /**
     * Testing consumeLetterSequence
     */
    @Test
    public void testConsumeLetterSequence() {
        String testInput = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        CharacterReader characterReader = new CharacterReader(new StringReader(testInput));

        Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", characterReader.consumeLetterSequence().toString());
        Assert.assertEquals("ab", characterReader.consumeLetterThenDigitSequence().toString());
        Assert.assertEquals("123", characterReader.consumeDigitSequence().toString());
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", characterReader.consumeData().toString());
        Assert.assertEquals("<title>", characterReader.consumeTagName().toString());
    }

    /**
     * Testing matches methods.
     */
    @Test
    public void testMatchesMethods() {
        String testInput = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        CharacterReader characterReader = new CharacterReader(new StringReader(testInput));

        Assert.assertTrue(characterReader.matches('a'));
        Assert.assertTrue(characterReader.matches("ab"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("Ab"));
        Assert.assertTrue(characterReader.matchesAny('a', 'b'));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'b', 'a'}));
        Assert.assertFalse(characterReader.matches('z'));
        Assert.assertFalse(characterReader.matches("Z"));
    }

    /**
     * Testing containsIgnoreCase method.
     */
    @Test
    public void testContainsIgnoreCase() {
        String testInput = "abcdefghijklmnopqrstuvwxyz<title>Hello<style>";
        CharacterReader characterReader = new CharacterReader(new StringReader(testInput));

        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/STYLE"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/Header"));
    }
}