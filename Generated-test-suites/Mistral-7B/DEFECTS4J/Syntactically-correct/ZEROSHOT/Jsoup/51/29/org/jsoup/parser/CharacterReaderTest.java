package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.CharacterReader.EOF;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderInstance() {
        String input = "This is a test for CharacterReader class.";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("This is a test for CharacterReader class.", reader.toString());
        assertEquals(0, reader.pos());
        assertEquals(false, reader.isEmpty());
    }

    @Test
    public void testCurrentAndConsume() {
        String input = "This is a test for CharacterReader class.";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('T', reader.current());
        assertEquals('T', reader.consume());
        assertEquals('i', reader.current());
        assertEquals(1, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals(EOF, reader.consume());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testMarkAndRewindToMark() {
        String input = "This is a test for CharacterReader class.";
        CharacterReader reader = new CharacterReader(input);
        reader.mark();
        assertEquals('T', reader.consume());
        reader.rewindToMark();
        assertEquals('T', reader.current());
        assertEquals(1, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        String input = "This is a test for CharacterReader class.";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("is a test for ", reader.consumeAsString());
        assertEquals(5, reader.pos());
        assertEquals('f', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        String input = "This is a test for CharacterReader class.";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(4, reader.nextIndexOf('s'));
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(-1, reader.nextIndexOf(Character.toString('z')));
    }

    @Test
    public void testConsumeTo() {
        String input = "This is a test for CharacterReader class.";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("for CharacterReader ", reader.consumeTo(' '));
        assertEquals(11, reader.pos());
        assertEquals('c', reader.current());
        assertEquals("class.", reader.consumeToEnd());
        assertEquals(23, reader.pos());
        assertEquals(EOF, reader.current());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        String input = "This is a test for CharacterReader class.<html>\n<head>\n<title>TestTitle</title>\n</head>\n<body>";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("for CharacterReader ", reader.consumeToAny(' '));
        assertEquals("class.<html>\n<head>\n<title>TestTitle</title>\n</head>\n<body>", reader.toString());
        assertEquals(23, reader.pos());
        assertEquals('<', reader.current());
    }

    @Test
    public void testConsumeToAnySorted() {
        String input = "This is a test for CharacterReader class.<html>\n<head>\n<title>TestTitle</title>\n</head>\n<body>";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("for CharacterReader ", reader.consumeToAnySorted(' '));
        assertEquals("class.<html>\n<head>\n<title>TestTitle</title>\n</head>\n<body>", reader.toString());
        assertEquals(23, reader.pos());
        assertEquals('<', reader.current());
    }

    @Test
    public void testConsumeData() {
        String input = "This is a test for CharacterReader class.<html>\n<head>\n<title>TestTitle</title>\n</head>\n<body>";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("This is a test for CharacterReader class.<html>\n<head>\n<title>TestTitle</title>\n</head>\n<body>", reader.consumeData());
        assertEquals(23, reader.pos());
        assertEquals(EOF, reader.current());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        String input = "<html>\n<head>\n<title>TestTitle</title>\n</head>\n<body>";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("html>", reader.consumeTagName());
        assertEquals(6, reader.pos());
        assertEquals('/', reader.current());
    }

    @Test
    public void testConsumeToEnd() {
        String input = "<html>\n<head>\n<title>TestTitle</title>\n</head>\n<body>";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("</body>", reader.consumeToEnd());
        assertEquals(31, reader.pos());
        assertEquals(EOF, reader.current());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "This is a test for CharacterReader class.";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("is a test for ", reader.consumeLetterSequence());
        assertEquals(11, reader.pos());
        assertEquals('f', reader.current());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String input = "This is a test for CharacterReader class.";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("is a test for ", reader.consumeLetterThenDigitSequence());
        assertEquals(11, reader.pos());
        assertEquals('f', reader.current());
        assertEquals("class.", reader.consumeLetterThenDigitSequence());
        assertEquals(23, reader.pos());
        assertEquals(EOF, reader.current());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "This is a test for CharacterReader class.#ff00ff";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("#ff00ff", reader.consumeHexSequence());
        assertEquals(13, reader.pos());
        assertEquals(EOF, reader.current());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "This is a test for CharacterReader class.12345";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("12345", reader.consumeDigitSequence());
        assertEquals(8, reader.pos());
        assertEquals('a', reader.current());
    }

    @Test
    public void testMatches() {
        String input = "This is a test for CharacterReader class.";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matches('i'));
        assertTrue(reader.matches("is"));
        assertFalse(reader.matches('z'));
        assertFalse(reader.matches("test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "This is a test for CharacterReader class.";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesIgnoreCase("is"));
        assertTrue(reader.matchesIgnoreCase("test"));
        assertFalse(reader.matchesIgnoreCase("z"));
    }

    @Test
    public void testMatchesAny() {
        String input = "This is a test for CharacterReader class.";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesAny('s', 't'));
        assertTrue(reader.matchesAny('i', 's'));
        assertFalse(reader.matchesAny('z'));
        assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testMatchesAnySorted() {
        String input = "This is a test for CharacterReader class.";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesAnySorted('s', 't'));
        assertTrue(reader.matchesAnySorted('i', 's'));
        assertFalse(reader.matchesAnySorted('z'));
        assertFalse(reader.matchesAnySorted('x'));
    }

    @Test
    public void testMatchesLetter() {
        String input = "This is a test for CharacterReader class.";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        String input = "This is a test for CharacterReader class.12345";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String input = "<html>\n<head>\n<title>TestTitle</title>\n</head>\n<body>";
        CharacterReader reader = new CharacterReader(input);
        String expected = "</body>";
        reader.mark();
        assertTrue(reader.matchConsume(expected));
        assertEquals(31, reader.pos());
        assertEquals(EOF, reader.current());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "<html>\n<head>\n<title>TestTitle</title>\n</head>\n<body>";
        CharacterReader reader = new CharacterReader(input);
        String expected = "</body>";
        assertTrue(reader.matchConsumeIgnoreCase(expected));
        assertEquals(31, reader.pos());
        assertEquals(EOF, reader.current());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "<html>\n<head>\n<title>TestTitle</title>\n</head>\n<body>";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("title"));
        assertFalse(reader.containsIgnoreCase("/head"));
        assertFalse(reader.containsIgnoreCase("head"));
    }

    @Test
    public void testToString() {
        String input = "<html>\n<head>\n<title>TestTitle</title>\n</head>\n<body>";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("</body>", reader.toString());
    }
}