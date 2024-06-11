package de.beiri22.stringincrementor.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {

	@Test
	public void testIndexedStringConstructor() {
		String inputString = "teststring";
		IndexedString indexedString = new IndexedString(inputString);
		assertNull(indexedString.values);
		assertEquals(inputString.length(), indexedString.values.length);
		assertNull(indexedString.index);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIndexedStringConstructorWithNullInput() {
		String inputString = null;
		try {
			IndexedString indexedString = new IndexedString(inputString);
		} catch (IllegalArgumentException e) {
			throw new AssertionError("Unexpected exception: " + e.getMessage());
		}
		assertNotNull(e);
	}

	@Test
	public void testIndexedStringCountMethod() {
		String inputString = "teststring";
		IndexedString indexedString = new IndexedString(inputString);
		Character c = 't';
		assertEquals(3, indexedString.count(c));
	}

	@Test
	public void testIndexedStringIndexOfMethodWithOneCharacterTarget() {
		String inputString = "testtesttest";
		IndexedString indexedString = new IndexedString(inputString);
		Character targetChar = 't';
		int expectedIndex = 0;
		int actualIndex = indexedString.indexOf(new char[] { targetChar });
		assertEquals(expectedIndex, actualIndex);
	}

	@Test
	public void testIndexedStringIndexOfMethodWithEmptyTarget() {
		String inputString = "testtesttest";
		IndexedString indexedString = new IndexedString(inputString);
		int expectedIndex = -1;
		int actualIndex = indexedString.indexOf(new char[0]);
		assertEquals(expectedIndex, actualIndex);
	}

	@Test
	public void testIndexedStringIndexOfMethodWithTargetNotInString() {
		String inputString = "testtesttest";
		IndexedString indexedString = new IndexedString(inputString);
		Character targetChar = 'x';
		int expectedIndex = -1;
		int actualIndex = indexedString.indexOf(new char[] { targetChar });
		assertEquals(expectedIndex, actualIndex);
	}

	@Test
	public void testIndexedStringIndexOfMethodWithTargetPartiallyInString() {
		String inputString = "testtesttesttesttesttesttest";
		IndexedString indexedString = new IndexedString(inputString);
		char[] target = new char[] { 't', 'e', 's', 't' };
		int expectedIndex = 5;
		int actualIndex = indexedString.indexOf(target);
		assertEquals(expectedIndex, actualIndex);
	}

	@Test
	public void testIndexedStringIndexOfMethodWithTargetCompletelyInString() {
		String inputString = "testtesttesttesttesttesttesttesttesttesttesttest";
		IndexedString indexedString = new IndexedString(inputString);
		char[] target = new char[] { 't', 'e', 's', 't', 'e', 's', 't', 'e', 's', 't', 'e', 's', 't' };
		int expectedIndex = 0;
		int actualIndex = indexedString.indexOf(target);
		assertEquals(expectedIndex, actualIndex);
	}

	@Test
	public void testIndexedStringIndexOfMethodWithTargetPartiallyInStringStartingFromMiddle() {
		String inputString = "testtesttesttesttesttesttesttesttesttesttesttest";
		IndexedString indexedString = new IndexedString(inputString);
		char[] target = new char[] { 's', 't', 'e' };
		int expectedIndex = 11;
		int actualIndex = indexedString.indexOf(target);
		assertEquals(expectedIndex, actualIndex);
	}

	@Test
	public void testIndexedStringIndexOfMethodWithTargetPartiallyInStringStartingFromEnd() {
		String inputString = "testtesttesttesttesttesttesttesttesttesttesttest";
		IndexedString indexedString = new IndexedString(inputString);
		char[] target = new char[] { 't', 'e', 's', 't' };
		int expectedIndex = 11;
		int actualIndex = indexedString.indexOf(target);
		assertEquals(expectedIndex, actualIndex);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIndexedStringIndexOfMethodWithNullTarget() {
		String inputString = "testtesttesttesttesttesttesttesttesttesttesttest";
		IndexedString indexedString = new IndexedString(inputString);
		try {
			indexedString.indexOf(null);
		} catch (IllegalArgumentException e) {
			throw new AssertionError("Unexpected exception: " + e.getMessage());
		}
		assertNotNull(e);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testIndexedStringIndexOfMethodWithTargetOutOfRange() {
		String inputString = "testtesttesttesttesttesttesttesttesttesttesttest";
		IndexedString indexedString = new IndexedString(inputString);
		char[] target = new char[inputString.length() + 1];
		try {
			indexedString.indexOf(target);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new AssertionError("Unexpected exception: " + e.getMessage());
		}
		assertNotNull(e);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testIndexedStringIndexOfMethodWithNegativeTargetLength() {
		String inputString = "testtesttesttesttesttesttesttesttesttesttesttest";
		IndexedString indexedString = new IndexedString(inputString);
		char[] target = new char[inputString.length() * -1];
		try {
			indexedString.indexOf(target);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new AssertionError("Unexpected exception: " + e.getMessage());
		}
		assertNotNull(e);
	}
}