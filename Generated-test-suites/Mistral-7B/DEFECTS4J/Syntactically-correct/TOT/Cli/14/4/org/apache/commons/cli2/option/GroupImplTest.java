package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.ParseContext;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for GroupImpl class
 */
public class GroupImplTest {

    private ParseContext context;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        final List<Option> options = new ArrayList<>();
        options.add(new Option()
                .withLongOpt("a")
                .withArgName("ARG")
                .withDescription("Option A")
                .isRequired(false));

        options.add(new Option()
                .withLongOpt("b")
                .withArgName("ARG")
                .withDescription("Option B")
                .isRequired(true)
                .withValueSeparator(':'));

        options.add(new Option()
                .withLongOpt("c")
                .withArgName("ARG1 ARG2")
                .withDescription("Option C")
                .isRequired(false)
                .withValueSeparator(',')
                .getValueParser(new IntArrayValueParser()));

        final Group group = new GroupImpl(options, "MyGroup", null, 1, 3);

        context = new ParseContext();
        formatter = new HelpFormatter();
    }

    @Test
    public void testCanProcess() {
        final CommandLine cmd = new CommandLine();

        // test option A
        cmd.parseArgument(0, "a", "ARG");
        assertTrue(group.canProcess(cmd, "a"));

        // test option B
        cmd.parseArgument(0, "b", "ARG");
        assertTrue(group.canProcess(cmd, "b"));

        cmd.parseArgument(0, "b", "ARG2");
        assertTrue(group.canProcess(cmd, "b"));

        cmd.parseArgument(0, "b", "ARG1:ARG2");
        assertTrue(group.canProcess(cmd, "b"));

        cmd.parseArgument(0, "b", "ARG1");
        assertFalse(group.canProcess(cmd, "b"));

        cmd.parseArgument(0, "b", "ARG:ARG2");
        assertFalse(group.canProcess(cmd, "b"));

        // test option C
        cmd.parseArgument(0, "c", "ARG1,ARG2");
        assertTrue(group.canProcess(cmd, "c"));

        cmd.parseArgument(0, "c", "ARG1");
        assertFalse(group.canProcess(cmd, "c"));

        cmd.parseArgument(0, "c", "ARG1,ARG2,");
        assertFalse(group.canProcess(cmd, "c"));

        cmd.parseArgument(0, "c", "ARG1,ARG3");
        assertFalse(group.canProcess(cmd, "c"));

        cmd.parseArgument(0, "c", "ARG1,ARG2,ARG3");
        assertTrue(group.canProcess(cmd, "c"));
    }

    @Test
    public void testProcessValid() throws OptionException {
        final CommandLine cmd = new CommandLine();

        // test option A
        cmd.parseArgument(0, "a", "ARG");
        group.process(cmd, cmd.iterator());
        assertNull(cmd.getValue("a"));

        // test option B
        cmd.parseArgument(0, "b", "ARG1:ARG2");
        group.process(cmd, cmd.iterator());
        assertNull(cmd.getValue("b"));

        cmd.parseArgument(0, "b", "ARG1:ARG2");
        group.process(cmd, cmd.iterator());
        assertEquals("ARG2", cmd.getValue("b"));

        // test option C
        cmd.parseArgument(0, "c", "ARG1,ARG2");
        group.process(cmd, cmd.iterator());
        final int[] ints = (int[]) cmd.getValue("c");
        assertEquals(2, ints.length);
        assertEquals(1, ints[0]);
        assertEquals(2, ints[1]);
    }

    @Test(expected = OptionException.class)
    public void testProcessInvalid() throws OptionException {
        final CommandLine cmd = new CommandLine();

        // test option B
        cmd.parseArgument(0, "b", "ARG");
        group.process(cmd, cmd.iterator());
        assertNull(cmd.getValue("b"));

        // test option B with no argument
        cmd.parseArgument(0, "b", null);
        group.process(cmd, cmd.iterator());
        assertNull(cmd.getValue("b"));

        // test option B with too many arguments
        cmd.parseArgument(0, "b", "ARG1:ARG2:ARG3");
        group.process(cmd, cmd.iterator());
        assertNull(cmd.getValue("b"));

        // test option C with no arguments
        cmd.parseArgument(0, "c", null);
        group.process(cmd, cmd.iterator());
        assertNull(cmd.getValue("c"));

        // test option C with too many arguments
        cmd.parseArgument(0, "c", "ARG1,ARG2,ARG3,ARG4");
        group.process(cmd, cmd.iterator());
        assertNull(cmd.getValue("c"));
    }

    @Test
    public void testValidate() throws OptionException {
        final CommandLine cmd = new CommandLine();

        // test option A
        cmd.parseArgument(0, "a", "ARG");
        group.validate(cmd);

        // test option B
        cmd.parseArgument(0, "b", "ARG1:ARG2");
        group.validate(cmd);

        cmd.parseArgument(0, "b", "ARG1:ARG2");
        group.validate(cmd);

        cmd.parseArgument(0, "b", "ARG1:ARG2:ARG3");
        assertFalse(group.validate(cmd));

        // test option C
        cmd.parseArgument(0, "c", "ARG1,ARG2");
        group.validate(cmd);

        cmd.parseArgument(0, "c", "ARG1,ARG2,ARG3");
        group.validate(cmd);

        cmd.parseArgument(0, "c", "ARG1,ARG2,ARG3,ARG4");
        assertFalse(group.validate(cmd));

        cmd.parseArgument(0, "c", "ARG1,ARG2,ARG3,ARG3");
        assertFalse(group.validate(cmd));

        cmd.parseArgument(0, "c", "ARG1,ARG2,ARG3,ARG3,ARG2");
        assertFalse(group.validate(cmd));
    }

    @Test
    public void testGetOptions() {
        final List options = group.getOptions();
        assertEquals(3, options.size());
        assertNotNull(options.get(0));
        assertNotNull(options.get(1));
        assertNotNull(options.get(2));
    }

    @Test
    public void testGetAnonymous() {
        final List anonymous = group.getAnonymous();
        assertEquals(0, anonymous.size());
    }

    @Test
    public void testFindOption() {
        final Option optionB = group.findOption("b");
        assertNotNull(optionB);
        assertEquals("b", optionB.getLongOpt());
    }

    @Test
    public void testFindOptionNull() {
        final Option optionA = group.findOption("a");
        assertNull(optionA);
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, Collections.emptySet(), null);
        assertTrue(sb.toString().startsWith("MyGroup:"));
    }

    @Test
    public void testHelpLines() {
        final List<HelpLine> lines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(5, lines.size());
        assertTrue(lines.get(0).getHead().contains("MyGroup:"));
        assertTrue(lines.get(1).getHead().contains("Option A:"));
        assertTrue(lines.get(2).getHead().contains("Option B:"));
        assertTrue(lines.get(3).getHead().contains("Option B:"));
        assertTrue(lines.get(4).getHead().contains("Option C:"));
    }
}