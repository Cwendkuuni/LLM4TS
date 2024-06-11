package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithSimpleOption() {
        Option option = OptionBuilder.withLongOpt("optionA")
                .hasArg()
                .create('a');

        assertEquals("optionA", option.getLongOpt());
        assertEquals("an argument", option.getArgName());
        assertNull(option.getValue());
        assertEquals(1, option.getNumberOfArgs());
        assertEquals(false, option.isRequired());
        assertEquals(false, option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithOptionalArgumentOption() {
        Option option = OptionBuilder.withLongOpt("optionB")
                .hasOptionalArg()
                .create('b');

        assertEquals("optionB", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
        assertEquals(1, option.getNumberOfArgs());
        assertEquals(true, option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithValueSeparatorOption() {
        Option option = OptionBuilder.withLongOpt("optionC")
                .withValueSeparator(':')
                .create('c');

        assertEquals("optionC", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
        assertEquals(1, option.getNumberOfArgs());
        assertEquals(false, option.isRequired());
        assertEquals(':', option.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithMissingLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.create();
    }
}