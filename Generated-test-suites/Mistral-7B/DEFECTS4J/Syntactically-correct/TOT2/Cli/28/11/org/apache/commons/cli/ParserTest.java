import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseWithNoOptions() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[0];
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(0, cmd.getArgs().length);
        Assert.assertNull(parser.getOptions());
        Assert.assertNull(parser.getRequiredOptions());
    }

    @Test
    public void testParseWithSimpleOptions() throws ParseException {
        Options options = new Options();
        Option optA = new Option("a", true, "A");
        Option optB = new Option("b", 1, "B");
        options.addOption(optA);
        options.addOption(optB);
        String[] arguments = new String[]{"-a", "--b=1"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals("A", cmd.getOptionValue("a"));
        Assert.assertEquals("1", cmd.getOptionValue("b"));
        Assert.assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Options options = new Options();
        Option optA = new Option("a", true, "A");
        Option optB = new Option("b", 1, "B");
        Option optC = new Option("c", 3, "C");
        options.addOption(optA);
        options.addOption(optB);
        options.addOption(optC);
        String[] arguments = new String[]{"-a", "--b=1", "arg1", "arg2"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals("A", cmd.getOptionValue("a"));
        Assert.assertEquals("1", cmd.getOptionValue("b"));
        Assert.assertEquals(2, cmd.getArgs().length);
        Assert.assertEquals("arg1", cmd.getArgs()[0]);
        Assert.assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Options options = new Options();
        Option optA = new Option("a", true, "A");
        Option optB = new Option("b", 1, "B");
        Option optC = new Option("c", 3, "C").required(true);
        options.addOption(optA);
        options.addOption(optB);
        options.addOption(optC);
        String[] arguments = new String[]{"-a", "--b=1"};
        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Option optA = new Option("a", true, "A");
        Option optB = new Option("b", 1, "B");
        options.addOption(optA);
        options.addOption(optB);
        String[] arguments = new String[]{"--x", "arg1", "--a"};
        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option optA = new Option("a", true, "A");
        Option optB = new Option("b", 1, "B").required(true);
        options.addOption(optA);
        options.addOption(optB);
        String[] arguments = new String[]{"-a"};
        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMixedOptionsAndArguments() throws ParseException {
        Options options = new Options();
        Option optA = new Option("a", true, "A");
        Option optB = new Option("b", 1, "B");
        Option optC = new Option("c", 3, "C").required(true);
        options.addOption(optA);
        options.addOption(optB);
        options.addOption(optC);
        String[] arguments = new String[]{"--b=1", "-a", "arg1", "--c"};
        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        Option optA = new Option("a", true, "A");
        Option optB = new Option("b", 1, "B");
        options.addOption(optA);
        options.addOption(optB);
        Properties properties = new Properties();
        properties.put("a", "valueA");
        properties.put("b", "valueB");
        Parser parser = new DefaultParser();
        CommandLine cmd
                = parser.parse(options, new String[0], properties, false);
        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
    }
}