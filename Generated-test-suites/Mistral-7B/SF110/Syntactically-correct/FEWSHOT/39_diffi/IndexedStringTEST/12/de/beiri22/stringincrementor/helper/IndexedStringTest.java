package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringConstructor() {
        String str = "abcdefghijklmnopqrstuvwxyz";
        IndexedString is = new IndexedString(str);
        assertEquals(str.length(), is.values.length);
        for (int i = 0; i < str.length(); ++i) {
            assertEquals(str.charAt(i), is.values[i]);
        }
        for (char c = '\0'; c < '\u0100'; ++c) {
            int length = is.index[c].length;
            assertEquals(str.length(), length);
            for (int i = 0; i < length; ++i) {
                int index = is.index[c][i];
                assertEquals(i, indexOf(is.values, c, index));
            }
        }
    }

    @Test
    public void testIndexOfEmptyTarget() {
        String str = "abc";
        IndexedString is = new IndexedString(str);
        assertNull(is.indexOf(new char[0]));
    }

    @Test
    public void testIndexOfSingleCharacter() {
        String str = "abc";
        IndexedString is = new IndexedString(str);
        assertEquals(0, is.indexOf(new char[]{'a'}));
        assertEquals(1, is.indexOf(new char[]{'b'}));
        assertEquals(2, is.indexOf(new char[]{'c'}));
        assertNull(is.indexOf(new char[]{'d'}));
    }

    @Test
    public void testIndexOfMultiCharacter() {
        String str = "abcdefghijklmnopqrstuvwxyz";
        IndexedString is = new IndexedString(str);
        assertEquals(0, is.indexOf(new char[]{'a', 'b'}));
        assertEquals(1, is.indexOf(new char[]{'b', 'c'}));
        assertEquals(36, is.indexOf(new char[]{'y', 'z'}));
        assertNull(is.indexOf(new char[]{'z', 'a'}));
    }

    @Test
    public void testIndexOfMixedCharacters() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        IndexedString is = new IndexedString(str);
        int index = is.indexOf(new char[]{'a', 'B'});
        assertEquals(-1, index);
        index = is.indexOf(new char[]{'A', 'b'});
        assertEquals(26, index);
    }

    private int indexOf(char[] arr, char c, int index) {
        for (int i = index; i < arr.length; ++i) {
            if (arr[i] == c) {
                return i;
            }
        }
        return -1;
    }

    @Test
    public void testIndexOfAtBeginning() {
        String str = "abc";
        IndexedString is = new IndexedString(str);
        assertEquals(0, is.indexOf(new char[]{'a'}));
    }

    @Test
    public void testIndexOfAtEnd() {
        String str = "abc";
        IndexedString is = new IndexedString(str);
        assertEquals(2, is.indexOf(new char[]{'c'}));
    }

    @Test
    public void testIndexOfInMiddle() {
        String str = "abc";
        IndexedString is = new IndexedString(str);
        assertEquals(1, is.indexOf(new char[]{'b'}));
    }

    @Test
    public void testIndexOfOverlapping() {
        String str = "abcabc";
        IndexedString is = new IndexedString(str);
        int index = is.indexOf(new char[]{'a', 'b'});
        assertEquals(0, index);
        index = is.indexOf(new char[]{'b', 'a'});
        assertEquals(1, index);
    }

    @Test
    public void testIndexOfSubstring() {
        String str = "abc";
        IndexedString is = new IndexedString(str);
        assertNull(is.indexOf(new char[]{'d', 'e'}));
        int index = is.indexOf(new char[]{'a', 'b', 'c'});
        assertEquals(0, index);
        index = is.indexOf(new char[]{'b', 'c', 'a'});
        assertEquals(1, index);
    }

    @Test
    public void testIndexOfAllCharacters() {
        String str = "abc";
        IndexedString is = new IndexedString(str);
        for (char c = '\0'; c < '\u0100'; ++c) {
            int index = is.indexOf(new char[]{c});
            if (index >= 0) {
                assertEquals(indexOf(is.values, c, index), index);
            }
        }
    }

    @Test
    public void testIndexOfWithNull() {
        String str = "abc";
        IndexedString is = new IndexedString(str);
        assertNull(is.indexOf(null));
    }

    @Test
    public void testIndexOfWithEmptyString() {
        String str = "abc";
        IndexedString is = new IndexedString(str);
        assertNull(is.indexOf(new String("")));
    }
}