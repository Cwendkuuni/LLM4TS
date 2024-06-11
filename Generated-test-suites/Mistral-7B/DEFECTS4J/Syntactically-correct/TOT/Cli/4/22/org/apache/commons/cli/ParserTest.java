import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseBasic() throws Exception {
        String[] args = new String[]{"-a", "1", "--b=2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(1, cmd.getOptionValue("a").length);
        Assert.assertEquals("1", cmd.getOptionValue("a")[0]);
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals(2, cmd.getOptionValue("b").length);
        Assert.assertEquals("2", cmd.getOptionValue("b")[0]);
        Assert.assertNull(cmd.getArgs());
    }

    @Test
    public void testParseWithArg() throws Exception {
        String[] args = new String[]{"-a", "1", "--b=2", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(1, cmd.getOptionValue("a").length);
        Assert.assertEquals("1", cmd.getOptionValue("a")[0]);
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals(2, cmd.getOptionValue("b").length);
        Assert.assertEquals("2", cmd.getOptionValue("b")[0]);
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws Exception {
        String[] args = new String[]{"-a", "--b"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Option optionB = options.getOption("b");
        processArgs(optionB, Arrays.asList(args).listIterator());

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertNull(cmd.getOptionValue("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals(Collections.EMPTY_LIST, cmd.getValues("b"));
        Assert.assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingOption() throws Exception {
        String[] args = new String[]{"-c", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(false, cmd.hasOption("c"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception {
        String[] args = new String[]{"--x", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(false, cmd.hasOption("x"));
    }

    @Test(expected = NoSuchOptionException.class)
    public void testNoSuchOption() throws Exception {
        String[] args = new String[]{"--x", "1"};
        Options options = new Options();
        options.addOption("x", true, "Option X");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(false, cmd.hasOption("x"));
    }

    @Test
    public void testParseWithProperties() throws Exception {
        String[] args = new String[]{"--propFile", "test.properties"};
        Properties properties = new Properties();
        properties.setProperty("prop.key", "prop.value");

        Options options = new Options();
        options.addOption("propFile", true, "Property file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertEquals(true, cmd.hasOption("propFile"));
        Assert.assertEquals("test.properties", cmd.getOptionValue("propFile"));
        Assert.assertEquals(cmd.getOptionValues("propFile"), new String[]{"test.properties"});

        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("prop.value", properties.getProperty("prop.key"));
    }

    @Test
    public void testParseWithRequiredOptions() throws Exception {
        String[] args = new String[]{"--option1", "value1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addRequiredOption("option3", true, "Option 3");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(true, cmd.hasOption("option1"));
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals(true, cmd.hasOption("option2"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2"));
        Assert.assertTrue(cmd.hasOption("option3"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws Exception {
        String[] args = new String[]{"--option1", "value1"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addRequiredOption("option2", true, "Option 2");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(true, cmd.hasOption("option1"));
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertFalse(cmd.hasOption("option2"));
    }
}