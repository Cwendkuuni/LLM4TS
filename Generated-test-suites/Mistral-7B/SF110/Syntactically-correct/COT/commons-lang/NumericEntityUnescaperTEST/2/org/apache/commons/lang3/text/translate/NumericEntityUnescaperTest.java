package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;

import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;

public class NumericEntityUnescaperTest {

	@Test
	public void testNumericEntityUnescaperConstructorWithOptions() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
		assertTrue(unescaper.isSet(OPTION.semiColonRequired));
		assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
	}

	@Test
	public void testNumericEntityUnescaperConstructorWithoutOptions() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		assertFalse(unescaper.isSet(OPTION.semiColonRequired));
		assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));
	}

	@Test
	public void testIsSet() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
		assertTrue(unescaper.isSet(OPTION.semiColonRequired));
		assertFalse(unescaper.isSet(OPTION.semiColonOptional));
		assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));
	}

	@Test
	public void testTranslateBasic() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "This is a &amp; test.";
		assertEquals(0, unescaper.translate(input, 0, out));
		assertEquals("This is a & test.", out.toString());
	}

	@Test
	public void testTranslateNumericEntity() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "This is a &amp;#x1F60A; test.";
		assertEquals(3, unescaper.translate(input, 0, out));
		assertEquals("This is a 🌍 test.", out.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTranslateMissingSemiColon() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
		StringWriter out = new StringWriter();
		CharSequence input = "This is a &amp;#x1F60A;";
		unescaper.translate(input, 0, out);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTranslateInvalidNumericEntity() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "This is a &amp;#abc; test.";
		unescaper.translate(input, 0, out);
	}

	@Test(expected = NumberFormatException.class)
	public void testTranslateInvalidHex() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "This is a &amp;#x invalid; test.";
		unescaper.translate(input, 0, out);
	}

	@Test
	public void testTranslateLongNumericEntity() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "This is a &amp;#65536 test.";
		assertEquals(3, unescaper.translate(input, 0, out));
		assertEquals("This is a 𠀀 test.", out.toString());
	}

	@Test
	public void testTranslateLongNumericEntityWithHex() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "This is a &amp;#xFFFF; test.";
		assertEquals(3, unescaper.translate(input, 0, out));
		assertEquals("This is a  test.", out.toString());
	}

	@Test
	public void testTranslateSemiColonRequired() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
		StringWriter out = new StringWriter();
		CharSequence input = "This is a &amp;#x1F60A;";
		assertEquals(4, unescaper.translate(input, 0, out));
		assertEquals("This is a 🌍;\n", out.toString());
	}

	@Test
	public void testTranslateSemiColonOptional() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
		StringWriter out = new StringWriter();
		CharSequence input = "This is a &amp;#x1F60A;";
		assertEquals(3, unescaper.translate(input, 0, out));
		assertEquals("This is a 🌍 test.", out.toString());
	}
}