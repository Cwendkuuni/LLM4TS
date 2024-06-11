package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringConstruction() {
        String input = "teststring";
        IndexedString is = new IndexedString(input);
        Assert.assertEquals(input.length(), is.values.length);
        for (int i = 0; i < input.length(); ++i) {
            Assert.assertEquals(input.charAt(i), is.values[i]);
        }
    }

    @Test
    public void testIndexOfSingleCharacter() {
        String input = "teststring";
        IndexedString is = new IndexedString(input);
        for (char c = '\u0000'; c <= '\u00ff'; ++c) {
            int index = is.indexOf(new char[]{c});
            if (index != -1) {
                Assert.assertEquals(index, is.index[c][0]);
            }
        }
    }

    @Test
    public void testIndexOfMultiCharacter() {
        String input = "teststringteststring";
        IndexedString is = new IndexedString(input);
        char[] target = {'t', 'e', 's', 't'};
        int index = is.indexOf(target);
        Assert.assertEquals(2, index);
    }

    @Test
    public void testIndexOfEmptyTarget() {
        String input = "teststring";
        IndexedString is = new IndexedString(input);
        int index = is.indexOf(new char[0][0]);
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testIndexOfNonExistingTarget() {
        String input = "teststring";
        IndexedString is = new IndexedString(input);
        char[] target = {'x', 'y', 'z'};
        int index = is.indexOf(target);
        Assert.assertEquals(-1, index);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexedStringNegativeLengthInput() {
        new IndexedString(new String(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexedStringNullInput() {
        new IndexedString(null);
    }
}