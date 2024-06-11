package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.junit.Test;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test for Parser class
 *
 * @author Expert 1
 */
public class ParserTest {

    @Test
    public void testParseWithValidOptions() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        Parser parser = new DefaultParser();
        String[] args = {"-o", "value"};
        CommandLine cmd = parser.parse(options, args, false);
        Assert.assertEquals("value", cmd.getOptionValue("option"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption("r", true, "Required option");
        List<Option> requiredOptions = options.getRequiredOptions();
        Parser parser = new DefaultParser();
        String[] args = {};
        try {
            parser.parse(options, args, false);
        } catch (MissingOptionException e) {
            Assert.assertEquals(1, requiredOptions.size());
            Assert.assertEquals("Required option", e.getMessage());
        }
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        Parser parser = new DefaultParser();
        String[] args = {"-i", "invalidOption"};
        try {
            parser.parse(options, args, false);
        } catch (UnrecognizedOptionException e) {
            Assert.assertEquals("-i,invalidOption", e.getOption());
        }
    }

    @Test
    public void testParseWithMultipleInvalidOptions() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        Parser parser = new DefaultParser();
        String[] args = {"-i", "invalidOption1", "-j", "invalidOption2"};
        try {
            parser.parse(options, args, false);
        } catch (UnrecognizedOptionsException e) {
            List<String> unrecognizedOptions = Arrays.asList("-i", "-j");
            Assert.assertArrayEquals(unrecognizedOptions.toArray(new String[0]), e.getUnrecognizedOptions());
        }
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "Long option description");
        Parser parser = new DefaultParser();
        String[] args = {"--longOption", "value"};
        CommandLine cmd = parser.parse(options, args, false);
        Assert.assertEquals("value", cmd.getOptionValue("longOption"));
    }

    @Test
    public void testParseWithDefaultValues() throws ParseException {
        Options options = new Options();
        options.addOption("d", "defaultValue", false, "Default value option");
        Parser parser = new DefaultParser();
        String[] args = {};
        CommandLine cmd = parser.parse(options, args, false);
        Assert.assertTrue(cmd.hasOption("defaultValue"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("p", true, "Property option");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("p", "propertyValue");
        CommandLine cmd = parser.parse(options, new String[0], properties, false);
        Assert.assertEquals("propertyValue", cmd.getOptionValue("property option"));
    }

    @Test
    public void testParseWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Argument option");
        Parser parser = new DefaultParser();
        String[] args = {"-a", "argumentValue"};
        CommandLine cmd = parser.parse(options, args, false);
        Assert.assertEquals("argumentValue", cmd.getArg(0));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("s", true, "Stop at non-option");
        Parser parser = new DefaultParser();
        String[] args = {"-s", "stopValue", "-n", "nonOption"};
        CommandLine cmd = parser.parse(options, args, true);
        Assert.assertEquals(Arrays.asList("stopValue", "nonOption"), cmd.getArgs());
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("m", true, "Missing argument option", true);
        Parser parser = new DefaultParser();
        String[] args = {"-m"};
        parser.parse(options, args, false);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMultipleMissingArguments() throws ParseException {
        Options options = new Options();
        options.addOption("m", true, "Missing argument option", true);
        Parser parser = new DefaultParser();
        String[] args = {"-m", "-m"};
        parser.parse(options, args, false);
    }

    @Test
    public void testParseWithLongOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("lo", true, "Long option with argument");
        Parser parser = new DefaultParser();
        String[] args = {"--lo", "longOptionArgument"};
        CommandLine cmd = parser.parse(options, args, false);
        Assert.assertEquals("longOptionArgument", cmd.getOptionValue("long option with argument"));
    }

    @Test
    public void testParseWithOptionGroups() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        group.addOption("g", true, "Group option");
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        String[] args = {"-g"};
        CommandLine cmd = parser.parse(options, args, false);
        Assert.assertTrue(cmd.hasOption("group option"));
        Assert.assertEquals(group, cmd.getOption("group option").getOptGroup());
    }
}