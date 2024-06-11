package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class GroupImplTest {
    private GroupImpl group;
    private Option option1;
    private Option option2;
    private Argument argument;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        // Create options and arguments
        List<Option> options = new ArrayList<>();
        option1 = new OptionBuilder()
                .withLongOpt("option1")
                .withDescription("description for option1")
                .createOption();

        option2 = new OptionBuilder()
                .withLongOpt("option2")
                .withDescription("description for option2")
                .createOption();

        argument = new ArgumentBuilder()
                .withLongOpt("argument")
                .withDescription("description for argument")
                .createArgument();

        // Create group
        List<Option> groupOptions = new ArrayList<>(options);
        groupOptions.add(argument);
        group = new GroupImpl(groupOptions, "group", "description for group", 1, 1, true);

        // Create command line
        commandLine = new GenericCommandLine();
    }

    @Test
    public void testCanProcess() {
        // Test option1
        commandLine.addOption("option1");
        assertTrue(group.canProcess(commandLine, "option1"));

        // Test option2
        commandLine.addOption("option2");
        assertTrue(group.canProcess(commandLine, "option2"));

        // Test argument
        commandLine.addArgument("argument");
        assertTrue(group.canProcess(commandLine, "argument"));

        // Test non-option
        assertFalse(group.canProcess(commandLine, "nonoption"));

        // Test option with prefix
        option1.setPrefix("p");
        commandLine.addOption("poption1");
        assertTrue(group.canProcess(commandLine, "poption1"));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());

        option1.setPrefix("p");
        prefixes = group.getPrefixes();
        assertTrue(prefixes.contains("p"));
    }

    @Test
    public void testGetTriggers() {
        Set triggers = group.getTriggers();
        assertTrue(triggers.isEmpty());

        triggers = option1.getTriggers();
        assertEquals(1, triggers.size());
        assertTrue(triggers.contains("option1"));

        triggers = option2.getTriggers();
        assertEquals(1, triggers.size());
        assertTrue(triggers.contains("option2"));

        triggers = argument.getTriggers();
        assertTrue(triggers.isEmpty());
    }

    @Test
    public void testProcess() {
        // Test option1
        commandLine.addOption("option1");
        group.process(commandLine, commandLine.getArgs().listIterator());
        assertEquals("option1", commandLine.getOptionValues("option1").get(0));

        // Test option2
        commandLine.addOption("option2");
        group.process(commandLine, commandLine.getArgs().listIterator());
        assertEquals("option2", commandLine.getOptionValues("option2").get(0));

        // Test argument
        commandLine.addArgument("argument");
        group.process(commandLine, commandLine.getArgs().listIterator());
        assertEquals("argument", commandLine.getArgValues().get(0));

        // Test non-option
        assertFalse(group.canProcess(commandLine, "nonoption"));
        assertNull(commandLine.getArgValues());
        assertNull(commandLine.getOptionValues("nonoption"));
    }

    @Test
    public void testValidate() throws OptionException {
        // Test valid command line
        commandLine.addOption("option1");
        commandLine.addOption("option2");
        group.validate(commandLine);

        // Test missing options
        commandLine.clear();
        commandLine.addOption("option1");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Missing option 'option2'", e.getMessage());
        }

        // Test too many options
        commandLine.addOption("option1");
        commandLine.addOption("option1");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Too many options", e.getMessage());
        }
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("group", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("description for group", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        final StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("group (description for group)\n", buffer.toString());

        Set helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);

        buffer.setLength(0);
        group.appendUsage(buffer, helpSettings, null);
        assertEquals("[group (description for group)]\n", buffer.toString());

        helpSettings.clear();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        buffer.setLength(0);
        group.appendUsage(buffer, helpSettings, null);
        assertEquals("group (description for group)\n" +
                "  option1: description for option1\n" +
                "  option2: description for option2\n" +
                "  argument: description for argument\n", buffer.toString());

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        buffer.setLength(0);
        group.appendUsage(buffer, helpSettings, null);
        assertEquals("group (description for group)\n" +
                "  option1: description for option1\n" +
                "  option2: description for option2\n" +
                "  argument: description for argument\n", buffer.toString());

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);

        buffer.setLength(0);
        group.appendUsage(buffer, helpSettings, null);
        assertEquals("group (description for group)\n" +
                "  argument: description for argument\n" +
                "  option1: description for option1\n" +
                "  option2: description for option2\n", buffer.toString());

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        buffer.setLength(0);
        group.appendUsage(buffer, helpSettings, null);
        assertEquals("group (description for group)\n" +
                "  argument: description for argument\n" +
                "  option1: description for option1\n" +
                "    description for option1\n" +
                "  option2: description for option2\n" +
                "    description for option2\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(1, helpLines.size());
        assertEquals("group (description for group)", helpLines.get(0).getHeadText());

        Set helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);

        final List<HelpLine> helpLines2 = group.helpLines(0, helpSettings, null);
        assertEquals(1, helpLines2.size());
        assertEquals("[group (description for group)]", helpLines2.get(0).getHeadText());

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        final List<HelpLine> helpLines3 = group.helpLines(0, helpSettings, null);
        assertEquals(2, helpLines3.size());
        assertEquals("group (description for group)\n" +
                "  option1: description for option1\n" +
                "  option2: description for option2\n", helpLines3.get(0).getHeadText());
        assertEquals("option1: description for option1\n" +
                "option2: description for option2\nargument: description for argument\n", helpLines3.get(1).getHeadText());

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        final List<HelpLine> helpLines4 = group.helpLines(0, helpSettings, null);
        assertEquals(2, helpLines4.size());
        assertEquals("group (description for group)\n" +
                "  argument: description for argument\n" +
                "  option1: description for option1\n" +
                "  option2: description for option2\n", helpLines4.get(0).getHeadText());
        assertEquals("argument: description for argument\noption1: description for option1\noption2: description for option2\n", helpLines4.get(1).getHeadText());
    }

    @Test
    public void testGetOptions() {
        assertEquals(3, group.getOptions().size());
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
        assertTrue(group.getOptions().contains(argument));
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(1, group.getAnonymous().size());
        assertTrue(group.getAnonymous().contains(argument));
    }

    @Test
    public void testFindOption() {
        Option foundOption = group.findOption("option1");
        assertTrue(foundOption == option1);

        foundOption = group.findOption("option2");
        assertTrue(foundOption == option2);

        foundOption = group.findOption("argument");
        assertTrue(foundOption == argument);

        foundOption = group.findOption("nonoption");
        assertNull(foundOption);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);
        assertFalse(commandLine.hasOption("option1"));
        assertFalse(commandLine.hasOption("option2"));
        assertFalse(commandLine.hasOption("argument"));
    }
}

class GenericCommandLine extends WriteableCommandLine {
    public GenericCommandLine() {
        super();
    }

    @Override
    public void parse(String[] args) {
        // do nothing
    }
}