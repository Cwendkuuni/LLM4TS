package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

	@Test
	public void testNumericEntityUnescaper_DefaultOptions() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		String input = "&hellip;abc&nbsp;&#160;&#x201C;&#x201D;&#171; & #65 & #x41 & #x03B4; & #x2190; & #x2191; & #x2192; & #x2193; & #x2194; & #x2195; & #x2196; & #x2197; & #x2198; ";
		StringWriter out = new StringWriter();
		int index = 0;

		for (int length = unescaper.translate(new StringQuasar(input), index, out); length > 0; index += length) {
			String result = out.toString().substring(0, index);
			String expected = input.substring(0, index);
			assertEquals(expected, result);
			input = input.substring(index);
			out.getBuffer().setLength(0);
		}
		assertEquals("", input);
	}

	@Test
	public void testNumericEntityUnescaper_SemiColonRequired() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
		String input = "&hellip;abc&nbsp;&#160;&#x201C;&#x201D;&#171; & #65 & #x41 & #x03B4; & #x2190; & #x2191; & #x2192; & #x2193; & #x2194; & #x2195; & #x2196; & #x2197; & #x2198; ";
		StringWriter out = new StringWriter();
		int index = 0;

		for (int length = unescaper.translate(new StringQuasar(input), index, out); length > 0; index += length) {
			String result = out.toString().substring(0, index);
			String expected = input.substring(0, index);
			if (expected.endsWith(";")) {
				assertEquals(expected, result);
			} else {
				fail("Expected semi-colon at end of numeric entity: " + expected);
			}
			input = input.substring(index);
			out.getBuffer().setLength(0);
		}
		assertEquals("", input);
	}

	@Test
	public void testNumericEntityUnescaper_SemiColonOptional() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
		String input = "&hellip;abc&nbsp;&#160;&#x201C;&#x201D;&#171; & #65 & #x41 & #x03B4; & #x2190; & #x2191; & #x2192; & #x2193; & #x2194; & #x2195; & #x2196; & #x2197; & #x2198; ";
		StringWriter out = new StringWriter();
		int index = 0;

		for (int length = unescaper.translate(new StringQuasar(input), index, out); length > 0; index += length) {
			String result = out.toString().substring(0, index);
			String expected = input.substring(0, index);
			if (expected.endsWith(";")) {
				assertEquals(expected, result);
			} else {
				String[] parts = expected.split("\\s+&[#x\\xc2-\\xf4]");
				assertEquals(parts.length, result.split("\\s+").length);
			}
			input = input.substring(index);
			out.getBuffer().setLength(0);
		}
		assertEquals("", input);
	}

	@Test
	public void testNumericEntityUnescaper_ErrorIfNoSemiColon() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
		String input = "&hellip;abc&nbsp;&#160;&#x201C;&#x201D;&#171; & #65 & #x41 & #x03B4; & #x2190; & #x2191; & #x2192; & #x2193; & #x2194; & #x2195; & #x2196; & #x2197; & #x2198; ";
		StringWriter out = new StringWriter();
		int index = 0;

		try {
			for (int length = unescaper.translate(new StringQuasar(input), index, out); length > 0; index += length) {
				String result = out.toString().substring(0, index);
				String expected = input.substring(0, index);
				if (!expected.endsWith(";")) {
					fail("Expected semi-colon at end of numeric entity: " + expected);
				}
				input = input.substring(index);
				out.getBuffer().setLength(0);
			}
			fail("Expected IllegalArgumentException for missing semi-colon");
		} catch (IllegalArgumentException e) {
			// expected
		}
		assertEquals("", input);
	}

	@Test
	public void testNumericEntityUnescaper_InvalidEntities() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		String input = "&hellip;abc&nbsp;&#160;&#x201C;&#x201D;&#171; & #65 & #x41 & #x03B4; & #x2190 & &#x2198;x & #x2198; & #111111111111; ";
		StringWriter out = new StringWriter();
		int index = 0;

		for (int length = unescaper.translate(new StringQuasar(input), index, out); length > 0; index += length) {
			String result = out.toString().substring(0, index);
			String expected = input.substring(0, index);
			assertEquals(expected, result);
			input = input.substring(index);
			out.getBuffer().setLength(0);
		}
		assertEquals("", input);
	}

	@Test
	public void testNumericEntityUnescaper_HexEntities() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		String input = "&hellip;abc&nbsp;&#160;&#x201C;&#x201D;&#171; & &#x26; & #xa9 & &#x2303; & #x23ce; & #x23d1; & #x23d2; & #x23d3; & #x23d4; & #x23d5; & #x23d6; & #x23d7; & #x23d8; ";
		StringWriter out = new StringWriter();
		int index = 0;

		for (int length = unescaper.translate(new StringQuasar(input), index, out); length > 0; index += length) {
			String result = out.toString().substring(0, index);
			String expected = input.substring(0, index);
			char[] chars = Character.toChars(Integer.parseInt(expected.substring(expected.indexOf("&") + 1, expected.indexOf(";")), 16));
			assertEquals(chars[0], result.charAt(0));
			assertEquals(chars[1], result.charAt(1));
			input = input.substring(index);
			out.getBuffer().setLength(0);
		}
		assertEquals("", input);
	}

	@Test
	public void testNumericEntityUnescaper_MixedEntities() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
		String input = "&hellip;abc&nbsp;&#160;&#x201C;&#x201D;&#171; & &#65; & &#x03B4; & &#x2190; & &#x2198;x & &#x2198; & &#111111111111; ";
		StringWriter out = new StringWriter();
		int index = 0;

		for (int length = unescaper.translate(new StringQuasar(input), index, out); length > 0; index += length) {
			String result = out.toString().substring(0, index);
			String expected = input.substring(0, index);
			if (expected.endsWith(";")) {
				assertEquals(expected, result);
			} else {
				String[] parts = expected.split("\\s+&[#x\\xc2-\\xf4]");
				if (parts.length % 2 == 1) {
					fail("Unexpected odd number of entities: " + expected);
				}
				for (int i = 0; i < parts.length; i += 2) {
					String entity = parts[i];
					char[] chars = Character.toChars(Integer.parseInt(entity.substring(entity.indexOf("&") + 1, entity.indexOf(";")), 10));
					assertEquals(chars[0], result.charAt(i / 2 * 2));
					assertEquals(chars[1], result.charAt(i / 2 * 2 + 1));
				}
			}
			input = input.substring(index);
			out.getBuffer().setLength(0);
		}
		assertEquals("", input);
	}

	private static class StringQuasar implements CharSequence {
		private final String cs;

		public StringQuasar(String cs) {
			this.cs = cs;
		}

		@Override
		public int length() {
			return cs.length();
		}

		@Override
		public char charAt(int index) {
			return cs.charAt(index);
		}

		@Override
		public CharSequence subSequence(int start, int end) {
			return new StringQuasar(cs.substring(start, end));
		}
	}

}