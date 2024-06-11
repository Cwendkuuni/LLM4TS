package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

	@Test
	public void testTranslateWithDefaultOptions() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

		String input = "&num1;&num2;";
		StringWriter output = new StringWriter();

		int length = unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
		String result = output.toString();

		assertEquals("num1num2", result);
		assertEquals(12, length);
	}

	@Test
	public void testTranslateWithSemiColonRequiredOption() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

		String input = "&num1;&num2";
		StringWriter output = new StringWriter();

		try {
			unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
		}

		input = "&num1;&num2;";
		length = unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
		result = output.toString();

		assertEquals("num1;num2", result);
		assertEquals(11, length);
	}

	@Test
	public void testTranslateWithSemiColonOptionalOption() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);

		String input = "&num1;&num2";
		StringWriter output = new StringWriter();

		length = unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
		result = output.toString();

		assertEquals("num1num2", result);
		assertEquals(12, length);
	}

	@Test
	public void testTranslateWithErrorIfNoSemiColonOption() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

		String input = "&num1";
		StringWriter output = new StringWriter();

		try {
			unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
		}

		input = "&num1;&num2";
		length = unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
		result = output.toString();

		assertEquals("num1num2", result);
		assertEquals(12, length);
	}

	@Test
	public void testTranslateHexEntitie() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

		String input = "&#x4142;&#x4343;";
		StringWriter output = new StringWriter();

		length = unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
		result = output.toString();

		assertEquals("ABCD", result);
		assertEquals(6, length);
	}

	@Test
	public void testTranslateHexEntitieWithSemiColonRequiredOption() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

		String input = "&#x4142;&#x4343";
		StringWriter output = new StringWriter();

		try {
			unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
		}

		input = "&#x4142;&#x4343;";
		length = unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
		result = output.toString();

		assertEquals("ABCD", result);
		assertEquals(6, length);
		assertEquals("ABCD", result.substring(0, result.length() - 1));
		assertEquals(";", result.charAt(result.length() - 1));
	}

	@Test
	public void testTranslateHexEntitieWithSemiColonOptionalOption() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);

		String input = "&#x4142;&#x4343";
		StringWriter output = new StringWriter();

		length = unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
		result = output.toString();

		assertEquals("ABCD", result);
		assertEquals(6, length);
	}

	@Test
	public void testTranslateHexEntitieWithErrorIfNoSemiColonOption() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

		String input = "#x4142#x4343";
		StringWriter output = new StringWriter();

		try {
			unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
		}

		input = "&#x4142;&#x4343;";
		length = unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
		result = output.toString();

		assertEquals("ABCD", result);
		assertEquals(6, length);
		assertEquals("ABCD", result.substring(0, result.length() - 1));
		assertEquals(";", result.charAt(result.length() - 1));
	}

	@Test
	public void testTranslateDecimalEntitie() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

		String input = "&num1;&#10;&#13;&#32;&#160;&#767;&#8203;&#8282;&#153;";
		StringWriter output = new StringWriter();

		length = unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
		result = output.toString();

		assertEquals("\n\r  ¶²µ", result);
		assertEquals(11, length);
	}

	@Test
	public void testTranslateDecimalEntitieWithSemiColonRequiredOption() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

		String input = "&num1;&#10;&#13;&#32;&#160;&#767;&#8203;&#8282;&#153";
		StringWriter output = new StringWriter();

		try {
			unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
		}

		input = "&num1;&#10;&#13;&#32;&#160;&#767;&#8203;&#8282;&#153;";
		length = unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
		result = output.toString();

		assertEquals("\n\r  ¶²µ", result);
		assertEquals(11, length);
		assertEquals("\n\r  ¶²µ", result.substring(0, result.length() - 1));
		assertEquals(";", result.charAt(result.length() - 1));
	}

	@Test
	public void testTranslateDecimalEntitieWithSemiColonOptionalOption() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);

		String input = "&num1;&#10;&#13;&#32;&#160;&#767;&#8203;&#8282;&#153";
		StringWriter output = new StringWriter();

		length = unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
		result = output.toString();

		assertEquals("\n\r  ¶²µ", result);
		assertEquals(11, length);
	}

	@Test
	public void testTranslateDecimalEntitieWithErrorIfNoSemiColonOption() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

		String input = "&num1;&#10;&#13;&#32;&#160;&#767;&#8203;&#8282;&#153";
		StringWriter output = new StringWriter();

		try {
			unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
		}

		input = "&num1;&#10;&#13;&#32;&#160;&#767;&#8203;&#8282;&#153;";
		length = unescaper.translate(new StringQuasher.QuasherInput(input), 0, output);
		result = output.toString();

		assertEquals("\n\r  ¶²µ", result);
		assertEquals(11, length);
		assertEquals("\n\r  ¶²µ", result.substring(0, result.length() - 1));
		assertEquals(";", result.charAt(result.length() - 1));
	}
}