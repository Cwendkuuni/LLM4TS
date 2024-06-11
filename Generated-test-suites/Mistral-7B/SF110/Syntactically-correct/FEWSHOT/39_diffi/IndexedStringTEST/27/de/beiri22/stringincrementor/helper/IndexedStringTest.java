package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringConstruction() {
        String s = "abcabccdeeff";
        IndexedString is = new IndexedString(s);
        assertEquals(s.length(), is.values.length);
        for (int i = 0; i < s.length(); ++i) {
            assertEquals(s.charAt(i), is.values[i]);
        }

        char[] expectedIndex = {0, 1, 2, 2, 3, 3, 4, 5, 5, 5, 6, 6, 6, 7, 8, 8, 8, 9, 9, 10, 10, 11};
        assertArrayEquals(expectedIndex, is.index[0]);
        assertArrayEquals(new int[]{1, 4}, is.index[6]);
        assertArrayEquals(new int[]{2, 5}, is.index[7]);
        assertArrayEquals(new int[]{-1}, is.index[12]);
    }

    @Test
    public void testIndexOfEmptyTarget() {
        IndexedString is = new IndexedString("abcd");
        assertNull(is.indexOf(new char[0]));
    }

    @Test
    public void testIndexOfSingleCharacter() {
        IndexedString is = new IndexedString("abcd");
        assertEquals(0, is.indexOf(new char[]{'a'}));
        assertEquals(1, is.indexOf(new char[]{'b'}));
        assertEquals(2, is.indexOf(new char[]{'c'}));
        assertEquals(3, is.indexOf(new char[]{'d'}));
        assertNull(is.indexOf(new char[]{'e'}));
    }

    @Test
    public void testIndexOfMultipleCharacters() {
        IndexedString is = new IndexedString("abcd");
        assertEquals(2, is.indexOf(new char[]{'c', 'd'}));
        assertEquals(3, is.indexOf(new char[]{'d', 'd'}));
        assertEquals(3, is.indexOf(new char[]{'d', 'e'}));
        assertNull(is.indexOf(new char[]{'e', 'f'}));
    }

    @Test
    public void testIndexOfNonExistingSequence() {
        IndexedString is = new IndexedString("abcd");
        assertNull(is.indexOf(new char[]{'e', 'f'}));
    }

    @Test
    public void testIndexOfPartialMatch() {
        IndexedString is = new IndexedString("ababab");
        assertTrue(is.indexOf(new char[]{'a', 'b'}) >= 0);
        assertTrue(is.indexOf(new char[]{'a', 'a'}) >= 0);
        assertTrue(is.indexOf(new char[]{'b', 'a'}) >= 0);
        assertTrue(is.indexOf(new char[]{'a', 'b', 'a'}) >= 0);
        assertTrue(is.indexOf(new char[]{'b', 'a', 'b'}) >= 0);
        assertTrue(is.indexOf(new char[]{'a'}) >= 0);
        assertTrue(is.indexOf(new char[]{'b'}) >= 0);
    }

    @Test
    public void testIndexOfMismatchedSequence() {
        IndexedString is = new IndexedString("ababab");
        assertFalse(is.indexOf(new char[]{'a', 'c'}) >= 0);
    }
}