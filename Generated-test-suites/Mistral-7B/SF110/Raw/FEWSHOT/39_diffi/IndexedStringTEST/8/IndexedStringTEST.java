package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringCharacterCount() {
        String input = "abcdef";
        IndexedString is = new IndexedString(input);
        Assert.assertEquals(5, is.count('a'));
        Assert.assertEquals(1, is.count('d'));
        Assert.assertEquals(0, is.count('z'));
    }

    @Test
    public void testIndexedStringConstruction() {
        String input1 = "abcdef";
        IndexedString is1 = new IndexedString(input1);

        String input2 = "Hello World!";
        IndexedString is2 = new IndexedString(input2);

        Assert.assertEquals(input1.length(), is1.values.length);
        Assert.assertArrayEquals(input1.toCharArray(), is1.values);

        Assert.assertEquals(input2.length(), is2.values.length);
        Assert.assertArrayEquals(input2.toCharArray(), is2.values);
    }

    @Test
    public void testIndexedStringIndexOf() {
        String target = "abc";
        IndexedString is = new IndexedString("abcdefabc");

        Assert.assertEquals(2, is.indexOf(target.toCharArray()));
        Assert.assertEquals(-1, is.indexOf("xyz".toCharArray()));
    }

    @Test
    public void testIndexedStringIndex() {
        String input = "abcabc";
        IndexedString is = new IndexedString(input);

        char c = 'a';
        int[] indices = is.index[c];
        Assert.assertNotNull(indices);
        Assert.assertEquals(3, indices.length);
        Assert.assertEquals(0, indices[0]);
        Assert.assertEquals(3, indices[1]);
        Assert.assertEquals(6, indices[2]);
    }
}