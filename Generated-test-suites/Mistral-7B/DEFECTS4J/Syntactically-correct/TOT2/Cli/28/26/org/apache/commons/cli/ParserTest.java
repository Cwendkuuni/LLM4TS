package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.utils.HelpFormatter;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link Parser}.
 */
public class ParserTest
{

    private Parser parser;

    @Before
    public void setUp()
    {
        parser = new DefaultParser();
    }

    /**
     * Test parsing with no options or arguments.
     */
    @Test
    public void testParseNoOptionsOrArguments()
    {
        final Options options = new Options();
        final String[] arguments = new String[0];
        final CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().isEmpty());
    }

    /**
     * Test parsing with required options.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption()
    {
        final Options options = new Options();
        final Option requiredOption = OptionBuilder.withLongOpt("requiredOption")
                .required(true).build();
        options.addOption(requiredOption);
        final String[] arguments = new String[0];
        final CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertTrue(cmd.getOptions().isEmpty());
        assertFalse(cmd.hasOption("requiredOption"));
    }

    /**
     * Test parsing with optional options.
     */
    @Test
    public void testParseOptionalOption()
    {
        final Options options = new Options();
        final Option optionalOption = OptionBuilder.withLongOpt("optionalOption")
                .hasArg(true).build();
        options.addOption(optionalOption);
        final String[] arguments = new String[]{"--optionalOption=foo"};
        final CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertTrue(cmd.getOptions().size() == 1);
        assertTrue(cmd.hasOption("optionalOption"));
        assertEquals("foo", cmd.getOptionValue("optionalOption"));
    }

    /**
     * Test parsing with an option group.
     */
    @Test
    public void testParseOptionGroup()
    {
        final Options options = new Options();
        final OptionGroup group = new OptionGroup("group");
        final Option option1 = OptionBuilder.withLongOpt("option1")
                .hasArg(true).build();
        final Option option2 = OptionBuilder.withLongOpt("option2")
                .hasArg(true).build();
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        final String[] arguments = new String[]{"--group", "--option1=foo1", "--option2=foo2"};
        final CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertTrue(cmd.getOptions().size() == 3);
        assertTrue(cmd.hasOption("group"));
        assertTrue(cmd.hasOption("option1"));
        assertEquals("foo1", cmd.getOptionValue("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals("foo2", cmd.getOptionValue("option2"));
    }

    /**
     * Test parsing with a long option.
     */
    @Test
    public void testParseLongOption()
    {
        final Options options = new Options();
        final Option longOption = OptionBuilder.withLongOpt("longOption")
                .hasArg(false).build();
        options.addOption(longOption);
        final String[] arguments = new String[]{"--longOption"};
        final CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertTrue(cmd.getOptions().size() == 1);
        assertTrue(cmd.hasOption("longOption"));
    }

    /**
     * Test parsing with a positional argument.
     */
    @Test(expected = MissingArgumentException.class)
    public void testParseMissingPositionalArgument()
    {
        final Options options = new Options();
        final Option argOption = OptionBuilder.withLongOpt("arg")
                .hasArg(true).build();
        options.addOption(argOption);
        final String[] arguments = new String[0];
        final CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertTrue(cmd.getOptions().isEmpty());
        assertFalse(cmd.hasOption("arg"));
        assertNull(cmd.getOptionValue("arg"));
        assertEquals(0, cmd.getArgs().size());
    }

    /**
     * Test parsing with positional arguments.
     */
    @Test
    public void testParsePositionalArguments()
    {
        final Options options = new Options();
        final Option argOption = OptionBuilder.withLongOpt("arg")
                .hasArg(true).build();
        options.addOption(argOption);
        final String[] arguments = new String[]{"--arg", "foo", "bar"};
        final CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("arg"));
        assertEquals("foo", cmd.getOptionValue("arg"));
        assertEquals(Collections.singletonList("bar"), cmd.getArgs());
    }

    /**
     * Test parsing with a mix of options and positional arguments.
     */
    @Test
    public void testParseMixedOptionsAndArguments()
    {
        final Options options = new Options();
        final Option argOption = OptionBuilder.withLongOpt("arg")
                .hasArg(true).build();
        options.addOption(argOption);
        final String[] arguments = new String[]{"--arg=foo", "bar", "--longOption"};
        final CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertTrue(cmd.getOptions().size() == 2);
        assertTrue(cmd.hasOption("arg"));
        assertEquals("foo", cmd.getOptionValue("arg"));
        assertEquals(Arrays.asList("bar"), cmd.getArgs());
        assertTrue(cmd.hasOption("longOption"));
    }

    /**
     * Test parsing with a missing option.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseMissingOption()
    {
        final Options options = new Options();
        final String[] arguments = new String[]{"--missingOption"};
        final CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertTrue(cmd.getOptions().isEmpty());
        assertFalse(cmd.hasOption("missingOption"));
    }

    /**
     * Test parsing with a mix of recognized and unrecognized options.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseMixedRecognizedAndUnrecognizedOptions()
    {
        final Options options = new Options();
        final Option argOption = OptionBuilder.withLongOpt("arg")
                .hasArg(true).build();
        options.addOption(argOption);
        final String[] arguments = new String[]{"--arg=foo", "--unknownOption", "bar"};
        final CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertTrue(cmd.getOptions().size() == 3);
        assertTrue(cmd.hasOption("arg"));
        assertEquals("foo", cmd.getOptionValue("arg"));
        assertTrue(cmd.getArgs().contains("bar"));
        assertTrue(cmd.hasOption("unknownOption"));
    }

    /**
     * Test parsing with properties.
     */
    @Test
    public void testParseProperties()
    {
        final Options options = new Options();
        final Option propOption = OptionBuilder.withLongOpt("prop")
                .hasArg(false).build();
        final Option argOption = OptionBuilder.withLongOpt("arg")
                .hasArg(true).build();
        options.addOption(propOption);
        options.addOption(argOption);
        final Properties properties = new Properties();
        properties.put("prop", "value");
        final String[] arguments = new String[0];
        final CommandLine cmd = parser.parse(options, arguments, properties);

        assertNotNull(cmd);
        assertTrue(cmd.getOptions().size() == 2);
        assertTrue(cmd.hasOption("prop"));
        assertTrue(cmd.hasOption("arg"));
        assertNull(cmd.getOptionValue("arg"));
        assertEquals("value", cmd.getOptionValue("prop"));
    }

    /**
     * Test parsing with a mix of properties and command line arguments.
     */
    @Test
    public void testParseMixedPropertiesAndArguments()
    {
        final Options options = new Options();
        final Option propOption = OptionBuilder.withLongOpt("prop")
                .hasArg(false).build();
        final Option argOption = OptionBuilder.withLongOpt("arg")
                .hasArg(true).build();
        options.addOption(propOption);
        options.addOption(argOption);
        final Properties properties = new Properties();
        properties.put("prop", "value1");
        final String[] arguments = new String[]{"--arg=value2"};
        final CommandLine cmd = parser.parse(options, arguments, properties);

        assertNotNull(cmd);
        assertTrue(cmd.getOptions().size() == 3);
        assertTrue(cmd.hasOption("prop"));
        assertEquals("value1", cmd.getOptionValue("prop"));
        assertTrue(cmd.hasOption("arg"));
        assertEquals("value2", cmd.getOptionValue("arg"));
    }

    /**
     * Test parsing with a mix of properties and command line options.
     */
    @Test
    public void testParseMixedPropertiesAndOptions()
    {
        final Options options = new Options();
        final Option propOption = OptionBuilder.withLongOpt("prop")
                .hasArg(false).build();
        final Option argOption = OptionBuilder.withLongOpt("arg")
                .hasArg(true).build();
        final Option longOption = OptionBuilder.withLongOpt("longOption")
                .hasArg(false).build();
        options.addOption(propOption);
        options.addOption(argOption);
        options.addOption(longOption);
        final Properties properties = new Properties();
        properties.put("prop", "value1");
        final String[] arguments = new String[]{"--longOption", "--arg=value2"};
        final CommandLine cmd = parser.parse(options, arguments, properties);

        assertNotNull(cmd);
        assertTrue(cmd.getOptions().size() == 4);
        assertTrue(cmd.hasOption("prop"));
        assertEquals("value1", cmd.getOptionValue("prop"));
        assertTrue(cmd.hasOption("arg"));
        assertEquals("value2", cmd.getOptionValue("arg"));
        assertTrue(cmd.hasOption("longOption"));
    }
}