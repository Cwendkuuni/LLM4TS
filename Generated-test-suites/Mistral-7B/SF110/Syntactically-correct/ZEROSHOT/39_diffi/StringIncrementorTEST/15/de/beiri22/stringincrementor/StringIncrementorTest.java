package de.beiri22.stringincrementor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest {

	@Test
	public void testDiff_EmptyStrings() {
		String a = "";
		String b = "";
		RelativeString result = StringIncrementor.diff(a, b);
		assertNull(result);
	}

	@Test
	public void testDiff_IdenticalStrings() {
		String a = "Hallo Welt";
		String b = "Hallo Welt";
		RelativeString result = StringIncrementor.diff(a, b);
		assertNull(result);
	}

	@Test
	public void testDiff_SingleCharacterDifference() {
		String a = "Hallo Welt";
		String b = "Hallo ABC";
		RelativeString result = StringIncrementor.diff(a, b);
		assertEquals(1, result.linksCount());
		StringLink link = result.getLink(0);
		assertEquals(4, link.getPosOrig());
		assertEquals(4, link.getPosNew());
		assertEquals(3, link.getLen());
	}

	@Test
	public void testDiff_MultipleDifferences() {
		String a = "Hallo Welt";
		String b = "Hallo ABCDEFG";
		RelativeString result = StringIncrementor.diff(a, b);
		assertEquals(2, result.linksCount());
		StringLink link1 = result.getLink(0);
		assertEquals(4, link1.getPosOrig());
		assertEquals(4, link1.getPosNew());
		assertEquals(3, link1.getLen());
		StringLink link2 = result.getLink(1);
		assertEquals(8, link2.getPosOrig());
		assertEquals(13, link2.getPosNew());
		assertEquals(5, link2.getLen());
	}

	@Test
	public void testDiff_VerboseMode() {
		String a = "Hallo Welt";
		String b = "Hallo ABCDEFG";
		String expectedOutput = "Link: StringLink[posOrig=4, posNew=4, len=3] @ 0%\n" +
				"Link: StringLink[posOrig=8, posNew=13, len=5] @ 66.66666666666666%\n";
		StringIncrementor.diff(a, b, true);
		try (ByteArrayInputStream input = new ByteArrayInputStream(expectedOutput.getBytes(StandardCharsets.UTF_8));
				BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
			String output = reader.readLine();
			assertEquals(expectedOutput, output);
		}
	}

	@Test
	public void testPatch_EmptyStrings() {
		String a = "";
		RelativeString r = new RelativeString();
		String result = StringIncrementor.patch(a, r);
		assertEquals("", result);
	}

	@Test
	public void testPatch_NoLinks() {
		String a = "Hallo Welt";
		RelativeString r = new RelativeString();
		String result = StringIncrementor.patch(a, r);
		assertEquals(a, result);
	}

	@Test
	public void testPatch_SingleLink() {
		String a = "Hallo Welt";
		RelativeString r = new RelativeString();
		r.addLink(new StringLink(4, 4, 3));
		String result = StringIncrementor.patch(a, r);
		assertEquals("Hallo ABC", result);
	}

	@Test
	public void testPatch_MultipleLinks() {
		String a = "Hallo Welt";
		RelativeString r = new RelativeString();
		r.addLink(new StringLink(4, 4, 3));
		r.addLink(new StringLink(8, 13, 5));
		String result = StringIncrementor.patch(a, r);
		assertEquals("Hallo ABCDEFG", result);
	}

	@Test
	public void testPatch_VerboseMode() {
		String a = "Hallo Welt";
		RelativeString r = new RelativeString();
		r.addLink(new StringLink(4, 4, 3));
		r.addLink(new StringLink(8, 13, 5));
		String expectedOutput = "Adding 3 absolute Bytes from pos#0\n" +
				"Adding Link: StringLink[posOrig=4, posNew=4, len=3]\n" +
				"Adding 5 absolute Bytes from pos#1\n" +
				"Adding Link: StringLink[posOrig=8, posNew=13, len=5]\n" +
				"Adding 3 absolute Bytes from pos#13\n";
		StringIncrementor.patch(a, r, true);
		try (ByteArrayInputStream input = new ByteArrayInputStream(expectedOutput.getBytes(StandardCharsets.UTF_8));
				BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
			String output = reader.readLine();
			assertEquals(expectedOutput, output);
		}
	}

	@Test
	public void testDiff_NegativeInput() {
		String a = "Hallo Welt";
		String b = "";
		assertNull(StringIncrementor.diff(a, b));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDiff_NullInput() {
		String a = null;
		String b = "Hallo Welt";
		StringIncrementor.diff(a, b);
	}

	@Test
	public void testPatch_NegativeInput() {
		String a = "Hallo Welt";
		RelativeString r = new RelativeString();
		assertNull(StringIncrementor.patch(a, r));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPatch_NullInput() {
		String a = "Hallo Welt";
		RelativeString r = null;
		StringIncrementor.patch(a, r);
	}
}