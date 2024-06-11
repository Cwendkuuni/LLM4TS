package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testBasicUsage() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Output file path");
        option.setRequired(false);
        options.addOption(option);

        CommandLine cmd = new WriteableCommandLineImpl(option, new ArrayList<>());
        options.parse(cmd, new String[]{"-o", "test.txt"});

        Assert.assertEquals("test.txt", cmd.getOptionValue("output"));
        Assert.assertFalse(cmd.hasOption("o"));
        Assert.assertNull(cmd.getOptionValue("nonexistent"));
        Assert.assertFalse(cmd.hasOption("nonexistent"));
    }

    @Test
    public void testSwitch() throws ParseException {
        Options options = new Options();
        Option option = new Option("v", "verbose", false, "Print verbose output");
        options.addOption(option);

        CommandLine cmd = new WriteableCommandLineImpl(option, new ArrayList<>());
        options.parse(cmd, new String[]{"-v"});

        Assert.assertTrue(cmd.hasOption("v"));
        Assert.assertFalse(cmd.hasOption("output"));
        Assert.assertNull(cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getOptionValue("nonexistent"));
        Assert.assertFalse(cmd.hasOption("nonexistent"));
    }

    @Test
    public void testMandatorySwitch() throws ParseException {
        Options options = new Options();
        Option option = new Option("v", "verbose", true, "Print verbose output");
        options.addOption(option);

        CommandLine cmd = new WriteableCommandLineImpl(option, new ArrayList<>());
        Assert.assertThrows(ParseException.class, () -> {
            options.parse(cmd, new String[]{});
        });

        options.parse(cmd, new String[]{"-v"});
        Assert.assertTrue(cmd.hasOption("v"));
        Assert.assertFalse(cmd.hasOption("output"));
        Assert.assertNull(cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getOptionValue("nonexistent"));
        Assert.assertFalse(cmd.hasOption("nonexistent"));
    }

    @Test
    public void testMandatoryArgument() throws ParseException {
        Options options = new Options();
        Option option = Options.optionBuilder("n")
                .longOpt("number")
                .valueSeparator(':')
                .hasArg()
                .required(true)
                .build();
        options.addOption(option);

        CommandLine cmd = new WriteableCommandLineImpl(option, new ArrayList<>());
        Assert.assertThrows(ParseException.class, () -> {
            options.parse(cmd, new String[]{});
        });

        options.parse(cmd, new String[]{"--number=123"});
        Assert.assertEquals("123", cmd.getOptionValue("number"));
        Assert.assertTrue(cmd.hasOption("number"));
        Assert.assertNull(cmd.getOptionValue("output"));
        Assert.assertFalse(cmd.hasOption("output"));
        Assert.assertNull(cmd.getOptionValue("nonexistent"));
        Assert.assertFalse(cmd.hasOption("nonexistent"));
    }

    @Test
    public void testMultipleArguments() throws ParseException {
        Options options = new Options();
        Option option = Options.optionBuilder("a")
                .longOpt("arg")
                .valueSeparator(':')
                .hasArg()
                .required(true)
                .build();
        options.addOption(option);

        CommandLine cmd = new WriteableCommandLineImpl(option, new ArrayList<>());
        Assert.assertThrows(ParseException.class, () -> {
            options.parse(cmd, new String[]{});
        });

        options.parse(cmd, new String[]{"--arg=one", "--arg=two"});
        Assert.assertEquals("one", cmd.getOptionValue("arg"));
        Assert.assertEquals("two", cmd.getOptionValue("arg"));
        Assert.assertTrue(cmd.hasOption("arg"));
        Assert.assertNull(cmd.getOptionValue("output"));
        Assert.assertFalse(cmd.hasOption("output"));
        Assert.assertNull(cmd.getOptionValue("nonexistent"));
        Assert.assertFalse(cmd.hasOption("nonexistent"));
    }

    @Test
    public void testPropertyOption() throws ParseException {
        Options options = new Options();
        Option propertyOption = new Option("p", "property", true, "Property value");
        propertyOption.setType(PropertyOption.TYPE_STRING);
        options.addOption(propertyOption);

        CommandLine cmd = new WriteableCommandLineImpl(new PropertyOption(), new ArrayList<>());
        options.parse(cmd, new String[]{"--property=value"});

        Map<String, String> properties = cmd.getProperties();
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value", properties.get("property"));
    }

    @Test
    public void testPropertyOptionWithDefaultValue() throws ParseException {
        Options options = new Options();
        Option propertyOption = new Option("p", "property", true, "Property value", "defaultValue");
        propertyOption.setType(PropertyOption.TYPE_STRING);
        options.addOption(propertyOption);

        CommandLine cmd = new WriteableCommandLineImpl(new PropertyOption(), new ArrayList<>());
        options.parse(cmd, new String[]{"--property"});

        Map<String, String> properties = cmd.getProperties();
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("defaultValue", properties.get("property"));
    }
}