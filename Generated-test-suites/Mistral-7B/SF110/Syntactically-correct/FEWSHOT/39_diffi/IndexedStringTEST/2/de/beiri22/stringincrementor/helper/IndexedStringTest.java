package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringConstructor() {
        String input = "abcabcabc";
        IndexedString is = new IndexedString(input);
        Assert.assertEquals(input.length(), is.values.length);
        for (int i = 0; i < input.length(); i++) {
            Assert.assertEquals(input.charAt(i), is.values[i]);
        }
    }

    @Test
    public void testIndexOfChar() {
        String input = "abcabcabc";
        IndexedString is = new IndexedString(input);
        char c = 'a';
        int index = is.indexOf(new char[]{c});
        Assert.assertEquals(0, index);

        c = 'b';
        index = is.indexOf(new char[]{c});
        Assert.assertEquals(5, index);

        c = 'c';
        index = is.indexOf(new char[]{c});
        Assert.assertEquals(11, index);

        char[] emptyTarget = new char[0];
        index = is.indexOf(emptyTarget);
        Assert.assertEquals(-1, index);

        char[] wrongTarget = new char[]{'x'};
        index = is.indexOf(wrongTarget);
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testIndexOfCharArrayWithSameLength() {
        String input = "ababab";
        IndexedString is = new IndexedString(input);
        char c = 'a';
        int[] expectedIndices = {0, 4};
        for (int i = 0; i < expectedIndices.length; i++) {
            int index = is.indexOf(new char[]{c});
            Assert.assertEquals(expectedIndices[i], index);
        }
    }

    @Test
    public void testIndexOfCharArrayWithDifferentLength() {
        String input = "abcabcabc";
        IndexedString is = new IndexedString(input);
        char c = 'a';
        int index = is.indexOf(new char[]{c, c});
        Assert.assertEquals(0, index);

        index = is.indexOf(new char[]{c, c, c});
        Assert.assertEquals(0, index);

        index = is.indexOf(new char[]{c, c, c, c});
        Assert.assertEquals(0, index);

        index = is.indexOf(new char[]{c, c, c, c, c});
        Assert.assertEquals(0, index);

        index = is.indexOf(new char[]{c, c, c, c, c, c});
        Assert.assertEquals(0, index);

        index = is.indexOf(new char[]{c, c, c, c, c, c, c});
        Assert.assertEquals(0, index);

        char[] wrongTarget = new char[]{'x'};
        index = is.indexOf(wrongTarget);
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testIndexOfCharArrayWithPartialMatch() {
        String input = "abcdabcdabcd";
        IndexedString is = new IndexedString(input);
        char[] target = new char[]{'a', 'b', 'd'};
        int index = is.indexOf(target);
        Assert.assertEquals(2, index);
    }

    @Test
    public void testIndexOfCharArrayWithNoMatch() {
        String input = "abcdabcdabcd";
        IndexedString is = new IndexedString(input);
        char[] target = new char[]{'e', 'f'};
        int index = is.indexOf(target);
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testIndexOfCharArrayWithEmptyInput() {
        String input = "";
        IndexedString is = new IndexedString(input);
        char[] target = new char[0];
        int index = is.indexOf(target);
        Assert.assertEquals(-1, index);
    }
}