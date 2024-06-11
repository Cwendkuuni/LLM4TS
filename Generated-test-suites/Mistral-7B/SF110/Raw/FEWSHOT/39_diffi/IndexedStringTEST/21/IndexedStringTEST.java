package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringConstructor() {
        String s = "abccdefccc";
        IndexedString is = new IndexedString(s);
        Assert.assertEquals(s.length(), is.values.length);
        for (int i = 0; i < s.length(); ++i) {
            Assert.assertEquals(s.charAt(i), is.values[i]);
        }
    }

    @Test
    public void testIndexOfEmptyTarget() {
        IndexedString is = new IndexedString("abcd");
        Assert.assertEquals(-1, is.indexOf(new char[0][0]));
    }

    @Test
    public void testIndexOfSingleCharacter() {
        IndexedString is = new IndexedString("abcd");
        Assert.assertEquals(0, is.indexOf(new char[]{'a'}));
        Assert.assertEquals(1, is.indexOf(new char[]{'b'}));
        Assert.assertEquals(2, is.indexOf(new char[]{'c'}));
        Assert.assertEquals(3, is.indexOf(new char[]{'d'}));
        Assert.assertEquals(-1, is.indexOf(new char[]{'e'}));
    }

    @Test
    public void testIndexOfMultipleCharacters() {
        IndexedString is = new IndexedString("abcd");
        Assert.assertEquals(0, is.indexOf(new char[]{'a', 'b'}));
        Assert.assertEquals(1, is.indexOf(new char[]{'b', 'c'}));
        Assert.assertEquals(2, is.indexOf(new char[]{'c', 'd'}));
        Assert.assertEquals(-1, is.indexOf(new char[]{'a', 'e'}));
    }

    @Test
    public void testIndexOfSubstring() {
        IndexedString is = new IndexedString("abcdabcd");
        Assert.assertEquals(0, is.indexOf(new char[]{'a', 'b', 'c', 'd'}));
        Assert.assertEquals(6, is.indexOf(new char[]{'a', 'b', 'c'}));
        Assert.assertEquals(12, is.indexOf(new char[]{'b', 'c', 'd'}));
        Assert.assertEquals(-1, is.indexOf(new char[]{'a', 'e'}));
    }

    @Test
    public void testIndexOfSubstringWithLeadingAndTrailingSpaces() {
        IndexedString is = new IndexedString("abcdabcd");
        Assert.assertEquals(0, is.indexOf(new char[]{' ', 'a', 'b', 'c', 'd', ' ', 'a', 'b', 'c'}));
        Assert.assertEquals(6, is.indexOf(new char[]{' ', 'a', 'b', 'c'}));
        Assert.assertEquals(12, is.indexOf(new char[]{'b', 'c', 'd', ' ', 'a', 'b', 'c'}));
        Assert.assertEquals(-1, is.indexOf(new char[]{'a', 'e'}));
    }
}