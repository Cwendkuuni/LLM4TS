package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private Options options;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        options = new Options();
        formatter = new HelpFormatter();
    }

    @Test
    public void testBasicUsage() throws ParseException {
        final Group group = new GroupImpl(
            asList(
                new Option("a", "A", false, "Option A")
                    .setValueSeparator(' ')
                    .setArgName("arg-a"),
                new Option("b", "B", true, "Option B")
            ),
            "My Group",
            "Description of my group",
            0,
            1);

        options.addOptionGroup(group);

        final CommandLine commandLine = new CommandLine("-a arg-a1 arg-a2 b");
        group.process(commandLine, commandLine.iterator());

        assertEquals(2, group.getAnonymous().size());
    }

    @Test
    public void testMinimum() throws ParseException {
        final Group group = new GroupImpl(
            asList(
                new Option("a", "A", false, "Option A")
                    .setValueSeparator(' ')
                    .setArgName("arg-a"),
                new Option("b", "B", true, "Option B")
            ),
            "My Group",
            "Description of my group",
            2,
            10);

        options.addOptionGroup(group);

        final CommandLine commandLine = new CommandLine("-a arg-a1 arg-a2");
        try {
            group.process(commandLine, commandLine.iterator());
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }

        final CommandLine commandLine2 = new CommandLine("-a arg-a1 arg-a2 arg-a3 arg-a4");
        group.process(commandLine2, commandLine2.iterator());
    }

    @Test
    public void testMaximum() throws ParseException {
        final Group group = new GroupImpl(
            asList(
                new Option("a", "A", false, "Option A")
                    .setValueSeparator(' ')
                    .setArgName("arg-a"),
                new Option("b", "B", true, "Option B")
            ),
            "My Group",
            "Description of my group",
            0,
            3);

        options.addOptionGroup(group);

        final CommandLine commandLine = new CommandLine("-a arg-a1 arg-a2 arg-a3 arg-a4");
        try {
            group.process(commandLine, commandLine.iterator());
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testAnonymous() throws ParseException {
        final Group group = new GroupImpl(
            asList(
                new Option("a", "A", false, "Option A")
                    .setValueSeparator(' ')
                    .setArgName("arg-a"),
                new Option("b", "B", true, "Option B"),
                new Argument("C", "C", true, "Argument C")
            ),
            "My Group",
            "Description of my group",
            0,
            1);

        options.addOptionGroup(group);

        final CommandLine commandLine = new CommandLine("--C arg-c1 arg-c2");
        group.process(commandLine, commandLine.iterator());

        assertTrue(group.getAnonymous().get(0).isSet(commandLine));
    }

    @Test
    public void testCanProcess() throws ParseException {
        final Group group = new GroupImpl(
            asList(
                new Option("a", "A", false, "Option A")
                    .setValueSeparator(' ')
                    .setArgName("arg-a"),
                new Option("b", "B", true, "Option B")
            ),
            "My Group",
            "Description of my group",
            0,
            1);

        final CommandLine commandLine = new CommandLine("-a arg-a1");
        assertTrue(group.canProcess(commandLine, "a"));

        final CommandLine commandLine2 = new CommandLine("-b");
        assertFalse(group.canProcess(commandLine2, "b"));

        final CommandLine commandLine3 = new CommandLine("--a arg-a1 arg-a2");
        assertTrue(group.canProcess(commandLine3, "a"));
    }

    @Test
    public void testValidate() throws ParseException {
        final Group group = new GroupImpl(
            asList(
                new Option("a", "A", false, "Option A")
                    .setValueSeparator(' ')
                    .setArgName("arg-a"),
                new Option("b", "B", true, "Option B")
            ),
            "My Group",
            "Description of my group",
            1,
            1);

        options.addOptionGroup(group);

        final CommandLine commandLine = new CommandLine();
        try {
            group.validate(commandLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }

        final CommandLine commandLine2 = new CommandLine("-a arg-a1");
        group.validate(commandLine2);
    }

    @Test
    public void testUsage() {
        final Group group = new GroupImpl(
            asList(
                new Option("a", "A", false, "Option A")
                    .setValueSeparator(' ')
                    .setArgName("arg-a"),
                new Option("b", "B", true, "Option B")
            ),
            "My Group",
            "Description of my group",
            0,
            1);

        options.addOptionGroup(group);

        final StringWriter writer = new StringWriter();

        formatter.printHelp("MyApp", options, writer, false);
        final String helpText = writer.toString();

        final HelpLine helpLine = new HelpLine(helpText);

        // check the usage information
        assertEquals("MyApp: [OPTIONS]\n", helpLine.getHeadText());
        assertEquals("My Group:\n", helpLine.getGroupText());
        assertEquals("", helpLine.getOptionDescriptions());
        assertEquals("Option A ARG_A [ARG_A ...]\n", helpLine.getOptionSpecs().get(0).getLongOpt());
        assertEquals("Option B\n", helpLine.getOptionSpecs().get(1).getLongOpt());
    }

    private static <T> List<T> asList(final T... elements) {
        final List<T> list = new ArrayList<T>(elements.length);

        for (final T element : elements) {
            list.add(element);
        }

        return list;
    }
}