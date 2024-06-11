package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.nextIndexOf('a'));
        Assert.assertEquals(-1, reader.nextIndexOf("abcd"));
        Assert.assertEquals("", reader.consumeTo('a'));
        Assert.assertEquals("", reader.consumeTo("abcd"));
        Assert.assertEquals("", reader.consumeToAny('a', 'b'));
        Assert.assertEquals("", reader.consumeToEnd());
        Assert.assertEquals("", reader.consumeLetterSequence());
        Assert.assertEquals("", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("", reader.consumeHexSequence());
        Assert.assertEquals("", reader.consumeDigitSequence());
        Assert.assertFalse(reader.matches('a'));
        Assert.assertFalse(reader.matches("abcd"));
        Assert.assertFalse(reader.matchesAny('b', 'c'));
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchConsume("abcd"));
        Assert.assertFalse(reader.matchConsumeIgnoreCase("AbCd"));
        Assert.assertFalse(reader.containsIgnoreCase("AbCd"));
        Assert.assertEquals("", reader.toString());
    }

    @Test
    public void testSingleCharacterReader() {
        CharacterReader reader = new CharacterReader("a");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.nextIndexOf('a'));
        Assert.assertEquals("a", reader.consumeAsString());
        Assert.assertEquals("a", reader.consumeTo('a').trim());
        Assert.assertEquals("a", reader.consumeTo("a").trim());
        Assert.assertEquals("a", reader.consumeToAny('a').trim());
        Assert.assertEquals("a", reader.consumeToEnd().trim());
        Assert.assertEquals("a", reader.consumeLetterSequence().trim());
        Assert.assertEquals("a", reader.consumeLetterThenDigitSequence().trim());
        Assert.assertEquals("a", reader.consumeHexSequence().trim());
        Assert.assertEquals("a", reader.consumeDigitSequence().trim());
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("a"));
        Assert.assertTrue(reader.matchesAny('a'));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchConsume("a"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("a"));
        Assert.assertFalse(reader.containsIgnoreCase("AbCd"));
        Assert.assertEquals("a", reader.toString().trim());
    }

    @Test
    public void testMultipleCharactersReader() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.nextIndexOf('a'));
        Assert.assertEquals("a", reader.consumeAsString());
        Assert.assertEquals("a", reader.consumeTo('a').trim());
        Assert.assertEquals("a", reader.consumeTo("a").trim());
        Assert.assertEquals("a", reader.consumeToAny('a').trim());
        Assert.assertEquals("bc", reader.consumeToEnd().trim());
        Assert.assertEquals("a", reader.consumeLetterSequence().trim());
        Assert.assertEquals("ab", reader.consumeLetterThenDigitSequence().trim());
        Assert.assertEquals("01", reader.consumeHexSequence().trim());
        Assert.assertEquals("1", reader.consumeDigitSequence().trim());
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("a"));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertTrue(reader.matches("b"));
        Assert.assertTrue(reader.matches('c'));
        Assert.assertTrue(reader.matches("c"));
        Assert.assertTrue(reader.matchesAny('a'));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchConsume("a"));
        Assert.assertTrue(reader.matchConsume("b"));
        Assert.assertTrue(reader.matchConsume("c"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("a"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("b"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("c"));
        Assert.assertFalse(reader.containsIgnoreCase("AbCd"));
        Assert.assertEquals("abc", reader.toString().trim());
    }

    @Test
    public void testEdgeCases() {
        CharacterReader reader = new CharacterReader("\u0000");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.nextIndexOf('\u0001'));
        Assert.assertEquals("", reader.consumeTo('\u0001').trim());
        Assert.assertEquals("", reader.consumeTo("\u0001").trim());
        Assert.assertEquals("", reader.consumeToAny('\u0001').trim());
        Assert.assertEquals("", reader.consumeToEnd().trim());
        Assert.assertEquals("", reader.consumeLetterSequence().trim());
        Assert.assertEquals("", reader.consumeLetterThenDigitSequence().trim());
        Assert.assertEquals("", reader.consumeHexSequence().trim());
        Assert.assertEquals("", reader.consumeDigitSequence().trim());
        Assert.assertFalse(reader.matches('\u0001'));
        Assert.assertFalse(reader.matches("\u0001"));
        Assert.assertFalse(reader.matchesAny('\u0001'));
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchConsume("\u0001"));
        Assert.assertFalse(reader.matchConsumeIgnoreCase("\u0001"));
        Assert.assertFalse(reader.containsIgnoreCase("\u0001AbCd"));
        Assert.assertEquals("", reader.toString().trim());

        reader = new CharacterReader("\u007F\u0080");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.nextIndexOf('\u007F'));
        Assert.assertEquals("\u007F", reader.consumeAsString());
        Assert.assertEquals("\u007F", reader.consumeTo('\u007F').trim());
        Assert.assertEquals("\u007F", reader.consumeTo("\u007F").trim());
        Assert.assertEquals("\u007F", reader.consumeToAny('\u007F').trim());
        Assert.assertEquals("\u0080", reader.consumeToEnd().trim());
        Assert.assertEquals("\u007F", reader.consumeLetterSequence().trim());
        Assert.assertEquals("\u007F", reader.consumeLetterThenDigitSequence().trim());
        Assert.assertEquals("", reader.consumeHexSequence().trim());
        Assert.assertEquals("", reader.consumeDigitSequence().trim());
        Assert.assertTrue(reader.matches('\u007F'));
        Assert.assertTrue(reader.matches("\u007F"));
        Assert.assertTrue(reader.matchesAny('\u007F'));
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchConsume("\u007F"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("\u007F"));
        Assert.assertFalse(reader.containsIgnoreCase("\u007FAbCd"));
        Assert.assertEquals("\u007F\u0080", reader.toString().trim());
    }

    @Test
    public void testErrorCases() {
        CharacterReader reader = new CharacterReader(null);
        Assert.assertNull(reader);

        reader = new CharacterReader("");
        try {
            reader.consume();
        } catch (NullPointerException e) {
            // Expected
        }

        reader = new CharacterReader("123");
        try {
            reader.consumeLetterThenDigitSequence();
            reader.consumeLetterSequence();
            reader.consumeHexSequence();
            reader.consumeDigitSequence();
            reader.consumeLetter();
            reader.consumeDigit();
            reader.consume();
            reader.nextIndexOf('z');
            reader.nextIndexOf("xyz");
            reader.matchConsume("xyz");
            reader.matchConsumeIgnoreCase("XYZ");
            reader.matchConsumeAny('z');
            reader.matchConsumeLetter();
            reader.matchConsumeDigit();
            reader.matchAny('z');
            reader.matchesLetter();
            reader.matchesDigit();
            reader.matchConsume("");
            reader.matchConsumeIgnoreCase("");
            reader.containsIgnoreCase("xyz");
            reader.toString();
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testComplexCases() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test Title</title></head><body>Test Body</body></html>");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.nextIndexOf('<'));
        Assert.assertEquals("<", reader.consumeAsString());
        Assert.assertEquals("<", reader.consumeTo('<').trim());
        Assert.assertEquals("<", reader.consumeTo("<").trim());
        Assert.assertEquals("<", reader.consumeToAny('<').trim());
        Assert.assertEquals("html", reader.consumeToEnd().substring(0, 5).trim());
        Assert.assertEquals("<", reader.consumeLetterSequence().trim());
        Assert.assertEquals("<title>", reader.consumeLetterThenDigitSequence().trim());
        Assert.assertEquals("Test", reader.consumeHexSequence().trim());
        Assert.assertEquals("T", reader.consumeDigitSequence().trim());
        Assert.assertTrue(reader.matches('<'));
        Assert.assertTrue(reader.matches("<"));
        Assert.assertTrue(reader.matchesAny('<'));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchConsume("<"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("<"));
        Assert.assertFalse(reader.containsIgnoreCase("<xyz>"));
        Assert.assertEquals("<html><head><title>Test Title</title></head><body>Test Body</body></html>", reader.toString().trim());
    }
}