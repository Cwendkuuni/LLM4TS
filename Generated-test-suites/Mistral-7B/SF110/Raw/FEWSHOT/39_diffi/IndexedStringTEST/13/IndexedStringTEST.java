package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringConstructor() {
        String s = "teststring";
        IndexedString is = new IndexedString(s);
        Assert.assertArrayEquals(s.toCharArray(), is.values);
        for (char c = '\0'; c < '\u0100'; ++c) {
            Assert.assertNotNull(is.index[c]);
        }
    }

    @Test
    public void testCount() {
        IndexedString is = new IndexedString("teststring");
        Assert.assertEquals(7, is.count('t'));
        Assert.assertEquals(3, is.count('s'));
        Assert.assertEquals(1, is.count('r'));
        Assert.assertEquals(1, is.count('g'));
        Assert.assertEquals(1, is.count('i'));
        Assert.assertEquals(1, is.count('n'));
        Assert.assertEquals(1, is.count('g'));
        Assert.assertEquals(2, is.count('e'));
    }

    @Test
    public void testIndexOf() {
        IndexedString is = new IndexedString("teststring");
        char[] target1 = {'t', 'e', 's', 't'};
        char[] target2 = {'s', 't', 'r'};
        char[] target3 = {'t', 'e', 's', 't', 's'};
        char[] target4 = {'g', 'i', 'n'};
        char[] target5 = {'a', 'b', 'c'};

        int result1 = is.indexOf(target1);
        int result2 = is.indexOf(target2);
        int result3 = is.indexOf(target3);
        int result4 = is.indexOf(target4);
        int result5 = is.indexOf(target5);

        Assert.assertEquals(0, result1);
        Assert.assertEquals(3, result2);
        Assert.assertEquals(0, result3);
        Assert.assertEquals(-1, result4);
        Assert.assertEquals(-1, result5);
    }
}