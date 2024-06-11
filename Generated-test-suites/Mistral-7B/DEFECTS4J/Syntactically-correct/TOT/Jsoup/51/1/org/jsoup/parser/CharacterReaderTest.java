package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * CharacterReaderTest class contains JUnit test cases for CharacterReader class.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmptyString() {
        CharacterReader characterReader = new CharacterReader("");
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeSingleCharacter() {
        CharacterReader characterReader = new CharacterReader("abc");
        characterReader.consume();
        Assert.assertNotEquals('a', characterReader.current());
        Assert.assertEquals('b', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeMultipleCharacters() {
        CharacterReader characterReader = new CharacterReader("abc");
        characterReader.consume(2);
        Assert.assertNotEquals('b', characterReader.current());
        Assert.assertEquals('c', characterReader.current());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderNullInput() {
        CharacterReader characterReader = new CharacterReader(null);
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader characterReader = new CharacterReader("abcdef");
        characterReader.mark();
        characterReader.consume(2);
        characterReader.rewindToMark();
        Assert.assertEquals('a', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeToCharacter() {
        CharacterReader characterReader = new CharacterReader("abcdef");
        String consumed = characterReader.consumeTo('d');
        Assert.assertEquals("abc", consumed);
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeToCharSequence() {
        CharacterReader characterReader = new CharacterReader("abcdef");
        String consumed = characterReader.consumeTo("de");
        Assert.assertEquals("de", consumed);
        Assert.assertEquals('f', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAnySingleCharacter() {
        CharacterReader characterReader = new CharacterReader("abcdabcd");
        String consumed = characterReader.consumeToAny('a');
        Assert.assertEquals("a", consumed);
        Assert.assertEquals('b', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAnyMultipleCharacters() {
        CharacterReader characterReader = new CharacterReader("abcdabcd");
        String consumed = characterReader.consumeToAny(new char[]{'b', 'c'});
        Assert.assertEquals("bc", consumed);
        Assert.assertEquals('d', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader("abcdabcd");
        String consumed = characterReader.consumeToAnySorted(new char[]{'b', 'd'});
        Assert.assertEquals("bd", consumed);
        Assert.assertEquals('a', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader characterReader = new CharacterReader("&amp;&lt;&#0;");
        String consumed = characterReader.consumeData();
        Assert.assertEquals("&amp;&lt;&#0;", consumed);
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader characterReader = new CharacterReader("<title>Test</title>");
        String consumed = characterReader.consumeTagName();
        Assert.assertEquals("title", consumed);
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader("<title>Test</title>");
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("<title>Test</title>", consumed);
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader("AbCdEfGhIj");
        String consumed
 = characterReader.consumeLetterSequence();
 Assert.assertEquals("AbCdEfGhIj", consumed);
 Assert.assertEquals(CharacterReader.EOF, characterReader.current());
 }

 @Test
 public void testCharacterReaderConsumeLetterThenDigitSequence() {
 CharacterReader characterReader = new CharacterReader("AbCdE1fGhIj12");
 String consumed = characterReader.consumeLetterThenDigitSequence();
 Assert.assertEquals("AbCdE1fGhIj12", consumed);
 Assert.assertEquals(CharacterReader.EOF, characterReader.current());
 }

 @Test
 public void testCharacterReaderConsumeHexSequence() {
 CharacterReader characterReader = new CharacterReader("&#x61; &#x62; &#x63;");
 String consumed = characterReader.consumeHexSequence();
 Assert.assertEquals("&#x61; &#x62; &#x63;", consumed);
 Assert.assertEquals(CharacterReader.EOF, characterReader.current());
 }

 @Test
 public void testCharacterReaderConsumeDigitSequence() {
 CharacterReader characterReader = new CharacterReader("1234567890");
 String consumed = characterReader.consumeDigitSequence();
 Assert.assertEquals("1234567890", consumed);
 Assert.assertEquals(CharacterReader.EOF, characterReader.current());
 }

 @Test
 public void testCharacterReaderMatches() {
 CharacterReader characterReader = new CharacterReader("abc");
 Assert.assertTrue(characterReader.matches('a'));
 Assert.assertTrue(characterReader.matches("a"));
 Assert.assertFalse(characterReader.matches('b'));
 Assert.assertFalse(characterReader.matches("b"));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCase() {
 CharacterReader characterReader = new CharacterReader("ABC");
 Assert.assertTrue(characterReader.matchesIgnoreCase("A"));
 Assert.assertTrue(characterReader.matchesIgnoreCase("a"));
 Assert.assertFalse(characterReader.matchesIgnoreCase("B"));
 Assert.assertFalse(characterReader.matchesIgnoreCase("b"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 CharacterReader characterReader = new CharacterReader("abcd");
 Assert.assertTrue(characterReader.matchesAny('a', 'b'));
 Assert.assertTrue(characterReader.matchesAny('b', 'a'));
 Assert.assertFalse(characterReader.matchesAny('e', 'a'));
 }

 @Test
 public void testCharacterReaderMatchesAnySorted() {
 CharacterReader characterReader = new CharacterReader("abcd");
 Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'b', 'a'}));
 Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'a', 'b'}));
 Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'e', 'a'}));
 }

 @Test
 public void testCharacterReaderMatchesLetter() {
 CharacterReader characterReader = new CharacterReader("AbCdEfGhIj");
 Assert.assertTrue(characterReader.matchesLetter());
 Assert.assertFalse(characterReader.matchesLetter());
 }

 @Test
 public void testCharacterReaderMatchesDigit() {
 CharacterReader characterReader = new CharacterReader("1234567890");
 Assert.assertTrue(characterReader.matchesDigit());
 Assert.assertFalse(characterReader.matchesDigit());
 }
}