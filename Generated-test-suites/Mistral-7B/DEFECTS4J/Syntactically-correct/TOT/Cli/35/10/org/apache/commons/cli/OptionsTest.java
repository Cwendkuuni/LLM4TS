package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the Options class.
 */
public class OptionsTest {

    /**
     * Test method for {@link Options#addOptionGroup(OptionGroup)} with an OptionGroup containing multiple options.
     */
    @Test
    public void testAddOptionGroupWithMultipleOptions() {
        String[] args = {"-g", "--group1", "--option1", "--option2"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.addOption(new Option("o1", "option1", true, "Option 1"));
        group.addOption(new Option("o2", "option2", true, "Option 2"));
        options.addOptionGroup(group);

        CommandLine cmd;
        try {
            cmd = options.parse(args);
            Assert.assertEquals("--group1", cmd.getOptionValues("group1")[0]);
            Assert.assertEquals(true, cmd.hasOption("o1"));
            Assert.assertEquals(true, cmd.hasOption("o2"));
        } catch (ParseException e) {
            // should not reach here
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)} with a single option.
     */
    @Test
    public void testAddOptionWithSingleOption() {
        String[] args = {"-f", "--file"};

        Options options = new Options();
        options.addOption("f", "file", true, "File to process");

        CommandLine cmd;
        try {
            cmd = options.parse(args);
            Assert.assertEquals("File to process", cmd.getOptionValue("file"));
            Assert.assertNull(cmd.getOptionValue("unknown"));
        } catch (ParseException e) {
            // should not reach here
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)} with an option requiring an argument.
     */
    @Test
    public void testAddOptionWithArgument() {
        String[] args = {"-d", "--directory=/path/to/dir"};

        Options options = new Options();
        options.addOption("d", "directory", true, "Directory path");

        CommandLine cmd;
        try {
            cmd = options.parse(args);
            Assert.assertEquals("/path/to/dir", cmd.getOptionValue("directory"));
            Assert.assertNull(cmd.getOptionValue("unknown"));
        } catch (ParseException e) {
            // should not reach here
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)} with a long option with an alias.
     */
    @Test
    public void testAddOptionWithLongAlias() {
        String[] args = {"--longOpt", "--longAlias"};

        Options options = new Options();
        Option option = new Option("lo", "longOpt", false, "Long option with an alias");
        option.addAliases("longAlias");
        options.addOption(option);

        CommandLine cmd;
        try {
            cmd = options.parse(args);
            Assert.assertTrue(cmd.hasOption("longOpt"));
            Assert.assertTrue(cmd.hasOption("longAlias"));
        } catch (ParseException e) {
            // should not reach here
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link Options#hasOption(String)} with a short option.
     */
    @Test
    public void testHasShortOption() {
        String[] args = {"-f"};

        Options options = new Options();
        options.addOption("f", "file", true, "File to process");

        CommandLine cmd;
        try {
            cmd = options.parse(args);
            Assert.assertTrue(options.hasOption("f"));
            Assert.assertFalse(options.hasOption("unknown"));
        } catch (ParseException e) {
            // should not reach here
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link Options#hasOption(String)} with a long option.
     */
    @Test
    public void testHasLongOption() {
        String[] args = {"--file"};

        Options options = new Options();
        options.addOption("f", "file", true, "File to process");

        CommandLine cmd;
        try {
            cmd = options.parse(args);
            Assert.assertTrue(options.hasOption("file"));
            Assert.assertFalse(options.hasOption("unknown"));
        } catch (ParseException e) {
            // should not reach here
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link Options#getOptionValue(String)} with a short option.
     */
    @Test
    public void testGetOptionValueWithShortOption() {
        String[] args = {"-f", "file.txt"};

        Options options = new Options();
        options.addOption("f", "file", true, "File to process");

        CommandLine cmd;
        try {
            cmd = options.parse(args);
            Assert.assertEquals("file.txt", cmd.getOptionValue("file"));
            Assert.assertNull(cmd.getOptionValue("unknown"));
        } catch (ParseException e) {
            // should not reach here
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link Options#getOptionValue(String)} with a long option.
     */
    @Test
    public void testGetOptionValueWithLongOption() {
        String[] args = {"--file", "file.txt"};

        Options options = new Options();
        options.addOption("f", "file", true, "File to process");

        CommandLine cmd;
        try {
            cmd = options.parse(args);
            Assert.assertEquals("file.txt", cmd.getOptionValue("file"));
            Assert.assertNull(cmd.getOptionValue("unknown"));
        } catch (ParseException e) {
            // should not reach here
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link Options#getOptionValue(String)} with an option that has no value.
     */
    @Test
    public void testGetOptionValueWithNoValue() {
        String[] args = {"-f"};

        Options options = new Options();
        options.addOption("f", "file", false, "File to process");

        CommandLine cmd;
        try {
            cmd = options.parse(args);
            Assert.assertNull(cmd.getOptionValue("file"));
            Assert.assertNull(cmd.getOptionValue("unknown"));
        } catch (ParseException e) {
            // should not reach here
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link Options#parse(String[])} with invalid arguments.
     */
    @Test(expected = ParseException.class)
    public void testParseWithInvalidArguments() {
        String[] args = {"--invalid", "-f", "--invalid=value"};

        Options options = new Options();
        options.addOption("f", "file", true, "File to process");

        options.parse(args);
    }

    /**
     * Test method for {@link Options#parse(String[])} with required options.
     */
    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOptions() {
        String[] args = {};

        Options options = new Options();
        options.addOption("f", "file", true, "File to process", true);

        options.parse(args);
    }
}