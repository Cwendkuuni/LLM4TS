import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseSimple() throws Exception {
        String[] args = {"-a", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "a option");
        options.addOption("b", true, "b option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("c"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingOption() throws Exception {
        String[] args = {"--missingOption"};
        Options options = new Options();
        options.addOption("a", true, "a option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // the test should fail with MissingOptionException
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws Exception {
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", true, "a option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // the test should fail with MissingArgumentException
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws Exception {
        String[] args = {"--unknownOption"};
        Options options = new Options();
        options.addOption("a", true, "a option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // the test should fail with UnrecognizedOptionException
    }

    @Test
    public void testParseBooleanOption() throws Exception {
        String[] args = {"-b"};
        Options options = new Options();
        options.addOption("b", false, "b option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(false, cmd.hasOption("b"));
        Assert.assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testParseBooleanOptionValue() throws Exception {
        String[] args = {"--noB"};
        Options options = new Options();
        options.addOption("b", false, "b option");
        options.addOption("nob", false, "no b option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(false, cmd.hasOption("b"));
        Assert.assertEquals(true, cmd.hasOption("nob"));
    }

    @Test
    public void testParseListOption() throws Exception {
        String[] args = {"--optionA", "one", "--optionA", "two"};
        Options options = new Options();
        options.addOption("a", true, "a option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        List<String> values = (List<String>) cmd.getOptionValues("a");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("one", values.get(0));
        Assert.assertEquals("two", values.get(1));
    }

    @Test
    public void testParseListOptionValue() throws Exception {
        String[] args = {"--optionA=value1,value2"};
        Options options = new Options();
        options.addOption("a", true, "a option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        List<String> values = (List<String>) cmd.getOptionValues("a");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOptionValue() throws Exception {
        String[] args = {"--optionA=value"};
        Options options = new Options();
        options.addOption("a", true, "a option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // the test should fail with IllegalArgumentException
    }

    @Test
    public void testParseOptionGroup() throws Exception {
        String[] args = {"--optionGroupA", "-a"};
        Options options = new Options();

        OptionGroup groupA = new OptionGroup("groupA", "group A");
        groupA.setRequired(true);
        options.addOptionGroup(groupA);
        groupA.addOption(new Option("a", true, "option A"));

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, groupA.isSelected());
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingOptionGroup() throws Exception {
        String[] args = {"--optionGroupB", "-b"};
        Options options = new Options();

        OptionGroup groupB = new OptionGroup("groupB", "group B");
        options.addOptionGroup(groupB);
        groupB.addOption(new Option("b", true, "option B"));

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // the test should fail with MissingOptionException
    }
}