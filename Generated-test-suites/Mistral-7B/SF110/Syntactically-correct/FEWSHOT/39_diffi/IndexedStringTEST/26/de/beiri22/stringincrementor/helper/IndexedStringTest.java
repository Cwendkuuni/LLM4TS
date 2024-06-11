package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringCreation() {
        String s = "abcabcdefghijklmnopqrstuvwxyz";
        IndexedString is = new IndexedString(s);
        Assert.assertArrayEquals(s.toCharArray(), is.values);
        // check the length of index array and sub arrays
        for (char c = '\0'; c < '\u0100'; ++c) {
            Assert.assertNotNull(is.index[c]);
            Assert.assertEquals(s.length(), is.index[c].length);
        }
    }

    @Test
    public void testIndexOfEmptyTarget() {
        IndexedString is = new IndexedString("abc");
        Assert.assertEquals(-1, is.indexOf(new char[0][0]));
    }

    @Test
    public void testIndexOfSingleCharacter() {
        IndexedString is = new IndexedString("abcabcdefghijklmnopqrstuvwxyz");
        char c = 'a';
        int result = is.indexOf(new char[]{c});
        Assert.assertEquals(0, result);
    }

    @Test
    public void testIndexOfMultipleCharacters() {
        IndexedString is = new IndexedString("abcabcdefghijklmnopqrstuvwxyz");
        char[] target = {'a', 'b'};
        int result = is.indexOf(target);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testIndexOfSubstring() {
        IndexedString is = new IndexedString("abcabcdefghijklmnopqrstuvwxyz");
        String substring = "bc";
        char[] chars = substring.toCharArray();
        int result = is.indexOf(chars);
        Assert.assertEquals(3, result);
    }

    @Test
    public void testIndexOfSubstringNotPresent() {
        IndexedString is = new IndexedString("abcabcdefghijklmnopqrstuvwxyz");
        String substring = "xy";
        char[] chars = substring.toCharArray();
        int result = is.indexOf(chars);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testIndexOfSubstringWithDifferentCase() {
        IndexedString is = new IndexedString("abcABCdefghijklMnOpqRSTuVwXYZ");
        String substring = "bc";
        char[] chars = substring.toCharArray();
        int result = is.indexOf(chars);
        Assert.assertEquals(3, result);
    }
}