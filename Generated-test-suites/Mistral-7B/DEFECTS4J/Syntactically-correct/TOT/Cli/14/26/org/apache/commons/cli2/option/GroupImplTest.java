package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.exceptions.OptionException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();

        options.addGroup(new GroupImpl(
            asList(
                new Option("g", "group", true, "group description", 1),
                new Option("a", "anonymous", true, "anonymous description"),
                new Option("o", "option", true, "option description")
            ),
            "group name"
        ));

        options.addGroup(new GroupImpl(
            asList(
                new Option("e", "empty", true, "empty group")
            ),
            null
        ));

        options.addOption("no-group", true, "no group option");
    }

    @Test
    public void testGroupWithOneOption() {
        final CommandLine cmd = new CommandLine();
        cmd.parse(options, new String[]{"-g"});

        assertEquals("group description", cmd.getValue("group"));
        assertNull(cmd.getValue("anonymous"));
        assertNull(cmd.getValue("option"));
    }

    @Test(expected = OptionException.class)
    public void testGroupWithLessOptionsThanMinimum() {
        final CommandLine cmd = new CommandLine();
        cmd.parse(options, new String[]{});
    }

    @Test(expected = OptionException.class)
    public void testGroupWithMoreOptionsThanMaximum() {
        final CommandLine cmd = new CommandLine();
        cmd.parse(options, new String[]{"-g", "-g"});
    }

    @Test
    public void testGroupWithAnonymousArgument() {
        final CommandLine cmd = new CommandLine();
        cmd.parse(options, new String[]{"-g", "value"});

        assertEquals("value", cmd.getValue("anonymous"));
        assertNull(cmd.getValue("group"));
        assertNull(cmd.getValue("option"));
    }

    @Test
    public void testGroupWithEmptyGroup() {
        final CommandLine cmd = new CommandLine();
        cmd.parse(options, new String[]{"--empty"});

        assertNull(cmd.getValue("group"));
        assertNull(cmd.getValue("anonymous"));
        assertNull(cmd.getValue("option"));
    }

    @Test
    public void testNestedGroups() {
        final CommandLine cmd = new CommandLine();
        cmd.parse(options, new String[]{"-g", "-a", "value1", "-o", "value2"});

        assertEquals("group name", cmd.getValue("group"));
        assertEquals("value1", cmd.getValue("anonymous"));
        assertEquals("value2", cmd.getValue("option"));
    }

    @Test
    public void testHelp() {
        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("TestGroupImpl", options, true);
    }

    private List<Object> asList(final Object... objects) {
        return Collections.unmodifiableList(Arrays.asList(objects));
    }
}