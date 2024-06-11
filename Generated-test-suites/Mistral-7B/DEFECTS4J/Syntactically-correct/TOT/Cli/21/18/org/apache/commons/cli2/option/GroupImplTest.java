package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.DefaultParser;
import org.apache.commons.cli2.builder.lit.LongOption;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test case for {@link GroupImpl} class
 */
public class GroupImplTest {
    private Options options;

    @Before
    public void setUp() {
        final List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(new LongOption("g", "group", true, "test group")
                .required(false)
                .addOption("gt", "gt", true, "greater than")
                .addArgument("arg1")
                .addArgumentName("arg1")
                .addArgName("arg1")
                .addArgName("a")
                .setValueSeparator('=')
                .setOptionClass(Group.class)
                .setDefaultValue("default value"));

        groupOptions.add(new LongOption("l", "list", true, "list of numbers")
                .required(true)
                .addArgumentList(true)
                .addArgumentName("numbers")
                .addArgName("num")
                .setValueSeparator(',')
                .setOptionClass(Group.class));

        options = new Options();
        options.addOptions(groupOptions);
    }

    /**
     * Test parsing a valid group with a required option and an optional argument
     */
    @Test
    public void testValidGroupWithOptionalArgument() {
        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, new String[]{"g", "group=test", "arg1=1", "l", "num=1,2"});

        final Group group = (Group) commandLine.getOptionValue("group");
        assertNotNull(group);
        assertEquals("test", group.getValue());
        assertTrue(group.isSet());
        assertEquals(1, group.getAnonymousArguments().size());
        assertEquals("1", group.getAnonymousArguments().get(0).getValue());
        assertEquals(2, group.getListArguments().size());
        assertEquals(1, group.getListArguments().get(0).getValueCount());
        assertEquals((Integer) 1, group.getListArguments().get(0).getValues().get(0));
        assertEquals(2, group.getListArguments().get(1).getValueCount());
        assertEquals(2, group.getListArguments().get(1).getValues().size());
        assertEquals((Integer) 2, group.getListArguments().get(1).getValues().get(0));
        assertEquals((Integer) 1, group.getListArguments().get(1).getValues().get(1));
    }

    /**
     * Test parsing a valid group with an optional option and an optional argument
     */
    @Test
    public void testValidGroupWithOptionalOptionAndArgument() {
        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, new String[]{"g", "group=test", "gt=5"});

        final Group group = (Group) commandLine.getOptionValue("group");
        assertNotNull(group);
        assertEquals("test", group.getValue());
        assertTrue(group.isSet());
        assertFalse(group.isRequired());
        assertNull(group.getAnonymousArguments().get(0).getValue());
        assertNull(group.getListArguments().get(0).getValues());
    }

    /**
     * Test parsing a valid group with a required option and a required argument
     */
    @Test(expected = OptionException.class)
    public void testValidGroupWithRequiredOptionAndArgument() {
        final DefaultParser parser = new DefaultParser();
        parser.parse(options, new String[]{"g", "group=test"});
    }

    /**
     * Test parsing a valid group with a required option and no argument
     */
    @Test(expected = OptionException.class)
    public void testValidGroupWithRequiredOptionAndNoArgument() {
        final DefaultParser parser = new DefaultParser();
        parser.parse(options, new String[]{"g", "group=test"});
    }

    /**
     * Test parsing a valid group with a required option and an empty argument list
     */
    @Test
    public void testValidGroupWithRequiredOptionAndEmptyArgumentList() {
        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, new String[]{"g", "group=test", "l"});

        final Group group = (Group) commandLine.getOptionValue("group");
        assertNotNull(group);
        assertEquals("test", group.getValue());
        assertTrue(group.isSet());
        assertTrue(group.isRequired());
        assertTrue(group.getListArguments().isEmpty());
    }

    /**
     * Test parsing a valid group with a required option and a non-empty argument list
     */
    @Test(expected = OptionException.class)
    public void testValidGroupWithRequiredOptionAndNonEmptyArgumentList() {
        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, new String[]{"g", "group=test", "l", "num=1,2"});

        final Group group = (Group) commandLine.getOptionValue("group");
        assertNotNull(group);
        assertEquals("test", group.getValue());
        assertTrue(group.isSet());
        assertTrue(group.isRequired());
        assertFalse(group.getAnonymousArguments().isEmpty());
        assertFalse(group.getListArguments().isEmpty());
    }

    /**
     * Test printing help for a valid group with an optional option and a required argument
     */
    @Test
    public void testPrintHelpForValidGroupWithOptionalOptionAndRequiredArgument() {
        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, new String[0]);
        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("test", commandLine, true);

        final String help = formatter.getHelpMessage();
        assertTrue(help.contains("--group"));
        assertTrue(help.contains("--gt"));
        assertTrue(help.contains("test (required)"));
        assertTrue(help.contains("arg1"));
        assertTrue(help.contains("--list"));
        assertTrue(help.contains("numbers"));
        assertTrue(help.contains("num="));
        assertTrue(help.contains("(comma-separated)"));
    }

    /**
     * Test printing help for a valid group with an optional option and an optional argument
     */
    @Test
    public void testPrintHelpForValidGroupWithOptionalOptionAndOptionalArgument() {
        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, new String[0]);
        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("test", commandLine, true);

        final String help = formatter.getHelpMessage();
        assertTrue(help.contains("--group"));
        assertTrue(help.contains("test"));
        assertFalse(help.contains("required"));
        assertTrue(help.contains("--gt"));
        assertFalse(help.contains("arg1"));
        assertTrue(help.contains("numbers"));
        assertTrue(help.contains("num="));
        assertTrue(help.contains("(comma-separated)"));
    }

    /**
     * Test printing help for a valid group with a required option and a required argument
     */
    @Test
    public void testPrintHelpForValidGroupWithRequiredOptionAndRequiredArgument() {
        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, new String[]{"--help"});
        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("test", commandLine, true);

        final String help = formatter.getHelpMessage();
        assertTrue(help.contains("--group"));
        assertTrue(help.contains("test"));
        assertTrue(help.contains("required"));
        assertTrue(help.contains("arg1"));
        assertTrue(help.contains("--list"));
        assertTrue(help.contains("numbers"));
        assertTrue(help.contains("num="));
        assertTrue(help.contains("(comma-separated)"));
    }

    /**
     * Test printing help for a valid group with a required option and no argument
     */
    @Test
    public void testPrintHelpForValidGroupWithRequiredOptionAndNoArgument() {
        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, new String[]{"--help"});
        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("test", commandLine, true);

        final String help = formatter.getHelpMessage();
        assertTrue(help.contains("--group"));
        assertTrue(help.contains("test"));
        assertTrue(help.contains("required"));
        assertFalse(help.contains("arg1"));
        assertTrue(help.contains("--list"));
        assertTrue(help.contains("--list="));
        assertTrue(help.contains("numbers"));
        assertTrue(help.contains("num="));
        assertTrue(help.contains("(comma-separated)"));
    }
}