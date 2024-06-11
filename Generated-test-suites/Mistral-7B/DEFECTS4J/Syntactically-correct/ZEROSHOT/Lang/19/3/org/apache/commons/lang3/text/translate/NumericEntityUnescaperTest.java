package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.util.Arrays;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * JUnit 4 test file for the {@link NumericEntityUnescaper} class.
 */
public class NumericEntityUnescaperTest {

	/**
	 * Test translate method with valid input and hex code point.
	 */
	@Test
	public void testTranslateHex() {
		StringWriter writer = new StringWriter();
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

		String input = "&#[xAA]BB;";
		int length = unescaper.translate(new StringReader(input), 0, writer);

		assertEquals("AA BB", writer.toString());
		assertEquals(3 + 2, length);
	}

	/**
	 * Test translate method with valid input and decimal code point.
	 */
	@Test
	public void testTranslateDecimal() {
		StringWriter writer = new StringWriter();
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

		String input = "&#[123]";
		int length = unescaper.translate(new StringReader(input), 0, writer);

		assertEquals("ï»,", writer.toString());
		assertEquals(3 + 1, length);
	}

	/**
	 * Test translate method with invalid input.
	 */
	@Test
	public void testTranslateInvalidInput() {
		StringWriter writer = new StringWriter();
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

		String input = "&[xAB]; &#[123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ]; &#[123456789ABCDEFGHIJKLMNOPQRSTUVWXYZx]; &#[123456789ABCDEFGHIJKLMNOPQRSTUVWXYZxAB]; &#[123456789ABCDEFGHIJKLMNOPQRSTUVWXYZxAB;";
		int length = unescaper.translate(new StringReader(input), 0, writer);

		assertEquals("", writer.toString());
		assertEquals(0, length);
	}

	/**
	 * Test translate method with invalid hex input.
	 */
	@Test(expected = NumberFormatException.class)
	public void testTranslateInvalidHexInput() {
		StringWriter writer = new StringWriter();
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

		String input = "&#[xABC];";
		unescaper.translate(new StringReader(input), 0, writer);
	}

	/**
	 * Test translate method with invalid decimal input.
	 */
	@Test(expected = NumberFormatException.class)
	public void testTranslateInvalidDecimalInput() {
		StringWriter writer = new StringWriter();
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

		String input = "&#[12345];";
		unescaper.translate(new StringReader(input), 0, writer);
	}

	/**
	 * Test translate method with input containing multiple entities.
	 */
	@Test
	public void testTranslateMultipleEntities() {
		StringWriter writer = new StringWriter();
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

		String input = "&#[xAA]BB &#[123] CD &#[xDE]EF;";
		int length = unescaper.translate(new StringReader(input), 0, writer);

		assertEquals("AA BB CD ï», EF", writer.toString());
		assertEquals(12 + 3, length);
	}

	/**
	 * Test translate method with input containing multiple entities and whitespace.
	 */
	@Test
	public void testTranslateMultipleEntitiesWithWhitespace() {
		StringWriter writer = new StringWriter();
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

		String input = "&#[xAA]BB &#[123] CD &#[xDE]EF &nbsp;";
		int length = unescaper.translate(new StringReader(input), 0, writer);

		assertEquals("AA BB CD ï», EF ", writer.toString());
		assertEquals(13 + 3, length);
	}

	/**
	 * Test translate method with input containing multiple entities, whitespace, and comments.
	 */
	@Test
	public void testTranslateMultipleEntitiesWithWhitespaceAndComments() {
		StringWriter writer = new StringWriter();
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

		String input = "&#[xAA]BB &#[123] CD &#[xDE]EF &nbsp; <!-- This is a comment -->&#x00A0;";
		int length = unescaper.translate(new StringReader(input), 0, writer);

		assertEquals("AA BB CD ï», EF ", writer.toString());
		assertEquals(13 + 3, length);
	}

	/**
	 * Test translate method with input containing multiple entities, whitespace, comments, and CDATA sections.
	 */
	@Test
	public void testTranslateMultipleEntitiesWithWhitespaceAndCDATA() {
		StringWriter writer = new StringWriter();
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

		String input = "&#[xAA]BB &#[123] CD &#[xDE]EF &nbsp; <!-- This is a comment --> <![CDATA[ <&-- This is a CDATA section -->> ]]>";
		int length = unescaper.translate(new StringReader(input), 0, writer);

		assertEquals("AA BB CD ï», EF ", writer.toString());
		assertEquals(13 + 3, length);
	}

	/**
	 * Test translate method with input containing multiple entities, whitespace, comments, CDATA sections, and other entities.
	 */
	@Test
	public void testTranslateMultipleEntitiesWithWhitespaceAndMixedEntities() {
		StringWriter writer = new StringWriter();
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

		String input = "&#[xAA]BB &#[123] CD &#[xDE]EF &nbsp; <!-- This is a comment --> <![CDATA[ <&-- This is a CDATA section -->> ]]> &amp; &apos; &quot;";
		int length = unescaper.translate(new StringReader(input), 0, writer);

		assertEquals("AA BB CD ï», EF &amp; &apos; &quot;", writer.toString());
		assertEquals(21 + 3, length);
	}
}

class StringReader extends StringBufferReader {
	public StringReader(String string) {
		super(string);
	}
}