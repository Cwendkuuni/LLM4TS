package de.beiri22.stringincrementor.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IndexedStringTest {

private IndexedString indexedString;

@Before
public void setUp() {
indexedString = new IndexedString("teststring12345teststring");
}

@After
public void tearDown() {
indexedString = null;
}

@Test
public void testIndexedStringConstructor() {
char[] values = indexedString.values;
int[][] index = indexedString.index;
assertEquals(11, values.length);
assertNotNull(index);
for (char c = '\u0000'; c < '\u0100'; c++) {
assertTrue(index[c] != null);
}
}

@Test
public void testCountChar() {
assertEquals(3, indexedString.count('t'));
assertEquals(2, indexedString.count('s'));
assertEquals(1, indexedString.count('1'));
assertEquals(1, indexedString.count('5'));
}

@Test
public void testIndexOfSingleChar() {
assertEquals(0, indexedString.indexOf('t'));
assertEquals(5, indexedString.indexOf('s'));
assertEquals(-1, indexedString.indexOf('z'));
}

@Test
public void testIndexOfCharArray() {
char[] target = {'t', 'e', 's', 't', 's'};
assertEquals(0, indexedString.indexOf(target));
char[] target2 = {'t', 'e', 's', 't', 's', 'g'};
assertEquals(-1, indexedString.indexOf(target2));
}

@Test(expected = IllegalArgumentException.class)
public void testIndexOfNullCharArray() {
indexedString.indexOf(null);
}

@Test(expected = ArrayIndexOutOfBoundsException.class)
public void testIndexOfCharArrayOutOfBounds() {
char[] target = new char[12];
for (int i = 0; i < target.length; i++) {
target[i] = 't';
}
indexedString.indexOf(target);
}
}