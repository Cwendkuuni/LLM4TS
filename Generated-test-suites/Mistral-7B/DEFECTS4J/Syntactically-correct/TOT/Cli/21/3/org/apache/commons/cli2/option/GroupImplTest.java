package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GroupImplTest {

    @Test
    public void testBasicGroup() {
        final String[] args = new String[]{"-g", "--group", "-a", "1", "-b", "2", "-c"};
        final List options = new ArrayList();
        options.add(new Option("a", 1, false, "Option A"));
        options.add(new Option("b", 1, false, "Option B"));
        options.add(new Option("c", 1, false, "Option C"));
        final Group group = new GroupImpl(options, "group", "My Group", 1, Integer.MAX_VALUE, false);

        final CommandLine parsed = group.parse(args);

        assertTrue("Option A should be present", parsed.hasOption("a"));
        assertTrue("Option B should be present", parsed.hasOption("b"));
        assertTrue("Option C should be present", parsed.hasOption("c"));
        assertEquals("Argument count should be 0", 0, parsed.getArgList().size());

        final HelpFormatter formatter = new HelpFormatter();
        final String help = formatter.printHelp("MyGroup", group.getOptions(), true);
        assertEquals("Help message should match", "-g,--group          My Group\n" +
                "                              Options:\n" +
                "                              --- \n" +
                "-a                            Option A\n" +
                "-b                            Option B\n" +
                "-c                            Option C\n", help);
    }

    @Test(expected = OptionException.class)
    public void testMinimumOptions() {
        final String[] args = new String[]{"-g", "--group", "-a", "1"};
        final List options = new ArrayList();
        options.add(new Option("a", 1, false, "Option A"));
        options.add(new Option("b", 1, false, "Option B"));
        options.add(new Option("c", 1, false, "Option C"));
        final Group group = new GroupImpl(options, "group", "My Group", 2, Integer.MAX_VALUE, false);

        group.parse(args);
        fail("An OptionException should have been thrown");
    }

    @Test(expected = OptionException.class)
    public void testMaximumOptions() {
        final String[] args = new String[]{"-g", "--group", "-a", "1", "-b", "1", "-c", "-d"};
        final List options = new ArrayList();
        options.add(new Option("a", 1, false, "Option A"));
        options.add(new Option("b", 1, false, "Option B"));
        options.add(new Option("c", 1, false, "Option C"));
        final Group group = new GroupImpl(options, "group", "My Group", 3, 3, false);

        group.parse(args);
        fail("An OptionException should have been thrown");
    }

    @Test(expected = OptionException.class)
    public void testInvalidOption() {
        final String[] args = new String[]{"-g", "--group", "-i", "invalid"};
        final List options = new ArrayList();
        options.add(new Option("a", 1, false, "Option A"));
        options.add(new Option("b", 1, false, "Option B"));
        final Group group = new GroupImpl(options, "group", "My Group", 1, Integer.MAX_VALUE, false);

        group.parse(args);
        fail("An OptionException should have been thrown");
    }

    @Test
    public void testAnonymousArguments() {
        final String[] args = new String[]{"-g", "--group", "-a", "1", "arg1", "arg2"};
        final List options = new ArrayList();
        options.add(new Option("a", 1, false, "Option A"));
        options.add(new Argument("arg", Integer.TYPE, "anonymous argument"));
        final Group group = new GroupImpl(options, "group", "My Group", 1, Integer.MAX_VALUE, false);

        final CommandLine parsed = group.parse(args);
        final List arguments = parsed.getArgList();

        assertTrue("Argument arg1 should be present", arguments.get(0).getValue().equals("arg1"));
        assertTrue("Argument arg2 should be present", arguments.get(1).getValue().equals("arg2"));
        assertEquals("Argument count should be 2", 2, arguments.size());

        final HelpFormatter formatter = new HelpFormatter();
        final String help = formatter.printHelp("MyGroup", group.getOptions(), true);
        assertEquals("Help message should match", "-g,--group          My Group\n" +
                "                              Options:\n" +
                "                              --- \n" +
                "-a                            Option A\n" +
                "anonymous argument           anonymous argument\n", help);
    }

    @Test
    public void testEmptyGroup() {
        final String[] args = new String[]{"-g", "--group"};
        final List options = new ArrayList();
        final Group group = new GroupImpl(options, "group", "My Group", 0, Integer.MAX_VALUE, false);

        try {
            group.parse(args);
            fail("An OptionException should have been thrown");
        } catch (OptionException e) {
            // expected
        }
    }
}