package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
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

import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testGroupImplCreation() {
        List options = new ArrayList();
        options.add(createOption("option1", "description1", false));
        options.add(createOption("option2", "description2", true, 1, 1));
        options.add(createOption("option3", "description3", false, 0, 1));
        options.add(createOption("option4", "description4", false, 1, 1));
        options.add(createArgument("arg", "description", false));

        Group group = new GroupImpl(options, "Group1", "Description for Group1", 0, 1);

        assertNotNull(group);
        assertSame(options, group.getOptions());
        assertSame("Group1", group.getPreferredName());
        assertSame("Description for Group1", group.getDescription());
        assertEquals(0, group.getMinimum());
        assertEquals(1, group.getMaximum());
    }

    @Test
    public void testCanProcessWithValidOptions() {
        List options = new ArrayList();
        options.add(createOption("option1", "description1", false));
        options.add(createOption("option2", "description2", true, 1, 1));
        options.add(createOption("option3", "description3", false, 0, 1));
        options.add(createOption("option4", "description4", false, 1, 1));
        options.add(createArgument("arg", "description", false));

        Group group = new GroupImpl(options, "Group1", "Description for Group1", 0, 1);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        // Test with valid options
        String[] arguments = {"-g", "option1", "--option2"};
        assertFalse(group.canProcess(commandLine, null));
        assertFalse(group.canProcess(commandLine, Arrays.asList(arguments)));

        arguments = new String[]{"-g", "option2"};
        assertTrue(group.canProcess(commandLine, null));
        assertTrue(group.canProcess(commandLine, Arrays.asList(arguments)));

        arguments = new String[]{"-g", "option1", "--option2", "--option3"};
        assertTrue(group.canProcess(commandLine, null));
        assertTrue(group.canProcess(commandLine, Arrays.asList(arguments)));

        arguments = new String[]{"-g", "option1", "--option2", "--option3", "arg"};
        assertFalse(group.canProcess(commandLine, null));
        assertFalse(group.canProcess(commandLine, Arrays.asList(arguments)));
    }

    @Test
    public void testCanProcessWithInvalidOptions() {
        List options = new ArrayList();
        options.add(createOption("option1", "description1", false));
        options.add(createOption("option2", "description2", true, 1, 1));
        options.add(createOption("option3", "description3", false, 0, 1));
        options.add(createOption("option4", "description4", false, 1, 1));
        options.add(createArgument("arg", "description", false));

        Group group = new GroupImpl(options, "Group1", "Description for Group1", 0, 1);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        // Test with invalid options
        String[] arguments = {"-g"};
        assertFalse(group.canProcess(commandLine, null));
        assertFalse(group.canProcess(commandLine, Arrays.asList(arguments)));

        arguments = new String[]{"-g", "invalidOption"};
        assertFalse(group.canProcess(commandLine, null));
        assertFalse(group.canProcess(commandLine, Arrays.asList(arguments)));

        arguments = new String[]{"-g", "option1", "--option2", "--option3", "invalidOption"};
        assertFalse(group.canProcess(commandLine, null));
        assertFalse(group.canProcess(commandLine, Arrays.asList(arguments)));

        arguments = new String[]{"-g", "option1", "--option2", "--option3", "arg", "invalidOption"};
        assertFalse(group.canProcess(commandLine, null));
        assertFalse(group.canProcess(commandLine, Arrays.asList(arguments)));
    }

    @Test
    public void testCanProcessWithAnonymousArguments() {
        List options = new ArrayList();
        options.add(createOption("option1", "description1", false));
        options.add(createArgument("arg1", "description1 for arg1", false));
        options.add(createArgument("arg2", "description1 for arg2", false));
        options.add(createOption("option2", "description2", false, 0, 1));

        Group group = new GroupImpl(options, "Group1", "Description for Group1", 0, 1);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        // Test with valid options including anonymous arguments
        String[] arguments = {"-g", "option1", "arg1", "arg2"};
        assertTrue(group.canProcess(commandLine, null));
        assertTrue(group.canProcess(commandLine, Arrays.asList(arguments)));

        // Test with only anonymous arguments
        arguments = new String[]{"-g", "arg1", "arg2"};
        assertFalse(group.canProcess(commandLine, null));
        assertFalse(group.canProcess(commandLine, Arrays.asList(arguments)));
    }

    @Test
    public void testProcess() throws OptionException {
        List options = new ArrayList();
        options.add(createOption("option1", "description1", false));
        options.add(createOption("option2", "description2", true, 1, 1));
        options.add(createOption("option3", "description3", false, 0, 1));
        options.add(createOption("option4", "description4", false, 1, 1));
        options.add(createArgument("arg", "description", false));

        Group group = new GroupImpl(options, "Group1", "Description for Group1", 0, 1);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        // Test with valid options
        String[] arguments = {"-g", "option2"};
        group.process(commandLine, Arrays.asList(arguments).listIterator());

        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertFalse(commandLine.hasOption("option3"));
        assertFalse(commandLine.hasOption("option4"));
        assertNull(commandLine.getOptionValue("arg"));

        // Test with valid options and anonymous arguments
        arguments = new String[]{"-g", "option1", "--option2", "arg1", "arg2"};
        group.process(commandLine, Arrays.asList(arguments).listIterator());

        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertFalse(commandLine.hasOption("option3"));
        assertFalse(commandLine.hasOption("option4"));
        assertSame("arg1", commandLine.getOptionValue("arg"));
        assertSame("arg2", commandLine.getOptionValue("arg2"));
    }

    @Test
    public void testValidate() throws OptionException {
        List options = new ArrayList();
        options.add(createOption("option1", "description1", false));
        options.add(createOption("option2", "description2", true, 1, 1));
        options.add(createOption("option3", "description3", false, 0, 1));
        options.add(createOption("option4", "description4", false, 1, 1));
        options.add(createArgument("arg", "description", false));

        Group group = new GroupImpl(options, "Group1", "Description for Group1", 0, 1);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        // Test with valid options
        String[] arguments = {"-g", "option1", "--option2"};
        group.process(commandLine, Arrays.asList(arguments).listIterator());
        group.validate(commandLine);

        // Test with insufficient options
        arguments = new String[]{"-g"};
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test with excess options
        arguments = new String[]{"-g", "option1", "--option2", "--option3"};
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test with invalid options
        arguments = new String[]{"-g", "invalidOption"};
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test with invalid options and anonymous arguments
        arguments = new String[]{"-g", "option1", "--option2", "--invalidOption", "arg1", "arg2"};
        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testUsage() {
        List options = new ArrayList();
        options.add(createOption("option1", "description1", false));
        options.add(createOption("option2", "description2", true, 1, 1));
        options.add(createOption("option3", "description3", false, 0, 1));
        options.add(createOption("option4", "description4", false, 1, 1));
        options.add(createArgument("arg", "description", false));

        Group group = new GroupImpl(options, "Group1", "Description for Group1", 0, 1);

        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);

        String expected = "usage: Group1 [-g <option1>...]\n" +
                "         [--option2 <arg1>]\n" +
                "         [--option2 <arg2>]\n" +
                "         [--option3]\n" +
                "         [--option4]\n" +
                "         [--arg]\n";

        assertEquals(expected, buffer.toString());

        List helpLines = group.helpLines(0, Collections.emptySet(), null);
        String output = "";
        for (Object line : helpLines) {
            output += (String) line + "\n";
        }

        assertEquals(expected, output);
    }

    private Option createOption(final String name,
                               final String description,
                               final boolean required,
                               final int min,
                               final int max) {
        return new OptionBuilder()
                .withLongOpt(name)
                .withDescription(description)
                .isRequired(required)
                .withMinValues(min)
                .withMaxValues(max)
                .create();
    }

    private Argument createArgument(final String name,
                                   final String description,
                                   final boolean required) {
        return new ArgumentBuilder()
                .withLongOpt(name)
                .withDescription(description)
                .isRequired(required)
                .create();
    }
}