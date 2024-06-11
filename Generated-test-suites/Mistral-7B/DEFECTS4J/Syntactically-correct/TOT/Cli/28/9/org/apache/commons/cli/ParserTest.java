package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testSimpleOptions() throws ParseException
    {
        String[] args = new String[]{"-a", "1", "-b", "2"};

        Options options = new Options();
        options.addOption("a", true, "Print A");
        options.addOption("b", true, "Print B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals(new Integer(1), cmd.getOptionValue("a"));
        Assert.assertEquals(new Integer(2), cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("c"));
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] args = new String[]{"-c"};

        Options options = new Options();
        options.addOption("a", true, "Print A");
        options.addOption("c", true, "Print C").required(true);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // the test should fail with a ParseException
    }

    @Test
    public void testShortOptions() throws ParseException
    {
        String[] args = new String[]{"-a", "1", "-b", "2", "--help"};

        Options options = new Options();
        options.addOption("a", true, "Print A");
        options.addOption("b", true, "Print B");
        options.addOption("h", false, "Print Help");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals(new Integer(1), cmd.getOptionValue("a"));
        Assert.assertEquals(new Integer(2), cmd.getOptionValue("b"));
        Assert.assertTrue(cmd.hasOption("h"));
    }

    @Test
    public void testLongOptions() throws ParseException
    {
        String[] args = new String[]{"--print-a=1", "--print-b=2", "--help"};

        Options options = new Options();
        options.addOption("printA", true, "Print A");
        options.addOption("printB", true, "Print B");
        options.addOption("help", false, "Print Help");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("printA"));
        Assert.assertTrue(cmd.hasOption("printB"));
        Assert.assertEquals(new Integer(1), cmd.getOptionValue("printA"));
        Assert.assertEquals(new Integer(2), cmd.getOptionValue("printB"));
        Assert.assertTrue(cmd.hasOption("help"));
    }

    @Test
    public void testOptionGroups() throws ParseException
    {
        String[] args = new String[]{"--group1", "-a", "1", "-b", "2", "--group2", "--group1", "-c"};

        Options options = new Options();

        OptionGroup group1 = new OptionGroup("group1");
        group1.setDescription("Option group 1");
        group1.addOption("a", true, "Print a");
        group1.addOption("c", true, "Print c");

        OptionGroup group2 = new OptionGroup("group2");
        group2.setDescription("Option group 2");
        group2.addOption("b", true, "Print b");

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals(new Integer(1), cmd.getOptionValue("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals(new Integer(2), cmd.getOptionValue("b"));
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertFalse(cmd.hasOption("d"));
        Assert.assertEquals(2, cmd.getNumOptions());
        Assert.assertEquals(2, cmd.getNumArgs());

        Assert.assertEquals("group1", cmd.getOptionGroup(group1).getName());
        Assert.assertEquals("group2", cmd.getOptionGroup(group2).getName());
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException
    {
        String[] args = new String[]{"--invalid"};

        Options options = new Options();
        options.addOption("a", true, "Print A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // the test should fail with a ParseException
    }

    @Test
    public void testProperties() throws ParseException
    {
        String[] args = new String[]{"--propertyFile", "test.properties"};

        Options options = new Options();
        options.addOption("pf", true, "Property file");

        Properties props = new Properties();
        props.setProperty("test.property1", "value1");
        props.setProperty("test.property2", "value2");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, props);

        Assert.assertEquals("test.properties", cmd.getOptionValue("pf"));
        Assert.assertEquals("value1", cmd.getOptionValue("test.property1"));
        Assert.assertEquals("value2", cmd.getOptionValue("test.property2"));
    }

    @Test
    public void testMixedOptions() throws ParseException
    {
        String[] args = new String[]{"--print-a", "-b", "2", "--print-b=3", "--help"};

        Options options = new Options();
        options.addOption("printA", true, "Print A");
        options.addOption("b", true, "Print B");
        options.addOption("h", false, "Print Help");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("printA"));
        Assert.assertFalse(cmd.hasOption("a"));
        Assert.assertEquals(new Integer(2), cmd.getOptionValue("b"));
        Assert.assertEquals(new Integer(3), cmd.getOptionValue("printB"));
        Assert.assertTrue(cmd.hasOption("h"));
    }
}