package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest
{
    @Test
    public void testIndexedStringConstructor()
    {
        String input = "teststring";
        IndexedString is = new IndexedString(input);
        Assert.assertArrayEquals(input.toCharArray(), is.values);
        int expectedLength = input.length();
        Assert.assertArrayEquals(new int[]{expectedLength}, is.index['t'].clone());
        Assert.assertArrayEquals(new int[]{expectedLength}, is.index['s'].clone());
        Assert.assertArrayEquals(new int[]{expectedLength}, is.index['e'].clone());
        Assert.assertArrayEquals(new int[]{expectedLength}, is.index['String'].clone());
        Assert.assertEquals(expectedLength, is.index['t'].length);
        Assert.assertEquals(expectedLength, is.index['s'].length);
        Assert.assertEquals(expectedLength, is.index['e'].length);
        Assert.assertEquals(expectedLength, is.index['S'].length);
        Assert.assertEquals(expectedLength, is.index['t'].length);
        Assert.assertEquals(expectedLength, is.index['r'].length);
        Assert.assertEquals(expectedLength, is.index['i'].length);
        Assert.assertEquals(expectedLength, is.index['n'].length);
        Assert.assertEquals(expectedLength, is.index['g'].length);
    }

    @Test
    public void testCountMethod()
    {
        IndexedString is = new IndexedString("testtesttest");
        Assert.assertEquals(3, is.count('t'));
        Assert.assertEquals(3, is.count('e'));
        Assert.assertEquals(3, is.count('s'));
        Assert.assertEquals(3, is.count('t'));
        Assert.assertEquals(3, is.count(' '));
        Assert.assertEquals(1, is.count('x'));
        Assert.assertEquals(0, is.count('z'));
    }

    @Test
    public void testIndexOfMethod()
    {
        String input = "testtesttesttesttesttesttesttesttesttesttesttest";
        IndexedString is = new IndexedString(input);
        char[] target1 = {'t', 'e', 's'};
        char[] target2 = {'t', 'e', 's', 't'};
        char[] target3 = {'t', 'e', 's', 't', 'e'};
        char[] target4 = {'z', 'e', 's', 't', 'e', 's', 't', 'e', 's', 't', 'e', 's'};
        char[] target5 = {'z', 'e', 's', 't', 'e', 's', 't', 'e', 's', 't', 'e', 's', 'z'};

        int result1 = is.indexOf(target1);
        int result2 = is.indexOf(target2);
        int result3 = is.indexOf(target3);
        int result4 = is.indexOf(target4);
        int result5 = is.indexOf(target5);

        Assert.assertEquals(0, result1);
        Assert.assertEquals(4, result2);
        Assert.assertEquals(8, result3);
        Assert.assertEquals(0, result4);
        Assert.assertEquals(-1, result5);
    }
}