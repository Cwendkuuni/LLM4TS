package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link Parser} class.
 */
public class ParserTest {

    @Test
    public void testSimpleParse() throws Exception {
        String[] args = {"-a", "1", "--optionB", "2"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws Exception {
        String[] args = {"--optionA"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.fail("Expected ParseException but got: " + cmd);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception {
        String[] args = {"--unknownOption"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.fail("Expected UnrecognizedOptionException but got: " + cmd);
    }

    @Test
    public void testParseWithProperties() throws Exception {
        String[] args = {"--propertyFile", "test.properties"};

        Options options = new Options();
        options.addOption("pf", true, "Property file");
        options.addLongOption("help", false, "Display help");

        Properties properties = new Properties();
        properties.setProperty("propertyA", "valueA");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertFalse(cmd.hasOption("help"));
        Assert.assertEquals("test.properties", cmd.getOptionValue("pf"));
        Assert.assertEquals(1, options.getOptions().size());
        Assert.assertEquals("valueA", options.getOption("propertyA").getValue());
    }

    @Test
    public void testParseWithMixedLongAndShortOptions() throws Exception {
        String[] args = {"-a", "1", "--optionB", "2", "-b", "3"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addLongOption("optionB", true, "Option B");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals(true, cmd.hasOption("optionB"));
        Assert.assertEquals("2", cmd.getOptionValue("optionB"));
        Assert.assertEquals(new String[]{"3"}, cmd.getArgs());
    }

    @Test
    public void testParseWithRequiredOptionGroup() throws Exception {
        String[] args = {"--groupOne", "--groupTwo"};

        Options options = new Options();
        OptionGroup groupOne = new OptionGroup("groupOne");
        groupOne.setRequired(true);
        groupOne.addOption("groupOneOption1", true, "Option 1");
        groupOne.addOption("groupOneOption2", true, "Option 2");
        options.addOptionGroup(groupOne);

        OptionGroup groupTwo = new OptionGroup("groupTwo");
        groupTwo.addOption("groupTwoOption1", true, "Option 1");
        options.addOptionGroup(groupTwo);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertTrue(groupOne.isSelected());
        Assert.assertTrue(groupTwo.isSelected());
    }

    @Test
    public void testParseWithOptionalOptionGroup() throws Exception {
        String[] args = {"--groupOne", "--groupTwo"};

        Options options = new Options();
        OptionGroup groupOne = new OptionGroup("groupOne");
        groupOne.addOption("groupOneOption1", true, "Option 1");
        groupOne.addOption("groupOneOption2", true, "Option 2");
        options.addOptionGroup(groupOne);

        OptionGroup groupTwo = new OptionGroup("groupTwo");
        groupTwo.addOption("groupTwoOption1", true, "Option 1");
        options.addOptionGroup(groupTwo);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertTrue(groupOne.isSelected());
        Assert.assertTrue(groupTwo.isSelected());
    }

    @Test
    public void testParseWithMixedShortAndLongOptions() throws Exception {
        String[] args = {"-a", "1", "--optionB", "2", "-b", "3"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addLongOption("optionB", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals(true, cmd.hasOption("optionB"));
        Assert.assertEquals("2", cmd.getOptionValue("optionB"));
        Assert.assertEquals(new String[]{"3"}, cmd.getArgs());
    }

    @Test
    public void testParseWithMixedShortAndLongOptionsAndProperties() throws Exception {
        String[] args = {"--propertyFile", "test.properties", "-a", "1", "--optionB", "2", "-b", "3"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addLongOption("optionB", true, "Option B");
        options.addOption("pf", true, "Property file");

        Properties properties = new Properties();
        properties.setProperty("propertyA", "valueA");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals(true, cmd.hasOption("optionB"));
        Assert.assertEquals("2", cmd.getOptionValue("optionB"));
        Assert.assertEquals("test.properties", cmd.getOptionValue("pf"));
        Assert.assertEquals(1, options.getOptions().size());
    }

    @Test
    public void testParseWithMissingValueForRequiredOption() throws Exception {
        String[] args = {"--optionA"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args, false);
            Assert.fail("Expected ParseException but got: " + cmd);
        } catch (ParseException e) {
            Assert.assertEquals("Missing value for required option: b", e.getMessage());
        }
    }

    @Test
    public void testParseWithMissingValueForOptionalOption() throws Exception {
        String[] args = {"--optionA"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithMissingValueForOptionalOptionGroup() throws Exception {
        String[] args = {"--groupOne"};

        Options options = new Options();
        OptionGroup groupOne = new OptionGroup("groupOne");
        groupOne.addOption("groupOneOption1", true, "Option 1");
        groupOne.addOption("groupOneOption2", true, "Option 2");
        options.addOptionGroup(groupOne);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertTrue(groupOne.isSelected());
        Assert.assertEquals(0, groupOne.getValues().length);
    }

    @Test
    public void testParseWithMissingValueForOptionalOptionGroupAndValue() throws Exception {
        String[] args = {"--groupOne", "valueA"};

        Options options = new Options();
        OptionGroup groupOne = new OptionGroup("groupOne");
        groupOne.addOption("groupOneOption1", true, "Option 1");
        groupOne.addOption("groupOneOption2", true, "Option 2");
        options.addOptionGroup(groupOne);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertTrue(groupOne.isSelected());
        Assert.assertEquals("valueA", groupOne.getValue("groupOneOption1"));
        Assert.assertEquals(1, groupOne.getValues().length);
    }
}