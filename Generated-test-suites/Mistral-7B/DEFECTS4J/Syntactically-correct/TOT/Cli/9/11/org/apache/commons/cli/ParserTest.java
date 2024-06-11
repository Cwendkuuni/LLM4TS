package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testSimpleParsing() throws Exception {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file path");
        options.addOption("o", "outputFile", true, "output file path");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"-i", "/path/to/input.txt", "-o", "/path/to/output.txt"}, options);

        Assert.assertEquals("/path/to/input.txt", cmd.getOptionValue("inputFile"));
        Assert.assertEquals("/path/to/output.txt", cmd.getOptionValue("outputFile"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingOption() throws Exception {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file path");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"-o", "/path/to/output.txt"}, options);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws Exception {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file path");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"-i"}, options);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file path");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"--invalidOption"}, options);
    }

    @Test
    public void testStopAtNonOption() throws Exception {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file path");
        options.addOption("a", "action", true, "action to perform");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"-i", "/path/to/input.txt", "-a", "print"}, options, true);

        Assert.assertEquals("/path/to/input.txt", cmd.getOptionValue("inputFile"));
        Assert.assertEquals("print", cmd.getOptionValue("action"));
        Assert.assertEquals(Arrays.asList("--").subList(0, cmd.getArgs().size()), cmd.getArgs());
    }

    @Test
    public void testPropertiesParsing() throws Exception {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file path");

        Properties properties = new Properties();
        properties.setProperty("inputFile", "/path/to/input.txt");
        properties.setProperty("action", "print");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[0], options, properties, true);

        Assert.assertEquals("/path/to/input.txt", cmd.getOptionValue("inputFile"));
        Assert.assertEquals("print", cmd.getOptionValue("action"));
        Assert.assertEquals(Arrays.asList("--"), cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testParseException() throws Exception {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file path");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"--"}, options);
    }

    @Test
    public void testOptionGroup() throws Exception {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.setRequired(false);

        options.addOptionGroup(group);
        options.addOption("g", "group1", false, "Group 1", group);
        options.addOption("o", "outputFile", true, "output file path");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"--group1"}, options);

        Assert.assertTrue(group.isSelected());
        Assert.assertNull(cmd.getOptionValue("outputFile"));
        Assert.assertEquals(Arrays.asList("--group1"), cmd.getArgs());
    }
}