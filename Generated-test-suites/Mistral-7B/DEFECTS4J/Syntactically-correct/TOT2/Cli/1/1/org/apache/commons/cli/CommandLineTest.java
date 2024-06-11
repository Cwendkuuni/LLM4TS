package org.junit.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.junit.Assert;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        cmd.parse(options.parse("--a"));
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption('a'));
        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertFalse(cmd.hasOption('b'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        cmd.parse(options.parse("--a=valueA --b=valueB"));
        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("c"));
        Assert.assertNull(cmd.getOptionValue('c'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        cmd.parse(options.parse("--a value1 --a value2"));
        String[] values = cmd.getOptionValues("a");
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, values);
    }

    // Add test cases for edge cases

    // Add test cases for exception handling scenarios

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidInput() {
        CommandLine cmd = new CommandLine();
        Options options = new Options();
        cmd.parse(options.parse("invalidInput"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithNullOption() {
        CommandLine cmd = new CommandLine();
        Options options = new Options();
        cmd.addOption(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyOptions() {
        CommandLine cmd = new CommandLine();
        Options options = new Options();
        cmd.parse(options.parse(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullArguments() {
        CommandLine cmd = new CommandLine();
        Options options = new Options();
        cmd.parse(options.parse((String[]) null));
    }
}