package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;

import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testGroupImplCreation() {
        // given
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("opt1")
                .withDescription("Option 1")
                .withValueSeparator('=')
                .create());
        options.add(OptionBuilder.withLongOpt("opt2")
                .withDescription("Option 2")
                .isRequired()
                .create());
        options.add(OptionBuilder.withLongOpt("opt3")
                .withDescription("Option 3")
                .create());
        options.add(OptionBuilder.withLongOpt("arg1")
                .withDescription("Argument 1")
                .isArgument()
                .create());

        final GroupImpl group = new GroupImpl(options, "MyGroup", "My Group", 1, Integer.MAX_VALUE, false);

        // when

        // then
        assertEquals("MyGroup", group.getPreferredName());
        assertEquals("My Group", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
        assertFalse(group.isRequired());
        assertTrue(group.canProcess(null, null));
        assertNull(group.findOption("opt1"));
        assertNotNull(group.findOption("opt2"));
        assertNull(group.findOption("opt3"));
        assertNull(group.findOption("arg1"));
        assertTrue(group.isArgumentAvailable());

        // appendUsage test
        final StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, null, null);
        assertEquals("My Group:\n" +
                "  My Group (required)\n" +
                "    Opt1: Option 1\n" +
                "    Opt2: Option 2\n" +
                "    Opt3: Option 3\n" +
                "    Arg1: Argument 1\n", sb.toString());

        // helpLines test
        final List<HelpLine> helpLines = group.helpLines(0, null, null);
        assertEquals(5, helpLines.size());
        for (final HelpLine helpLine : helpLines) {
            assertEquals("My Group", helpLine.getOptionSpec().getLongOpt());
        }

        // validate test
        final WriteableCommandLine commandLine = new WriteableCommandLine("command");
        group.validate(commandLine);

        // process test
        commandLine.setOption("opt1", "value1");
        commandLine.setOption("opt2", "value2");
        group.process(commandLine, commandLine.listIterator());
        assertEquals(2, commandLine.getOptionValues("opt2").size());
        assertEquals("value1", commandLine.getOptionValue("opt1"));
        assertNull(commandLine.getOptionValue("opt3"));
        assertNull(commandLine.getOptionValue("arg1"));

        // test anonymous arguments
        Argument argument = null;
        for (final Iterator<Option> it = group.getAnonymous().iterator(); it.hasNext();) {
            argument = (Argument) it.next();
            break;
        }
        assertEquals("Arg1", argument.getLongOpt());
        assertEquals("Argument 1", argument.getDescription());
        assertNull(argument.getValue());
        assertFalse(argument.isRequired());

        // test minimum and maximum
        final WriteableCommandLine commandLine2 = new WriteableCommandLine("command2");
        for (int i = 0; i < 5; i++) {
            commandLine2.setOption("opt2", "value2");
        }
        try {
            group.validate(commandLine2);
            fail("Invalid number of options");
        } catch (final OptionException e) {
            // expected
        }

        final WriteableCommandLine commandLine3 = new WriteableCommandLine("command3");
        for (int i = 0; i < 1; i++) {
            commandLine3.setOption("opt2", "value2");
        }
        try {
            group.validate(commandLine3);
            fail("Invalid number of options");
        } catch (final OptionException e) {
            // expected
        }

        final WriteableCommandLine commandLine4 = new WriteableCommandLine("command4");
        for (int i = 0; i < 2; i++) {
            commandLine4.setOption("opt2", "value2");
        }
        group.validate(commandLine4);

        final WriteableCommandLine commandLine5 = new WriteableCommandLine("command5");
        for (int i = 0; i < 3; i++) {
            commandLine5.setOption("opt2", "value2");
        }
        group.validate(commandLine5);

        final WriteableCommandLine commandLine6 = new WriteableCommandLine("command6");
        for (int i = 0; i < 4; i++) {
            commandLine6.setOption("opt2", "value2");
        }
        group.validate(commandLine6);

        final WriteableCommandLine commandLine7 = new WriteableCommandLine("command7");
        for (int i = 0; i < 5; i++) {
            commandLine7.setOption("opt2", "value2");
        }
        try {
            group.validate(commandLine7);
            fail("Invalid number of options");
        } catch (final OptionException e) {
            // expected
        }
    }
}