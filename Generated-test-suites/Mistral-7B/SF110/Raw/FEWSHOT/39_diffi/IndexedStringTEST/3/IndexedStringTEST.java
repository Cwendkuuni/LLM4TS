package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringConstructor() {
        String input = "abcabcdefghijklm";
        IndexedString is = new IndexedString(input);
        assertEquals(input.length(), is.values.length);
        for (int i = 0; i < input.length(); ++i) {
            assertEquals(input.charAt(i), is.values[i]);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexedStringConstructorWithNullInput() {
        new IndexedString(null);
    }

    @Test
    public void testIndexOfEmptyString() {
        IndexedString is = new IndexedString("abc");
        assertNull(is.indexOf(new char[0]));
    }

    @Test
    public void testIndexOfSingleCharacter() {
        IndexedString is = new IndexedString("abcabcdefghijklm");
        assertEquals(5, is.indexOf(new char[]{'e'}));
    }

    @Test
    public void testIndexOfMultipleCharacters() {
        IndexedString is = new IndexedString("abcabcdefghijklm");
        assertEquals(11, is.indexOf(new char[]{'i', 'j'}));
    }

    @Test
    public void testIndexOfNonExistentCharacters() {
        IndexedString is = new IndexedString("abcabcdefghijklm");
        assertNull(is.indexOf(new char[]{'z', 'z'}));
    }

    @Test
    public void testIndexOfSubString() {
        IndexedString is = new IndexedString("abcabcdefghijklm");
        assertEquals(5, is.indexOf(new char[]{'e', 'f'}));
        assertEquals(0, is.indexOf(new char[]{'a', 'b'}));
    }

    @Test
    public void testIndexOfSubStringWithLeadingAndTrailingSpaces() {
        IndexedString is = new IndexedString(" abc def ghi jkl mno");
        assertEquals(1, is.indexOf(new char[]{' ', ' ', 'a'}));
    }

    @Test
    public void testIndexOfSubStringWithLeadingAndTrailingNonPrintableCharacters() {
        IndexedString is = new IndexedString("\u0001abc\u0001def\u0001ghi\u0001jkl\u0001mno");
        assertEquals(3, is.indexOf(new char[]{'d', 'e', 'f'}));
    }

    @Test
    public void testIndexOfSubStringWithMiddleNonPrintableCharacter() {
        IndexedString is = new IndexedString("\u0001abc\u0001def\u0001ghi\u0001jkl\u0001mno");
        assertEquals(5, is.indexOf(new char[]{'g', 'h', 'i'}));
    }
}