package de.beiri22.stringincrementor.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {

	@Test
	public void testIndexedStringConstruction() {
		String s = "hello world";
		IndexedString is = new IndexedString(s);
		assertEquals(s.length(), is.values.length);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testIndexedStringConstructionWithNullString() {
		new IndexedString(null);
	}

	@Test
	public void testCountCharacter() {
		IndexedString is = new IndexedString("hello world");
		assertEquals(5, is.count('l'));
		assertEquals(11, is.count('o'));
		assertEquals(1, is.count('x'));
	}

	@Test
	public void testIndexOfEmptyArray() {
		IndexedString is = new IndexedString("hello world");
		assertNull(is.indexOf(new char[0]));
	}

	@Test
	public void testIndexOfSingleCharacter() {
		IndexedString is = new IndexedString("hello world");
		assertEquals(4, is.indexOf(new char[]{'l'}));
		assertEquals(0, is.indexOf(new char[]{'h'}));
		assertEquals(-1, is.indexOf(new char[]{'z'}));
	}

	@Test
	public void testIndexOfMultipleCharacters() {
		IndexedString is = new IndexedString("hello world");
		assertArrayEquals(new int[] {4, 5}, is.indexOf(new char[]{'l', 'o'}));
		assertArrayEquals(new int[] {0, 5}, is.indexOf(new char[]{'h', 'o'}));
		assertArrayEquals(new int[] {-1, -1}, is.indexOf(new char[]{'z', 'x'}));
	}

	@Test
	public void testIndexOfOverlappingSubstring() {
		IndexedString is = new IndexedString("hello world");
		assertEquals(4, is.indexOf(new char[]{'l', 'o'}));
		assertEquals(5, is.indexOf(new char[]{'l', 'o', 'l'}));
		assertEquals(6, is.indexOf(new char[]{'l', 'o', 'l', 'd'}));
	}

	@Test
	public void testIndexOfSubstringNotPresent() {
		IndexedString is = new IndexedString("hello world");
		assertEquals(-1, is.indexOf(new char[]{'z', 'z'}));
	}

	@Test (expected = IllegalArgumentException.class)
	public void testIndexOfNullCharacterArray() {
		IndexedString is = new IndexedString("hello world");
		is.indexOf(null);
	}
}