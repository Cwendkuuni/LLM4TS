package org.apache.commons.cli2.option;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private GroupImpl group;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "a"));
        options.add(new Option("b", "option B", false, "b"));
        options.add(new Option("c", "option C", false, "c"));
        options.add(new Option("d", "option D", false, "d"));

        group = new GroupImpl(options, "Group", "Group of options", 1, Integer.MAX_VALUE, false);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");

        commandLine.parse("-a --b c d");

        assertTrue(group.canProcess(commandLine, "a"));
        assertTrue(group.canProcess(commandLine, "b"));
        assertTrue(group.canProcess(commandLine, "c"));
        assertTrue(group.canProcess(commandLine, "d"));

        assertFalse(group.canProcess(commandLine, "e"));
        assertFalse(group.canProcess(commandLine, "1"));
        assertFalse(group.canProcess(commandLine, ""));
        assertFalse(group.canProcess(commandLine, null));
    }

    @Test(expected = OptionException.class)
    public void testValidateMinimum() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");

        commandLine.parse("-a");

        group.validate(commandLine);

        // should throw an OptionException
    }

    @Test(expected = OptionException.class)
    public void testValidateMaximum() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");

        commandLine.parse("-a --a");

        group.validate(commandLine);

        // should throw an OptionException
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = group.getPrefixes();

        assertFalse(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        SortedMap<String, Option> optionMap = group.getOptionMap();

        assertEquals(4, optionMap.size());

        List<String> triggersList = new ArrayList<>();
        optionMap.forEach((k, v) -> triggersList.add(k));

        List<String> expectedTriggers = Arrays.asList("a", "b", "c", "d");

        assertEquals(expectedTriggers, triggersList);
    }

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");

        commandLine.parse("-a --b c d");

        group.process(commandLine, commandLine.listIterator());

        WriteableCommandLine resultCommandLine = commandLine.clone();

        assertEquals("-a --b true false false false", resultCommandLine.toString());
    }

    @Test
    public void testGetName() {
        assertEquals("Group", group.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Group of options", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, Collections.emptySet(), null);

        String expectedUsage = "|Group of options|";

        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<String> helpLines = group.helpLines(0, Collections.emptySet(), null);

        List<String> expectedHelpLines = Arrays.asList(
                "|Group of options|",
                "  ",
                "  (",
                "  )",
                "");

        assertEquals(expectedHelpLines, helpLines);
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();

        assertNotNull(options);

        assertEquals(4, options.size());
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymous = group.getAnonymous();

        assertNotNull(anonymous);

        assertEquals(0, anonymous.size());
    }

    @Test
    public void testFindOption() {
        Option optionA = group.findOption("a");

        assertNotNull(optionA);

        assertEquals("a", optionA.getLongOpt());
        assertEquals("option A", optionA.getDescription());
        assertTrue(optionA.isRequired());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
    }
}