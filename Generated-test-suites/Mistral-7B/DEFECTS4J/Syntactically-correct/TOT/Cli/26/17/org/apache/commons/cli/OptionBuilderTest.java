package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderLongOptWithArg() {
        Option option = OptionBuilder.withLongOpt("testLongOpt")
                .hasArg()
                .create();

        assertEquals("testLongOpt", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getValue());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());

        Option withValue = OptionBuilder.withLongOpt("testLongOpt")
                .hasArg()
                .withValueSeparator(':')
                .create('t');

        assertEquals("testLongOpt", withValue.getLongOpt());
        assertEquals("arg", withValue.getArgName());
        assertEquals(1, withValue.getArgs().length);
        assertNull(withValue.getValue());
        assertFalse(withValue.isRequired());
        assertFalse(withValue.hasOptionalArg());
        assertFalse(withValue.isUnbounded());
        assertNull(withValue.getType());
        assertEquals(':', withValue.getValueSeparator());

        Option withValueAndType = OptionBuilder.withLongOpt("testLongOpt")
                .hasArg()
                .withValueSeparator(':')
                .withType(String.class)
                .create('t');

        assertEquals("testLongOpt", withValueAndType.getLongOpt());
        assertEquals("arg", withValueAndType.getArgName());
        assertEquals(1, withValueAndType.getArgs().length);
        assertNull(withValueAndType.getValue());
        assertFalse(withValueAndType.isRequired());
        assertFalse(withValueAndType.hasOptionalArg());
        assertFalse(withValueAndType.isUnbounded());
        assertEquals(String.class, withValueAndType.getType());
        assertEquals(':', withValueAndType.getValueSeparator());

        Option withValueAndRequired = OptionBuilder.withLongOpt("testLongOpt")
                .isRequired()
                .hasArg()
                .withValueSeparator(':')
                .create('t');

        assertEquals("testLongOpt", withValueAndRequired.getLongOpt());
        assertEquals("arg", withValueAndRequired.getArgName());
        assertEquals(1, withValueAndRequired.getArgs().length);
        assertNull(withValueAndRequired.getValue());
        assertTrue(withValueAndRequired.isRequired());
        assertFalse(withValueAndRequired.hasOptionalArg());
        assertFalse(withValueAndRequired.isUnbounded());
        assertNull(withValueAndRequired.getType());
        assertEquals(':', withValueAndRequired.getValueSeparator());
    }

    @Test
    public void testOptionBuilderLongOptWithoutArg() {
        Option option = OptionBuilder.withLongOpt("testLongOpt")
                .create();

        assertEquals("testLongOpt", option.getLongOpt());
        assertEquals(null, option.getArgName());
        assertEquals(null, option.getArgs());
        assertNull(option.getValue());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderShortOptWithArg() {
        Option option = OptionBuilder.create('t')
                .hasArg()
                .create();

        assertEquals('t', option.getOpt());
        assertEquals("arg", option.getArgName());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getValue());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());

        Option withValue = OptionBuilder.create('t')
                .hasArg()
                .withValueSeparator(':')
                .create();

        assertEquals('t', withValue.getOpt());
        assertEquals("arg", withValue.getArgName());
        assertEquals(1, withValue.getArgs().length);
        assertNull(withValue.getValue());
        assertFalse(withValue.isRequired());
        assertFalse(withValue.hasOptionalArg());
        assertFalse(withValue.isUnbounded());
        assertNull(withValue.getType());
        assertEquals(':', withValue.getValueSeparator());

        Option withValueAndType = OptionBuilder.create('t')
                .hasArg()
                .withValueSeparator(':')
                .withType(String.class)
                .create();

        assertEquals('t', withValueAndType.getOpt());
        assertEquals("arg", withValueAndType.getArgName());
        assertEquals(1, withValueAndType.getArgs().length);
        assertNull(withValueAndType.getValue());
        assertFalse(withValueAndType.isRequired());
        assertFalse(withValueAndType.hasOptionalArg());
        assertFalse(withValueAndType.isUnbounded());
        assertEquals(String.class, withValueAndType.getType());
        assertEquals(':', withValueAndType.getValueSeparator());

        Option withValueAndRequired = OptionBuilder.create('t')
                .isRequired()
                .hasArg()
                .withValueSeparator(':')
                .create();

        assertEquals('t', withValueAndRequired.getOpt());
        assertEquals("arg", withValueAndRequired.getArgName());
        assertEquals(1, withValueAndRequired.getArgs().length);
        assertNull(withValueAndRequired.getValue());
        assertTrue(withValueAndRequired.isRequired());
        assertFalse(withValueAndRequired.hasOptionalArg());
        assertFalse(withValueAndRequired.isUnbounded());
        assertNull(withValueAndRequired.getType());
        assertEquals(':', withValueAndRequired.getValueSeparator());
    }

    @Test
    public void testOptionBuilderShortOptWithoutArg() {
        Option option = OptionBuilder.create('t')
                .create();

        assertEquals('t', option.getOpt());
        assertEquals(null, option.getArgName());
        assertEquals(null, option.getArgs());
        assertNull(option.getValue());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderShortOptWithOptionalArg() {
        Option option = OptionBuilder.create('t')
                .hasOptionalArg()
                .create();

        assertEquals('t', option.getOpt());
        assertEquals("arg", option.getArgName());
        assertEquals(null, option.getArgs());
        assertNull(option.getValue());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());

        Option withValue = OptionBuilder.create('t')
                .hasOptionalArg()
                .withValueSeparator(':')
                .create();

        assertEquals('t', withValue.getOpt());
        assertEquals("arg", withValue.getArgName());
        assertEquals(null, withValue.getArgs());
        assertNull(withValue.getValue());
        assertFalse(withValue.isRequired());
        assertTrue(withValue.hasOptionalArg());
        assertFalse(withValue.isUnbounded());
        assertNull(withValue.getType());
        assertEquals(':', withValue.getValueSeparator());

        Option withValueAndType = OptionBuilder.create('t')
                .hasOptionalArg()
                .withValueSeparator(':')
                .withType(String.class)
                .create();

        assertEquals('t', withValueAndType.getOpt());
        assertEquals("arg", withValueAndType.getArgName());
        assertEquals(null, withValueAndType.getArgs());
        assertNull(withValueAndType.getValue());
        assertFalse(withValueAndType.isRequired());
        assertTrue(withValueAndType.hasOptionalArg());
        assertFalse(withValueAndType.isUnbounded());
        assertEquals(String.class, withValueAndType.getType());
        assertEquals(':', withValueAndType.getValueSeparator());

        Option withValueAndRequired = OptionBuilder.create('t')
                .isRequired()
                .hasOptionalArg()
                .create();

        assertEquals('t', withValueAndRequired.getOpt());
        assertEquals("arg", withValueAndRequired.getArgName());
        assertEquals(null, withValueAndRequired.getArgs());
        assertNull(withValueAndRequired.getValue());
        assertTrue(withValueAndRequired.isRequired());
        assertTrue(withValueAndRequired.hasOptionalArg());
        assertFalse(withValueAndRequired.isUnbounded());
        assertNull(withValueAndRequired.getType());
        assertNull(withValueAndRequired.getValueSeparator());
    }

    @Test
    public void testOptionBuilderShortOptWithUnlimitedArgs() {
        Option option = OptionBuilder.create('t')
                .hasArgs()
                .create();

        assertEquals('t', option.getOpt());
        assertEquals(null, option.getArgName());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertNull(option.getValue());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.isUnbounded());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());

        Option withValue = OptionBuilder.create('t')
                .hasArgs()
                .withValueSeparator(':')
                .create();

        assertEquals('t', withValue.getOpt());
        assertEquals(null, withValue.getArgName());
        assertEquals(Option.UNLIMITED_VALUES, withValue.getArgs().length);
        assertNull(withValue.getValue());
        assertFalse(withValue.isRequired());
        assertFalse(withValue.hasOptionalArg());
        assertTrue(withValue.isUnbounded());
        assertNull(withValue.getType());
        assertEquals(':', withValue.getValueSeparator());

        Option withValueAndType = OptionBuilder.create('t')
                .hasArgs()
                .withValueSeparator(':')
                .withType(String[].class)
                .create();

        assertEquals('t', withValueAndType.getOpt());
        assertEquals(null, withValueAndType.getArgName());
        assertEquals(Option.UNLIMITED_VALUES, withValueAndType.getArgs().length);
        assertNull(withValueAndType.getValue());
        assertFalse(withValueAndType.isRequired());
        assertFalse(withValueAndType.hasOptionalArg());
        assertTrue(withValueAndType.isUnbounded());
        assertEquals(String[].class, withValueAndType.getType());
        assertEquals(':', withValueAndType.getValueSeparator());

        Option withValueAndRequired = OptionBuilder.create('t')
                .isRequired()
                .hasArgs()
                .create();

        assertEquals('t', withValueAndRequired.getOpt());
        assertEquals(null, withValueAndRequired.getArgName());
        assertEquals(Option.UNLIMITED_VALUES, withValueAndRequired.getArgs().length);
        assertNull(withValueAndRequired.getValue());
        assertTrue(withValueAndRequired.isRequired());
        assertFalse(withValueAndRequired.hasOptionalArg());
        assertTrue(withValueAndRequired.isUnbounded());
        assertNull(withValueAndRequired.getType());
        assertNull(withValueAndRequired.getValueSeparator());
    }

    @Test
    public void testOptionBuilderShortOptWithSpecifiedArgs() {
        Option option = OptionBuilder.create('t')
                .hasArgs(2)
                .create();

        assertEquals('t', option.getOpt());
        assertEquals(null, option.getArgName());
        assertEquals(new int[]{2}, option.getArgs().length);
        assertNull(option.getValue());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());

        Option withValue = OptionBuilder.create('t')
                .hasArgs(2)
                .withValueSeparator(':')
                .create();

        assertEquals('t', withValue.getOpt());
        assertEquals(null, withValue.getArgName());
        assertEquals(new int[]{2}, withValue.getArgs().length);
        assertNull(withValue.getValue());
        assertFalse(withValue.isRequired());
        assertFalse(withValue.hasOptionalArg());
        assertFalse(withValue.isUnbounded());
        assertNull(withValue.getType());
        assertEquals(':', withValue.getValueSeparator());

        Option withValueAndType = OptionBuilder.create('t')
                .hasArgs(2)
                .withValueSeparator(':')
                .withType(String[].class)
                .create();

        assertEquals('t', withValueAndType.getOpt());
        assertEquals(null, withValueAndType.getArgName());
        assertEquals(new int[]{2}, withValueAndType.getArgs().length);
        assertNull(withValueAndType.getValue());
        assertFalse(withValueAndType.isRequired());
        assertFalse(withValueAndType.hasOptionalArg());
        assertFalse(withValueAndType.isUnbounded());
        assertEquals(String[].class, withValueAndType.getType());
        assertEquals(':', withValueAndType.getValueSeparator());

        Option withValueAndRequired = OptionBuilder.create('t')
                .isRequired()
                .hasArgs(2)
                .create();

        assertEquals('t', withValueAndRequired.getOpt());
        assertEquals(null, withValueAndRequired.getArgName());
        assertEquals(new int[]{2}, withValueAndRequired.getArgs().length);
        assertNull(withValueAndRequired.getValue());
        assertTrue(withValueAndRequired.isRequired());
        assertFalse(withValueAndRequired.hasOptionalArg());
        assertFalse(withValueAndRequired.isUnbounded());
        assertNull(withValueAndRequired.getType());
        assertNull(withValueAndRequired.getValueSeparator());
    }
}