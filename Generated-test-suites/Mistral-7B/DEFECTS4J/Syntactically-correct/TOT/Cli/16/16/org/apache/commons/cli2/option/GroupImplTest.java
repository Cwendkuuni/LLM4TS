package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.ParseContext;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private Group group;
    private ParseContext context;

    @Before
    public void setUp() throws Exception {
        List<Option> options = Arrays.asList(
                new Option("g", "group", true, "G")
                        .setValueSeparator(':')
                        .setArgName("groupName")
                        .setDescription("defines a new group"),
                new Option("a", "anonymous", true, "A")
                        .setArgName("argument")
                        .setDescription("an anonymous argument"),
                new Option("o", "option", true, "O")
                        .setArgName("optionName")
                        .setDescription("an option within the group"),
                new Option("h", "help", false, "H")
                        .setDescription("prints this help message"),
                new Argument("i", "input", true, "I")
                        .setDescription("input file name")
        );

        group = new GroupImpl(options, "Group", "A group of options", 1, 1);
        context = new ParseContext();
    }

    @Test
    public void testCanProcessWithCorrectArgument() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test", "-g", "groupName");
        assertTrue(group.canProcess(cmd, "-g"));
    }

    @Test
    public void testCanProcessWithWrongArgumentTriggersOption() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test", "-o", "optionName");
        assertFalse(group.canProcess(cmd, "-o"));
    }

    @Test
    public void testCanProcessWithWrongArgumentTriggersAnonymousArgument() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test", "-A", "argument");
        assertFalse(group.canProcess(cmd, "-A"));
    }

    @Test
    public void testCanProcessWithWrongArgumentNotTriggersAnything() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test", "-x", "notAGroup");
        assertFalse(group.canProcess(cmd, "-x"));
    }

    @Test
    public void testCanProcessWithCorrectOption() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test", "-g", "groupName", "--anonymous", "argument");
        assertTrue(group.canProcess(cmd, "--anonymous"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanProcessWithMissingArgument() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test", "-g", "groupName");
        group.canProcess(cmd, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanProcessWithMissingOption() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test", "--groupName");
        group.canProcess(cmd, null);
    }

    @Test
    public void testCanProcessWithMissingOptionButTriggersAnonymousArgument() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test", "--groupName", "--anonymous", "argument");
        assertTrue(group.canProcess(cmd, "--anonymous"));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithMissingArgument() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test");
        group.process(cmd, context.getPosixParser().getInputIterator());
    }

    @Test
    public void testProcessWithCorrectArgument() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test", "-g", "groupName", "--anonymous", "argument");
        group.process(cmd, context.getPosixParser().getInputIterator());
        assertTrue(context.getParseResults().getOptionValues("groupName").size() > 0);
        assertTrue(context.getParseResults().getOptionValues("anonymous").size() > 0);
    }

    @Test
    public void testProcessWithCorrectOption() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test", "-g", "groupName", "--option", "optionName");
        group.process(cmd, context.getPosixParser().getInputIterator());
        assertTrue(context.getParseResults().getOptionValues("groupName").size() > 0);
        assertTrue(context.getParseResults().getOptionValues("option").size() > 0);
    }

    @Test(expected = OptionException.class)
    public void testProcessWithMissingOptionButTriggersAnonymousArgument() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test", "--groupName", "--anonymous", "argument");
        group.process(cmd, context.getPosixParser().getInputIterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithMissingOptionButTriggersOption() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test", "--groupName", "--option", "optionName");
        group.process(cmd, context.getPosixParser().getInputIterator());
    }

    @Test
    public void testValidateWithCorrectArguments() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test", "-g", "groupName", "--anonymous", "argument");
        group.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMissingArgument() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test", "-g", "groupName");
        group.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMoreArgumentsThanAllowed() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test", "-g", "groupName", "--anonymous", "argument1", "--anonymous", "argument2");
        group.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMissinOption() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test", "-g", "groupName", "--option", "optionName");
        group.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidOption() throws Exception {
        CommandLine cmd = TestUtils.createCommandLine("test", "-g", "groupName", "--invalidOption", "invalidOptionName");
        group.validate(cmd);
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("Group", group.getPreferredName());
    }

    @Test
    public void testGetDescription() throws Exception {
        assertEquals("A group of options", group.getDescription());
    }

    @Test
    public void testGetTriggers() throws Exception {
        Set triggers = group.getTriggers();
        assertTrue(triggers.contains("g"));
        assertTrue(triggers.contains("group"));
        assertFalse(triggers.contains("o"));
        assertFalse(triggers.contains("option"));
    }

    @Test
    public void testGetPrefixes() throws Exception {
        Set prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetOptions() throws Exception {
        List options = group.getOptions();
        assertEquals(4, options.size());
        assertTrue(options.contains(group.findOption("g")));
        assertTrue(options.contains(group.findOption("a")));
        assertTrue(options.contains(group.findOption("o")));
        assertTrue(options.contains(group.findOption("h")));
    }

    @Test
    public void testGetAnonymous() throws Exception {
        List anonymous = group.getAnonymous();
        assertEquals(1, anonymous.size());
        assertTrue(anonymous.contains(group.findOption("i")));
    }

    @Test
    public void testFindOptionWithCorrectTrigger() throws Exception {
        Option option = group.findOption("g");
        assertNotNull(option);
    }

    @Test
    public void testFindOptionWithWrongTrigger() throws Exception {
        Option option = group.findOption("o");
        assertNull(option);
    }

    @Test
    public void testGetMinimum() throws Exception {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() throws Exception {
        assertEquals(1, group.getMaximum());
    }

    @Test
    public void testIsRequired() throws Exception {
        assertTrue(group.isRequired());
    }

    @Test
    public void testHelpUsage() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("test", group, true);
        assertTrue(help.contains("defines a new group"));
        assertTrue(help.contains("--anonymous [ARGUMENT]"));
        assertTrue(help.contains("--groupName [GROUPNAME]"));
        assertFalse(help.contains("--help"));
        assertFalse(help.contains("--option [OPTIONNAME]"));
        assertFalse(help.contains("--input [INPUT]"));
    }

    @Test
    public void testHelpLines() throws Exception {
        List lines = group.helpLines(1, Collections.emptySet(), null);
        assertTrue(lines.size() > 1);
        assertTrue(lines.get(0).toString().contains("defines a new group"));
        assertTrue(lines.get(1).toString().contains("--anonymous [ARGUMENT]"));
    }
}