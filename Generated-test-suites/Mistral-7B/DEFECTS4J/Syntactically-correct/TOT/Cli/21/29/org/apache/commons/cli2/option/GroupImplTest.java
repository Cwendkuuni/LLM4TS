package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;

import org.junit.Test;

/**
 * Test class for the GroupImpl class.
 */
public class GroupImplTest {

    /**
     * Test the constructor.
     */
    @Test
    public void testConstructor() {
        // Test with no options
        List options = Collections.emptyList();
        final Group group = new GroupImpl(options, null, null, 0, 1, false);
        assertEquals(0, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertNull(group.getName());
        assertNull(group.getDescription());
        assertTrue(group.isRequired());
        assertFalse(group.hasOptions());
        assertTrue(group.canProcess(null, null));
        assertNull(group.getPrefixes());
        assertTrue(group.getTriggers().isEmpty());

        // Test with some options
        final Option option1 = new Option("o1", "Option 1");
        final Option option2 = new Option("o2", "Option 2");
        final Option option3 = new Option("o3", "Option 3");

        final List options2 = new ArrayList();
        options2.add(option1);
        options2.add(option2);
        options2.add(option3);

        final Group group2 = new GroupImpl(options2, "Test Group", "Test Group for options", 1, 3, false);
        assertEquals(1, group2.getMinimum());
        assertEquals(3, group2.getMaximum());
        assertEquals("Test Group", group2.getName());
        assertEquals("Test Group for options", group2.getDescription());
        assertFalse(group2.isRequired());
        assertTrue(group2.hasOptions());
        assertTrue(group2.canProcess(null, null));
        assertEquals(Arrays.asList(option1, option2, option3), group2.getOptions());
        assertEquals(Arrays.asList("o1", "o2", "o3"), group2.getTriggers());
        assertEquals(new HashSet(Collections.singletonList("o1")), group2.getPrefixes());
    }

    /**
     * Test the canProcess method.
     */
    @Test
    public void testCanProcess() {
        final Option option1 = new Option("o1", "Option 1");
        final Option option2 = new Option("o2", "Option 2");
        final Option option3 = new Option("o3", "Option 3");

        final List options = new ArrayList();
        options.add(option1);
        options.add(option2);
        options.add(option3);

        final Group group = new GroupImpl(options, "Test Group", "Test Group for options", 1, 3, false);

        // Test with no arguments
        assertFalse(group.canProcess(null, null));

        // Test with only one argument
        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parse("-o1 value1");
        assertTrue(group.canProcess(commandLine, null));

        // Test with multiple arguments
        final List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        assertFalse(group.canProcess(commandLine, arguments));

        // Test with an argument that matches an option
        commandLine.parse("-o1 value1 arg1");
        arguments.clear();
        arguments.add("arg1");
        assertTrue(group.canProcess(commandLine, arguments));

        // Test with an argument that matches a trigger
        commandLine.clear();
        commandLine.parse("-t1 value1 arg1");
        assertTrue(group.canProcess(commandLine, arguments));

        // Test with an argument that matches a prefix
        commandLine.clear();
        commandLine.parse("-p o prefix arg1");
        assertTrue(group.canProcess(commandLine, arguments));

        // Test with an argument that matches a trigger and a prefix
        commandLine.clear();
        commandLine.parse("-t1 value1 -p o prefix arg1");
        assertTrue(group.canProcess(commandLine, arguments));

        // Test with an argument that does not match any option
        commandLine.clear();
        commandLine.parse("-n arg1");
        assertFalse(group.canProcess(commandLine, arguments));

        // Test with an argument that does not match any trigger or prefix
        commandLine.clear();
        commandLine.parse("arg1");
        assertFalse(group.canProcess(commandLine, arguments));
    }

    /**
     * Test the process method.
     */
    @Test
    public void testProcess() {
        final Option option1 = new Option("o1", "Option 1");
        final Option option2 = new Option("o2", "Option 2");
        final Option option3 = new Option("o3", "Option 3");

        final List options = new ArrayList();
        options.add(option1);
        options.add(option2);
        options.add(option3);

        final Group group = new GroupImpl(options, "Test Group", "Test Group for options", 1, 3, false);

        // Test with no arguments
        final WriteableCommandLine commandLine = new WriteableCommandLine();
        assertThrows(OptionException.class, () -> group.process(commandLine, null));

        // Test with only one argument
        commandLine.parse("-o1 value1");
        final List arguments = new ArrayList();
        group.process(commandLine, arguments);
        assertEquals(1, arguments.size());
        assertEquals(option1, arguments.get(0));

        // Test with multiple arguments
        commandLine.clear();
        commandLine.parse("-o1 value1 arg1 arg2");
        arguments.clear();
        group.process(commandLine, arguments);
        assertEquals(2, arguments.size());
        assertEquals(option1, arguments.get(0));
        assertEquals(option3, arguments.get(1));

        // Test with an argument that matches an option
        commandLine.clear();
        commandLine.parse("-o1 value1 arg1");
        arguments.clear();
        group.process(commandLine, arguments);
        assertEquals(1, arguments.size());
        assertEquals(option1, arguments.get(0));

        // Test with an argument that matches a trigger
        commandLine.clear();
        commandLine.parse("-t1 value1 arg1");
        arguments.clear();
        group.process(commandLine, arguments);
        assertEquals(1, arguments.size());
        assertEquals(option1, arguments.get(0));

        // Test with an argument that matches a prefix
        commandLine.clear();
        commandLine.parse("-p o prefix arg1");
        arguments.clear();
        group.process(commandLine, arguments);
        assertEquals(1, arguments.size());
        assertEquals(option1, arguments.get(0));

        // Test with an argument that matches a trigger and a prefix
        commandLine.clear();
        commandLine.parse("-t1 value1 -p o prefix arg1");
        arguments.clear();
        group.process(commandLine, arguments);
        assertEquals(1, arguments.size());
        assertEquals(option1, arguments.get(0));
    }

    /**
     * Test the validate method.
     */
    @Test
    public void testValidate() {
        final Option option1 = new Option("o1", "Option 1");
        final Option option2 = new Option("o2", "Option 2");
        final Option option3 = new Option("o3", "Option 3");

        final List options = new ArrayList();
        options.add(option1);
        options.add(option2);
        options.add(option3);

        final Group group = new GroupImpl(options, "Test Group", "Test Group for options", 1, 3, false);

        // Test with a command line that has the required number of options
        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parse("--test-group value1 value2 value3");
        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            // expected when the command line has more than 3 arguments
            assertNull(e.getMessage());
        }

        // Test with a command line that has less than the minimum number of options
        commandLine.clear();
        commandLine.parse("--test-group value1");
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test with a command line that has more than the maximum number of options
        commandLine.clear();
        commandLine.parse("--test-group value1 value2 value3 value4 value5");
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test with a command line that has an option with the wrong value
        commandLine.clear();
        commandLine.parse("--test-group value1 value2 value3 value4 value5 --o3 valueX");
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test with a command line that has an option with a wrong trigger
        commandLine.clear();
        commandLine.parse("--test-group value1 value2 --oX value3");
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test with a command line that has an anonymous argument
        commandLine.clear();
        commandLine.parse("--test-group value1 value2 value3 arg1");
        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            // expected when the command line has 5 arguments
            assertNull(e.getMessage());
        }
    }
}