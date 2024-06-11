import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionsTest
{
    @Test
    public void testBasicOptions() throws Exception
    {
        Options options = new Options();
        Option opt = new Option("a", "alpha", false, "an option with a short name");
        options.addOption(opt);
        CommandLineParser parser = new DefaultParser();

        String[] args = {"-a"};
        CommandLine cmd = parser.parse(options.toArray(), args);

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("beta"));
        assertNull(cmd.getOptionValue("a"));
    }

    @Test
    public void testOptionsWithArguments() throws Exception
    {
        Options options = new Options();
        Option opt = new Option("b", "beta", true, "an option with a long name and an argument");
        options.addOption(opt);

        String[] args = {"--beta", "value"};
        CommandLine cmd = parser.parse(options.toArray(), args);

        assertTrue(cmd.hasOption("beta"));
        assertEquals("value", cmd.getOptionValue("beta"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOption() throws Exception
    {
        Options options = new Options();
        Option opt = new Option("x", "x", true, "an invalid option"); // The option 'x' is not defined

        String[] args = {"--x"};
        CommandLineParser parser = new DefaultParser();
        parser.parse(options.toArray(), args);
    }

    @Test
    public void testOptionsWithLongName() throws Exception
    {
        Options options = new Options();
        Option opt = new Option("long-option", "long option", false, "an option with a long name");
        options.addOption(opt);

        String[] args = {"--long-option"};
        CommandLine cmd = parser.parse(options.toArray(), args);

        assertTrue(cmd.hasOption("long-option"));
        assertFalse(cmd.hasOption("short-option"));
        assertNull(cmd.getOptionValue("long-option"));
    }

    @Test
    public void testOptionsWithShortName() throws Exception
    {
        Options options = new Options();
        Option opt = new Option("s", "short-option", false, "an option with a short name");
        options.addOption(opt);

        String[] args = {"-s"};
        CommandLine cmd = parser.parse(options.toArray(), args);

        assertTrue(cmd.hasOption("short-option"));
        assertFalse(cmd.hasOption("long-option"));
        assertNull(cmd.getOptionValue("short-option"));
    }

    @Test
    public void testOptionsWithLongAndShortName() throws Exception
    {
        Options options = new Options();
        Option opt = new Option("s", "short-option", false, "an option with a short name");
        Option longOpt = new Option("long-option", "long option", false, "an option with a long name");
        opt.setLongOpt(longOpt);
        options.addOption(opt);

        String[] args = {"--long-option", "-s"};
        CommandLine cmd = parser.parse(options.toArray(), args);

        assertTrue(cmd.hasOption("long-option"));
        assertTrue(cmd.hasOption("short-option"));
        assertSame(opt, cmd.getOption("long-option"));
        assertSame(opt, cmd.getOption("short-option"));
    }

    @Test
    public void testOptionsWithRequired() throws Exception
    {
        Options options = new Options();
        Option opt = new Option("r", "required", true, "an option that is required");
        options.addOption(opt);

        String[] args = {};
        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options.toArray(), args);
            assertFalse(true);
        } catch (ParseException e) {
            // expected
        }

        args = new String[]{"--required"};
        CommandLine cmd = parser.parse(options.toArray(), args);
        assertTrue(cmd.hasOption("required"));
    }

    @Test
    public void testOptionsWithOptionGroups() throws Exception
    {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group1", "group1");
        group.setRequired(true);
        Option groupOption = new Option("g", "group", true, "an option that is part of a group");
        group.addOption(groupOption);
        options.addOptionGroup(group);

        options.addOption(new Option("a", "alpha", false, "an option not in a group"));

        String[] args = {};
        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options.toArray(), args);
            assertFalse(true);
        } catch (ParseException e) {
            // expected
        }

        args = new String[]{"--g"};
        CommandLine cmd = parser.parse(options.toArray(), args);
        assertTrue(cmd.hasOption("group"));
        assertTrue(cmd.hasOption("alpha"));
    }

    @Test
    public void testParsePartialName() throws Exception
    {
        Options options = new Options();
        Option opt1 = new Option("opt1", "option one", false, "option one");
        Option opt2 = new Option("opt2", "option two", false, "option two");
        options.addOption(opt1);
        options.addOption(opt2);

        String[] args = {"--opt"};
        CommandLine cmd = parser.parse(options.toArray(), args);

        List<String> matchingOptions = options.getMatchingOptions("opt");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("opt1"));
        assertTrue(matchingOptions.contains("opt2"));
    }
}