package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringConstructor() {
        String testString = "testString";
        IndexedString indexedString = new IndexedString(testString);
        Assert.assertArrayEquals(testString.toCharArray(), indexedString.values);
    }

    @Test
    public void testIndexOfEmptyTarget() {
        String testString = "testString";
        IndexedString indexedString = new IndexedString(testString);
        char[] emptyTarget = {};
        Assert.assertFalse(indexedString.indexOf(emptyTarget) >= 0);
    }

    @Test
    public void testIndexOfSingleCharTarget() {
        String testString1 = "testString1";
        IndexedString indexedString1 = new IndexedString(testString1);
        char target1 = 't';
        int result1 = indexedString1.indexOf(new char[]{target1});
        Assert.assertEquals(0, result1);

        String testString2 = "testString2";
        IndexedString indexedString2 = new IndexedString(testString2);
        char target2 = 'x';
        int result2 = indexedString2.indexOf(new char[]{target2});
        Assert.assertEquals(-1, result2);
    }

    @Test
    public void testIndexOfMultiCharTarget() {
        String testString = "testTestString";
        IndexedString indexedString = new IndexedString(testString);
        char[] target = {'t', 'e', 's', 'T'};
        int result = indexedString.indexOf(target);
        Assert.assertEquals(3, result);

        char[] target2 = {'t', 'e', 's', 'T', 'g'};
        int result2 = indexedString.indexOf(target2);
        Assert.assertEquals(-1, result2);
    }

    @Test
    public void testIndexOfOverlappingSubstring() {
        String testString = "testTestString";
        IndexedString indexedString = new IndexedString(testString);
        char[] target = {'t', 'e', 's', 'T'};
        int result = indexedString.indexOf(target);
        Assert.assertEquals(3, result);

        char[] targetOverlapping = {'t', 'e', 's', 'T', 'g'};
        int resultOverlapping = indexedString.indexOf(targetOverlapping);
        Assert.assertEquals(-1, resultOverlapping);

        char[] targetSubstring = {'e', 's', 'T'};
        int resultSubstring = indexedString.indexOf(targetSubstring);
        Assert.assertEquals(3, resultSubstring);
    }
}