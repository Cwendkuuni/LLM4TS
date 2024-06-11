package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

private char[] getRandomCharArray(int length) {
char[] charArray = new char[length];
for (int i = 0; i < length; i++) {
charArray[i] = (char) ((int) (Math.random() * 256));
}
return charArray;
}

@Test
public void testIndexedStringConstruction() {
String testString = "Lorem ipsum dolor sit amet";
IndexedString indexedString = new IndexedString(testString);
Assert.assertArrayEquals(testString.toCharArray(), indexedString.values);
}

@Test
public void testEmptyStringConstruction() {
IndexedString indexedString = new IndexedString("");
Assert.assertArrayEquals(new char[0], indexedString.values);
}

@Test(expected = IllegalArgumentException.class)
public void testNullStringConstruction() {
IndexedString indexedString = new IndexedString(null);
}

@Test
public void testGetCharAtIndex() {
IndexedString indexedString = new IndexedString("abcdef");
Assert.assertEquals('a', indexedString.values[0]);
Assert.assertEquals('f', indexedString.values[5]);
}

@Test
public void testCountChar() {
IndexedString indexedString = new IndexedString("Lorem ipsum dolor sit amet");
Assert.assertEquals(8, indexedString.count('m'));
Assert.assertEquals(0, indexedString.count('z'));
}

@Test
public void testIndexOfSingleChar() {
IndexedString indexedString = new IndexedString("Lorem ipsum dolor sit amet");
Assert.assertEquals(2, indexedString.indexOf('o'));
Assert.assertEquals(-1, indexedString.indexOf('z'));
}

@Test
public void testIndexOfCharArray() {
IndexedString indexedString = new IndexedString("Lorem ipsum dolor sit amet");
char[] target = {'o', 'r'};
Assert.assertEquals(5, indexedString.indexOf(target));
char[] target2 = {'z', 'x'};
Assert.assertEquals(-1, indexedString.indexOf(target2));
}

@Test
public void testIndexOfCharArrayWithMisMatchingChar() {
IndexedString indexedString = new IndexedString("Lorem ipsum dolor sit amet");
char[] target = {'o', 'r', 'x'};
Assert.assertEquals(-1, indexedString.indexOf(target));
}

@Test
public void testIndexOfCharArrayWithSubstring() {
IndexedString indexedString = new IndexedString("Lorem ipsum dolor sit amet");
char[] target = {'i', 'p', 's', 'u'};
Assert.assertEquals(3, indexedString.indexOf(target));
}

@Test
public void testIndexOfCharArrayWithPrefix() {
IndexedString indexedString = new IndexedString("Lorem ipsum dolor sit amet");
char[] target = {'L', 'o', 'r', 'e', 'm'};
Assert.assertEquals(0, indexedString.indexOf(target));
}

@Test
public void testIndexOfCharArrayWithSuffix() {
IndexedString indexedString = new IndexedString("Lorem ipsum dolor sit amet");
char[] target = {'m', 'e', 't'};
Assert.assertEquals(12, indexedString.indexOf(target));
}
}