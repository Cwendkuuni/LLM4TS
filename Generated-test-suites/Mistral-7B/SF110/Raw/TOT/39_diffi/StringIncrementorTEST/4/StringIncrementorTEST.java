package de.beiri22.stringincrementor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.helper.StringLink;
import de.beiri22.stringincrementor.relativestring.ExtBoolArray;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest {

	@Test
	public void testDiff_SameStrings() {
		String a = "abcdefghijklmnopqrstuvwxyz";
		String b = "abcdefghijklmnopqrstuvwxyz";
		RelativeString result = StringIncrementor.diff(a, b);
		assertNull(result);
	}

	@Test
	public void testDiff_EmptyString() {
		String a = "";
		String b = "abcdefghijklmnopqrstuvwxyz";
		RelativeString result = StringIncrementor.diff(a, b);
		assertEquals(new RelativeString(""), result);
	}

	@Test
	public void testDiff_Substring() {
		String a = "abcdefghijklmnopqrstuvwxyz";
		String b = "xyzabcdefghijklmnopqrstuvwxyz";
		RelativeString result = StringIncrementor.diff(a, b);
		assertEquals(new StringLink(10, 3, 3), result.getLink(0));
		assertEquals(new StringLink(22, 21, 1), result.getLink(1));
	}

	@Test
	public void testDiff_Error_NegativeLength() {
		String a = "abcdefghijklmnopqrstuvwxyz";
		String b = "abcdefghijklmnopqrstuvwxyz";
		try {
			StringIncrementor.diff(a, b, -1);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testDiff_Error_Null() {
		String a = "abcdefghijklmnopqrstuvwxyz";
		try {
			StringIncrementor.diff(a, null);
			fail("Expected NullPointerException");
		} catch (NullPointerException e) {
			// expected
		}
	}

	@Test
	public void testDiff_Verbose_SameStrings() {
		String a = "abcdefghijklmnopqrstuvwxyz";
		String b = "abcdefghijklmnopqrstuvwxyz";
		System.setOut(new PrintStream(new ByteArrayOutputStream()));
		RelativeString result = StringIncrementor.diff(a, b, true);
		assertEquals(new RelativeString(""), result);
		assertNull(System.out.flushBufferStr());
	}

	@Test
	public void testDiff_Verbose_Substring() {
		String a = "abcdefghijklmnopqrstuvwxyz";
		String b = "xyzabcdefghijklmnopqrstuvwxyz";
		System.setOut(new PrintStream(new ByteArrayOutputStream()));
		RelativeString result = StringIncrementor.diff(a, b, true);
		assertEquals(new StringLink(10, 3, 3), result.getLink(0));
		assertEquals(new StringLink(22, 21, 1), result.getLink(1));
		assertEquals("Link: @ 25%\nAdding 9 absolute Bytes from pos#0\nAdding Link: [10, 3, 3]\nAdding Link: @ 75%\nAdding 1 absolute Bytes from pos#11\nAdding Link: [22, 21, 1]\n", System.out.flushBufferStr());
	}

	@Test
	public void testPatch_EmptyString() {
		String a = "";
		RelativeString r = new RelativeString();
		String result = StringIncrementor.patch(a, r);
		assertEquals("", result);
	}

	@Test
	public void testPatch_NoLinks() {
		String a = "abcdefghijklmnopqrstuvwxyz";
		RelativeString r = new RelativeString();
		String result = StringIncrementor.patch(a, r);
		assertEquals("abcdefghijklmnopqrstuvwxyz", result);
	}

	@Test
	public void testPatch_SingleLink() {
		String a = "abcdefghijklmnopqrstuvwxyz";
		RelativeString r = new RelativeString();
		r.addLink(new StringLink(10, 3, 3));
		String result = StringIncrementor.patch(a, r);
		assertEquals("abcdefabcdefghijklmnopqrstuvwxyz", result);
	}

	@Test
	public void testPatch_MultipleLinks() {
		String a = "abcdefghijklmnopqrstuvwxyz";
		RelativeString r = new RelativeString();
		r.addLink(new StringLink(10, 3, 3));
		r.addLink(new StringLink(22, 21, 1));
		String result = StringIncrementor.patch(a, r);
		assertEquals("abcxydzefghijklmnopqrstuvwxyz", result);
	}

	@Test
	public void testPatch_Error_Null() {
		String a = "abcdefghijklmnopqrstuvwxyz";
		try {
			StringIncrementor.patch(a, null);
			fail("Expected NullPointerException");
		} catch (NullPointerException e) {
			// expected
		}
	}

	@Test
	public void testPatch_Error_InvalidLinkPosition() {
		String a = "abcdefghijklmnopqrstuvwxyz";
		RelativeString r = new RelativeString();
		r.addLink(new StringLink(-1, 3, 3));
		try {
			StringIncrementor.patch(a, r);
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			// expected
		}
	}

	@Test
	public void testPatch_Verbose_SingleLink() {
		String a = "abcdefghijklmnopqrstuvwxyz";
		RelativeString r = new RelativeString();
		r.addLink(new StringLink(10, 3, 3));
		System.setOut(new PrintStream(new ByteArrayOutputStream()));
		String result = StringIncrementor.patch(a, r, true);
		assertEquals("abcdefabcdefghijklmnopqrstuvwxyz", result);
		assertEquals("Adding 9 absolute Bytes from pos#0\n", System.out.flushBufferStr());
	}

	@Test
	public void testPatch_Verbose_MultipleLinks() {
		String a = "abcdefghijklmnopqrstuvwxyz";
		RelativeString r = new RelativeString();
		r.addLink(new StringLink(10, 3, 3));
		r.addLink(new StringLink(22, 21, 1));
		System.setOut(new PrintStream(new ByteArrayOutputStream()));
		String result = StringIncrementor.patch(a, r, true);
		assertEquals("abcxydzefghijklmnopqrstuvwxyz", result);
		assertEquals("Adding 9 absolute Bytes from pos#0\nAdding Link: @ 25%\nAdding 3 absolute Bytes from pos#10\nAdding 1 absolute Bytes from pos#22\n", System.out.flushBufferStr());
	}

}