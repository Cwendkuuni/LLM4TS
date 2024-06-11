package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.TestUtils;
import org.apache.commons.cli2.options.OptionBuilder;
import org.apache.commons.cli2.options.OptionBuilders;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private List options;
    private GroupImpl group;

    @Before
    public void setup() {
        options = List.of(
                OptionBuilder.withLongOpt("a")
                        .withValueSeparator('=')
                        .withDescription("Option A")
                        .create(),
                OptionBuilder.withLongOpt("b")
                        .withDescription("Option B")
                        .create(),
                OptionBuilder.withLongOpt("c")
                        .withValueSeparator('=')
                        .withDescription("Option C with value")
                        .create(),
                OptionBuilder.withLongOpt("d")
                        .withDescription("Option D")
                        .create(),
                OptionBuilder.withLongOpt("e")
                        .withDescription("Option E")
                        .isRequired()
                        .create()
        );

        group = new GroupImpl(options, "MyGroup", "My Group", 1, 3);
    }

    @Test
    public void testCanProcess() {
        CommandLine commandLine = TestUtils.createDefaultCommandLine();

        assertFalse(group.canProcess(commandLine, null));

        assertTrue(group.canProcess(commandLine, "a"));
        assertTrue(group.canProcess(commandLine, "a=foo"));
        assertTrue(group.canProcess(commandLine, "b"));

        assertFalse(group.canProcess(commandLine, "c"));
        assertFalse(group.canProcess(commandLine, "d"));
        assertFalse(group.canProcess(commandLine, "e"));
        assertFalse(group.canProcess(commandLine, "f"));
    }

    @Test
    public void testProcess() throws OptionException {
        CommandLine commandLine = TestUtils.createDefaultCommandLine();

        group.process(commandLine, commandLine.listIterator());

        assertEquals(1, commandLine.getOptionValues("a").length);
        assertEquals("foo", commandLine.getOptionValues("a")[0]);
        assertEquals(null, commandLine.getOptionValues("b"));
        assertNull(commandLine.getOptionValues("c"));
        assertNull(commandLine.getOptionValues("d"));
        assertNull(commandLine.getOptionValues("e"));

        // Test with too few options
        commandLine = TestUtils.createDefaultCommandLine();
        group.process(commandLine, commandLine.listIterator());

        assertEquals(0, commandLine.getOptionValues("a").length);
        assertEquals(null, commandLine.getOptionValues("b"));
        assertEquals(null, commandLine.getOptionValues("c"));
        assertNull(commandLine.getOptionValues("d"));
        assertNull(commandLine.getOptionValues("e"));
    }

    @Test(expected = OptionException.class)
    public void testProcessInvalidOption() throws OptionException {
        CommandLine commandLine = TestUtils.createDefaultCommandLine();
        commandLine.parse(options.toArray(new Option[]{}));

        group.process(commandLine, commandLine.listIterator());

        commandLine.parse(new String[]{"--foo"});
        group.process(commandLine, commandLine.listIterator());
    }

    @Test
    public void testValidate() throws OptionException {
        CommandLine commandLine = TestUtils.createDefaultCommandLine();

        group.validate(commandLine);

        group.validate(commandLine);
        group.validate(commandLine);
        group.validate(commandLine);

        // Test with too few options
        commandLine = TestUtils.createDefaultCommandLine();
        group.validate(commandLine);

        // Test with too many options
        commandLine = TestUtils.createDefaultCommandLine();
        commandLine.parse(options.toArray(new Option[]{}));
        commandLine.parse(new String[]{"--a=foo", "--a=bar"});
        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder sb = new StringBuilder();

        group.appendUsage(sb, null, null);

        assertTrue(sb.toString().contains("MyGroup"));
        assertTrue(sb.toString().contains("Option A"));
        assertTrue(sb.toString().contains("Option B"));
        assertTrue(sb.toString().contains("Option C with value"));
        assertTrue(sb.toString().contains("Option D"));
        assertTrue(sb.toString().contains("Option E"));
    }

    @Test
    public void testHelpLines() {
        final StringBuilder sb = new StringBuilder();
        final List helpLines = group.helpLines(0, null, null);

        for (Object line : helpLines) {
            sb.append(line);
            sb.append("\n");
        }

        // The first line should contain "MyGroup"
        assertTrue(sb.toString().contains("MyGroup"));

        // The last line should contain "Option E" and be indented
        assertTrue(sb.toString().contains("Option E"));
        assertTrue(sb.toString().split("\n")[sb.toString().split("\n").length - 1].startsWith("  "));
    }
}