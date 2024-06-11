package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        final List<Option> options = new ArrayList<>();
        options.add(new OptionBuilder("opt1")
                .longOpt("longOpt1")
                .description("Description for opt1")
                .isRequired(false)
                .build());
        options.add(new OptionBuilder("opt2")
                .longOpt("longOpt2")
                .description("Description for opt2")
                .isRequired(false)
                .build());
        options.add(new OptionBuilder("arg")
                .argName("ARG")
                .description("Description for arg")
                .isRequired(false)
                .build());
        options.add(new OptionBuilder("opt3")
                .longOpt("longOpt3")
                .description("Description for opt3")
                .isRequired(true)
                .build());

        final Group group = new GroupImpl(options, "group", "Description for group", 1, 3, false);

        assertNotNull(group.getName());
        assertEquals("group", group.getName());
        assertNotNull(group.getDescription());
        assertEquals("Description for group", group.getDescription());
        assertEquals(3, group.getMinimum());
        assertEquals(3, group.getMaximum());
        assertNull(group.getParent());

        assertFalse(group.canProcess(null, "opt1"));
        assertTrue(group.canProcess(null, "opt1"));
        assertFalse(group.canProcess(null, "arg"));
        assertTrue(group.canProcess(null, "arg"));

        final Set<String> prefixes = group.getPrefixes();
        assertEquals(0, prefixes.size());

        final Set<String> triggers = group.getTriggers();
        assertEquals(0, triggers.size());

        final List<Option> groupOptions = group.getOptions();
        assertEquals(3, groupOptions.size());
        assertNotNull(groupOptions.get(0));
        assertNotNull(groupOptions.get(1));
        assertNotNull(groupOptions.get(2));

        final List<Option> anonymous = group.getAnonymous();
        assertEquals(1, anonymous.size());
        assertNotNull(anonymous.get(0));

        final Option opt1 = group.findOption("opt1");
        assertNotNull(opt1);

        final Option opt3 = group.findOption("opt3");
        assertNotNull(opt3);

        assertNull(group.findOption("opt4"));

        final WriteableCommandLine commandLine = new WriteableCommandLine();

        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("Unexpected exception while validating group: " + e.getMessage());
        }

        final List<String> arguments = Arrays.asList("opt1", "opt2", "opt3");
        group.process(commandLine, arguments.listIterator());

        final StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("group (Description for group)\n" +
                "  opt1, --longOpt1       Description for opt1\n" +
                "  opt2, --longOpt2       Description for opt2\n" +
                "  opt3, --longOpt3       Description for opt3\n" +
                "  ARG                   Description for arg\n", buffer.toString());

        final List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(4, helpLines.size());
        assertEquals("group (Description for group)\n", helpLines.get(0).getHeader());
        assertEquals("  opt1, --longOpt1       Description for opt1\n", helpLines.get(1).getOptionText());
        assertEquals("  opt2, --longOpt2       Description for opt2\n", helpLines.get(2).getOptionText());
        assertEquals("  opt3, --longOpt3       Description for opt3\n", helpLines.get(3).getOptionText());
    }
}