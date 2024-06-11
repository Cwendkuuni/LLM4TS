package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testEmptyString() {
        IndexedString is = new IndexedString("");
        Assert.assertEquals(0, is.values.length);
        Assert.assertNull(is.index);
    }

    @Test
    public void testSingleCharacter() {
        IndexedString is = new IndexedString("a");
        Assert.assertEquals(1, is.values.length);
        Assert.assertEquals('a', is.values[0]);
        Assert.assertArrayEquals(new int[]{0}, is.index['a']);
    }

    @Test
    public void testMultipleCharacters() {
        IndexedString is = new IndexedString("abc");
        Assert.assertEquals(3, is.values.length);
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c'}, is.values);
        Assert.assertArrayEquals(new int[][]{new int[]{0}, new int[]{1}, new int[]{2}}, is.index);
    }

    @Test
    public void testCharacterIndexing() {
        IndexedString is = new IndexedString("hello world");
        Assert.assertEquals(11, is.values.length);
        Assert.assertEquals('h', is.values[0]);
        Assert.assertArrayEquals(new int[][]{new int[]{0}, new int[]{7}, new int[]{10}}, is.index['h']);
        Assert.assertArrayEquals(new int[][]{new int[]{1}, new int[]{5}, new int[]{6}}, is.index['e']);
        Assert.assertArrayEquals(new int[][]{new int[]{2}, new int[]{8}, new int[]{9}}, is.index['l']);
        Assert.assertArrayEquals(new int[][]{new int[]{3}, new int[]{10}}, is.index['o']);
        Assert.assertArrayEquals(new int[]{}, is.index[' ']);
        Assert.assertArrayEquals(new int[]{}, is.index[' ']);
        Assert.assertArrayEquals(new int[]{}, is.index['x']);
    }

    @Test
    public void testIndexOf() {
        IndexedString is = new IndexedString("abcabcabc");
        Assert.assertEquals(0, is.indexOf(new char[]{'a'}));
        Assert.assertEquals(1, is.indexOf(new char[]{'b'}));
        Assert.assertEquals(4, is.indexOf(new char[]{'a'}));
        Assert.assertEquals(7, is.indexOf(new char[]{'c'}));
        Assert.assertEquals(-1, is.indexOf(new char[]{'d'}));
    }
}