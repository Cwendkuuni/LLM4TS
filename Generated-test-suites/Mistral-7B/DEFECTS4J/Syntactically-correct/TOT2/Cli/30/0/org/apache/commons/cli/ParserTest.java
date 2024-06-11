package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TestUtilities;
import org.junit.Test;

/**
 * Test for Parser class.
 */
public class ParserTest
{
    @Test
    public void testParser() throws ParseException
    {
        // Test the basic usage of Parser
        String[] args = { "--option1", "value1", "--option2", "value2" };

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(getTestOptions(), args, false);

        // Check the parsed CommandLine
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertEquals("value2", cmd.getOptionValue("option2"));
        assertNull(cmd.getOptionValue("unknownOption"));
        assertFalse(cmd.hasOption("unknownOption"));
        assertEquals(2, cmd.getArgList().size());
        assertEquals("value1", cmd.getArgList().get(0));
        assertEquals("value2", cmd.getArgList().get(1));

        // Test the usage of Parser with required options
        Options optionsWithRequiredOptions = new Options();
        optionsWithRequiredOptions.addOption("requiredOption", true, "optional description");

        String[] argsWithRequiredOptions = { "--requiredOption" };

        CommandLine cmdWithRequiredOptions = parser.parse(optionsWithRequiredOptions, argsWithRequiredOptions, false);

        assertThrows(MissingOptionException.class, () -> parser.parse(optionsWithRequiredOptions, argsWithRequiredOptions, false));
        assertSame(cmdWithRequiredOptions, parser.parse(optionsWithRequiredOptions, argsWithRequiredOptions, true));

        // Test the usage of Parser with help
        String[] argsWithHelp = { "--help" };

        CommandLine cmdWithHelp = parser.parse(getTestOptions(), argsWithHelp, false);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ParserTest", getTestOptions().getOptions(), true);
        String helpText = formatter.getHelpMessage();

        assertTrue(helpText.contains("--help"));
        assertFalse(cmdWithHelp.hasOptions());
        assertFalse(cmdWithHelp.getArgList().isEmpty());

        // Test the usage of Parser with invalid arguments
        String[] argsInvalidUsage = { "--", "option1=value1", "option2=value2" };

        assertThrows(ParseException.class, () -> parser.parse(getTestOptions(), argsInvalidUsage, false));

        // Test the usage of Parser with properties
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");

        CommandLine cmdWithProperties = parser.parse(getTestOptions(), null, properties, false);

        assertEquals("value1", cmdWithProperties.getOptionValue("option1"));
        assertEquals("value2", cmdWithProperties.getOptionValue("option2"));

        // Test the usage of Parser with a mixture of arguments and options
        String[] argsWithArgsAndOptions = { "--option1", "value1", "--option2", "value2", "arg1", "arg2" };

        CommandLine cmdWithArgsAndOptions = parser.parse(getTestOptions(), argsWithArgsAndOptions, false);

        assertEquals(2, cmdWithArgsAndOptions.getArgList().size());
        assertEquals("arg1", cmdWithArgsAndOptions.getArgList().get(0));
        assertEquals("arg2", cmdWithArgsAndOptions.getArgList().get(1));

        // Test the usage of Parser with multiple options with the same name
        Options optionsWithMultipleOptions = new Options();
        optionsWithMultipleOptions.addOption("option", true, "description1");
        optionsWithMultipleOptions.addOption("option", true, "description2");

        String[] argsWithMultipleOptions = { "--option", "value1", "--option", "value2" };

        CommandLine cmdWithMultipleOptions = parser.parse(optionsWithMultipleOptions, argsWithMultipleOptions, false);

        assertEquals(2, cmdWithMultipleOptions.getOptionValues("option").length);
        assertEquals("value1", cmdWithMultipleOptions.getOptionValues("option")[0]);
        assertEquals("value2", cmdWithMultipleOptions.getOptionValues("option")[1]);

        // Test the usage of Parser with a long option
        Options optionsWithLongOption = new Options();
        optionsWithLongOption.addOption("longOption", false, "description");

        String[] argsWithLongOption = { "--long-option" };

        CommandLine cmdWithLongOption = parser.parse(optionsWithLongOption, argsWithLongOption, false);

        assertTrue(cmdWithLongOption.hasOption("longOption"));
    }

    /**
     * Get the test Options.
     *
     * @return the test Options.
     */
    private static Options getTestOptions()
    {
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        return options;
    }
}