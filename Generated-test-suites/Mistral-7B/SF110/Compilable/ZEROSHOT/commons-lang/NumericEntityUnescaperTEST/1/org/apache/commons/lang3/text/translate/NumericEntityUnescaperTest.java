package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION.*;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

	@Test
	public void testTranslateWithDefaultOptions() throws Exception {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		String input = "&amp; &#160; &#x20AC; &#xFFFF; &#x1F600; &Hello;&#x1F601;";
		StringWriter out = new StringWriter();
		String expected = "&amp; \u00A0 \u20AC ffffff \uD83C\uDC00 Hello\uD83C\uDC01";
		int index = 0;
		int length = input.length();

		while (index < length) {
			int translatedLength = unescaper.translate(new String(input.toCharArray(), index, length - index), index, out);
			index += translatedLength;
		}

		assertEquals(expected, out.toString());
	}

	@Test
	public void testTranslateWithSemiColonRequiredOption() throws Exception {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(semiColonRequired);
		String input = "&amp; #160; #x20AC; #xFFFF; #x1F600; Hello#x1F601;";
		StringWriter out = new StringWriter();
		String expected = "&amp; \u00A0 ; #x20AC ; #xFFFF ; #x1F600 ; Hello ; \uD83C\uDC01";
		int index = 0;
		int length = input.length();

		while (index < length) {
			int translatedLength = unescaper.translate(new String(input.toCharArray(), index, length - index), index, out);
			index += translatedLength;
		}

		assertEquals(expected, out.toString());
	}

	@Test
	public void testTranslateWithNoSemiColonOption() throws Exception {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(semiColonOptional);
		String input = "&amp; #160; #x20AC; #xFFFF; #x1F600; Hello#x1F601;";
		StringWriter out = new StringWriter();
		String expected = "&amp; \u00A0 #x20AC #xFFFF #x1F600 Hello\uD83C\uDC01";
		int index = 0;
		int length = input.length();

		while (index < length) {
			int translatedLength = unescaper.translate(new String(input.toCharArray(), index, length - index), index, out);
			index += translatedLength;
		}

		assertEquals(expected, out.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTranslateWithErrorIfNoSemiColonOption() throws Exception {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(errorIfNoSemiColon);
		String input = "&amp; #160; #x20AC; #xFFFF; #x1F600; Hello#x1F601";
		StringWriter out = new StringWriter();
		int index = 0;
		int length = input.length();

		while (index < length) {
			unescaper.translate(new String(input.toCharArray(), index, length - index), index, out);
		}
	}

	@Test
	public void testTranslateWithHexValue() throws Exception {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		String input = "&amp; #x2122;";
		StringWriter out = new StringWriter();
		String expected = "&amp; \u00A2";
		int index = 0;
		int length = input.length();

		while (index < length) {
			int translatedLength = unescaper.translate(new String(input.toCharArray(), index, length - index), index, out);
			index += translatedLength;
		}

		assertEquals(expected, out.toString());
	}

	@Test
	public void testTranslateWithNegativeNumber() throws Exception {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		String input = "&amp; #-123;";
		StringWriter out = new StringWriter();
		int index = 0;
		int length = input.length();

		assertFalse(unescaper.translate(new String(input.toCharArray(), index, length - index), index, out) > 0);
		assertEquals("", out.toString());
	}

	@Test
	public void testTranslateWithOutOfRangeNumber() throws Exception {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		String input = "&amp; #123456789012345678901234567890;";
		StringWriter out = new StringWriter();
		int index = 0;
		int length = input.length();

		int translatedLength = unescaper.translate(new String(input.toCharArray(), index, length - index), index, out);
		index += translatedLength;
		char[] chars = Character.toChars(0x110000);
		out.write(chars[0]);
		out.write(chars[1]);
		assertEquals(2 + length - index, translatedLength);
		assertEquals(new String(chars), out.toString());
	}

	@Test
	public void testIsSet() throws Exception {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		assertTrue(unescaper.isSet(semiColonRequired));
		assertTrue(unescaper.isSet(semiColonOptional));
		assertFalse(unescaper.isSet(errorIfNoSemiColon));

		NumericEntityUnescaper unescaperWithOptions = new NumericEntityUnescaper(semiColonRequired, semiColonOptional);
		assertTrue(unescaperWithOptions.isSet(semiColonRequired));
		assertTrue(unescaperWithOptions.isSet(semiColonOptional));
		assertFalse(unescaperWithOptions.isSet(errorIfNoSemiColon));

		NumericEntityUnescaper unescaperWithErrorOption = new NumericEntityUnescaper(errorIfNoSemiColon);
		assertFalse(unescaperWithErrorOption.isSet(semiColonRequired));
		assertFalse(unescaperWithErrorOption.isSet(semiColonOptional));
		assertTrue(unescaperWithErrorOption.isSet(errorIfNoSemiColon));
	}
}