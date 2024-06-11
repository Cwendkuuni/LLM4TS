package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringConstructor() {
        String s = "abcabcabc";
        IndexedString is = new IndexedString(s);
        Assert.assertArrayEquals(s.toCharArray(), is.values);
        for (char c = '\0'; c < '\u0100'; ++c) {
            Assert.assertArrayNotNull(is.index[c]);
        }
    }

    @Test
    public void testIndexOf() {
        String s = "abcabcabc";
        IndexedString is = new IndexedString(s);
        char[] target1 = {'a', 'b', 'c'};
        Assert.assertEquals(0, is.indexOf(target1));

        char[] target2 = {'b', 'b', 'c'};
        Assert.assertEquals(2, is.indexOf(target2));

        char[] target3 = {'d', 'e', 'f'};
        Assert.assertEquals(-1, is.indexOf(target3));

        char[] target4 = {'a', 'a'};
        Assert.assertEquals(0, is.indexOf(target4));
        Assert.assertEquals(1, is.indexOf(target4, 1));
        Assert.assertEquals(2, is.indexOf(target4, 2));
    }

    @Test
    public void testCount() {
        String s = "abcabcabc";
        IndexedString is = new IndexedString(s);
        Assert.assertEquals(3, is.count('a'));
        Assert.assertEquals(2, is.count('b'));
        Assert.assertEquals(2, is.count('c'));
        Assert.assertEquals(0, is.count('d'));
    }
}