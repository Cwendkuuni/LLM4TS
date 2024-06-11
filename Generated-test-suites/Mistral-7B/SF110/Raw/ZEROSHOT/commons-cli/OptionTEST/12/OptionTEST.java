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
	public void testOptionConstructionWithEmptyArgs() {
		Option option = new Option("opt", "description");
		assertEquals("opt", option.getOpt());
		assertEquals("description", option.getDescription());
		assertFalse(option.hasArg());
		assertNull(option.getValue());
		assertNull(option.getLongOpt());
		assertFalse(option.hasOptionalArg());
		assertFalse(option.hasArgName());
		assertFalse(option.isRequired());
		assertFalse(option.hasValues());
		assertEquals(0, option.getArgs());
		assertEquals(Option.UNINITIALIZED, option.getId());
		assertTrue(option.toString().startsWith("[ option:"));
	}

	@Test
	public void testOptionConstructionWithSingleArg() {
		Option option = new Option("opt", "description", true, "arg");
		assertEquals("opt", option.getOpt());
		assertEquals("description", option.getDescription());
		assertTrue(option.hasArg());
		assertEquals("arg", option.getArgName());
		assertNull(option.getValue());
		assertFalse(option.hasLongOpt());
		assertTrue(option.hasOptionalArg());
		assertFalse(option.isRequired());
		assertFalse(option.hasValues());
		assertEquals(1, option.getArgs());
		assertEquals(Option.UNINITIALIZED, option.getId());
		assertTrue(option.toString().startsWith("[ option:"));
	}

	@Test
	public void testOptionConstructionWithLongOptAndSingleArg() {
		Option option = OptionBuilder.withLongOpt("longOpt").withArgName("arg").withDescription("description").build();
		assertEquals("longOpt", option.getLongOpt());
		assertEquals("description", option.getDescription());
		assertTrue(option.hasArgName());
		assertEquals("arg", option.getArgName());
		assertNull(option.getValue());
		assertTrue(option.hasLongOpt());
		assertFalse(option.hasOptionalArg());
		assertFalse(option.isRequired());
		assertFalse(option.hasValues());
		assertEquals(1, option.getArgs());
		assertEquals(Option.UNINITIALIZED, option.getId());
		assertTrue(option.toString().startsWith("[ option:"));
	}

	@Test
	public void testOptionConstructionWithNoArg() {
		Option option = new Option("opt", "description");
		assertEquals("opt", option.getOpt());
		assertEquals("description", option.getDescription());
		assertFalse(option.hasArg());
		assertNull(option.getValue());
		assertNull(option.getLongOpt());
		assertFalse(option.hasOptionalArg());
		assertFalse(option.isRequired());
		assertFalse(option.hasArgName());
		assertFalse(option.hasValues());
		assertEquals(0, option.getArgs());
		assertEquals(Option.UNINITIALIZED, option.getId());
		assertTrue(option.toString().startsWith("[ option:"));
	}

	@Test
	public void testOptionConstructionWithOptionalArg() {
		Option option = new Option("opt", "description", false, "arg");
		assertEquals("opt", option.getOpt());
		assertEquals("description", option.getDescription());
		assertTrue(option.hasArgName());
		assertEquals("arg", option.getArgName());
		assertNull(option.getValue());
		assertFalse(option.hasLongOpt());
		assertTrue(option.hasOptionalArg());
		assertFalse(option.isRequired());
		assertFalse(option.hasValues());
		assertEquals(1, option.getArgs());
		assertEquals(Option.UNINITIALIZED, option.getId());
		assertTrue(option.toString().startsWith("[ option:"));
	}

	@Test
	public void testOptionConstructionWithOptionalArgAndValue() {
		Option option = OptionBuilder.withLongOpt("longOpt").withArgName("arg").withDescription("description").withValueSeparator('=').build();
		assertEquals("longOpt", option.getLongOpt());
		assertEquals("description", option.getDescription());
		assertTrue(option.hasArgName());
		assertEquals("arg", option.getArgName());
		assertNull(option.getValue());
		assertTrue(option.hasLongOpt());
		assertTrue(option.hasOptionalArg());
		assertTrue(option.hasValueSeparator());
		assertFalse(option.isRequired());
		assertEquals(0, option.getArgs());
		assertEquals(Option.UNINITIALIZED, option.getId());
		assertTrue(option.toString().startsWith("[ option:"));
	}

	@Test
	public void testOptionConstructionWithRequiredArg() {
		Option option = new Option("opt", "description", true, "arg");
		assertEquals("opt", option.getOpt());
		assertEquals("description", option.getDescription());
		assertTrue(option.hasArgName());
		assertEquals("arg", option.getArgName());
		assertNull(option.getValue());
		assertFalse(option.hasLongOpt());
		assertFalse(option.hasOptionalArg());
		assertTrue(option.isRequired());
		assertFalse(option.hasValues());
		assertEquals(1, option.getArgs());
		assertEquals(Option.UNINITIALIZED, option.getId());
		assertTrue(option.toString().startsWith("[ option:"));
	}

	@Test
	public void testOptionConstructionWithRequiredArgAndValue() {
		Option option = OptionBuilder.withLongOpt("longOpt").withArgName("arg").withDescription("description").withValueSeparator('=').withValue("defaultValue").build();
		assertEquals("longOpt", option.getLongOpt());
		assertEquals("description", option.getDescription());
		assertTrue(option.hasArgName());
		assertEquals("arg", option.getArgName());
		assertEquals("defaultValue", option.getValue());
		assertTrue(option.hasLongOpt());
		assertFalse(option.hasOptionalArg());
		assertFalse(option.isRequired());
		assertTrue(option.hasValueSeparator());
		assertEquals(1, option.getArgs());
		assertEquals(Option.UNINITIALIZED, option.getId());
		assertTrue(option.toString().startsWith("[ option:"));
	}

	@Test
	public void testOptionConstructionWithMultivaluedArg() {
		Option option = new Option("opt", "description", false, null, ' ', true);
		assertEquals("opt", option.getOpt());
		assertEquals("description", option.getDescription());
		assertFalse(option.hasArgName());
		assertFalse(option.hasLongOpt());
		assertFalse(option.hasOptionalArg());
		assertTrue(option.isRequired());
		assertTrue(option.hasValueSeparator());
		assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
		assertEquals(Option.UNINITIALIZED, option.getId());
		assertTrue(option.toString().startsWith("[ option:"));
	}

	@Test
	public void testOptionClone() {
		Option option = OptionBuilder.withLongOpt("longOpt").withArgName("arg").withDescription("description").build();
		Option clone = option.clone();
		assertEquals(option, clone);
		assertEquals(option.getOpt(), clone.getOpt());
		assertEquals(option.getLongOpt(), clone.getLongOpt());
		assertEquals(option.getDescription(), clone.getDescription());
		assertEquals(option.getArgName(), clone.getArgName());
		assertEquals(option.getValue(), clone.getValue());
		assertEquals(option.getValues(), clone.getValues());
		assertEquals(option.getId(), clone.getId());
		assertTrue(option.toString().equals(clone.toString()));
	}

	@Test
	public void testOptionGettersAndSetters() {
		Option option = OptionBuilder.withLongOpt("longOpt").withArgName("arg").withDescription("description").build();
		assertEquals("description", option.getDescription());
		assertEquals("arg", option.getArgName());
		assertFalse(option.isRequired());
		assertFalse(option.hasOptionalArg());
		assertFalse(option.hasLongOpt());
		assertFalse(option.hasArg());
		assertNull(option.getValue());
		assertEquals(0, option.getArgs());
		assertEquals(Option.UNINITIALIZED, option.getId());
		option.setDescription("newDescription");
		assertEquals("newDescription", option.getDescription());
		option.setArgName("newArgName");
		assertEquals("newArgName", option.getArgName());
		option.setRequired(true);
		assertTrue(option.isRequired());
		option.setOptionalArg(true);
		assertTrue(option.hasOptionalArg());
		option.setLongOpt("newLongOpt");
		assertEquals("newLongOpt", option.getLongOpt());
		Option newOption = new Option("newOpt", "newDescription");
		option.setType(newOption);
		assertEquals(newOption, option.getType());
		option.clearValues();
		assertEquals(0, option.getValues().size());
		option.addValue("value");
		assertEquals(1, option.getValues().size());
		assertEquals("value", option.getValues().get(0));
		option.setValue("newValue");
		assertEquals("newValue", option.getValue());
		assertEquals("newValue", option.getValues().get(0));
	}

	@Test
	public void testOptionToString() {
		Option option = OptionBuilder.withLongOpt("longOpt").withArgName("arg").withDescription("description").build();
		assertEquals("[ option:longOpt description :: ... ]", option.toString());
	}

	@Test
	public void testOptionEquals() {
		Option option1 = OptionBuilder.withLongOpt("longOpt1").withArgName("arg1").withDescription("description1").build();
		Option option2 = OptionBuilder.withLongOpt("longOpt1").withArgName("arg1").withDescription("description1").build();
		Option option3 = OptionBuilder.withLongOpt("longOpt2").withArgName("arg2").withDescription("description2").build();
		assertTrue(option1.equals(option1));
		assertTrue(option1.equals(option2));
		assertFalse(option1.equals(option3));
		assertFalse(option1.equals(null));
		assertFalse(option1.equals("test"));
		Option option4 = new Option("opt4", "description4");
		assertFalse(option1.equals(option4));
	}

	@Test
	public void testOptionHashCode() {
		Option option1 = OptionBuilder.withLongOpt("longOpt1").withArgName("arg1").withDescription("description1").build();
		Option option2 = OptionBuilder.withLongOpt("longOpt1").withArgName("arg1").withDescription("description1").build();
		Option option3 = OptionBuilder.withLongOpt("longOpt2").withArgName("arg2").withDescription("description2").build();
		assertEquals(option1.hashCode(), option1.hashCode());
		assertEquals(option1.hashCode(), option2.hashCode());
		assertNotSame(option1.hashCode(), option3.hashCode());
	}
}