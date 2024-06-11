package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        String input = "HelloWorld";
        CharacterReader reader = new CharacterReader(input);

        // Test basic functionality
        assertEquals(0, reader.pos());
        assertEquals(11, reader.length());
        assertFalse(reader.isEmpty());
        assertEquals('H', reader.current());
        assertEquals('H', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('e', reader.current());
        assertEquals(1, reader.nextIndexOf('e'));
        assertEquals("e", reader.consumeAsString());
        assertEquals(2, reader.pos());
        assertEquals(-1, reader.nextIndexOf('z'));

        // Test mark and rewind functionality
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(1, reader.pos());

        // Test consumeTo functionality
        assertEquals("llo", reader.consumeTo('l'));
        assertEquals("ld", reader.consumeTo('d'));
        assertEquals("ld", reader.consumeToEnd());

        // Test consumeToString functionality
        assertEquals("llo", reader.consumeTo('l').trim());
        assertEquals("ld", reader.consumeTo('d').trim());
        assertEquals("ld", reader.consumeToEnd().trim());

        // Test consumeToAny functionality
        assertEquals("ld", reader.consumeToAny('l', 'd'));
        assertEquals("ld", reader.consumeToAnySorted('l', 'd'));
        assertEquals("", reader.consumeToAny('z'));

        // Test consumeData functionality
        assertEquals("HelloWorld", reader.consumeData());

        // Test consumeTagName functionality
        assertEquals("title", reader.consumeTagName()); // should be replaced with actual test case

        // Test other functionality
        assertEquals("", reader.consumeToAny());
        assertEquals("", reader.consumeLetterSequence());
        assertEquals("123", reader.consumeLetterThenDigitSequence());
        assertEquals("1a2b3c", reader.consumeHexSequence());
        assertEquals("123", reader.consumeDigitSequence());

        // Test matching functionality
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches("ll"));
        assertFalse(reader.matches("oo"));
        assertFalse(reader.matches("LL"));
        assertTrue(reader.matchesAny('l', 'd'));
        assertTrue(reader.matchesAnySorted('d', 'l'));
        assertFalse(reader.matchesAny('z'));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesLetter(), reader.matchesDigit()); // should not be able to match both at once

        // Test consuming matching functionality
        assertEquals("ll", reader.matchConsume("ll"));
        assertEquals("ld", reader.matchConsume("ld"));
        assertEquals("", reader.matchConsume("oo"));
        assertEquals("d", reader.matchConsume("d"));

        // Test containsIgnoreCase functionality
        assertTrue(reader.containsIgnoreCase("title"));
        assertFalse(reader.containsIgnoreCase("style"));

        // Test toString functionality
        assertEquals("llo", reader.toString());
    }
}