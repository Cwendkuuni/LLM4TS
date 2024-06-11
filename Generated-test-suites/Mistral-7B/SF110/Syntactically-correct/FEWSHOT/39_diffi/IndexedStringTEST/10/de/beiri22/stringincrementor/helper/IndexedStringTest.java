package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringConstructor() {
        String testString = "abccdeeef";
        IndexedString is = new IndexedString(testString);
        Assert.assertEquals(testString.length(), is.values.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexedStringConstructorNullString() {
        new IndexedString(null);
    }

    @Test
    public void testIndexedStringIndex() {
        String testString = "abccdeeef";
        IndexedString is = new IndexedString(testString);
        char testChar = 'c';
        int[] indexArray = is.index[testChar];
        Assert.assertNotNull(indexArray);
        Assert.assertEquals(3, indexArray.length);
        int expectedIndex = 2;
        Assert.assertEquals(expectedIndex, indexArray[0]);
        Assert.assertEquals(expectedIndex + 1, indexArray[1]);
        Assert.assertEquals(expectedIndex + 2, indexArray[2]);
    }

    @Test
    public void testIndexedStringEmptyString() {
        IndexedString is = new IndexedString("");
        Assert.assertEquals(0, is.values.length);
    }

    @Test
    public void testIndexOf() {
        String testString = "abccdeeef";
        IndexedString is = new IndexedString(testString);
        char[] testTarget = {'c', 'd', 'e'};
        int result = is.indexOf(testTarget);
        int expectedIndex = 2;
        Assert.assertEquals(expectedIndex, result);
    }

    @Test
    public void testIndexOfEmptyTarget() {
        String testString = "abccdeeef";
        IndexedString is = new IndexedString(testString);
        char[] testTarget = {};
        int result = is.indexOf(testTarget);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testIndexOfNullTarget() {
        String testString = "abccdeeef";
        IndexedString is = new IndexedString(testString);
        char[] testTarget = null;
        int result = is.indexOf(testTarget);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testIndexOfDifferentTarget() {
        String testString = "abccdeeef";
        IndexedString is = new IndexedString(testString);
        char[] testTarget = {'b', 'e'};
        int result = is.indexOf(testTarget);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testIndexOfSubstring() {
        String testString = "abccdeeef";
        IndexedString is = new IndexedString(testString);
        String testSubstring = "cd";
        int result = is.indexOf(testSubstring.toCharArray());
        int expectedIndex = 2;
        Assert.assertEquals(expectedIndex, result);
    }

    @Test
    public void testIndexOfSubstringEndOfString() {
        String testString = "abccdeeef";
        IndexedString is = new IndexedString(testString);
        String testSubstring = "ef";
        int result = is.indexOf(testSubstring.toCharArray());
        int expectedIndex = 10;
        Assert.assertEquals(expectedIndex, result);
    }

    @Test
    public void testIndexOfSubstringEmptyString() {
        String testString = "abccdeeef";
        IndexedString is = new IndexedString(testString);
        String testSubstring = "";
        int result = is.indexOf(testSubstring.toCharArray());
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testIndexOfSubstringNullString() {
        String testString = "abccdeeef";
        IndexedString is = new IndexedString(testString);
        String testSubstring = null;
        int result = is.indexOf(testSubstring.toCharArray());
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testIndexOfSubstringLongerThanString() {
        String testString = "abccdeeef";
        IndexedString is = new IndexedString(testString);
        String testSubstring = "hijklmnop";
        int result = is.indexOf(testSubstring.toCharArray());
        Assert.assertEquals(-1, result);
    }
}