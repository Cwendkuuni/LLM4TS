package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for the {@link Parser} class.
 */
public class ParserTest
{
    private CommandLineParser parser;
    private Properties properties;
    private Options options;

    @Before
    public void setUp()
    {
        parser = new DefaultParser();
        properties = new Properties();
        options = new Options();
    }

    /**
     * Test for the {@link Parser#parse(Options, String[], boolean)} method.
     *
     * @throws ParseException if there is an error parsing the arguments.
     */
    @Test
    public void testParse() throws ParseException
    {
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");
        options.addOption("c", 3, "Option C");

        String[] arguments = new String[]{"-a", "--b=2", "--c=3"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(true, cmd.hasOption("a"));
        assertEquals(2, cmd.getOptionValue("b").intValue());
        assertEquals(3, cmd.getOptionValue("c").intValue());
    }

    /**
     * Test for the {@link Parser#parse(Options, String[], Properties)} method.
     *
     * @throws ParseException if there is an error parsing the arguments.
     */
    @Test
    public void testParseWithProperties() throws ParseException
    {
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");
        options.addOption("c", 3, "Option C");

        properties.setProperty("b", "3");

        String[] arguments = new String[]{"-a", "--b=2"};
        CommandLine cmd = parser.parse(options, arguments, properties);

        assertEquals(true, cmd.hasOption("a"));
        assertEquals(3, cmd.getOptionValue("c").intValue());
    }

    /**
     * Test for the {@link Parser#parse(Options, String[], boolean, boolean)} method.
     *
     * @throws ParseException if there is an error parsing the arguments.
     */
    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");
        options.addOption("c", 3, "Option C");

        String[] arguments = new String[]{"-a", "--b=2", "nonOption"};
        CommandLine cmd = parser.parse(options, arguments, true);

        assertEquals(true, cmd.hasOption("a"));
        assertEquals(2, cmd.getOptionValue("b").intValue());
        assertTrue(cmd.getArgs().length > 0);
        assertEquals("nonOption", cmd.getArgs()[0]);
    }

    /**
     * Test for the case where an option is required but not provided.
     *
     * @throws ParseException if there is an error parsing the arguments.
     */
    @Test
    public void testMissingRequiredOption()
    {
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");
        options.addRequiredOption("c", "Option C is required.");

        String[] arguments = new String[]{"-a"};

        assertThrows(MissingOptionException.class, () -> {
            parser.parse(options, arguments);
        });
    }

    /**
     * Test for the case where an invalid option is provided.
     */
    @Test
    public void testUnrecognizedOption()
    {
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        String[] arguments = new String[]{"--invalid"};

        assertThrows(UnrecognizedOptionException.class, () -> {
            parser.parse(options, arguments);
        });
    }

    /**
     * Test for the case where an option is provided with an invalid value.
     */
    @Test
    public void testInvalidOptionValue()
    {
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");
        options.addOption("c", 3, "Option C");

        String[] arguments = new String[]{"-a", "--b=foo"};

        assertThrows(ParseException.class, () -> {
            parser.parse(options, arguments);
        });
    }

    /**
     * Test for the case where an option with an optional argument is provided with no argument.
     */
    @Test
    public void testMissingOptionalArgument()
    {
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");
        options.addOption("c", 3, "Option C")
                .setArgName("name");

        String[] arguments = new String[]{"-a", "--c"};

        assertThrows(MissingArgumentException.class, () -> {
            parser.parse(options, arguments);
        });
    }

    /**
     * Test for the case where an option with an optional argument is provided with an argument.
     */
    @Test
    public void testProvidedOptionalArgument()
    {
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");
        options.addOption("c", 3, "Option C")
                .setArgName("name");

        String[] arguments = new String[]{"-a", "--c=John"};

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(true, cmd.hasOption("a"));
        assertEquals(3, cmd.getArgs().length);
        assertEquals("John", cmd.getArgs()[0]);
    }
}