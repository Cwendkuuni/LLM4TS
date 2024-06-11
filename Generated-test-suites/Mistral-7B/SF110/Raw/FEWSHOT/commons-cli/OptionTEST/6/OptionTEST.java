package org.apache.commons.cli;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionConstruction() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.isRequired());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertEquals("[ option: opt :: description ]", option.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithInvalidOpt() {
        new Option(null, "description");
    }

    @Test
    public void testOptionConstructionWithArg() {
        Option option = new Option("opt", "description", true, "argName");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertTrue(option.hasArg());
        assertFalse(option.hasLongOpt());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertEquals("[ option: opt :: description [ARG] ]", option.toString());
    }

    @Test
    public void testOptionConstructionWithLongOpt() {
        Option option = new Option("opt", "--opt-arg", false, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("--opt-arg", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.hasArg());
        assertTrue(option.hasLongOpt());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertEquals("[ option: opt :: description ]", option.toString());
    }

    @Test
    public void testOptionConstructionWithArgAndLongOpt() {
        Option option = new Option("opt", "--opt-arg", true, "argName", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("--opt-arg", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertTrue(option.hasArg());
        assertTrue(option.hasLongOpt());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertEquals("[ option: opt :: description [ARG] ]", option.toString());
    }

    @Test
    public void testOptionGettersAndSetters() {
        Option option = new Option("opt", "description");
        option.setLongOpt("--long-opt");
        option.setArgName("argName");
        option.setDescription("new description");
        option.setRequired(true);
        option.setOptionalArg(false);
        option.setNumberOfArgs(2);
        option.setType(Integer.class);
        option.setValues(new ArrayList<>( Arrays.asList("val1", "val2") ) );
        option.setValueSeparator(':');

        assertEquals("opt", option.getOpt());
        assertEquals("--long-opt", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertEquals("new description", option.getDescription());
        assertTrue(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(2, option.getArgs());
        assertEquals(Integer.class, option.getType());
        assertEquals(Arrays.asList("val1", "val2"), option.getValuesList());
        assertEquals(':', option.getValueSeparator());

        option.clearValues();
        assertFalse(option.hasValues());
    }

    @Test
    public void testOptionAddValue() {
        Option option = new Option("opt", "description");
        option.setNumberOfArgs(1);
        option.setValueSeparator(':');

        option.addValue("value1");
        option.addValue("value2");

        assertEquals(Arrays.asList("value1", "value2"), option.getValuesList());
        assertEquals(2, option.size());
    }

    @Test(expected = RuntimeException.class)
    public void testOptionAddValueOutOfBounds() {
        Option option = new Option("opt", "description");
        option.setNumberOfArgs(1);
        option.setValueSeparator(':');

        option.addValue("value1");
        option.addValue("value2");
        option.addValue("value3");
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("opt", "description");
        assertEquals("[ option: opt :: description ]", option.toString());

        option.setLongOpt("--long-opt");
        assertEquals("[ option: opt :: description ]", option.toString());

        option.setArgName("argName");
        assertEquals("[ option: opt :: description [ARG] ]", option.toString());

        option.setOptionalArg(true);
        assertEquals("[ option: opt :: description [ARG] ]", option.toString());

        option.setRequired(true);
        assertEquals("[ option: opt :: description [ARG] ]", option.toString());

        option.setNumberOfArgs(2);
        assertEquals("[ option: opt :: description [ARG1] [ARG2] ]", option.toString());

        option.setType(Integer.class);
        assertEquals("[ option: opt :: description [ARG] :: Integer ]", option.toString());

        option.setValues(Arrays.asList("val1", "val2"));
        option.setValueSeparator(':');
        assertEquals("[ option: opt :: description [ARG] :: Integer [VAL1:val1, VAL2:val2] ]", option.toString());
    }

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("opt1", "description1");
        Option option2 = new Option("opt1", "description1");
        Option option3 = new Option("opt2", "description2");

        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("opt1", "description1");
        Option option2 = new Option("opt1", "description1");
        Option option3 = new Option("opt2", "description2");

        int hashCode1 = option1.hashCode();
        int hashCode2 = option1.hashCode();
        int hashCode3 = option2.hashCode();
        int hashCode4 = option3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testOptionClone() {
        Option option = new Option("opt", "description");
        Option clonedOption = option.clone();

        assertEquals(option, clonedOption);
        assertEquals(option.getOpt(), clonedOption.getOpt());
        assertEquals(option.getLongOpt(), clonedOption.getLongOpt());
        assertEquals(option.getArgName(), clonedOption.getArgName());
        assertEquals(option.getDescription(), clonedOption.getDescription());
        assertEquals(option.isRequired(), clonedOption.isRequired());
        assertEquals(option.hasOptionalArg(), clonedOption.hasOptionalArg());
        assertEquals(option.getNumberOfArgs(), clonedOption.getNumberOfArgs());
        assertEquals(option.getType(), clonedOption.getType());
        assertEquals(option.getValues(), clonedOption.getValues());
        assertEquals(option.getValueSeparator(), clonedOption.getValueSeparator());
    }
}