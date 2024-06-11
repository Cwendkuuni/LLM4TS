package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.ParseException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class GroupImplTest {

    private OptionGroup group;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("optA")
                .withDescription("Option A")
                .create());
        options.add(OptionBuilder.withLongOpt("optB")
                .withDescription("Option B")
                .create());
        options.add(OptionBuilder.withLongOpt("optC")
                .withDescription("Option C")
                .create());

        group = new OptionGroup("Group", "Test group", options, 1, 3);
    }

    @Test
    public void testCanProcessValidArguments() throws ParseException {
        CommandLine cmd = new CommandLine();
        cmd.parse(group.parseArguments(Arrays.asList("--optA", "--optB", "--optC")));
        assertTrue(group.canProcess(cmd, "--optC"));
    }

    @Test
    public void testCanProcessLongOptionWithBursting() throws ParseException {
        CommandLine cmd = new CommandLine();
        cmd.parse(group.parseArguments(Arrays.asList("--optC", "--optB", "--optA")));
        assertTrue(group.canProcess(cmd, "--optA"));
    }

    @Test
    public void testCanProcessShortOptionWithBursting() throws ParseException {
        CommandLine cmd = new CommandLine();
        cmd.parse(group.parseArguments(Arrays.asList("-c", "-b", "-a")));
        assertTrue(group.canProcess(cmd, "-a"));
    }

    @Test(expected = ParseException.class)
    public void testCanProcessInvalidArgument() throws ParseException {
        CommandLine cmd = new CommandLine();
        cmd.parse(group.parseArguments(Arrays.asList("--invalid")));
        assertFalse(group.canProcess(cmd, "--invalid"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanProcessNullCommandLine() {
        assertFalse(group.canProcess(null, "--optA"));
    }

    @Test
    public void testProcessValidArguments() throws ParseException {
        CommandLine cmd = new CommandLine();
        cmd.parse(group.parseArguments(Arrays.asList("--optA", "--optB", "--optC")));
        group.process(cmd, cmd.parser().getPositiveParsingIterator());

        assertEquals(3, cmd.getValues("optA").size());
        assertEquals(3, cmd.getValues("optB").size());
        assertEquals(3, cmd.getValues("optC").size());
    }

    @Test(expected = ParseException.class)
    public void testProcessInvalidArgument() throws ParseException {
        CommandLine cmd = new CommandLine();
        cmd.parse(group.parseArguments(Arrays.asList("--optA", "--optB", "--optX")));

        try {
            group.process(cmd, cmd.parser().getPositiveParsingIterator());
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        assertFalse(cmd.hasOption("optX"));
    }

    @Test
    public void testValidateValidArguments() {
        CommandLine cmd = new CommandLine();
        cmd.parse(group.parseArguments(Arrays.asList("--optA", "--optB", "--optC")));
        group.validate(cmd);
    }

    @Test(expected = ParseException.class)
    public void testValidateInvalidArguments() throws ParseException {
        CommandLine cmd = new CommandLine();
        cmd.parse(group.parseArguments(Arrays.asList("--optA", "--optB")));

        try {
            group.validate(cmd);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        assertFalse(cmd.hasOption("optC"));
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, null, null);
        String expected = "Group:Test group\n" +
                "  Group: (required)\n" +
                "    This is a test group\n" +
                "  optA:Option A (required)\n" +
                "    Option A\n" +
                "  optB:Option B\n" +
                "    Option B\n" +
                "  optC:Option C\n" +
                "    Option C\n";

        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<String> lines = group.helpLines(1, null, null);
        String expected = "Test group\n" +
                "  Group: test-group (required)\n" +
                "  optA:Option A (required)\n" +
                "    Option A\n" +
                "  optB:Option B\n" +
                "    Option B\n" +
                "  optC:Option C\n" +
                "    Option C\n";

        for (String line : lines) {
            assertEquals(expected, line);
        }
    }
}