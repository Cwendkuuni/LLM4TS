package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    @Test
    public void testParseSimple() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, new String[]{"-a"});
        Assert.assertEquals("Option A was not set correctly", true, cmd.hasOption("a"));
    }

    @Test
    public void testParseWithValue() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "value"});
        Assert.assertEquals("Option A value was not set correctly", "value", cmd.getOptionValue("a"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-b"});
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiedOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.getOption("a").setRequired(true);

        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"--parse"});
    }

    @Test
    public void testParseWithGroup() throws ParseException
    {
        Options options = new Options();
        OptionGroup group = new OptionGroup("Group1", "Group 1");
        group.setRequired(true);
        group.addOption("g1", true, "Option G1");
        group.addOption("g2", true, "Option G2");
        options.addOptionGroup(group);
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--group1", "--g1", "--g2"});
        Assert.assertEquals("Option A was not set correctly", true, cmd.hasOption("a"));
        Assert.assertTrue("Group1 was not selected", group.isSelected());
    }

    @Test(expected = ParseException.class)
    public void testParseWithGroupMissingOption() throws ParseException
    {
        Options options = new Options();
        OptionGroup group = new OptionGroup("Group1", "Group 1");
        group.setRequired(true);
        group.addOption("g1", true, "Option G1");
        group.addOption("g2", true, "Option G2");
        options.addOptionGroup(group);
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"--group1", "--g1"});
    }

    @Test
    public void testParseWithMultipleValues() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A", 1);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "value1", "-a", "value2"});
        List<String> values = (List<String>) cmd.getValues("a");
        Assert.assertEquals("Number of values for Option A was incorrect", 2, values.size());
        Assert.assertEquals("Value of Option A[0] was incorrect", "value1", values.get(0));
        Assert.assertEquals("Value of Option A[1] was incorrect", "value2", values.get(1));
    }
}