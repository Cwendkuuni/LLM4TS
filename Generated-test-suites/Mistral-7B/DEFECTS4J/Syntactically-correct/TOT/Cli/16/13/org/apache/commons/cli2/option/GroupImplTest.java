package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class GroupImplTest {

    private static final String GROUP_NAME = "myGroup";
    private static final String GROUP_DESCRIPTION = "This is my group";
    private static final int GROUP_MINIMUM = 1;
    private static final int GROUP_MAXIMUM = 3;

    private GroupImpl group;

    @Before
    public void setUp() {
        List options = new ArrayList();

        // Option 1
        Option option1 = new OptionBuilder(GROUP_NAME + ".option1")
                .withDescription("Option 1 description")
                .withLongOpt("long-opt-1")
                .isRequired()
                .create();
        options.add(option1);

        // Option 2
        Option option2 = new OptionBuilder(GROUP_NAME + ".option2")
                .withDescription("Option 2 description")
                .withLongOpt("long-opt-2")
                .create();
        options.add(option2);

        // Option 3
        Option option3 = new OptionBuilder(GROUP_NAME + ".option3")
                .withDescription("Option 3 description")
                .withLongOpt("long-opt-3")
                .isRequired()
                .create();
        options.add(option3);

        group = new GroupImpl(options, GROUP_NAME, GROUP_DESCRIPTION, GROUP_MINIMUM, GROUP_MAXIMUM);
    }

    @Test
    public void testCanProcessSuccess() {
        CommandLine cmd = TestUtils.createMockCommandLine();
        cmd.parse(group.parse(null));

        String[] args = {"long-opt-1", "long-opt-2", "value-for-option-2"};
        cmd.parseArguments(args);

        assertTrue("Option 1 processed", group.canProcess(cmd, "long-opt-1"));
        assertTrue("Option 2 processed", group.canProcess(cmd, "long-opt-2"));
    }

    @Test
    public void testCanProcessFail() {
        CommandLine cmd = TestUtils.createMockCommandLine();
        cmd.parse(group.parse(null));

        String[] args = {"long-opt-1", "long-opt-4"};
        cmd.parseArguments(args);

        assertFalse("Option 4 not processed", group.canProcess(cmd, "long-opt-4"));
    }

    @Test(expected = ParseException.class)
    public void testValidateSuccess() {
        CommandLine cmd = TestUtils.createMockCommandLine();
        cmd.parse(group.parse(null));

        // Valid command line
        String[] args = {"long-opt-1", "long-opt-2", "value-for-option-2"};
        cmd.parseArguments(args);
        group.validate(cmd);
    }

    @Test(expected = ParseException.class)
    public void testValidateFailureMinimum() {
        CommandLine cmd = TestUtils.createMockCommandLine();
        cmd.parse(group.parse(null));

        // Invalid command line with less than minimum options
        String[] args = {"long-opt-1"};
        cmd.parseArguments(args);
        group.validate(cmd);
    }

    @Test(expected = ParseException.class)
    public void testValidateFailureMaximum() {
        CommandLine cmd = TestUtils.createMockCommandLine();
        cmd.parse(group.parse(null));

        // Invalid command line with more than maximum options
        String[] args = {"long-opt-1", "long-opt-1", "long-opt-2"};
        cmd.parseArguments(args);
        group.validate(cmd);
    }

    @Test
    public void testProcessSuccess() {
        CommandLine cmd = TestUtils.createMockCommandLine();
        cmd.parse(group.parse(null));

        String[] args = {"long-opt-1", "long-opt-2"};
        cmd.parseArguments(args);

        List<String> values = new ArrayList<>();
        group.process(cmd, TestUtils.getListIterator(args));

        // Option 1 and Option 2 should be processed
        assertEquals("Option 1 value", "", ((Option) group.findOption(GROUP_NAME + ".option1")).getValue(cmd));
        assertEquals("Option 2 value", "value-for-option-2", ((Option) group.findOption(GROUP_NAME + ".option2")).getValue(cmd));
    }

    @Test
    public void testProcessFail() {
        CommandLine cmd = TestUtils.createMockCommandLine();
        cmd.parse(group.parse(null));

        String[] args = {"long-opt-1", "long-opt-4"};
        cmd.parseArguments(args);

        List<String> values = new ArrayList<>();
        try {
            group.process(cmd, TestUtils.getListIterator(args));
            fail("Test should have thrown ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testHelp() {
        HelpFormatter formatter = new HelpFormatter();

        HelpLine helpLine = group.helpLines(0, null, null).get(0);
        formatter.printHelp(System.out, helpLine.getLongOpt(), helpLine.getOptions(), helpLine.getUsageTemplate(), GROUP_DESCRIPTION, true);
    }
}