package org.apache.commons.cli2.option;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
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
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

import static org.mockito.Mockito.*;

public class GroupImplTest {

    private GroupImpl group;
    private List<Option> options;
    private Option option1;
    private Option option2;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        options = new ArrayList<>();
        option1 = mock(Option.class);
        option2 = mock(Option.class);
        commandLine = mock(WriteableCommandLine.class);

        when(option1.getTrigger()).thenReturn("option1");
        when(option1.isRequired()).thenReturn(false);
        when(option1.canProcess(commandLine, "option1")).thenReturn(true);

        when(option2.getTrigger()).thenReturn("option2");
        when(option2.isRequired()).thenReturn(false);
        when(option2.canProcess(commandLine, "option2")).thenReturn(true);

        options.add(option1);
        options.add(option2);

        group = new GroupImpl(options, "group", "description", 1, 2, false);
    }

    @Test
    public void testCanProcess() {
        // test with valid option
        assertTrue(group.canProcess(commandLine, "option1"));
        verify(commandLine).looksLikeOption("option1");

        // test with bursting option
        when(option1.canProcess(commandLine, "option1")).thenReturn(false);
        when(option2.canProcess(commandLine, "option1")).thenReturn(true);
        assertTrue(group.canProcess(commandLine, "option1"));
        verify(commandLine, times(2)).looksLikeOption("option1");

        // test with invalid argument
        assertFalse(group.canProcess(commandLine, "invalid"));
        verify(commandLine, never()).looksLikeOption("invalid");

        // test with anonymous argument
        Argument arg = mock(Argument.class);
        options.add(arg);
        group = new GroupImpl(options, "group", "description", 1, 2, false);
        assertTrue(group.canProcess(commandLine, "argument"));
        verify(commandLine).looksLikeOption("argument");

        // test with minimum constraint
        group = new GroupImpl(options, "group", "description", 2, 2, false);
        assertFalse(group.canProcess(commandLine, "option1"));
        verify(commandLine, never()).looksLikeOption("option2");

        // test with maximum constraint
        group = new GroupImpl(options, "group", "description", 1, 1, false);
        assertFalse(group.canProcess(commandLine, "option1"));
        verify(commandLine, never()).looksLikeOption("option2");
    }

    @Test
    public void testProcess() {
        // test with valid option
        group.process(commandLine, listIterator(Collections.emptyList().listIterator()));
        verify(option1).process(commandLine, listIterator(Collections.emptyList().listIterator()));
        verify(option2).process(commandLine, listIterator(Collections.emptyList().listIterator()));

        // test with bursting option
        when(option1.canProcess(commandLine, "option1")).thenReturn(false);
        when(option2.canProcess(commandLine, "option1")).thenReturn(true);
        group.process(commandLine, listIterator(Collections.emptyList().listIterator()));
        verify(option1, times(2)).process(commandLine, listIterator(Collections.emptyList().listIterator()));
        verify(option2).process(commandLine, listIterator(Collections.emptyList().listIterator()));

        // test with invalid argument
        group.process(commandLine, listIterator("invalid".split(" ")));
        verify(commandLine, never()).looksLikeOption("invalid");

        // test with anonymous argument
        Argument arg = mock(Argument.class);
        options.add(arg);
        group.process(commandLine, listIterator("argument".split(" ")));
        verify(arg).process(commandLine, listIterator("argument".split(" ")));

        // test with minimum constraint
        group = new GroupImpl(options, "group", "description", 2, 2, false);
        group.process(commandLine, listIterator("option1".split(" ")));
        group.process(commandLine, listIterator("option2".split(" ")));
        verify(option1, times(2)).process(commandLine, listIterator("option1".split(" ")));
        verify(option2, times(2)).process(commandLine, listIterator("option2".split(" ")));

        // test with maximum constraint
        group = new GroupImpl(options, "group", "description", 1, 1, false);
        group.process(commandLine, listIterator("option1".split(" ")));
        group.process(commandLine, listIterator("option2".split(" ")));
        verify(option1, times(1)).process(commandLine, listIterator("option1".split(" ")));
        verify(option2, never()).process(commandLine, listIterator("option2".split(" ")));
    }

    @Test
    public void testValidate() {
        // test with valid options
        group.validate(commandLine);
        verifyNoMoreInteractions(commandLine);

        // test with missing option
        when(commandLine.hasOption("option1")).thenReturn(false);
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Missing required option 'option1'", e.getMessage());
        }
        verify(commandLine).hasOption("option1");

        // test with extra option
        when(commandLine.hasOption("option3")).thenReturn(true);
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Unrecognized option: option3", e.getMessage());
        }
        verify(commandLine).hasOption("option3");

        // test with anonymous argument
        Argument arg = mock(Argument.class);
        options.add(arg);
        group.validate(commandLine);
        verifyNoMoreInteractions(commandLine);
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = group.getPrefixes();
        assertEquals(Collections.emptySet(), prefixes);
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = group.getTriggers();
        assertEquals(new HashSet<>(Collections.singletonList("option1")), triggers);

        Set<String> triggers2 = group.getTriggers();
        assertEquals(new HashSet<>(Arrays.asList("option1", "option2")), triggers2);
    }

    @Test
    public void testGetName() {
        assertEquals("group", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("description", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, new HashSet<>(), null);
        String expectedUsage = "group: " +
                "description " +
                "[option1 (description of option1)] " +
                "[option2 (description of option2)] ";
        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(1, new HashSet<>(), null);
        String expectedHelpLines = "group: " +
                "description " +
                "  option1 (description of option1)\n" +
                "  option2 (description of option2)";
        assertEquals(expectedHelpLines, HelpLine.toString(helpLines));

        List<HelpLine> helpLines2 = group.helpLines(1, new HashSet<>(Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME)), null);
        String expectedHelpLines2 = "group: " +
                "description";
        assertEquals(expectedHelpLines2, HelpLine.toString(helpLines2));
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();
        assertEquals(new ArrayList<>(Arrays.asList(option1, option2)), options);
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymous = group.getAnonymous();
        assertEquals(new ArrayList<>(), anonymous);

        Argument arg = mock(Argument.class);
        options.add(arg);
        group = new GroupImpl(options, "group", "description", 1, 2, false);
        List<Option> anonymous2 = group.getAnonymous();
        assertEquals(Collections.singletonList(arg), anonymous2);
    }

    @Test
    public void testFindOption() {
        Option foundOption = group.findOption("option1");
        assertEquals(option1, foundOption);

        Option notFoundOption = group.findOption("option3");
        assertNull(notFoundOption);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(2, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);
        verify(option1).defaults(commandLine);
        verify(option2).defaults(commandLine);
    }
}