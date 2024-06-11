package org.apache.commons.cli;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OptionTest {

	@Test
	public void testOptionConstruction() {
		Option opt = new Option("opt", "Description");
		assertEquals("opt", opt.getOpt());
		assertEquals("Description", opt.getDescription());
		assertNull(opt.getLongOpt());
		assertFalse(opt.hasArg());
		assertFalse(opt.isRequired());
		assertNull(opt.getValue());
		assertTrue(opt.hasNoValues());
		assertEquals(1, opt.getId());
		assertEquals("[ option: opt :: Description ]", opt.toString());
		assertEquals(Option.UNINITIALIZED, opt.getArgs());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidOptionCharacter() {
		new Option("\u0000", "Description");
	}

	@Test
	public void testOptionConstructionWithLongOpt() {
		Option opt = new Option("opt", "long-opt", false, "Description");
		assertEquals("opt", opt.getOpt());
		assertEquals("long-opt", opt.getLongOpt());
		assertFalse(opt.hasArg());
		assertFalse(opt.isRequired());
		assertNull(opt.getValue());
		assertTrue(opt.hasNoValues());
		assertEquals(1, opt.getId());
		assertEquals("[ option: opt :: long-opt :: Description ]", opt.toString());
		assertEquals(Option.UNINITIALIZED, opt.getArgs());
	}

	@Test
	public void testOptionConstructionWithArg() {
		Option opt = new Option("opt", false, "Description");
		assertEquals("opt", opt.getOpt());
		assertNull(opt.getLongOpt());
		assertTrue(opt.hasArg());
		assertFalse(opt.isRequired());
		assertNull(opt.getValue());
		assertTrue(opt.hasNoValues());
		assertEquals(1, opt.getId());
		assertEquals("[ option: opt :: Description ]", opt.toString());
		assertEquals(Option.UNINITIALIZED, opt.getArgs());
	}

	@Test
	public void testOptionConstructionWithArgAndDescription() {
		Option opt = new Option("opt", "arg", false, "Description");
		assertEquals("opt", opt.getOpt());
		assertNull(opt.getLongOpt());
		assertTrue(opt.hasArg());
		assertEquals("arg", opt.getArgName());
		assertFalse(opt.isRequired());
		assertNull(opt.getValue());
		assertTrue(opt.hasNoValues());
		assertEquals(1, opt.getId());
		assertEquals("[ option: opt :: arg :: Description ]", opt.toString());
		assertEquals(Option.UNINITIALIZED, opt.getArgs());
	}

	@Test
	public void testOptionConstructionWithLongOptAndArg() {
		Option opt = new Option("opt", "long-opt", "arg", "Description");
		assertEquals("opt", opt.getOpt());
		assertEquals("long-opt", opt.getLongOpt());
		assertTrue(opt.hasArg());
		assertEquals("arg", opt.getArgName());
		assertFalse(opt.isRequired());
		assertNull(opt.getValue());
		assertTrue(opt.hasNoValues());
		assertEquals(1, opt.getId());
		assertEquals("[ option: opt :: long-opt :: arg :: Description ]", opt.toString());
		assertEquals(Option.UNINITIALIZED, opt.getArgs());
	}

	@Test
	public void testOptionId() {
		Option opt1 = new Option("opt1", "Description");
		Option opt2 = new Option("opt2", "Description");
		assertEquals(1, opt1.getId());
		assertEquals(2, opt2.getId());
	}

	@Test
	public void testOptionDescription() {
		Option opt = new Option("opt", "Description");
		assertEquals("Description", opt.getDescription());
	}

	@Test
	public void testOptionOptionalArg() {
		Option opt = new Option("opt", "Description");
		assertFalse(opt.isOptionalArg());
		Option opt2 = new Option("opt", "Description", true, null);
		assertTrue(opt2.isOptionalArg());
	}

	@Test
	public void testOptionHasArg() {
		Option opt = new Option("opt", "Description");
		assertFalse(opt.hasArg());
		Option opt2 = new Option("opt", "Description", true, null);
		assertTrue(opt2.hasArg());
	}

	@Test
	public void testOptionHasLongOpt() {
		Option opt = new Option("opt", "Description");
		assertFalse(opt.hasLongOpt());
		Option opt2 = new Option("opt", "long-opt", null, "Description");
		assertTrue(opt2.hasLongOpt());
	}

	@Test
	public void testOptionHasRequiredArg() {
		Option opt = new Option("opt", "Description");
		assertFalse(opt.isRequired());
		Option opt2 = new Option("opt", "Description", false, null);
		assertFalse(opt2.isRequired());
		Option opt3 = new Option("opt", "Description", true, null);
		assertTrue(opt3.isRequired());
	}

	@Test
	public void testOptionHasArgName() {
		Option opt = new Option("opt", "Description");
		assertFalse(opt.hasArgName());
		Option opt2 = new Option("opt", "arg", null, "Description");
		assertTrue(opt2.hasArgName());
		assertEquals("arg", opt2.getArgName());
	}

	@Test
	public void testOptionHasArgs() {
		Option opt = new Option("opt", "Description");
		assertFalse(opt.hasArgs());
		Option opt2 = new Option("opt", "arg", false, "Description");
		assertFalse(opt2.hasArgs());
		Option opt3 = new Option("opt", "arg1 arg2", false, "Description");
		assertTrue(opt3.hasArgs());
	}

	@Test
	public void testOptionSetArgs() {
		Option opt = new Option("opt", "Description");
		assertFalse(opt.hasArgs());
		opt.setArgs(2);
		assertFalse(opt.hasArgs());
		assertEquals(Option.UNLIMITED_VALUES, opt.getArgs());
		Option opt2 = new Option("opt", "arg1 arg2", false, "Description");
		assertFalse(opt2.hasArgs());
		opt2.setArgs(2);
		assertTrue(opt2.hasArgs());
		assertEquals(2, opt2.getArgs());
	}

	@Test
	public void testOptionSetValueSeparator() {
		Option opt = new Option("opt", "Description");
		assertFalse(opt.hasValueSeparator());
		opt.setValueSeparator(';');
		assertTrue(opt.hasValueSeparator());
		assertEquals(';', opt.getValueSeparator());
	}

	@Test
	public void testOptionGetValue() {
		Option opt = new Option("opt", "Description");
		assertNull(opt.getValue());
		Option opt2 = new Option("opt", "arg1 arg2", false, "Description");
		assertNull(opt2.getValue());
		List values = new ArrayList();
		values.add("value1");
		values.add("value2");
		opt2.setValues(values);
		assertEquals("value1", opt2.getValue());
		assertEquals("value2", opt2.getValue(1));
		assertEquals("value1 value2", opt2.getValue(new String[]{"value1", "value2"}));
	}

	@Test
	public void testOptionGetValues() {
		Option opt = new Option("opt", "Description");
		assertNull(opt.getValues());
		Option opt2 = new Option("opt", "arg1 arg2", false, "Description");
		assertNull(opt2.getValues());
		List values = new ArrayList();
		values.add("value1");
		values.add("value2");
		opt2.setValues(values);
		assertEquals(values.toArray(new String[values.size()]), opt2.getValues());
		assertEquals(values.toArray(new String[values.size()]), opt2.getValuesList().toArray());
	}

	@Test
	public void testOptionToString() {
		Option opt = new Option("opt", "Description");
		assertEquals("[ option: opt :: Description ]", opt.toString());
		Option opt2 = new Option("opt", "arg1 arg2", false, "Description");
		assertEquals("[ option: opt :: arg1 arg2 :: Description ]", opt2.toString());
	}

	@Test
	public void testOptionClearValues() {
		Option opt = new Option("opt", "Description");
		List values = new ArrayList();
		values.add("value1");
		values.add("value2");
		opt.setValues(values);
		assertEquals(values.toArray(new String[values.size()]), opt.getValues());
		opt.clearValues();
		assertNull(opt.getValue());
		assertEquals(0, opt.getValuesList().size());
	}

	@Test
	public void testOptionEquals() {
		Option opt1 = new Option("opt1", "Description1");
		Option opt2 = new Option("opt1", "Description1");
		assertTrue(opt1.equals(opt2));

		Option opt3 = new Option("opt2", "Description1");
		assertFalse(opt1.equals(opt3));

		Option opt4 = new Option("opt1", "Description2");
		assertFalse(opt1.equals(opt4));
	}

	@Test
	public void testOptionHashCode() {
		Option opt1 = new Option("opt1", "Description1");
		Option opt2 = new Option("opt1", "Description1");
		assertEquals(opt1.hashCode(), opt2.hashCode());

		Option opt3 = new Option("opt2", "Description1");
		assertNotEquals(opt1.hashCode(), opt3.hashCode());

		Option opt4 = new Option("opt1", "Description2");
		assertNotEquals(opt1.hashCode(), opt4.hashCode());
	}

	@Test
	public void testOptionClone() {
		Option opt = new Option("opt", "Description");
		Option clone = opt.clone();
		assertEquals(opt, clone);
		assertNotSame(opt, clone);
		assertEquals(opt.getId(), clone.getId());
		assertEquals(opt.getOpt(), clone.getOpt());
		assertEquals(opt.getLongOpt(), clone.getLongOpt());
		assertEquals(opt.getDescription(), clone.getDescription());
		assertEquals(opt.isRequired(), clone.isRequired());
		assertEquals(opt.hasArg(), clone.hasArg());
		assertEquals(opt.hasOptionalArg(), clone.hasOptionalArg());
		assertEquals(opt.hasLongOpt(), clone.hasLongOpt());
		assertEquals(opt.hasArgName(), clone.hasArgName());
		assertEquals(opt.hasArgs(), clone.hasArgs());
		assertEquals(opt.getArgs(), clone.getArgs());
		assertEquals(opt.getValueSeparator(), clone.getValueSeparator());
		assertTrue(opt.hasNoValues());
		assertTrue(clone.hasNoValues());
		assertTrue(opt.hasValuesList());
		assertTrue(clone.hasValuesList());
	}
}