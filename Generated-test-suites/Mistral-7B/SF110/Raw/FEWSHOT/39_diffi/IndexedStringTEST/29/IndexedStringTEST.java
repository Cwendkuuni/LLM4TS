package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringCreation() {
        String testString = "abababccdefdefff";
        IndexedString indexedString = new IndexedString(testString);
        char[] expectedValues = testString.toCharArray();
        Assert.assertArrayEquals(expectedValues, indexedString.values);
    }

    @Test
    public void testIndexedStringIndexCreation() {
        String testString = "abababccdefdefff";
        IndexedString indexedString = new IndexedString(testString);
        for (char c = '\u0000'; c < '\u0100'; c++) {
            if (indexedString.count(c) > 0) {
                Assert.assertNotNull(indexedString.index[c]);
            } else {
                Assert.assertNull(indexedString.index[c]);
            }
        }
    }

    @Test
    public void testIndexOfChar() {
        String testString = "abababccdefdefff";
        IndexedString indexedString = new IndexedString(testString);
        char[] target = {'b', 'a', 'b'};
        int index = indexedString.indexOf(target);
        Assert.assertEquals(5, index);
    }

    @Test
    public void testIndexOfEmptyCharArray() {
        String testString = "abababccdefdefff";
        IndexedString indexedString = new IndexedString(testString);
        int index = indexedString.indexOf(new char[0][0]);
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testIndexOfCharNotInString() {
        String testString = "abababccdefdefff";
        IndexedString indexedString = new IndexedString(testString);
        char target = 'z';
        int index = indexedString.indexOf(new char[]{target});
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testIndexOfCharArrayWithMismatchingLength() {
        String testString = "abababccdefdefff";
        IndexedString indexedString = new IndexedString(testString);
        char[] target = {'b', 'a', 'b'};
        Assert.assertFalse(indexedString.indexOf(new char[]{target[0], target[1], 'x'}) >= 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexOfNullCharArray() {
        String testString = "abababccdefdefff";
        IndexedString indexedString = new IndexedString(testString);
        indexedString.indexOf(null);
    }
}