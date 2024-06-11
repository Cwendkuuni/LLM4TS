package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringConstructor() {
        String s = "abcdefghijk";
        IndexedString is = new IndexedString(s);
        Assert.assertArrayEquals(s.toCharArray(), is.values);
        for (char c = '\0'; c < '\u0100'; ++c) {
            Assert.assertNotNull(is.index[c]);
            Assert.assertEquals(6, is.index[c].length);
        }
    }

    @Test
    public void testIndexOf_emptyArray() {
        IndexedString is = new IndexedString("abcdefghijk");
        char[] emptyArray = {};
        int result = is.indexOf(emptyArray);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testIndexOf_singleCharacter() {
        IndexedString is = new IndexedString("abcdefghijk");
        char c = 'a';
        int index = is.indexOf(new char[]{c});
        Assert.assertEquals(0, index);
    }

    @Test
    public void testIndexOf_singleCharacter_notPresent() {
        IndexedString is = new IndexedString("abcdefghijk");
        char c = 'x';
        int index = is.indexOf(new char[]{c});
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testIndexOf_multipleCharacters_present() {
        IndexedString is = new IndexedString("abcabcdefghijk");
        char[] target = new char[]{'a', 'b'};
        int index = is.indexOf(target);
        Assert.assertEquals(2, index);
    }

    @Test
    public void testIndexOf_multipleCharacters_notPresent() {
        IndexedString is = new IndexedString("abcdefghijk");
        char[] target = new char[]{'x', 'y'};
        int index = is.indexOf(target);
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testIndexOf_multipleCharacters_notPresent_mixed() {
        IndexedString is = new IndexedString("abcabcdefghijk");
        char[] target = new char[]{'a', 'x'};
        int index = is.indexOf(target);
        Assert.assertEquals(-1, index);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexOf_nullArray() {
        IndexedString is = new IndexedString("abcdefghijk");
        is.indexOf(null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testIndexOf_targetBiggerThanString() {
        IndexedString is = new IndexedString("abcdefghijk");
        char[] target = new char[12];
        is.indexOf(target);
    }
}