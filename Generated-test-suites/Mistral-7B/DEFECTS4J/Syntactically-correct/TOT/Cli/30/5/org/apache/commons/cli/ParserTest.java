import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    @Test
    public void testParseWithBasicOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "An option with a boolean value");
        options.addOption("b", "optionB", false, "An option with a boolean value, not required");
        options.addOption("c", "optionC", true, "An option with a required argument");

        CommandLineParser parser = new DefaultParser();

        String[] args = {"-a", "true", "-c", "value"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertEquals("value", cmd.getOptionValue("c"));
        assertFalse(cmd.hasOption("d"));
        assertNull(cmd.getOptionValue("d"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "An option with a boolean value");

        CommandLineParser parser = new DefaultParser();

        String[] args = {"-d"};
        parser.parse(options, args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingOptionArgument() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "An option with a required argument");

        CommandLineParser parser = new DefaultParser();

        String[] args = {"-a"};
        parser.parse(options, args);
    }

    @Test
    public void testParseWithOptionalOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", false, "An option with a boolean value, not required");

        CommandLineParser parser = new DefaultParser();

        String[] args = {};
        CommandLine cmd = parser.parse(options, args);

        assertFalse(cmd.hasOption("a"));

        args = new String[]{"-a"};
        cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));

        args = new String[]{"-a", "true"};
        cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "An option with a boolean value");

        CommandLineParser parser = new DefaultParser();

        String[] args = {};
        CommandLine cmd = parser.parse(options, null, new Properties(), false);

        assertNull(cmd.getOptionValue("a"));

        Properties props = new Properties();
        props.setProperty("optionA", "true");

        cmd = parser.parse(options, null, props, false);

        assertTrue(cmd.hasOption("a"));
        assertEquals("true", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithGroupedOptions() throws ParseException
    {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "Group of related options");
        group.addOption(new Option("a", "optionA", true, "An option in the group", group));
        group.addOption(new Option("b", "optionB", true, "Another option in the group", group));

        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();

        String[] args = {"--group", "-a", "true", "--group", "-b", "false"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("group"));
        assertTrue(cmd.hasOptionGroup("group"));
        assertSame(group, cmd.getOptionGroup("group"));

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));

        assertEquals("true", cmd.getOptionValue("a"));
        assertEquals("false", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithLongOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("longOptionA", "longOptionA", true, "A long option with a boolean value");

        CommandLineParser parser = new DefaultParser();

        String[] args = {"--longOptionA", "true"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("longOptionA"));
        assertEquals("true", cmd.getOptionValue("longOptionA"));
    }

    @Test
    public void testParseWithMixedShortAndLongOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "An option with a boolean value");
        options.addOption("l", "longOptionA", true, "A long option with a boolean value");

        CommandLineParser parser = new DefaultParser();

        String[] args = {"-a", "true", "--longOptionA", "true"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("l"));
        assertTrue(cmd.hasOption("longOptionA"));

        assertEquals("true", cmd.getOptionValue("a"));
        assertEquals("true", cmd.getOptionValue("l"));
        assertEquals("true", cmd.getOptionValue("longOptionA"));
    }
}