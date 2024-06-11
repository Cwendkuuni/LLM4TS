package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

	@Test
	public void testDefaultConstruction() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		assertEquals(EnumSet.of(NumericEntityUnescaper.OPTION.semiColonRequired), unescaper.options);
	}

	@Test
	public void testCustomConstructionWithOptions() {
		NumericEntityUnescaper.OPTION[] options = { NumericEntityUnescaper.OPTION.semiColonOptional, NumericEntityUnescaper.OPTION.errorIfNoSemiColon };
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(options);
		assertEquals(Arrays.asList(NumericEntityUnescaper.OPTION.semiColonOptional, NumericEntityUnescaper.OPTION.errorIfNoSemiColon), Arrays.asList(unescaper.options.toArray(new NumericEntityUnescaper.OPTION[0])));
	}

	@Test
	public void testIsSet_semiColonRequired() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));

		NumericEntityUnescaper unescaperWithoutSemiColonRequired = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
		assertFalse(unescaperWithoutSemiColonRequired.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
	}

	@Test
	public void testIsSet_semiColonOptional() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));

		NumericEntityUnescaper unescaperWithSemiColonOptional = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
		assertTrue(unescaperWithSemiColonOptional.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
	}

	@Test
	public void testIsSet_errorIfNoSemiColon() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));

		NumericEntityUnescaper unescaperWithErrorIfNoSemiColon = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
		assertTrue(unescaperWithErrorIfNoSemiColon.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
	}

	@Test
	public void testTranslate_emptyString() throws Exception {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter writer = new StringWriter();
		assertEquals(0, unescaper.translate(CharSequence.class.cast(""), 0, writer));
	}

	@Test
	public void testTranslate_noEntity() throws Exception {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter writer = new StringWriter();
		assertEquals(0, unescaper.translate(CharSequence.class.cast("test"), 0, writer));
	}

	@Test
	public void testTranslate_singleEntity_decimal_noSemiColon() throws Exception {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter writer = new StringWriter();
		assertEquals(3, unescaper.translate(CharSequence.class.cast("&amp;#123;"), 0, writer));
		assertEquals("\u007B", writer.toString());
	}

	@Test
	public void testTranslate_singleEntity_decimal_semiColon() throws Exception {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter writer = new StringWriter();
		assertEquals(4, unescaper.translate(CharSequence.class.cast("&amp;#123;"), 0, writer));
		assertEquals("\u007B", writer.toString());
		assertEquals("", writer.getBuffer().toString());
	}

	@Test
	public void testTranslate_singleEntity_hex_noSemiColon() throws Exception {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter writer = new StringWriter();
		assertEquals(4, unescaper.translate(CharSequence.class.cast("&amp;#x41;"), 0, writer));
		assertEquals("A", writer.toString());
	}

	@Test
	public void testTranslate_singleEntity_hex_semiColon() throws Exception {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter writer = new StringWriter();
		assertEquals(5, unescaper.translate(CharSequence.class.cast("&amp;#x41;"), 0, writer));
		assertEquals("A", writer.toString());
		assertEquals("", writer.getBuffer().toString());
	}

	@Test
	public void testTranslate_multipleEntities() throws Exception {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter writer = new StringWriter();
		assertEquals(10, unescaper.translate(CharSequence.class.cast("&amp;#123; &amp;#x41; &amp;#xDF;"), 0, writer));
		assertEquals("\u007B\u0041\u00DF", writer.toString());
	}

	@Test
	public void testTranslate_multipleEntities_semiColonRequired() throws Exception {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
		StringWriter writer = new StringWriter();
		assertEquals(11, unescaper.translate(CharSequence.class.cast("&amp;#123; &amp;#x41; &amp;#xDF;"), 0, writer));
		assertEquals("\u007B\u0041\u00DF\r\n", writer.toString());
		assertEquals("", writer.getBuffer().toString());
	}

	@Test
	public void testTranslate_multipleEntities_errorIfNoSemiColon() throws Exception {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
		StringWriter writer = new StringWriter();
		assertThrows(IllegalArgumentException.class, () -> unescaper.translate(CharSequence.class.cast("&amp;#123; &amp;#x41; &amp;#xDF;"), 0, writer));
	}
}