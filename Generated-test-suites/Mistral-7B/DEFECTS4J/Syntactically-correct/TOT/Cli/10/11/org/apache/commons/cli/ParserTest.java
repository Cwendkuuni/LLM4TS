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
 * Tests the {@link Parser} class.
 */
public class ParserTest {

    @Test
    public void testSimpleParse() throws ParseException {
        String[] args = {"-a", "1", "--optionB", "2", "--optionC", "3"};

        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", true, "B value");
        options.addOption("c", true, "C value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertNotNull(cmd);
        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException {
        String[] args = {"--unknown"};

        Options options = new Options();
        options.addOption("a", true, "A value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.fail("Expected ParseException");
    }

    @Test
    public void testMissingArgument() throws ParseException {
        String[] args = {"-a"};

        Options options = new Options();
        options.addOption("a", true, "A value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertNotNull(cmd);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertNull(cmd.getOptionValue("a"));
    }

    @Test
    public void testStopAtNonOption() throws ParseException {
        String[] args = {"--", "-n", "value"};

        Options options = new Options();
        options.addOption("n", true, "N value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertNotNull(cmd);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getArgs().get(0));
        Assert.assertFalse(cmd.hasOption("n"));
        Assert.assertNull(cmd.getOptionValue("n"));
    }

    @Test
    public void testProperties() throws ParseException {
        String[] args = {"--foo=bar", "-a"};

        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("foo", true, "Foo value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertNotNull(cmd);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("foo"));
        Assert.assertNull(cmd.getOptionValue("a"));
        Assert.assertEquals("bar", cmd.getOptionValue("foo"));
    }

    @Test
    public void testHelp() throws ParseException {
        String[] args = {"--help"};

        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("h", "help", false, "Print help");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertNotNull(cmd);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertFalse(cmd.hasOption("h"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("SimpleTest", options);
    }
}