package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

public class OptionTest {

	@Test
	public void testOptionWithNoArg() throws Exception {
		Option option = new Option(null, "testOption", false, "This is a test option with no argument.");
		assertEquals("testOption", option.getOpt());
		assertEquals("This is a test option with no argument.", option.getDescription());
		assertFalse(option.hasArg());
		assertFalse(option.hasLongOpt());
		assertFalse(option.isRequired());
		assertNull(option.getValue());
		assertFalse(option.hasValueSeparator());
		assertNull(option.getType());
		assertEquals(Boolean.TYPE, option.getTypeClass());
		assertFalse(option.hasArgName());
		assertEquals("", option.getArgName());
	}

	@Test
	public void testOptionWithOneArg() throws Exception {
		Option option = OptionBuilder.withArgName("argName").hasArg().isRequired().withDescription("Test option with one argument.")
				.create("testOption");
		assertEquals("testOption", option.getOpt());
		assertEquals("Test option with one argument.", option.getDescription());
		assertTrue(option.hasArg());
		assertFalse(option.hasLongOpt());
		assertTrue(option.isRequired());
		assertNull(option.getValue());
		assertFalse(option.hasValueSeparator());
		assertNull(option.getType());
		assertEquals("argName", option.getArgName());
		assertTrue(option.hasArgName());
		assertNull(option.getTypeClass());
	}

	@Test
	public void testOptionWithOneOptionalArg() throws Exception {
		Option option = OptionBuilder.withArgName("argName").hasOptionalArg().isRequired(false)
				.withDescription("Test option with one optional argument.")
				.create("testOption");
		assertEquals("testOption", option.getOpt());
		assertEquals("Test option with one optional argument.", option.getDescription());
		assertTrue(option.hasArg());
		assertFalse(option.isRequired());
		assertNull(option.getValue());
		assertFalse(option.hasValueSeparator());
		assertNull(option.getType());
		assertEquals("argName", option.getArgName());
		assertTrue(option.hasArgName());
		assertNull(option.getTypeClass());
	}

	@Test
	public void testOptionWithLongOpt() throws Exception {
		Option option = OptionBuilder.withLongOpt("testLongOption").hasArg().isRequired(false)
				.withDescription("Test option with long option")
				.create("t");
		assertEquals("t", option.getOpt());
		assertEquals("Test option with long option", option.getDescription());
		assertTrue(option.hasArg());
		assertFalse(option.isRequired());
		assertFalse(option.hasLongOpt());
		assertEquals("testLongOption", option.getLongOpt());
		assertNull(option.getValue());
		assertFalse(option.hasValueSeparator());
		assertNull(option.getType());
		assertEquals("", option.getArgName());
		assertFalse(option.hasArgName());
		assertNull(option.getTypeClass());
	}

	@Test
	public void testOptionWithLongOptAndArgName() throws Exception {
		Option option = OptionBuilder.withLongOpt("testLongOption").hasArg().isRequired(false)
				.withDescription("Test option with long option and argName")
				.create("t");
		assertEquals("t", option.getOpt());
		assertEquals("Test option with long option and argName", option.getDescription());
		assertTrue(option.hasArg());
		assertFalse(option.isRequired());
		assertFalse(option.hasLongOpt());
		assertEquals("testLongOption", option.getLongOpt());
		assertEquals("argName", option.getArgName());
		assertTrue(option.hasArgName());
		assertNull(option.getType());
		assertNull(option.getTypeClass());
	}

	@Test
	public void testOptionWithValues() throws Exception {
		Option option = OptionBuilder.withArgName("argName").hasArg().isRequired(false)
				.withDescription("Test option with values")
				.withValues(new String[]{"value1", "value2"})
				.create("t");
		assertEquals("t", option.getOpt());
		assertEquals("Test option with values", option.getDescription());
		assertTrue(option.hasArg());
		assertFalse(option.isRequired());
		assertFalse(option.hasLongOpt());
		assertEquals("argName", option.getArgName());
		assertTrue(option.hasArgName());
		assertEquals(String[].class, option.getTypeClass());
		assertEquals(new String[]{"value1", "value2"}, option.getValues());
		assertNull(option.getValue());
		assertFalse(option.hasValueSeparator());
		assertNull(option.getType());
	}

	@Test
	public void testOptionWithValuesAndValueSeparator() throws Exception {
		Option option = OptionBuilder.withArgName("argName").hasArg().isRequired(false)
				.withDescription("Test option with values and value separator")
				.withValues(new String[]{"value1", "value2"})
				.withValueSeparator('-')
				.create("t");
		assertEquals("t", option.getOpt());
		assertEquals("Test option with values and value separator", option.getDescription());
		assertTrue(option.hasArg());
		assertFalse(option.isRequired());
		assertFalse(option.hasLongOpt());
		assertEquals("argName", option.getArgName());
		assertTrue(option.hasArgName());
		assertEquals(String[].class, option.getTypeClass());
		assertEquals(new String[]{"value1", "value2"}, option.getValues());
		assertEquals('-', option.getValueSeparator());
		assertNull(option.getType());
		assertEquals("value1-value2", option.getValue());
	}

	@Test
	public void testOptionWithValue() throws Exception {
		Option option = OptionBuilder.withArgName("argName").hasArg().isRequired(false)
				.withDescription("Test option with value")
				.withValue("testValue")
				.create("t");
		assertEquals("t", option.getOpt());
		assertEquals("Test option with value", option.getDescription());
		assertTrue(option.hasArg());
		assertFalse(option.isRequired());
		assertFalse(option.hasLongOpt());
		assertEquals("argName", option.getArgName());
		assertTrue(option.hasArgName());
		assertEquals(String.class, option.getTypeClass());
		assertEquals("testValue", option.getValue());
		assertNull(option.getType());
		assertNull(option.getValues());
		assertFalse(option.hasValueSeparator());
	}

	@Test
	public void testOptionCloning() throws Exception {
		Option originalOption = OptionBuilder.withArgName("argName").hasArg().isRequired(false)
				.withDescription("Test option cloning")
				.create("t");
		Option clonedOption = (Option)originalOption.clone();
		assertEquals(originalOption, clonedOption);
		assertTrue(originalOption.equals(clonedOption));
		assertEquals(originalOption.hashCode(), clonedOption.hashCode());
	}

	@Test
	public void testOptionEquals() throws Exception {
		Option option1 = OptionBuilder.withArgName("argName1").hasArg().isRequired(false)
				.withDescription("Test option equals")
				.create("t1");
		Option option2 = OptionBuilder.withArgName("argName2").hasArg().isRequired(false)
				.withDescription("Test option equals")
				.create("t2");
		Option option3 = Option.class.newInstance();
		Option option4 = OptionBuilder.withArgName("argName1").hasArg().isRequired(false)
				.withDescription("Test option equals")
				.create("t1");
		assertTrue(option1.equals(option1));
		assertTrue(option1.equals(option4));
		assertFalse(option1.equals(option2));
		assertFalse(option1.equals(option3));
		assertFalse(option1.equals(null));
	}

	@Test
	public void testOptionToString() throws Exception {
		Option option = OptionBuilder.withArgName("argName").hasArg().isRequired(false)
				.withDescription("Test option toString")
				.create("t");
		String expected = "[ option: t " +
				" [ARG]" +
				" :: Test option toString" +
				"]";
		assertEquals(expected, option.toString());
	}
}