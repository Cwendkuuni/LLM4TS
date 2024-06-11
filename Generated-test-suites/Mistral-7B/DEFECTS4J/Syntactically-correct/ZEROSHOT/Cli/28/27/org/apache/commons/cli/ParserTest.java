package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser.
 */
public class ParserTest
{
    /**
     * Test parse method with basic options.
     */
    @Test
    public void testParseBasicOptions() throws Exception
    {
        String[] args = {"-a", "1", "-b", "2", "--", "-c", "3"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, null);

        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));
    }

    /**
     * Test parse method with required options.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws Exception
    {
        String[] args = {"--", "-a", "1"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "Error message");

        parser.setOptions(options);
        parser.parse(options, args, null);
    }

    /**
     * Test parse method with optional options.
     */
    @Test
    public void testParseOptionalOptions() throws Exception
    {
        String[] args = {"--", "-a", "1", "-b", "2"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, null);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertNull(cmd.getOptionValue("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
    }

    /**
     * Test parse method with long options.
     */
    @Test
    public void testParseLongOptions() throws Exception
    {
        String[] args = {"--long-option-a", "value-a", "--long-option-b", "value-b", "--"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("longOptionA", true, "Long option A");
        options.addOption("longOptionB", true, "Long option B");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, null);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("value-a", cmd.getOptionValue("longOptionA"));
        Assert.assertEquals("value-b", cmd.getOptionValue("longOptionB"));
    }

    /**
     * Test parse method with option groups.
     */
    @Test
    public void testParseOptionGroups() throws Exception
    {
        String[] args = {"--group1", "-a", "--group2", "-b"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.addOption("a", true, "Option A in group1");
        options.addOptionGroup(group1);

        OptionGroup group2 = new OptionGroup("group2", "Group 2");
        group2.addOption("b", true, "Option B in group2");
        options.addOptionGroup(group2);

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, null);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("true", cmd.getOptionValue("group1.a"));
        Assert.assertEquals("true", cmd.getOptionValue("group2.b"));
    }

    /**
     * Test parse method with properties.
     */
    @Test
    public void testParseProperties() throws Exception
    {
        String[] args = {"--", "-p", "property1=value1", "-p", "property2=value2"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addPropertyConstants(true);
        parser.setOptions(options);

        Properties properties = new Properties();
        CommandLine cmd = parser.parse(options, args, properties);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("value1", properties.getProperty("property1"));
        Assert.assertEquals("value2", properties.getProperty("property2"));
    }

    /**
     * Test parse method with properties and options.
     */
    @Test
    public void testParsePropertiesAndOptions() throws Exception
    {
        String[] args = {"--", "-p", "property1=value1", "-a"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addPropertyConstants(true);
        parser.setOptions(options);

        Properties properties = new Properties();
        CommandLine cmd = parser.parse(options, args, properties);

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value1", properties.getProperty("property1"));
        Assert.assertNull(cmd.getOptionValue("a"));
    }
}