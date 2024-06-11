package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringWithEmptyString() {
        IndexedString indexedString = new IndexedString("");
        assertEquals(0, indexedString.values.length);
        assertEquals(0, indexedString.index.length);
    }

    @Test
    public void testIndexedStringWithSingleCharacter() {
        IndexedString indexedString = new IndexedString("a");
        assertEquals(1, indexedString.values.length);
        assertEquals(1, indexedString.index['a'].length);
        assertEquals(0, indexedString.index['a'][0]);
    }

    @Test
    public void testIndexedStringWithMultipleCharacters() {
        IndexedString indexedString = new IndexedString("hello world");
        assertEquals(11, indexedString.values.length);
        assertEquals(256, indexedString.index.length);

        assertArrayEquals(new int[]{0, 5}, indexedString.index['h']);
        assertArrayEquals(new int[]{1, 6}, indexedString.index['e']);
        assertArrayEquals(new int[]{2, 7}, indexedString.index['l']);
        assertArrayEquals(new int[]{3, 8}, indexedString.index['o']);
        assertArrayEquals(new int[]{4, 9, 10}, indexedString.index['w']);
        assertArrayEquals(new int[]{5, 11}, indexedString.index[' ']);
        assertArrayEquals(new int[]{6, 11}, indexedString.index['\u000D']); // Windows line separator
        assertNull(indexedString.index['z']);
    }

    @Test
    public void testIndexOfWithEmptyTargetArray() {
        IndexedString indexedString = new IndexedString("hello world");
        assertNull(indexedString.indexOf(new char[0][0]));
    }

    @Test
    public void testIndexOfWithSingleCharacterTarget() {
        IndexedString indexedString = new IndexedString("hello world");
        assertEquals(0, indexedString.indexOf(new char[]{'h'}));
        assertEquals(5, indexedString.indexOf(new char[]{'o'}));
        assertEquals(6, indexedString.indexOf(new char[]{'w'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'z'}));
    }

    @Test
    public void testIndexOfWithMultipleCharacterTarget() {
        IndexedString indexedString = new IndexedString("hello world");
        assertEquals(0, indexedString.indexOf(new char[]{'h', 'e'}));
        assertEquals(5, indexedString.indexOf(new char[]{'o', 'l'}));
        assertEquals(8, indexedString.indexOf(new char[]{'l', 'o'}));
        assertEquals(10, indexedString.indexOf(new char[]{'w', 'o'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'z', 'z'}));
    }

    @Test
    public void testIndexOfWithTargetOverlappingString() {
        IndexedString indexedString = new IndexedString("hello world");
        assertEquals(-1, indexedString.indexOf(new char[]{'e', 'l'})); // 'ell' is not present in the string
        assertEquals(-1, indexedString.indexOf(new char[]{'l', 'l'})); // 'll' is not present in the string
        assertEquals(-1, indexedString.indexOf(new char[]{'o', 'l', 'l'})); // 'oll' is not present in the string
        assertEquals(-1, indexedString.indexOf(new char[]{'l', 'o', 'l'})); // 'lol' is not present in the string
        assertEquals(-1, indexedString.indexOf(new char[]{'l', 'o', 'l', 'o'})); // 'lolo' is not present in the string

        // test with overlapping substring
        assertEquals(5, indexedString.indexOf(new char[]{'o', 'l', 'o'})); // 'olo' is present at index 5, 6, 7
    }

    @Test
    public void testIndexOfWithTargetSubstringAtStart() {
        IndexedString indexedString = new IndexedString("hello world");
        assertEquals(0, indexedString.indexOf(new char[]{'h', 'e', 'l', 'l', 'o'}));
        assertEquals(0, indexedString.indexOf(new char[]{'h', 'e', 'l', 'l', 'o', ' '}));
        assertEquals(0, indexedString.indexOf(new char[]{'h', 'e', 'l', 'l', 'o', ' ', 'w'}));
        assertEquals(0, indexedString.indexOf(new char[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o'}));
        assertEquals(0, indexedString.indexOf(new char[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r'}));
        assertEquals(0, indexedString.indexOf(new char[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', ' '}));
        assertEquals(0, indexedString.indexOf(new char[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', ' ', 'w'}));
    }

    @Test
    public void testIndexOfWithTargetSubstringAtEnd() {
        IndexedString indexedString = new IndexedString("hello world");
        assertEquals(11, indexedString.indexOf(new char[]{' ', 'w'}));
        assertEquals(11, indexedString.indexOf(new char[]{' ', 'w', '\u000D'}));
        assertEquals(11, indexedString.indexOf(new char[]{' ', 'w', 'o'}));
        assertEquals(11, indexedString.indexOf(new char[]{' ', 'w', 'o', 'r'}));
        assertEquals(11, indexedString.indexOf(new char[]{' ', 'w', 'o', 'r', ' '}));
        assertEquals(11, indexedString.indexOf(new char[]{' ', 'w', 'o', 'r', ' ', 'w'}));
        assertEquals(11, indexedString.indexOf(new char[]{' ', 'w', 'o', 'r', ' ', 'w', 'o'}));
    }
}