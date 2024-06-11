package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {
    @Test
    public void testIndexedStringConstructor() {
        String s = "abcabcc";
        IndexedString is = new IndexedString(s);
        Assert.assertArrayEquals(s.toCharArray(), is.values);
        for (char c = '\0'; c < '\u0100'; ++c) {
            Assert.assertNotNull(is.index[c]);
        }
    }

    @Test
    public void testCount() {
        IndexedString is = new IndexedString("abcabcc");
        Assert.assertEquals(3, is.count('a'));
        Assert.assertEquals(2, is.count('b'));
        Assert.assertEquals(1, is.count('c'));
        Assert.assertEquals(0, is.count('d'));
    }

    @Test
    public void testIndexOfEmptyArray() {
        IndexedString is = new IndexedString("abcabcc");
        Assert.assertEquals(-1, is.indexOf(new char[0]));
    }

    @Test
    public void testIndexOfSingleCharacter() {
        IndexedString is = new IndexedString("abcabcc");
        Assert.assertEquals(1, is.indexOf(new char[]{'a'}));
        Assert.assertEquals(3, is.indexOf(new char[]{'b'}));
        Assert.assertEquals(5, is.indexOf(new char[]{'c'}));
        Assert.assertEquals(-1, is.indexOf(new char[]{'d'}));
    }

    @Test
    public void testIndexOfMultipleCharacters() {
        IndexedString is = new IndexedString("abcabcc");
        Assert.assertEquals(1, is.indexOf(new char[]{'a', 'b'}));
        Assert.assertEquals(3, is.indexOf(new char[]{'b', 'c'}));
        Assert.assertEquals(4, is.indexOf(new char[]{'c', 'a'}));
        Assert.assertEquals(5, is.indexOf(new char[]{'c', 'c'}));
        Assert.assertEquals(-1, is.indexOf(new char[]{'d', 'e'}));
    }

    @Test
    public void testIndexOfMultipleCharactersAtDifferentPositions() {
        IndexedString is = new IndexedString("abcabcc");
        Assert.assertEquals(0, is.indexOf(new char[]{'a', 1}));
        Assert.assertEquals(2, is.indexOf(new char[]{'b', 2}));
        Assert.assertEquals(4, is.indexOf(new char[]{'c', 5}));
        Assert.assertEquals(-1, is.indexOf(new char[]{'d', 1}));
    }

    @Test
    public void testIndexOfNull() {
        IndexedString is = new IndexedString("abcabcc");
        Assert.assertNull(is.indexOf(null));
    }
}