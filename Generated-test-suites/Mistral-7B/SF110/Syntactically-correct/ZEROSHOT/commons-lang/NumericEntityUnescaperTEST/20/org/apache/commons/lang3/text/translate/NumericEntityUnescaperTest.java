package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

	@Test
	public void testEmptyString() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "";
		int length = unescaper.translate(input, 0, out);
		assertEquals(0, length);
		assertEquals("", out.toString());
	}

	@Test
	public void testSingleCharacter() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "A";
		int length = unescaper.translate(input, 0, out);
		assertEquals('A', input.charAt(0));
		assertEquals("A", out.toString());
		assertEquals(1, length);
	}

	@Test
	public void testNumericEntityWithNoSemiColon() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "&num;";
		int length = unescaper.translate(input, 0, out);
		assertEquals(' ', input.charAt(1));
		assertEquals("&num", out.toString());
		assertEquals(1, length);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNumericEntityMissingSemiColonWithErrorOption() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
		StringWriter out = new StringWriter();
		CharSequence input = "&num";
		unescaper.translate(input, 0, out);
	}

	@Test
	public void testNumericEntityWithSemiColon() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "&num;";
		int length = unescaper.translate(input, 0, out);
		assertEquals(' ', input.charAt(1));
		assertEquals("&num;", out.toString());
		assertEquals(1, length);
	}

	@Test
	public void testNumericEntityWithHexValue() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "&#xFFFF;";
		int length = unescaper.translate(input, 0, out);
		assertEquals('ÿ', input.charAt(1));
		assertEquals("&#xFFFF;", out.toString());
		assertEquals(3, length);
	}

	@Test
	public void testNumericEntityWithHexValueAndOptionalSemiColon() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
		StringWriter out = new StringWriter();
		CharSequence input = "&#xFFFF";
		int length = unescaper.translate(input, 0, out);
		assertEquals('ÿ', input.charAt(0));
		assertEquals("&#xFFFF", out.toString());
		assertEquals(2, length);
	}

	@Test
	public void testNumericEntityWithHexValueAndRequiredSemiColon() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
		StringWriter out = new StringWriter();
		CharSequence input = "&#xFFFF";
		try {
			unescaper.translate(input, 0, out);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// Expected
		}
		assertEquals("", out.toString());
		assertEquals(0, length);
	}

	@Test
	public void testNumericEntityWithDecValue() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "&#123;";
		int length = unescaper.translate(input, 0, out);
		assertEquals(' Writer'[123], input.charAt(1));
		assertEquals("&amp;#123;", out.toString());
		assertEquals(3, length);
	}

	@Test
	public void testNumericEntityWithMixedValues() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "Some text &amp;#123;&#xFFFF;text";
		int length = unescaper.translate(input, 0, out);
		assertEquals("Some text &amp;#123;&#xFFFF;text", input.toString());
		assertEquals("Some text &amp;#123;&#xFFFF;text", out.toString());
		assertEquals(input.length(), length);
	}

	@Test
	public void testNumericEntityWithHexValueLongerThanTwoChars() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "&#x1234ab;";
		int length = unescaper.translate(input, 0, out);
		assertEquals('Writer'[0x1234ab], input.charAt(1));
		assertEquals("&amp;#x1234ab;", out.toString());
		assertEquals(3, length);
	}

	@Test
	public void testNumericEntityWithHexValueLongerThanTwoCharsAndOptionalSemiColon() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
		StringWriter out = new StringWriter();
		CharSequence input = "&#x1234ab";
		int length = unescaper.translate(input, 0, out);
		assertEquals('Writer'[0x1234ab], input.charAt(0));
		assertEquals("&amp;#x1234ab", out.toString());
		assertEquals(2, length);
	}

	@Test
	public void testNumericEntityWithHexValueLongerThanTwoCharsAndRequiredSemiColon() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
		StringWriter out = new StringWriter();
		CharSequence input = "&#x1234ab";
		try {
			unescaper.translate(input, 0, out);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// Expected
		}
		assertEquals("", out.toString());
		assertEquals(0, length);
	}
}