package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

public class GroupImplTest {

    @org.junit.Test
    public void testGroupImplCreation() {
        List options = new ArrayList();

        Option option1 = new Option("o1", "option1", false, "Option1 description");
        Argument arg1 = new Argument("a1", "argument1", true, "Argument1 description");
        Argument arg2 = new Argument("a2", "argument2", true, "Argument2 description");
        Option option2 = new Option("o2", "option2", true, "Option2 description")
                .setArgName("arg2")
                .setValueSeparator(' ')
                .setValueParser(new StringParser());

        options.add(option1);
        options.add(arg1);
        options.add(arg2);
        options.add(option2);

        Group group = new GroupImpl(options, "Group1", "Group1 description", 1, 1);

        assertNotNull(group);
        assertEquals("Group1", group.getPreferredName());
        assertEquals("Group1 description", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertTrue(group instanceof Group);
        assertFalse(group.isRequired());
        assertNull(group.getName());
        assertEquals(options, group.getOptions());
        assertEquals(Collections.singletonList(arg1), group.getAnonymous());
        assertEquals(Collections.singleton("o1"), group.getTriggers());
        assertEquals(Collections.singletonMap("o1", option1), group.getOptionMap());
        assertEquals(Collections.singleton("a1"), group.getPrefixes());

        assertTrue(group.canProcess(new WriteableCommandLine(), "o1"));
        assertTrue(group.canProcess(new WriteableCommandLine(), "a1"));
        assertFalse(group.canProcess(new WriteableCommandLine(), "x"));
        assertFalse(group.canProcess(new WriteableCommandLine(), null));
    }

    @org.junit.Test
    public void testGroupImplProcess() {
        List options = new ArrayList();

        Option option1 = new Option("o1", "option1", false, "Option1 description");
        Argument arg1 = new Argument("a1", "argument1", true, "Argument1 description");
        Argument arg2 = new Argument("a2", "argument2", true, "Argument2 description");
        Option option2 = new Option("o2", "option2", true, "Option2 description")
                .setArgName("arg2")
                .setValueSeparator(' ')
                .setValueParser(new StringParser());

        options.add(option1);
        options.add(arg1);
        options.add(arg2);
        options.add(option2);

        Group group = new GroupImpl(options, "Group1", "Group1 description", 1, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.addOption(option1);
        commandLine.addOption(option2, "arg1 arg2");
        commandLine.addArgument(arg1, "arg1Value");
        commandLine.addArgument(arg2, "arg2Value");

        group.process(commandLine, commandLine.iterator());

        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
        assertTrue(commandLine.hasArgument(arg1));
        assertTrue(commandLine.hasArgument(arg2));
    }

    @org.junit.Test
    public void testGroupImplValidate() {
        List options = new ArrayList();

        Option option1 = new Option("o1", "option1", false, "Option1 description");
        Argument arg1 = new Argument("a1", "argument1", true, "Argument1 description");
        Argument arg2 = new Argument("a2", "argument2", true, "Argument2 description");
        Option option2 = new Option("o2", "option2", true, "Option2 description")
                .setArgName("arg2")
                .setValueSeparator(' ')
                .setValueParser(new StringParser());

        options.add(option1);
        options.add(arg1);
        options.add(arg2);
        options.add(option2);

        Group group = new GroupImpl(options, "Group1", "Group1 description", 1, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.addOption(option1);
        commandLine.addOption(option1);

        try {
            group.validate(commandLine);
            assertFalse(true);
        } catch (OptionException e) {
            assertEquals("Unexpected option", OptionException.class, e.getClass());
            assertEquals("Unexpected option 'o1'", e.getMessage());
        }

        commandLine.removeOption(option1);

        try {
            group.validate(commandLine);
            assertTrue(true);
        } catch (OptionException e) {
            assertFalse(true);
        }

        commandLine.addArgument(arg1, "arg1Value");

        try {
            group.validate(commandLine);
            assertFalse(true);
        } catch (OptionException e) {
            assertEquals("Argument 'arg1Value' for option 'arg1' is missing",
                    e.getMessage());
        }

        commandLine.addArgument(arg1, "arg1Value");

        try {
            group.validate(commandLine);
            assertTrue(true);
        } catch (OptionException e) {
            assertFalse(true);
        }

        commandLine.addOption(option2, "arg1 arg2");

        try {
            group.validate(commandLine);
            assertTrue(true);
        } catch (OptionException e) {
            assertFalse(true);
        }

        commandLine.removeOption(option2);

        try {
            group.validate(commandLine);
            assertTrue(true);
        } catch (OptionException e) {
            assertFalse(true);
        }
    }

    @org.junit.Test
    public void testGroupImplUsage() {
        List options = new ArrayList();

        Option option1 = new Option("o1", "option1", false, "Option1 description");
        Argument arg1 = new Argument("a1", "argument1", true, "Argument1 description");
        Argument arg2 = new Argument("a2", "argument2", true, "Argument2 description");
        Option option2 = new Option("o2", "option2", true, "Option2 description")
                .setArgName("arg2")
                .setValueSeparator(' ')
                .setValueParser(new StringParser());

        options.add(option1);
        options.add(arg1);
        options.add(arg2);
        options.add(option2);

        Group group = new GroupImpl(options, "Group1", "Group1 description", 1, 1);

        StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, null, null);

        String result = buffer.toString();

        String expected = "Group1 description:\n" +
                "  Group1:\n" +
                "    option1 OPTION, --option1\n" +
                "      Option1 description\n" +
                "    option2 ARG_NAME [ARGUMENT]...\n" +
                "      Option2 description\n" +
                "        ARGUMENT ARGUMENT...\n";

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testGroupImplHelpLines() {
        List options = new ArrayList();

        Option option1 = new Option("o1", "option1", false, "Option1 description");
        Argument arg1 = new Argument("a1", "argument1", true, "Argument1 description");
        Argument arg2 = new Argument("a2", "argument2", true, "Argument2 description");
        Option option2 = new Option("o2", "option2", true, "Option2 description")
                .setArgName("arg2")
                .setValueSeparator(' ')
                .setValueParser(new StringParser());

        options.add(option1);
        options.add(arg1);
        options.add(arg2);
        options.add(option2);

        Group group = new GroupImpl(options, "Group1", "Group1 description", 1, 1);

        List helpLines = group.helpLines(0, null, null);

        String expected = "Group1 (Group1 description):\n" +
                "  option1 OPTION, --option1\n" +
                "    Option1 description\n" +
                "  option2 ARG_NAME [ARGUMENT]...\n" +
                "    Option2 description\n" +
                "      ARGUMENT ARGUMENT...\n";

        String result = HelpLine.formatHelpLines(helpLines);

        assertEquals(expected, result);
    }
}