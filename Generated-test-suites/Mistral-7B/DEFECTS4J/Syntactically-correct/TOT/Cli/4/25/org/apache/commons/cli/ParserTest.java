package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link Parser}.
 */
public class ParserTest
{
    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}.
     */
    @Test
    public void testParse()
    {
        // given
        Options options = new Options();

        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("o", "option2", true, "Option 2");
        Option option3 = new Option("t", "template", false, "Template");
        Option option4 = new Option("h", "help", false, "Print this help message.");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);

        // when
        String[] arguments = new String[]{"-o", "value1", "--option2", "value2", "-t"};
        CommandLineParser parser = new DefaultParser();

        try
        {
            CommandLine cmd = parser.parse(options, arguments, null);

            // then
            Assert.assertEquals(2, cmd.getArgs().size());
            Assert.assertEquals("value1", cmd.getOptionValues(option1.getKey())[0]);
            Assert.assertEquals("value2", cmd.getOptionValues(option2.getKey())[0]);
            Assert.assertNull(cmd.getOptionValues(option3.getKey()));
            Assert.assertFalse(cmd.hasOption("help"));
        }
        catch (ParseException e)
        {
            // expected
        }

        // when-then-throw (missing option with no argument)
        String[] argumentsMissingOption = new String[]{"-o", "value1", "-x"};

        try
        {
            parser.parse(options, argumentsMissingOption, null);
            Assert.fail();
        }
        catch (MissingOptionException e)
        {
            Assert.assertEquals("No valid option 'x'", e.getMessage());
        }

        // when-then-throw (missing option with argument)
        String[] argumentsMissingOptionWithArgument = new String[]{"-o", "value1", "--x", "value"};

        try
        {
            parser.parse(options, argumentsMissingOptionWithArgument, null);
            Assert.fail();
        }
        catch (MissingArgumentException e)
        {
            Assert.assertEquals("Missing argument for option: x", e.getMessage());
        }

        // when-then-throw (unrecognized option)
        String[] argumentsUnrecognizedOption = new String[]{"-u", "value"};

        try
        {
            parser.parse(options, argumentsUnrecognizedOption, null);
            Assert.fail();
        }
        catch (UnrecognizedOptionException e)
        {
            Assert.assertEquals("Unrecognized option: u", e.getMessage());
        }

        // when-then-throw (illegal option value)
        String[] argumentsIllegalOptionValue = new String[]{"-o", "value1", "-o", "invalidValue"};

        try
        {
            parser.parse(options, argumentsIllegalOptionValue, null);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertEquals("Illegal option value 'invalidValue' for option '-o'");
            Assert.assertEquals(1, e.getArguments().size());
            Assert.assertEquals("invalidValue", e.getArguments()[0]);
        }

        // when-then-throw (required option missing)
        String[] argumentsMissingRequiredOption = new String[]{"-o", "value1"};

        try
        {
            parser.parse(options, argumentsMissingRequiredOption, null);
            Assert.fail();
        }
        catch (MissingOptionException e)
        {
            Assert.assertEquals("Option 'option2' is required", e.getMessage());
        }
    }
}