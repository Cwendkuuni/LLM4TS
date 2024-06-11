package org.apache.commons.cli.test;

import junit.framework.TestCase;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class OptionBuilderTest extends TestCase {

    public void testOptionWithLongOptAndDescription() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("This option sets some long option")
                .hasArg()
                .isRequired()
                .create('l');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("This option sets some long option", option.getDescription());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getValue());

        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"-l", "value"};
        try {
            CommandLine cmd = parser.parse(OptionBuilder.class.getPackage().getName() + ":testOptionWithLongOptAndDescription", args);
            assertEquals("value", option.getValue(cmd));
        } catch (ParseException e) {
            fail("Failed to parse command line: " + e.getMessage());
        }
    }

    public void testOptionWithValueSeparator() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("This option sets some long option with value separator")
                .hasArg(true)
                .isRequired()
                .withValueSeparator(':')
                .create('l');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("This option sets some long option with value separator", option.getDescription());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals(true, option.isOptionalArg());

        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"-l", "value1:value2"};
        try {
            CommandLine cmd = parser.parse(OptionBuilder.class.getPackage().getName() + ":testOptionWithValueSeparator", args);
            assertEquals("value1:value2", option.getValue(cmd));
        } catch (ParseException e) {
            fail("Failed to parse command line: " + e.getMessage());
        }
    }

    public void testOptionWithValueSeparatorAndArgName() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("This option sets some long option with value separator and arg name")
                .hasArg(true)
                .isRequired()
                .withValueSeparator(':')
                .withArgName("name")
                .create('l');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("This option sets some long option with value separator and arg name", option.getDescription());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals(true, option.isOptionalArg());
        assertEquals("name", option.getArgName());

        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"-l", "name=value1:value2"};
        try {
            CommandLine cmd = parser.parse(OptionBuilder.class.getPackage().getName() + ":testOptionWithValueSeparatorAndArgName", args);
            assertEquals("value1:value2", option.getValue(cmd));
            assertEquals("name", option.getArgName());
        } catch (ParseException e) {
            fail("Failed to parse command line: " + e.getMessage());
        }
    }

    public void testOptionWithHasArgs() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("This option sets some long option with multiple arguments")
                .hasArgs(2)
                .isRequired()
                .create('l');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("This option sets some long option with multiple arguments", option.getDescription());
        assertEquals(2, option.getArgs().length);
        assertEquals(false, option.isOptionalArg());

        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"-l", "arg1", "arg2"};
        try {
            CommandLine cmd = parser.parse(OptionBuilder.class.getPackage().getName() + ":testOptionWithHasArgs", args);
            assertEquals(2, option.getValues().length);
            assertEquals("arg1", option.getValues()[0]);
            assertEquals("arg2", option.getValues()[1]);
        } catch (ParseException e) {
            fail("Failed to parse command line: " + e.getMessage());
        }
    }

    public void testOptionWithHasOptionalArgs() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("This option sets some long option with optional arguments")
                .hasOptionalArgs(2)
                .isRequired()
                .create('l');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("This option sets some long option with optional arguments", option.getDescription());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertEquals(true, option.isOptionalArg());

        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"-l", "arg1", "arg2"};
        try {
            CommandLine cmd = parser.parse(OptionBuilder.class.getPackage().getName() + ":testOptionWithHasOptionalArgs", args);
            assertEquals(2, option.getValues().length);
            assertEquals("arg1", option.getValues()[0]);
            assertEquals("arg2", option.getValues()[1]);
        } catch (ParseException e) {
            fail("Failed to parse command line: " + e.getMessage());
        }

        String[] optionalArgs = new String[]{"arg3", "arg4"};
        try {
            CommandLine cmd = parser.parse(OptionBuilder.class.getPackage().getName() + ":testOptionWithHasOptionalArgs", args, optionalArgs);
            assertEquals(4, option.getValues().length);
            assertEquals("arg1", option.getValues()[0]);
            assertEquals("arg2", option.getValues()[1]);
            assertEquals("arg3", option.getValues()[2]);
            assertEquals("arg4", option.getValues()[3]);
        } catch (ParseException e) {
            fail("Failed to parse command line: " + e.getMessage());
        }
    }

    public void testOptionWithType() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("This option sets some long option with specified type")
                .hasArg()
                .isRequired()
                .withType(Integer.class)
                .create('l');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("This option sets some long option with specified type", option.getDescription());
        assertEquals(1, option.getArgs().length);
        assertEquals(true, option.isRequired());
        assertEquals(Integer.class, option.getType());

        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"-l", "123"};
        try {
            CommandLine cmd = parser.parse(OptionBuilder.class.getPackage().getName() + ":testOptionWithType", args);
            assertEquals(123, option.getValue(cmd, Integer.class));
        } catch (ParseException e) {
            fail("Failed to parse command line: " + e.getMessage());
        }
    }
}