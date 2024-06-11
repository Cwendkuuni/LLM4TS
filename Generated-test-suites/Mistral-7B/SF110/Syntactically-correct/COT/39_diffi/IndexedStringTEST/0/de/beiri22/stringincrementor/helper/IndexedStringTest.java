package de.beiri22.stringincrementor.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {

	@Test
	public void testIndexedStringConstructor() {
		String input = "testString";
		IndexedString is = new IndexedString(input);
		assertEquals(input.length(), is.values.length);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIndexedStringConstructorWithNullInput() {
		new IndexedString(null);
	}

	@Test
	public void testIndexOfSimpleCharacter() {
		IndexedString is = new IndexedString("testString");
		char target = 't';
		int result = is.indexOf(new char[] { target });
		int expected = 0;
		assertEquals(expected, result);
	}

	@Test
	public void testIndexOfEmptyTargetArray() {
		IndexedString is = new IndexedString("testString");
		int result = is.indexOf(new char[] {});
		assertNull(result);
	}

	@Test
	public void testIndexOfSingleCharacterNotPresent() {
		IndexedString is = new IndexedString("testString");
		char target = 'x';
		int result = is.indexOf(new char[] { target });
		assertNull(result);
	}

	@Test
	public void testIndexOfMultipleCharactersPresent() {
		IndexedString is = new IndexedString("testStringtestString");
		char[] target = { 't', 's' };
		int result = is.indexOf(target);
		int[] expected = { 0, 5 };
		assertArrayEquals(expected, new int[] { result, result + 1 });
	}

	@Test
	public void testIndexOfMultipleCharactersNotPresent() {
		IndexedString is = new IndexedString("testStringtestString");
		char[] target = { 'x', 'x' };
		int result = is.indexOf(target);
		assertNull(result);
	}

	@Test
	public void testIndexOfSubstringPresent() {
		IndexedString is = new IndexedString("testStringtestString");;
		String target = "String";
		int result = is.indexOf(target.toCharArray());
		int expected = 5;
		assertEquals(expected, result);
	}

	@Test
	public void testIndexOfSubstringNotPresent() {
		IndexedString is = new IndexedString("testStringtestString");
		String target = "Test";
		int result = is.indexOf(target.toCharArray());
		assertNull(result);
	}

	@Test
	public void testIndexOfSubstringWithLeadingAndTrailingSpaces() {
		IndexedString is = new IndexedString(" testString testString ");
		String target = " String ";
		int result = is.indexOf(target.toCharArray());
		int expected = 2;
		assertEquals(expected, result);
	}

	@Test
	public void testIndexOfSubstringWithMidSpaces() {
		IndexedString is = new IndexedString(" testString testString ");
		String target = "String test";
		int result = is.indexOf(target.toCharArray());
		int expected = 1;
		assertEquals(expected, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIndexOfTargetBeyondStringSize() {
		IndexedString is = new IndexedString("testString");
		char[] target = new char[is.values.length + 1];
		is.indexOf(target);
	}
}