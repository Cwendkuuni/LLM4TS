package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Parser.
 */
public class ParserTest {

    private CommandLineParser parser;
    private Options options;

    @Before
    public void setup() {
        options = new Options();
        parser = new DefaultParser();
    }

    /**
     * Test parsing with no arguments.
     *
     * @throws ParseException if an error occurs.
     */
    @Test
    public void testParseWithNoArguments() throws ParseException {
        String[] args = new String[0];

        CommandLine result = parser.parse(options, args, false);

        assertNotNull(result);
        assertTrue(result.getArgs().isEmpty());
        assertFalse(result.hasOption("help"));
    }

    /**
     * Test parsing with simple arguments.
     *
     * @throws ParseException if an error occurs.
     */
    @Test
    public void testParseWithSimpleArguments() throws ParseException {
        options.addOption("a", true, "Option A");
        String[] args = new String[]{"-a"};

        CommandLine result = parser.parse(options, args, false);

        assertNotNull(result);
        assertEquals(1, result.getArgs().size());
        assertNull(result.getOptionValues("a"));
        assertTrue(result.hasOption("a"));
    }

    /**
     * Test parsing with an optional argument.
     *
     * @throws ParseException if an error occurs.
     */
    @Test
    public void testParseWithOptionalArgument() throws ParseException {
        options.addOption("b", true, "Option B")
                .required(false)
                .argName("ARG");
        String[] args = new String[]{"-b", "VALUE"};

        CommandLine result = parser.parse(options, args, false);

        assertNotNull(result);
        assertEquals(1, result.getArgs().size());
        assertNotNull(result.getOptionValues("b"));
        assertEquals(1, result.getOptionValues("b").length);
        assertEquals("VALUE", result.getOptionValues("b")[0]);
        assertTrue(result.hasOption("b"));
    }

    /**
     * Test parsing with multiple optional arguments.
     *
     * @throws ParseException if an error occurs.
     */
    @Test
    public void testParseWithMultipleOptionalArguments() throws ParseException {
        options.addOption("c", true, "Option C")
                .required(false)
                .argName("ARG1 ARG2");
        String[] args = new String[]{"-c", "VALUE1", "-c", "VALUE2"};

        CommandLine result = parser.parse(options, args, false);

        assertNotNull(result);
        assertEquals(2, result.getArgs().size());
        assertNotNull(result.getOptionValues("c"));
        assertEquals(2, result.getOptionValues("c").length);
        assertEquals("VALUE1", result.getOptionValues("c")[0]);
        assertEquals("VALUE2", result.getOptionValues("c")[1]);
        assertTrue(result.hasOption("c"));
    }

    /**
     * Test parsing with required arguments.
     *
     * @throws ParseException if an error occurs.
     */
    @Test(expected = ParseException.class)
    public void testParseWithRequiredArguments() throws ParseException {
        options.addOption("d", true, "Option D")
                .argName("ARG")
                .required(true);
        String[] args = new String[0];

        parser.parse(options, args, false);
    }

    /**
     * Test parsing with required arguments and simple arguments.
     *
     * @throws ParseException if an error occurs.
     */
    @Test(expected = ParseException.class)
    public void testParseWithRequiredArgumentsAndSimpleArguments() throws ParseException {
        options.addOption("d", true, "Option D")
                .argName("ARG")
                .required(true);
        String[] args = new String[]{"-a"};

        parser.parse(options, args, false);
    }

    /**
     * Test parsing with required arguments and required options.
     *
     * @throws ParseException if an error occurs.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseWithRequiredArgumentsAndRequiredOptions() throws ParseException {
        options.addOption("e", true, "Option E")
                .argName("ARG")
                .required(true);
        String[] args = new String[0];

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, false);
    }

    /**
     * Test parsing with required arguments and optional options.
     *
     * @throws ParseException if an error occurs.
     */
    @Test
    public void testParseWithRequiredArgumentsAndOptionalOptions() throws ParseException {
        options.addOption("f", true, "Option F")
                .argName("ARG")
                .required(false);
        String[] args = new String[]{"-f"};

        CommandLine result = parser.parse(options, args, false);

        assertNotNull(result);
        assertTrue(result.hasOption("f"));
    }

    /**
     * Test parsing with simple options and multiple values.
     *
     * @throws ParseException if an error occurs.
     */
    @Test
    public void testParseWithSimpleOptionsAndMultipleValues() throws ParseException {
        options.addOption("g", true, "Option G");
        String[] args = new String[]{"-g", "VALUE1", "-g", "VALUE2"};

        CommandLine result = parser.parse(options, args, false);

        assertNotNull(result);
        assertEquals(0, result.getArgs().size());
        assertNotNull(result.getOptionValues("g"));
        assertEquals(2, result.getOptionValues("g").length);
        assertEquals("VALUE1", result.getOptionValues("g")[0]);
        assertEquals("VALUE2", result.getOptionValues("g")[1]);
        assertTrue(result.hasOption("g"));
    }

    /**
     * Test parsing with complex options and multiple values.
     *
     * @throws ParseException if an error occurs.
     */
    @Test
    public void testParseWithComplexOptionsAndMultipleValues() throws ParseException {
        Option option = new Option("h", "h", true, "Option H")
                .longOpt("long-h")
                .required(false)
                .args(new String[2]);
        options.addOption(option);
        String[] args = new String[]{"-h", "VALUE1", "-h", "VALUE2"};

        CommandLine result = parser.parse(options, args, false);

        assertNotNull(result);
        assertEquals(0, result.getArgs().size());
        assertNotNull(result.getOptionValues("h"));
        assertEquals(2, result.getOptionValues("h").length);
        assertEquals("VALUE1", result.getOptionValues("h")[0]);
        assertEquals("VALUE2", result.getOptionValues("h")[1]);
        assertTrue(result.hasOption("h"));
    }

    /**
     * Test parsing with an option group.
     *
     * @throws ParseException if an error occurs.
     */
    @Test
    public void testParseWithOptionGroup() throws ParseException {
        OptionGroup group = new OptionGroup("group", "Group options")
                .required(false);
        options.addOptionGroup(group);
        group.addOption("i", "i", true, "Option I")
                .longOpt("long-i")
                .required(false)
                .args(new String[2]);
        String[] args = new String[]{"--group", "-i", "VALUE1", "-i", "VALUE2"};

        CommandLine result = parser.parse(options, args, false);

        assertNotNull(result);
        assertEquals(0, result.getArgs().size());
        assertNotNull(result.getOptionValues("i"));
        assertEquals(2, result.getOptionValues("i").length);
        assertEquals("VALUE1", result.getOptionValues("i")[0]);
        assertEquals("VALUE2", result.getOptionValues("i")[1]);
        assertTrue(result.hasOption("i"));
        assertTrue(result.isSet("group"));
    }

    /**
     * Test parsing with complex options and values from properties.
     *
     * @throws ParseException if an error occurs.
     */
    @Test
    public void testParseWithComplexOptionsAndValuesFromProperties() throws ParseException {
        OptionGroup group = new OptionGroup("group", "Group options")
                .required(false);
        options.addOptionGroup(group);
        group.addOption("j", "j", true, "Option J")
                .longOpt("long-j")
                .required(false)
                .args(new String[2])
                .valueSeparator('=');
        Properties props = new Properties();
        props.setProperty("j", "VALUE1=VALUE1_1 VALUE1_2 VALUE1_3");
        CommandLine result = parser.parse(options, null, props, false);

        assertNotNull(result);
        assertEquals(0, result.getArgs().size());
        assertNotNull(result.getOptionValues("j"));
        assertEquals(3, result.getOptionValues("j").length);
        assertEquals("VALUE1_1", result.getOptionValues("j")[0]);
        assertEquals("VALUE1_2", result.getOptionValues("j")[1]);
        assertEquals("VALUE1_3", result.getOptionValues("j")[2]);
        assertTrue(result.hasOption("j"));
        assertTrue(result.isSet("group"));
    }

    /**
     * Test parsing with invalid options.
     *
     * @throws ParseException if an error occurs.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithInvalidOptions() throws ParseException {
        options.addOption("k", true, "Option K")
                .argName("ARG");
        String[] args = new String[]{"-x"};

        parser.parse(options, args, false);
    }

    /**
     * Test parsing with invalid options from properties.
     *
     * @throws ParseException if an error occurs.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithInvalidOptionsFromProperties() throws ParseException {
        options.addOption("l", true, "Option L")
                .argName("ARG");
        Properties props = new Properties();
        props.setProperty("l", "ARG");

        parser.parse(options, null, props, false);
    }

    /**
     * Test parsing with multiple commands.
     *
     * @throws ParseException if an error occurs.
     */
    @Test
    public void testParseWithMultipleCommands() throws ParseException {
        CommandLineParser parser1 = new DefaultParser();
        CommandLineParser parser2 = new DefaultParser();
        options.addOption("m", true, "Option M")
                .argName("ARG");
        String[] args1 = new String[]{"-m"};
        String[] args2 = new String[]{"--"};

        CommandLine result1 = parser1.parse(options, args1, false);
        CommandLine result2 = parser2.parse(options, args2, false);

        assertNotNull(result1);
        assertEquals(0, result1.getArgs().size());
        assertNotNull(result1.getOptionValues("m"));
        assertEquals(1, result1.getOptionValues("m").length);
        assertEquals("", result1.getOptionValues("m")[0]);
        assertTrue(result1.hasOption("m"));

        assertNotNull(result2);
        assertTrue(result2.getArgs().isEmpty());
        assertFalse(result2.hasOptions());
    }
}