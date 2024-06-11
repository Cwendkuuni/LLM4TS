package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        String input = "abcdefghijk&lmnopqrst<uvwxyzABCDEFGHIJKLMNOPQRST>1234567890";
        CharacterReader reader = new CharacterReader(input);

        // test basic methods
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        assertEquals(EOF, reader.current());
        assertEquals(EOF, reader.consume());

        // test position and mark methods
        reader.mark();
        reader.advance();
        reader.unconsume();
        reader.rewindToMark();
        assertEquals(0, reader.pos());

        // test consumeAsString method
        assertEquals("a", reader.consumeAsString());
        assertEquals("bcdefghijk&lmnopqrst<uvwxyzABCDEFGHIJKLMNOPQRST>1234567890", reader.toString());

        // test nextIndexOf method with single character
        assertEquals(7, reader.nextIndexOf('b'));
        assertEquals(-1, reader.nextIndexOf('z'));

        // test nextIndexOf method with CharSequence
        CharSequence seq = "efghij";
        assertEquals(5, reader.nextIndexOf(seq));
        assertEquals(-1, reader.nextIndexOf("xyz"));

        // test consumeTo method with single character
        assertEquals("bcdefghijk&", reader.consumeTo('&'));

        // test consumeTo method with CharSequence
        assertEquals("efghijklmopqrst<uvwxyzABCDEFGHIJKLMNOPQRST>", reader.consumeTo(seq));

        // test consumeToAny method
        assertEquals("abcdefghijk&lmnopqrst<uvwxyzABCDEFGHIJKLMNOPQRST>1234567890", reader.consumeToAny(',', ';', '!', '?'));

        // test consumeToAnySorted method
        assertEquals("1234567890ABCDEFGHIJKLMNOPQRSTuvwxyz", reader.consumeToAnySorted('0', '9', 'A', 'Z'));

        // test consumeData method
        assertEquals("&amp;", reader.consumeData());

        // test consumeTagName method
        assertEquals("title", reader.consumeTagName());
        assertEquals("", reader.consumeTagName());

        // test consumeToEnd method
        assertEquals("ABCDEFGHIJKLMNOPQRST>1234567890", reader.consumeToEnd());

        // test consumeLetterSequence method
        assertEquals("defghijk&lmnopqrst<uvwxyzABCDEFGHIJKLMNOPQRST", reader.consumeLetterSequence());

        // test consumeLetterThenDigitSequence method
        assertEquals("defghijklmopqrst<", reader.consumeLetterThenDigitSequence());

        // test consumeHexSequence method
        assertEquals("567890", reader.consumeHexSequence());

        // test consumeDigitSequence method
        assertEquals("1234567890", reader.consumeDigitSequence());

        // test matches method
        assertTrue(reader.matches('d'));
        assertFalse(reader.matches('z'));

        // test matches method with CharSequence
        CharSequence seq2 = "efghij";
        assertTrue(reader.matches(seq2));
        assertFalse(reader.matches("xyz"));

        // test matchesIgnoreCase method
        CharSequence seq3 = "Title";
        assertTrue(reader.matchesIgnoreCase(seq3));

        // test matchesAny method
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertFalse(reader.matchesAny('z', 'a', 'b'));

        // test matchesAnySorted method
        assertTrue(reader.matchesAnySorted(new char[]{'1', '2', '3'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z', 'a', 'b'}));

        // test matchesLetter method
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());

        // test matchConsume method
        assertEquals("efghijklmopqrst<uvwxyzABC DEFGHIJKLMNOPQRST>", reader.consumeTo(seq2));

        // test matchConsumeIgnoreCase method
        assertEquals("TITLE", reader.consumeTagName());

        // test containsIgnoreCase method
        assertTrue(reader.containsIgnoreCase("title"));
        assertFalse(reader.containsIgnoreCase("XYZ"));

        // test toString method
        assertEquals("", reader.toString());
    }

}