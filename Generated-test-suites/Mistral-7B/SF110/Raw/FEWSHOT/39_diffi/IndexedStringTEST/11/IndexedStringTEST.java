package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringConstructor() {
        String s = "abcabcabcabca";
        IndexedString is = new IndexedString(s);
        Assert.assertEquals(s.length(), is.values.length);
        for (int i = 0; i < s.length(); i++) {
            Assert.assertEquals(s.charAt(i), is.values[i]);
        }
    }

    @Test
    public void testIndexOfSingleCharacter() {
        String s = "abcabcabcabca";
        IndexedString is = new IndexedString(s);
        for (char c = '\u0000'; c <= '\u00FF'; c++) {
            int count = is.count(c);
            for (int i = 0; i < count; i++) {
                int index = is.indexOf(new char[]{c});
                Assert.assertNotEquals(-1, index);
            }
        }
    }

    @Test
    public void testIndexOfMultipleCharacters() {
        String s = "abcabcabcabca";
        IndexedString is = new IndexedString(s);
        char[] target1 = {'a', 'b', 'c'};
        int index1 = is.indexOf(target1);
        Assert.assertEquals(0, index1);

        char[] target2 = {'b', 'c', 'a'};
        int index2 = is.indexOf(target2);
        Assert.assertEquals(6, index2);

        char[] target3 = {'d', 'e', 'f'};
        int index3 = is.indexOf(target3);
        Assert.assertEquals(-1, index3);
    }

    @Test
    public void testIndexOfEmptyArray() {
        String s = "abcabcabcabca";
        IndexedString is = new IndexedString(s);
        int index = is.indexOf(new char[0]);
        Assert.assertEquals(-1, index);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexOfNullArray() {
        String s = "abcabcabcabca";
        IndexedString is = new IndexedString(s);
        is.indexOf(null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testIndexOfArrayTooLong() {
        String s = "abcabcabcabca";
        IndexedString is = new IndexedString(s);
        char[] target = new char[s.length() + 1];
        is.indexOf(target);
    }
}