package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    @Test
    public void testParseBasic() throws ParseException
    {
        String[] arguments = {"-a", "1", "--quiet", "arg1", "--quiet", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");
        options.addOption("q", false, "An option with a flag");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertFalse("quiet option should not be set by default", cmd.hasOption("quiet"));
        Assert.assertTrue("a option should be set", cmd.hasOption("a"));
        Assert.assertTrue("a option value should be set", cmd.getOptionValue("a") != null);
        Assert.assertEquals("a option value should be '1'", "1", cmd.getOptionValue("a"));
        Assert.assertEquals("number of args should be 2", 2, cmd.getArgs().size());
        Assert.assertEquals("args[0] should be 'arg1'", "arg1", cmd.getArgs().get(0));
        Assert.assertEquals("args[1] should be 'arg2'", "arg2", cmd.getArgs().get(1));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException
    {
        String[] arguments = {"--non-existent"};
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");

        Parser parser = new DefaultParser();
        parser.parse(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testParseMissingValue() throws ParseException
    {
        String[] arguments = {"-a", "--quiet"};
        Options options = new Options();
        options.addOption("a", true, "An option with a flag and value");
        options.addOption("q", false, "An option with a flag");

        Parser parser = new DefaultParser();
        parser.parse(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() throws ParseException
    {
        String[] arguments = {"--invalid"};
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");

        Parser parser = new DefaultParser();
        parser.parse(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testParseMixedOptions() throws ParseException
    {
        String[] arguments = {"--quiet", "-a", "1", "--quiet", "--invalid", "arg1", "--quiet", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");
        options.addOption("q", false, "An option with a flag");
        options.addOption("i", false, "An invalid option");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertTrue("quiet option should be set", cmd.hasOption("quiet"));
        Assert.assertTrue("a option should be set", cmd.hasOption("a"));
        Assert.assertTrue("a option value should be set", cmd.getOptionValue("a") != null);
        Assert.assertEquals("a option value should be '1'", "1", cmd.getOptionValue("a"));
        Assert.assertEquals("number of args should be 3", 3, cmd.getArgs().size());
        Assert.assertEquals("args[0] should be 'arg1'", "arg1", cmd.getArgs().get(0));
        Assert.assertEquals("args[1] should be 'arg2'", "arg2", cmd.getArgs().get(1));
        Assert.assertFalse("invalid option should not be set", cmd.hasOption("i"));
    }

    @Test
    public void testParseProperties() throws ParseException
    {
        String[] arguments = {"--foo", "value1", "--bar", "value2"};
        Properties properties = new Properties();
        properties.put("foo", "value3");
        properties.put("bar.baz", "value4");

        Options options = new Options();
        options.addOption("foo", true, "An option with a flag and value");
        options.addOption("b", "baz", true, "An option with a flag, a value and a short name");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, properties, false);

        Assert.assertTrue("foo option should be set", cmd.hasOption("foo"));
        Assert.assertEquals("foo option value should be 'value1'", "value1", cmd.getOptionValue("foo"));
        Assert.assertTrue("b option should be set", cmd.hasOption("b"));
        Assert.assertEquals("b option value should be 'value2'", "value2", cmd.getOptionValue("b"));
        Assert.assertTrue("b option short name should be set", cmd.hasOption("baz"));
        Assert.assertEquals("b option short name value should be 'value2'", "value2", cmd.getOptionValue("baz"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingProperty() throws ParseException
    {
        String[] arguments = {"--foo"};
        Properties properties = new Properties();

        Options options = new Options();
        options.addOption("foo", true, "An option with a flag and value");

        Parser parser = new DefaultParser();
        parser.parse(options, (String[]) null, properties, false);
    }

    @Test
    public void testParseMultipleProperties() throws ParseException
    {
        String[] arguments = {"--foo", "value1", "--foo", "value2"};
        Properties properties = new Properties();
        properties.put("foo", "value1,value2");

        Options options = new Options();
        options.addOption("foo", true, "An option with a flag and value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, properties, false);

        Assert.assertTrue("foo option should be set", cmd.hasOption("foo"));
        Assert.assertEquals("foo option value should be 'value1,value2'", "value1,value2", cmd.getOptionValue("foo"));
    }
}

class DefaultParser extends Parser
{
    @Override
    protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException
    {
        return arguments;
    }
}