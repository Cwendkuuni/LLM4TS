package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        String[] args = {"--input", "file.txt", "-o", "output.csv", "-h"};
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file");
        options.addRequiredOption("o", "output", true, "Output file");
        options.addOption("h", "help", false, "Print usage help");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertFalse(cmd.hasOption("help"));
        Assert.assertEquals("file.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.csv", cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getOptionValue("unknown"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        String[] args = {"--input", "file.txt"};
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file");
        options.addRequiredOption("o", "output", true, "Output file");

        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        String[] args = {"--unknown"};
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file");
        options.addRequiredOption("o", "output", true, "Output file");

        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingOptionArgument() throws ParseException {
        String[] args = {"--option"};
        Options options = new Options();
        options.addOption("o", "option", true, "Option value");

        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = {"--input", "file.txt"};
        Properties properties = new Properties();
        properties.setProperty("input", "input.txt");

        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file");
        options.addOption("p", "properties", true, "Properties file");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
    }

    @Test
    public void testParseWithStopAtNonOptions() throws ParseException {
        String[] args = {"--input", "file.txt", "non-option", "-o", "output.csv"};
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file");
        options.addRequiredOption("o", "output", true, "Output file");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertEquals("non-option", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithStopAtNonOptionsAndUnknownOption() throws ParseException {
        String[] args = {"--input", "file.txt", "--unknown"};
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("--unknown", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        String[] args = {"--input", "file.txt", "--group", "groupA"};
        Options options = new Options();
        OptionGroup groupA = new OptionGroup("groupA", "Group A");
        groupA.addRequiredOption("a", "optionA", true, "Option A");
        groupA.addRequiredOption("b", "optionB", true, "Option B");
        options.addOptionGroup(groupA);
        options.addRequiredOption("i", "input", true, "Input file");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("file.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("groupA", cmd.getOptionValue("group"));
    }
}

class DefaultParser extends Parser {
    @Override
    protected String[] flatten(Options options, String[] arguments, boolean stopAtNonOption) {
        return Arrays.copyOfRange(arguments, 0, arguments.length);
    }
}