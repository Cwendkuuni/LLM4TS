package org.apache.commons.cli;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OptionTest {

	@Test
	public void testOptionConstruction() {
		Option option = new Option("opt", "Description");
		assertEquals("opt", option.getOpt());
		assertEquals("Description", option.getDescription());
		assertEquals(null, option.getLongOpt());
		assertEquals(false, option.hasOptionalArg());
		assertEquals(0, option.getArgs());
		assertEquals(null, option.getType());
		assertEquals(null, option.getValues());
		assertEquals(false, option.hasArg());
		assertEquals(false, option.hasValues());
		assertEquals(false, option.isRequired());
		assertEquals(false, option.hasArgName());
		assertEquals(false, option.hasLongOpt());
		assertEquals(-1, option.getId());
		assertEquals(Option.UNINITIALIZED, option.numberOfArgs);
		assertEquals(false, option.hasValueSeparator());
		assertNull(option.getValue());
		assertEquals("", option.getArgName());
		assertEquals(false, option.hasArgs());
		assertFalse(option.equals(null));
		assertFalse(option.equals(""));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOptionConstructionInvalidOptArgument() {
		new Option("", "Description");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOptionConstructionNullDescription() {
		new Option("opt", null);
	}

	@Test
	public void testOptionConstructionWithArg() {
		Option option = new Option("opt", true, "Description");
		assertEquals("opt", option.getOpt());
		assertEquals("Description", option.getDescription());
		assertEquals(null, option.getLongOpt());
		assertEquals(true, option.hasOptionalArg());
		assertEquals(1, option.getArgs());
		assertEquals(null, option.getType());
		assertEquals(null, option.getValues());
		assertEquals(true, option.hasArg());
		assertEquals(false, option.hasValues());
		assertEquals(false, option.isRequired());
		assertEquals(false, option.hasArgName());
		assertEquals(false, option.hasLongOpt());
		assertEquals(-1, option.getId());
		assertEquals(Option.UNINITIALIZED, option.numberOfArgs);
		assertEquals(false, option.hasValueSeparator());
		assertNull(option.getValue());
		assertEquals("arg", option.getArgName());
		assertEquals(false, option.hasArgs());
		assertFalse(option.equals(null));
		assertFalse(option.equals(""));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOptionConstructionInvalidHasArgArgument() {
		new Option("opt", true, "");
	}

	@Test
	public void testOptionConstructionWithLongOpt() {
		Option option = new Option("opt", null, false, "Long option description");
		assertEquals("opt", option.getOpt());
		assertEquals("Long option description", option.getDescription());
		assertEquals("longOpt", option.getLongOpt());
		assertEquals(false, option.hasOptionalArg());
		assertEquals(0, option.getArgs());
		assertEquals(null, option.getType());
		assertEquals(null, option.getValues());
		assertEquals(false, option.hasArg());
		assertEquals(false, option.hasValues());
		assertEquals(false, option.isRequired());
		assertEquals(false, option.hasArgName());
		assertEquals(true, option.hasLongOpt());
		assertEquals(-1, option.getId());
		assertEquals(Option.UNINITIALIZED, option.numberOfArgs);
		assertEquals(false, option.hasValueSeparator());
		assertNull(option.getValue());
		assertEquals("", option.getArgName());
		assertEquals(false, option.hasArgs());
		assertFalse(option.equals(null));
		assertFalse(option.equals(""));
	}

	@Test
	public void testOptionGettersSetters() {
		Option option = new Option("opt", null, false, "Description");
		option.setArgName("argName");
		option.setDescription("new description");
		option.setOptionalArg(true);
		option.setRequired(true);
		option.setNumberOfArgs(2);
		option.setType(Byte.TYPE);
		assertEquals("opt", option.getOpt());
		assertEquals(null, option.getLongOpt());
		assertEquals("argName", option.getArgName());
		assertEquals("new description", option.getDescription());
		assertEquals(true, option.hasOptionalArg());
		assertEquals(true, option.isRequired());
		assertEquals(2, option.getArgs());
		assertEquals(Byte.TYPE, option.getType());
		assertEquals(null, option.getValue());
		assertEquals(false, option.hasArgs());
		assertEquals(false, option.hasValues());
		assertEquals(false, option.equals(null));
		assertEquals(false, option.equals(""));
	}

	@Test
	public void testOptionHasArg() {
		Option option = new Option("opt", null, false, "Description");
		Option optionWithArg = new Option("opt", false, "Description");
		Option optionWithArgs = new Option("opt", null, false, "Description", 2);
		assertEquals(false, option.hasArg());
		assertEquals(true, optionWithArg.hasArg());
		assertEquals(true, optionWithArgs.hasArgs());
	}

	@Test
	public void testOptionHasOptionalArg() {
		Option option = new Option("opt", null, false, "Description");
		Option optionWithOptionalArg = new Option("opt", true, "Description");
		assertEquals(false, option.hasOptionalArg());
		assertEquals(true, optionWithOptionalArg.hasOptionalArg());
	}

	@Test
	public void testOptionHasArgName() {
		Option option = new Option("opt", null, false, "Description");
		Option optionWithArgName = new Option("opt", null, false, "Description", "argName");
		assertEquals(false, option.hasArgName());
		assertEquals(true, optionWithArgName.hasArgName());
		assertEquals("argName", optionWithArgName.getArgName());
	}

	@Test
	public void testOptionHasLongOpt() {
		Option option = new Option("opt", null, false, "Description");
		Option optionWithLongOpt = new Option("opt", "longOpt", false, "Description");
		assertEquals(false, option.hasLongOpt());
		assertEquals(true, optionWithLongOpt.hasLongOpt());
		assertEquals("longOpt", optionWithLongOpt.getLongOpt());
	}

	@Test
	public void testOptionHasValues() {
		Option option = new Option("opt", null, false, "Description");
		Option optionWithValues = new Option("opt", null, false, "Description", null, new ArrayList<String>());
		assertEquals(false, option.hasValues());
		assertEquals(true, optionWithValues.hasValues());
		assertEquals(0, optionWithValues.getValues().size());
	}

	@Test
	public void testOptionHasValueSeparator() {
		Option option = new Option("opt", null, false, "Description");
		Option optionWithValueSeparator = new Option("opt", null, false, "Description", null, null, ',');
		assertEquals(false, option.hasValueSeparator());
		assertEquals(true, optionWithValueSeparator.hasValueSeparator());
		assertEquals(',', optionWithValueSeparator.getValueSeparator());
	}

	@Test
	public void testOptionEquals() {
		Option option1 = new Option("opt1", null, false, "Description1");
		Option option2 = new Option("opt1", null, false, "Description1");
		Option option3 = new Option("opt2", null, false, "Description2");
		assertEquals(option1, option1);
		assertEquals(option1, option2);
		assertNotEquals(option1, null);
		assertNotEquals(option1, option3);
		assertNotEquals(option1, "test");
	}

	@Test
	public void testOptionHashCode() {
		Option option1 = new Option("opt1", null, false, "Description1");
		Option option2 = new Option("opt1", null, false, "Description1");
		Option option3 = new Option("opt2", null, false, "Description2");
		assertEquals(option1.hashCode(), option1.hashCode());
		assertEquals(option1.hashCode(), option2.hashCode());
		assertNotEquals(option1.hashCode(), option3.hashCode());
		assertNotEquals(option1.hashCode(), 123);
	}

	@Test
	public void testOptionClone() throws CloneNotSupportedException {
		Option option = new Option("opt", null, false, "Description");
		Option clonedOption = option.clone();
		assertEquals(option, clonedOption);
		assertEquals(option.getOpt(), clonedOption.getOpt());
		assertEquals(option.getDescription(), clonedOption.getDescription());
		assertEquals(option.getLongOpt(), clonedOption.getLongOpt());
		assertEquals(option.isRequired(), clonedOption.isRequired());
		assertEquals(option.hasOptionalArg(), clonedOption.hasOptionalArg());
		assertEquals(option.hasArg(), clonedOption.hasArg());
		assertEquals(option.hasValues(), clonedOption.hasValues());
		assertEquals(option.getValues(), clonedOption.getValues());
		assertEquals(option.getValue(), clonedOption.getValue());
		assertEquals(option.getValueSeparator(), clonedOption.getValueSeparator());
		assertEquals(option.argName, clonedOption.argName);
		assertEquals(option.numberOfArgs, clonedOption.numberOfArgs);
		assertEquals(option.type, clonedOption.type);
	}

	@Test
	public void testOptionClearValues() {
		Option option = new Option("opt", null, false, "Description");
		List<String> values = new ArrayList<String>();
		values.add("value1");
		values.add("value2");
		option.setValues(values);
		option.clearValues();
		assertNull(option.getValues());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testOptionAddValue() {
		Option option = new Option("opt", null, false, "Description");
		option.addValue("value1");
	}

	@Test
	public void testOptionAddValueForProcessing() {
		Option option = new Option("opt", null, false, "Description");
		option.setValues(new ArrayList<String>());
		option.addValueForProcessing("value1");
		List<String> values = option.getValues();
		assertEquals(1, values.size());
		assertEquals("value1", values.get(0));
	}

	@Test
	public void testOptionAddValueForProcessingWithValueSeparator() {
		Option option = new Option("opt", null, false, "Description", null, null, ',');
		option.setValues(new ArrayList<String>());
		option.addValueForProcessing("value1");
		option.addValueForProcessing("value2");
		List<String> values = option.getValues();
		assertEquals(2, values.size());
		assertEquals("value1", values.get(0));
		assertEquals("value2", values.get(1));
	}

	@Test(expected = RuntimeException.class)
	public void testOptionAddValueForProcessingListFull() {
		Option option = new Option("opt", null, false, "Description", 3);
		option.setValues(new ArrayList<String>());
		List<String> values = option.getValues();
		option.addValueForProcessing("value1");
		option.addValueForProcessing("value2");
		option.addValueForProcessing("value3");
	}
}