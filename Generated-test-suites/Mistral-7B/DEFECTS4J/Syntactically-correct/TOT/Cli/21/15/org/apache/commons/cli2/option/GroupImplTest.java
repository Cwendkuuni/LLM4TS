package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.ParseException;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link OptionGroupImpl}.
 */
public class GroupImplTest {

    private OptionGroupImpl group;
    private CommandLine cmd;

    @Before
    public void setUp() {
        final List options = new ArrayList<>();
        options.add(OptionBuilder.withName("option1")
                .withDescription("option1 description")
                .withLongOpt("long-option1")
                .createOption());
        options.add(OptionBuilder.withName("option2")
                .withDescription("option2 description")
                .withLongOpt("long-option2")
                .createOption());

        group = new OptionGroupImpl(options, "MyGroup", "My group");
        cmd = new CommandLine();
    }

    @Test
    public void testCanProcess() {
        // given

        // when
        assertTrue(group.canProcess(cmd, "option1"));
        assertTrue(group.canProcess(cmd, "long-option1"));
        assertTrue(group.canProcess(cmd, "option2"));
        assertTrue(group.canProcess(cmd, "long-option2"));

        // when an invalid trigger is provided
        assertFalse(group.canProcess(cmd, "unknownOption"));
    }

    @Test
    public void testProcess() throws ParseException {
        // given

        // when multiple valid options are provided in the correct order
        group.process(cmd, Arrays.asList("MyGroup", "option1", "value1").iterator());
        group.process(cmd, Arrays.asList("MyGroup", "option2", "value2").iterator());

        // then the options have been set on the command line
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertEquals("value2", cmd.getOptionValue("option2"));

        // when an invalid trigger is provided
        assertThrows(ParseException.class, () -> group.process(cmd, Arrays.asList("MyGroup", "unknownOption").iterator()));

        // when an option is provided multiple times
        group.process(cmd, Arrays.asList("MyGroup", "option1", "value1").iterator());
        group.process(cmd, Arrays.asList("MyGroup", "option1", "value2").iterator());

        // then the last option value overwrites the previous one
        assertEquals("value2", cmd.getOptionValue("option1"));
    }

    @Test
    public void testValidate() throws ParseException {
        // given

        // when multiple valid options are provided
        group.validate(cmd);

        // then no exceptions are thrown
        // and the options have been set on the command line
        assertEquals("option1 description", cmd.getOption("option1").getDescription());
        assertEquals("option2 description", cmd.getOption("option2").getDescription());

        // when an invalid number of options is provided
        cmd.clear();
        group.validate(cmd);

        // then an exception is thrown
        assertThrows(ParseException.class, () -> group.validate(cmd));
    }

    @Test
    public void testGetPrefixes() {
        // given

        // when
        final Set prefixes = group.getPrefixes();

        // then the prefixes are empty since this group does not have any

        assertNotNull(prefixes);
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        // given

        // when
        final Set triggers = group.getTriggers();

        // then the triggers are the names of the options in this group

        assertNotNull(triggers);
        assertEquals(2, triggers.size());
        assertTrue(triggers.containsAll(Arrays.asList("option1", "option2")));
    }

    @Test
    public void testHelp() {
        // given

        // when help is generated for this group with no settings
        final HelpFormatter formatter = new HelpFormatter();
        group.appendUsage(new StringBuilder(), null, null);
        final String help = formatter.format(new StringReader(sb.toString()));

        // then the help text starts with the group name and description
        assertNotNull(help);
        assertTrue(help.startsWith("My group"));
        assertTrue(help.contains("option1 description"));
        assertTrue(help.contains("option2 description"));

        // when help is generated for this group with the group name expanded
        final Set helpSettings = new HashSet<>(Arrays.asList(DisplaySetting.DISPLAY_GROUP_NAME, DisplaySetting.DISPLAY_GROUP_EXPANDED));
        group.appendUsage(new StringBuilder(), helpSettings, null);
        final String expandedHelp = formatter.format(new StringReader(sb.toString()));

        // then the help text starts with the group name and description followed by
        // the options in alphabetical order
        assertTrue(expandedHelp.startsWith("My group"));
        assertTrue(expandedHelp.contains("option1 description"));
        assertTrue(expandedHelp.contains("option2 description"));

        // when help is generated for this group with the group name and all options
        // expanded
        final Set helpSettings2 = new HashSet<>(Arrays.asList(DisplaySetting.DISPLAY_GROUP_NAME, DisplaySetting.DISPLAY_GROUP_EXPANDED, DisplaySetting.DISPLAY_GROUP_ARGUMENT));
        group.appendUsage(new StringBuilder(), helpSettings2, null);
        final String expandedWithArgsHelp = formatter.format(new StringReader(sb.toString()));

        // then the help text starts with the group name and description followed by
        // the options in alphabetical order with their arguments
        assertTrue(expandedWithArgsHelp.startsWith("My group"));
        assertTrue(expandedWithArgsHelp.contains("option1 description"));
        assertTrue(expandedWithArgsHelp.contains("option2 description"));
    }
}