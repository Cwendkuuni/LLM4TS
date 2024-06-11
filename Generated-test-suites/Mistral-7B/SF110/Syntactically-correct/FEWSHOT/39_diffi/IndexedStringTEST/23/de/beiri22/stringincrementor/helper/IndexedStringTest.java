package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringCreation() {
        String testString = "testString";
        IndexedString is = new IndexedString(testString);
        Assert.assertArrayEquals(testString.toCharArray(), is.values);
    }

    @Test
    public void testIndexOfChar() {
        String testString = "testString";
        IndexedString is = new IndexedString(testString);
        char c = 't';
        int index = is.indexOf(new char[]{c});
        Assert.assertEquals(0, index);
    }

    @Test
    public void testIndexOfEmptyArray() {
        String testString = "testString";
        IndexedString is = new IndexedString(testString);
        int index = is.indexOf(new char[0][]);
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testIndexOfSingleCharNotPresent() {
        String testString = "testString";
        IndexedString is = new IndexedString(testString);
        char c = 'x';
        int index = is.indexOf(new char[]{c});
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testIndexOfMultipleChars() {
        String testString = "testString";
        IndexedString is = new IndexedString(testString);
        char[] target = {'t', 'e', 's', 't'};
        int index = is.indexOf(target);
        Assert.assertEquals(0, index);
    }

    @Test
    public void testIndexOfMultipleCharsNotPresentInOrder() {
        String testString = "testString";
        IndexedString is = new IndexedString(testString);
        char[] target = {'s', 'g', 't', 'e', 's'};
        int index = is.indexOf(target);
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testIndexOfMultipleCharsNotPresentOutOfOrder() {
        String testString = "testString";
        IndexedString is = new IndexedString(testString);
        char[] target = {'s', 't', 'e', 'g', 't'};
        int index = is.indexOf(target);
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testIndexOfMultipleCharsPresent() {
        String testString = "testString";
        IndexedString is = new IndexedString(testString);
        char[] target = {'t', 'e', 's', 't'};
        int index = is.indexOf(target);
        Assert.assertEquals(0, index);
    }
}