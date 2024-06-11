import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseSimpleCommandLine() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", false, "Output flag");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-o"});
        Assert.assertEquals("Output flag", cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getArgs());
    }

    @Test
    public void testParseCommandLineWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", false, "Output flag");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-o", "out.txt"});
        Assert.assertEquals("out.txt", cmd.getOptionValue("output"));
        Assert.assertEquals(new String[]{"out.txt"}, cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testParseCommandLineWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "Output flag");
        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-a"});
    }

    @Test(expected = ParseException.class)
    public void testParseCommandLineWithMissingValueForOptionalOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "Output flag");
        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-o"});
    }

    @Test(expected = ParseException.class)
    public void testParseCommandLineWithInvalidOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"--invalid"});
    }

    @Test
    public void testParseCommandLineWithOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.addOption("h", "help", false, "Print this help message.");
        group.addOption("v", "verbose", true, "Print verbose output.");
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-h", "--verbose"});
        Assert.assertTrue(cmd.hasOption("help"));
        Assert.assertTrue(cmd.hasOption("verbose"));
        Assert.assertNull(cmd.getArgs());
    }

    @Test
    public void testParseCommandLineWithLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("outputfile", "of", false, "Output file.");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--outputfile"});
        Assert.assertNull(cmd.getOptionValue("outputfile"));
        Assert.assertNull(cmd.getArgs());

        cmd = parser.parse(options, new String[]{"--outputfile", "output.txt"});
        Assert.assertEquals("output.txt", cmd.getOptionValue("outputfile"));
        Assert.assertNull(cmd.getArgs());

        cmd = parser.parse(options, new String[]{"--outputfile", "output.txt", "arg1", "arg2"});
        Assert.assertEquals("output.txt", cmd.getOptionValue("outputfile"));
        Assert.assertEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testParseCommandLineWithMultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", false, "Output flag");
        options.addOption("v", "verbose", false, "Verbose flag");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-o", "-v"});
        Assert.assertTrue(cmd.hasOption("output"));
        Assert.assertTrue(cmd.hasOption("verbose"));
        Assert.assertNull(cmd.getArgs());
    }

    @Test
    public void testParseCommandLineWithMixedShortAndLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", false, "Output flag");
        options.addOption("h", "help", false, "Print this help message.");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-h", "-o"});
        Assert.assertTrue(cmd.hasOption("help"));
        Assert.assertTrue(cmd.hasOption("output"));
        Assert.assertNull(cmd.getArgs());
    }

    @Test
    public void testParseCommandLineWithUnmatchedArgument() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2", "-o"});
        Assert.assertEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
        Assert.assertNull(cmd.getOptionValue("output"));
    }

    @Test
    public void testParseCommandLineWithExtraArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-o", "arg1", "arg2"});
        Assert.assertEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
        Assert.assertNull(cmd.getOptionValue("output"));
    }

    @Test
    public void testParseCommandLineWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("p", "prop", true, "Property name");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("prop1", "value1");
        properties.setProperty("prop2", "value2");
        CommandLine cmd = parser.parse(options, new String[]{}, properties);
        Assert.assertEquals(2, cmd.getOptions().size());
        Option opt1 = cmd.getOption(0);
        Assert.assertEquals("value1", opt1.getValue());
        Option opt2 = cmd.getOption(1);
        Assert.assertEquals("value2", opt2.getValue());
        Assert.assertEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testParseCommandLineWithPropertiesAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("p", "prop", true, "Property name");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("prop1", "value1");
        properties.setProperty("prop2", "value2");
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"}, properties);
        Assert.assertEquals(2, cmd.getOptions().size());
        Option opt1 = cmd.getOption(0);
        Assert.assertEquals("value1", opt1.getValue());
        Option opt2 = cmd.getOption(1);
        Assert.assertEquals("value2", opt2.getValue());
        Assert.assertEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }
}