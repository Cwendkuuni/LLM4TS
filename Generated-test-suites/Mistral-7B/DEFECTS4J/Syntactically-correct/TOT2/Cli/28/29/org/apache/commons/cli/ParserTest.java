package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testSimpleParse() throws ParseException
    {
        // given
        String[] args = new String[]{"-a", "arg1", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "short option a");
        options.addOption("b", true, "short option b with an argument");

        Parser parser = new DefaultParser();

        // when
        CommandLine commandLine = parser.parse(options, args);

        // then
        Assert.assertEquals("Option a should be set to true", true, commandLine.hasOption("a"));
        Assert.assertEquals("Option b should be set to arg2", "arg1", commandLine.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException
    {
        // given
        String[] args = new String[]{"--invalidOption"};
        Options options = new Options();
        options.addOption("a", true, "short option a");

        Parser parser = new DefaultParser();

        // when
        parser.parse(options, args);

        // then a ParseException should be thrown
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        // given
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", true, "short option a", true, "This is a required option");

        Parser parser = new DefaultParser();

        // when
        parser.parse(options, args);

        // then a MissingOptionException should be thrown
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException
    {
        // given
        String[] args = new String[]{"-a", "--optionB"};
        Options options = new Options();
        options.addOption("a", true, "short option a");
        options.addOption("b", true, "short option b with an argument");

        Parser parser = new DefaultParser();

        // when
        parser.parse(options, args);

        // then a MissingArgumentException should be thrown
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        // given
        String[] args = new String[]{"--foo=bar"};
        Properties properties = new Properties();
        properties.setProperty("foo", "bar");

        Options options = new Options();
        options.addOption("f", true, "short option foo");

        Parser parser = new DefaultParser();

        // when
        CommandLine commandLine = parser.parse(options, args, properties, false);

        // then
        Assert.assertEquals("Option foo should be set to bar", "bar", commandLine.getOptionValue("f"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidProperties() throws ParseException
    {
        // given
        String[] args = new String[]{"--foo=bar"};
        Properties properties = new Properties();
        properties.setProperty("foo", "bar");

        Options options = new Options();
        options.addOption("f", true, "short option foo");

        Parser parser = new DefaultParser();

        // when
        parser.parse(options, args, properties, true);

        // then a ParseException should be thrown
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        // given
        String[] args = new String[]{"--", "nonOption"};
        Options options = new Options();
        options.addOption("a", true, "short option a");

        Parser parser = new DefaultParser();

        // when
        CommandLine commandLine = parser.parse(options, args, null, true);

        // then
        Assert.assertEquals("Option a should not be set", false, commandLine.hasOption("a"));
        Assert.assertEquals("nonOption should be in the args list", "nonOption", commandLine.getArgList().get(0));
    }
}