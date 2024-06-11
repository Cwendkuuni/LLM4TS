package org.apache.commons.cli.parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test cases for {@link Parser}.
 */
public class ParserTest {

    private Parser parser;
    private Options options;

    /**
     * Set up the test.
     */
    @Before
    public void setUp() {
        options = new Options();
        options.addRequiredOption("o", "option", true, "An option value");
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    /**
     * Test parsing with required option.
     */
    @Test
    public void parseWithRequiredOption() {
        String[] arguments = {"-o", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals("value", cmd.getOptionValue("option"));
    }

    /**
     * Test parsing with optional option.
     */
    @Test
    public void parseWithOptionalOption() {
        String[] arguments = {"-o", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals("value", cmd.getOptionValue("option", "defaultValue"));
    }

    /**
     * Test parsing with no options.
     */
    @Test
    public void parseWithNoOptions() {
        String[] arguments = {};
        CommandLine cmd = parser.parse(options, arguments);

        assertNull(cmd.getOptionValue("option"));
    }

    /**
     * Test parsing with missing required option.
     */
    @Test
    public void parseWithMissingRequiredOption() {
        String[] arguments = {};

        assertThrows(MissingOptionException.class, () ->
                parser.parse(options, arguments));
    }

    /**
     * Test parsing with missing argument.
     */
    @Test
    public void parseWithMissingArgument() {
        String[] arguments = {"-o"};

        assertThrows(MissingArgumentException.class, () ->
                parser.parse(options, arguments));
    }

    /**
     * Test parsing with extra arguments.
     */
    @Test
    public void parseWithExtraArguments() {
        String[] arguments = {"-o", "value", "extra1", "extra2"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals("value", cmd.getOptionValue("option"));
        assertEquals(2, cmd.getArgs().size());
        assertEquals("extra1", cmd.getArgs().get(0));
        assertEquals("extra2", cmd.getArgs().get(1));
    }

    /**
     * Test parsing options from properties.
     */
    @Test
    public void parseWithProperties() {
        options.addOption("p", "property", true, "A property value");
        Properties properties = new Properties();
        properties.setProperty("p", "value");

        String[] arguments = {};
        CommandLine cmd = parser.parse(options, arguments, properties);

        assertEquals("value", cmd.getOptionValue("property"));
    }

    /**
     * Test parsing with invalid option.
     */
    @Test
    public void parseWithInvalidOption() {
        String[] arguments = {"-i", "value"};

        assertThrows(UnrecognizedOptionException.class, () ->
                parser.parse(options, arguments));
    }

    /**
     * Test parsing with multiple invalid options.
     */
    @Test
    public void parseWithMultipleInvalidOptions() {
        String[] arguments = {"-i", "-i", "value"};

        assertThrows(UnrecognizedOptionException.class, () ->
                parser.parse(options, arguments));
    }
}