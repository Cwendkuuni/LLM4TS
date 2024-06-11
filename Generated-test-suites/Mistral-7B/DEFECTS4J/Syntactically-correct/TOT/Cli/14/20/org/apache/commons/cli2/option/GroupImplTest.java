package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parse.DefaultParser;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    @Test
    public void testGroupWithOptions() throws Exception {
        final String[] args = {"test", "--group"};

        final Options options = new Options();
        options.addGroup(new OptionBuilder("g").longOpt("group")
                .hasArg(true)
                .isRequired()
                .build());
        options.addOption(new OptionBuilder("t").longOpt("test")
                .hasArg(false)
                .build());

        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("test"));
        assertTrue(commandLine.hasOption("group"));
        assertFalse(commandLine.hasOption("help"));

        final Set<String> groupArgValues = commandLine.getOptionValues("group");
        assertEquals(1, groupArgValues.size());
        assertEquals("test", groupArgValues.iterator().next());
    }

    @Test
    public void testGroupWithMissingOption() throws Exception {
        final String[] args = {"test"};

        final Options options = new Options();
        options.addGroup(new OptionBuilder("g").longOpt("group")
                .hasArg(true)
                .isRequired()
                .build());
        options.addOption(new OptionBuilder("t").longOpt("test")
                .hasArg(false)
                .build());

        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, args);

        assertFalse(commandLine.hasOption("group"));
        assertNull(commandLine.getOptionValues("group"));
        assertFalse(commandLine.hasOption("help"));

        assertEquals(0, commandLine.getOptionValues("test").size());
    }

    @Test
    public void testGroupWithExtraOption() throws Exception {
        final String[] args = {"test", "--group", "--extra"};

        final Options options = new Options();
        options.addGroup(new OptionBuilder("g").longOpt("group")
                .hasArg(true)
                .isRequired()
                .build());
        options.addOption(new OptionBuilder("t").longOpt("test")
                .hasArg(false)
                .build());

        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("test"));
        assertTrue(commandLine.hasOption("group"));
        assertTrue(commandLine.hasOption("extra"));

        final Set<String> groupArgValues = commandLine.getOptionValues("group");
        assertEquals(1, groupArgValues.size());
        assertEquals("test", groupArgValues.iterator().next());
    }

    @Test
    public void testGroupWithMinimum() throws Exception {
        final String[] args = {"test", "--group"};

        final Options options = new Options();
        options.addGroup(new OptionBuilder("g").longOpt("group")
                .hasArg(true)
                .isRequired()
                .build());
        options.addOption(new OptionBuilder("t").longOpt("test")
                .hasArg(false)
                .build());

        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("test"));
        assertTrue(commandLine.hasOption("group"));
        assertFalse(commandLine.hasOption("help"));

        final Set<String> groupArgValues = commandLine.getOptionValues("group");
        assertEquals(1, groupArgValues.size());
        assertEquals("test", groupArgValues.iterator().next());

        // test minimum
        try {
            final String[] argsMin = {"test"};
            final CommandLine commandLineMin = parser.parse(options, argsMin);
            fail();
        } catch (final OptionException e) {
            assertEquals("Missing option '--group'", e.getMessage());
        }
    }

    @Test
    public void testGroupWithMaximum() throws Exception {
        final String[] args = {"test", "--group", "--group"};

        final Options options = new Options();
        options.addGroup(new OptionBuilder("g").longOpt("group")
                .hasArg(true)
                .isRequired()
                .build());
        options.addOption(new OptionBuilder("t").longOpt("test")
                .hasArg(false)
                .build());

        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("test"));
        assertTrue(commandLine.hasOption("group"));
        assertTrue(commandLine.hasOption("group"));

        final Set<String> groupArgValues = commandLine.getOptionValues("group");
        assertEquals(2, groupArgValues.size());
        assertEquals("test", groupArgValues.iterator().next());
        assertEquals("test", groupArgValues.iterator().next());

        // test maximum
        try {
            final String[] argsMax = {"test", "--group", "--group", "--group"};
            final CommandLine commandLineMax = parser.parse(options, argsMax);
            fail();
        } catch (final OptionException e) {
            assertEquals("Too many occurrences of option '--group'", e.getMessage());
        }
    }

    @Test
    public void testGroupWithMixedOptions() throws Exception {
        final String[] args = {"test", "--test", "--group", "test2"};

        final Options options = new Options();
        options.addGroup(new OptionBuilder("g").longOpt("group")
                .hasArg(true)
                .isRequired()
                .build());
        options.addOption(new OptionBuilder("t").longOpt("test")
                .hasArg(false)
                .build());

        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("test"));
        assertTrue(commandLine.hasOption("group"));
        assertFalse(commandLine.hasOption("help"));

        final Set<String> groupArgValues = commandLine.getOptionValues("group");
        assertEquals(1, groupArgValues.size());
        assertEquals("test", groupArgValues.iterator().next());

        // test anonymous
        final List<Option> optionsGroup = commandLine.getOptions();
        final Option testOption = optionsGroup.get(1);
        assertTrue(testOption.isAnonymous());

        final List<Option> optionsAnonymous = testOption.getAnonymous();
        assertEquals(1, optionsAnonymous.size());
        assertEquals("test2", optionsAnonymous.get(0).getLongOpt());
        assertFalse(optionsAnonymous.get(0).isAnonymous());
    }

    @Test
    public void testGroupWithNestedOptions() throws Exception {
        final String[] args = {"test", "--group"};

        final Options options = new Options();
        options.addGroup(new OptionBuilder("g").longOpt("group")
                .hasArg(false)
                .build());
        options.addOption(new OptionBuilder("t").longOpt("test")
                .hasArg(false)
                .build());
        options.addOption(new OptionBuilder("n").longOpt("nested")
                .hasArg(false)
                .build());
        options.addGroup(new OptionBuilder("g").longOpt("group2")
                .hasArg(false)
                .build());

        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("test"));
        assertTrue(commandLine.hasOption("group"));
        assertFalse(commandLine.hasOption("help"));

        // test nested group
        final List<Option> optionsGroup = commandLine.getOptions();
        final Option groupOption = optionsGroup.get(0);
        assertTrue(groupOption.isGroup());

        final List<Option> optionsGroup2 = groupOption.getOptions();
        assertTrue(optionsGroup2.isEmpty());

        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Test", options);
    }

    @Test
    public void testGroupWithPrefixArg() throws Exception {
        final String[] args = {"test", "--group", "arg"};

        final Options options = new Options();
        options.addGroup(new OptionBuilder("g")
                .longOpt("group")
                .hasArg(true)
                .isRequired()
                .build());
        options.addOption(new OptionBuilder("t").longOpt("test")
                .hasArg(false)
                .build());

        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("test"));
        assertTrue(commandLine.hasOption("group"));
        assertFalse(commandLine.hasOption("help"));

        final Set<String> groupArgValues = commandLine.getOptionValues("group");
        assertEquals(1, groupArgValues.size());
        assertEquals("arg", groupArgValues.iterator().next());
    }

    @Test
    public void testGroupWithPrefixOption() throws Exception {
        final String[] args = {"test", "--group=arg"};

        final Options options = new Options();
        options.addGroup(new OptionBuilder("g").longOpt("group")
                .hasArg(true)
                .isRequired()
                .build());
        options.addOption(new OptionBuilder("t").longOpt("test")
                .hasArg(false)
                .build());

        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("test"));
        assertTrue(commandLine.hasOption("group"));
        assertFalse(commandLine.hasOption("help"));

        final Set<String> groupArgValues = commandLine.getOptionValues("group");
        assertEquals(1, groupArgValues.size());
        assertEquals("arg", groupArgValues.iterator().next());
    }

    @Test
    public void testGroupWithPrefixOptionLong() throws Exception {
        final String[] args = {"test", "--group=arg"};

        final Options options = new Options();
        options.addGroup(new OptionBuilder("g")
                .longOpt("group")
                .hasArg(true)
                .isRequired()
                .build());
        options.addOption(new OptionBuilder("t").longOpt("test")
                .hasArg(false)
                .build());

        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("test"));
        assertTrue(commandLine.hasOption("group"));
        assertFalse(commandLine.hasOption("help"));

        final Set<String> groupArgValues = commandLine.getOptionValues("group");
        assertEquals(1, groupArgValues.size());
        assertEquals("arg", groupArgValues.iterator().next());
    }

    @Test
    public void testGroupWithPrefixAndArg() throws Exception {
        final String[] args = {"test", "--group=arg", "arg2"};

        final Options options = new Options();
        options.addGroup(new OptionBuilder("g").longOpt("group")
                .hasArg(true)
                .isRequired()
                .build());
        options.addOption(new OptionBuilder("t").longOpt("test")
                .hasArg(false)
                .build());

        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("test"));
        assertTrue(commandLine.hasOption("group"));
        assertFalse(commandLine.hasOption("help"));

        final Set<String> groupArgValues = commandLine.getOptionValues("group");
        assertEquals(1, groupArgValues.size());
        assertEquals("arg", groupArgValues.iterator().next());

        final List<Option> optionsArg = commandLine.getOptions();
        final Option argOption = optionsArg.get(optionsArg.size() - 1);
        assertTrue(argOption.isAnonymous());

        final List<Option> optionsAnonymous = argOption.getAnonymous();
        assertEquals(1, optionsAnonymous.size());
        assertEquals("arg2", optionsAnonymous.get(0).getLongOpt());
        assertFalse(optionsAnonymous.get(0).isAnonymous());
    }
}