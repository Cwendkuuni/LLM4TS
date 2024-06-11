package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithLongOptAndDescription() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("this is a test option")
                .create();

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("this is a test option", option.getDescription());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithShortOptAndLongOptAndDescription() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withShortOpt('s')
                .withDescription("this is a test option")
                .create();

        assertEquals("longOpt", option.getLongOpt());
        assertEquals('s', option.getOpt());
        assertEquals("this is a test option", option.getDescription());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithArgName() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withArgName("argName")
                .create();

        assertEquals("argName", option.getArgName());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithArgNameAndHasArg() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withArgName("argName")
                .hasArg()
                .create();

        assertEquals("argName", option.getArgName());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithArgNameAndHasArgAndValueSeparator() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withArgName("argName")
                .hasArg()
                .withValueSeparator(':')
                .create();

        assertEquals("argName", option.getArgName());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithArgNameAndHasArgAndValueSeparatorAndValue() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withArgName("argName")
                .hasArg()
                .withValueSeparator(':')
                .withValue("value")
                .create();

        assertEquals("argName", option.getArgName());
        assertEquals("value", option.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithInvalidValueSeparator() {
        OptionBuilder.withLongOpt("longOpt")
                .withArgName("argName")
                .hasArg()
                .withValueSeparator((char) 0)
                .create();
    }

    @Test
    public void testOptionBuilderWithOptionalArg() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withOptionalArg(true)
                .create();

        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithOptionalArgAndValue() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withOptionalArg(true)
                .withValue("value")
                .create();

        assertEquals("value", option.getValue());
    }

    @Test
    public void testOptionBuilderWithOptionalArgs() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withOptionalArgs()
                .create();

        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithOptionalArgsAndValues() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withOptionalArgs(2)
                .create();

        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithType() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withType(Integer.class)
                .create();

        assertNull(option.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithInvalidType() {
        OptionBuilder.withLongOpt("longOpt")
                .withType(String[].class)
                .create();
    }

    @Test
    public void testOptionBuilderWithIsRequired() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .isRequired()
                .create();

        assertNull(option.getValue());
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderWithIsRequiredAndValue() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .isRequired()
                .withValue("value")
                .create();

        assertEquals("value", option.getValue());
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderWithHasArgs() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasArgs()
                .create();

        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithHasArgsAndValues() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasArgs(2)
                .create();

        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithHasArgsAndValuesAndValueSeparator() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasArgs(2)
                .withValueSeparator(':')
                .create();

        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithHasArgsAndValuesAndValue() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasArgs(2)
                .create();

        assertEquals(new String[2], option.getValue());
    }

    @Test
    public void testOptionBuilderWithHasArgsAndValuesAndValueSeparatorAndValues() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasArgs(2)
                .withValueSeparator(':')
                .withValues("value1", "value2")
                .create();

        assertEquals(new String[]{"value1", "value2"}, option.getValue());
    }

    @Test
    public void testOptionBuilderWithHasOptionalArgs() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasOptionalArgs()
                .create();

        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithHasOptionalArgsAndValues() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasOptionalArgs(2)
                .create();

        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithHasOptionalArgsAndValuesAndValueSeparator() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasOptionalArgs(2)
                .withValueSeparator(':')
                .create();

        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithHasOptionalArgsAndValuesAndValues() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasOptionalArgs(2)
                .withValues("value1", "value2")
                .create();

        assertEquals(new String[]{"value1", "value2"}, option.getValue());
    }
}