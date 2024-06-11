package de.beiri22.stringincrementor.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {

	@Test
	public void testIndexedStringConstructor() {
		String s = "abcdefabcde";
		IndexedString is = new IndexedString(s);
		assertEquals(s.length(), is.values.length);
		for (int i = 0; i < is.values.length; ++i) {
			assertEquals(s.charAt(i), is.values[i]);
		}
		for (char c = '\0'; c < '\u0100'; ++c) {
			assertNotNull(is.index[c]);
			assertEquals(2, is.index[c].length);
			assertEquals(is.count(c), is.index[c][0]);
			assertEquals(s.length(), is.index[c][1]);
		}
	}

	@Test
	public void testIndexOfChar() {
		String s = "abcdefabcde";
		IndexedString is = new IndexedString(s);
		for (char c = '\0'; c < '\u0100'; ++c) {
			char[] cc = { c };
			int index = is.indexOf(cc);
			if (index != -1) {
				assertEquals(Integer.toString(index), Arrays.toString(getLocation(s, c)));
			}
		}
	}

	@Test
	public void testIndexOfEmptyArray() {
		String s = "abcdefabcde";
		IndexedString is = new IndexedString(s);
		assertFalse(is.indexOf(new char[0]) >= 0);
	}

	@Test
	public void testIndexOfNullArray() {
		String s = "abcdefabcde";
		IndexedString is = new IndexedString(s);
		assertNull(is.indexOf(null));
	}

	@Test
	public void testIndexOfArrayWithMismatchedLength() {
		String s = "abcdefabcde";
		IndexedString is = new IndexedString(s);
		char[] target = new char[s.length() + 1];
		assertFalse(is.indexOf(target) >= 0);
	}

	@Test
	public void testIndexOfArrayWithNonMatchingChars() {
		String s = "abcba";
		IndexedString is = new IndexedString(s);
		char[] target = new char[] { 'd' };
		assertFalse(is.indexOf(target) >= 0);
	}

	@Test(expected = NullPointerException.class)
	public void testIndexOfNullTarget() {
		String s = "abcdefabcde";
		IndexedString is = new IndexedString(s);
		is.indexOf(null);
	}

	private String getLocation(String s, char c) {
		int index = -1;
		for (int i = 0; i < s.length(); ++i) {
			if (s.charAt(i) == c) {
				index = i;
				break;
			}
		}
		return Integer.toString(index);
	}
}