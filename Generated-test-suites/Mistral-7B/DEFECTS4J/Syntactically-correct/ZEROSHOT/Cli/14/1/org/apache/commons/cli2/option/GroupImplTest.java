package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.test.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GroupImplTest {

    private static final String TEST_GROUP_NAME = "testGroup";
    private static final String TEST_GROUP_DESCRIPTION = "testGroup Description";
    private static final int TEST_MINIMUM = 1;
    private static final int TEST_MAXIMUM = 3;

    private WriteableCommandLine commandLine;
    private GroupImpl group;

    @Before
    public void setUp() {
        commandLine = TestUtils.createCommandLine();
        final List<Option> options = new ArrayList<>();

        options.add(createTestOption("testOption1", "testOption1 Description"));
        options.add(createTestOption("testOption2", "testOption2 Description"));
        options.add(createTestArgument("testArgument1", "testArgument1 Description"));
        options.add(createTestArgument("testArgument2", "testArgument2 Description"));

        group = new GroupImpl(options, TEST_GROUP_NAME, TEST_GROUP_DESCRIPTION, TEST_MINIMUM, TEST_MAXIMUM);
    }

    @Test
    public void canProcess() {
        assertTrue(group.canProcess(commandLine, "testOption1"));
        assertTrue(group.canProcess(commandLine, "testOption2"));
        assertFalse(group.canProcess(commandLine, "testArgument1"));
        assertFalse(group.canProcess(commandLine, "testArgument2"));

        final List<String> givenArguments = List.of("testOption1", "testOption2");
        assertTrue(group.canProcess(commandLine, givenArguments));

        final List<String> invalidArguments = List.of("invalidOption", "testOption1");
        assertFalse(group.canProcess(commandLine, invalidArguments));
    }

    @Test
    public void getAnonymous() {
        final List<Option> groupOptions = group.getOptions();
        final List<Option> anonymousOptions = group.getAnonymous();

        assertEquals(2, groupOptions.size());
        assertEquals(2, anonymousOptions.size());

        assertTrue(groupOptions.get(0) instanceof Option);
        assertTrue(groupOptions.get(1) instanceof Option);
        assertTrue(anonymousOptions.get(0) instanceof Argument);
        assertTrue(anonymousOptions.get(1) instanceof Argument);
    }

    @Test
    public void getMinimum() {
        assertEquals(TEST_MINIMUM, group.getMinimum());
    }

    @Test
    public void getMaximum() {
        assertEquals(TEST_MAXIMUM, group.getMaximum());
    }

    @Test
    public void isRequired() {
        assertTrue(group.isRequired());
    }

    @Test
    public void validate() throws OptionException {
        final List<String> givenArguments = List.of("testOption1", "testOption2");
        group.validate(commandLine);

        final List<String> missingArgument = List.of("testOption1");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }

        final List<String> tooManyArguments = List.of("testOption1", "testOption2", "testArgument1");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }

        final List<String> missingArgumentAndTooManyArguments = List.of("testArgument1");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void appendUsage() {
        final StringBuilder expectedUsage = new StringBuilder();
        expectedUsage.append("testGroup (");
        expectedUsage.append(TEST_GROUP_DESCRIPTION);
        expectedUsage.append("\n");
        expectedUsage.append("[testOption1] (testOption1 Description)\n");
        expectedUsage.append("[testOption2] (testOption2 Description)\n");
        expectedUsage.append(")\n");

        final StringBuilder actualUsage = new StringBuilder();
        group.appendUsage(actualUsage, Collections.emptySet(), null);

        assertEquals(expectedUsage.toString(), actualUsage.toString());
    }

    @Test
    public void helpLines() {
        final List<HelpLine> expectedHelpLines = new ArrayList<>();

        final HelpLine testGroupHelpLine = new HelpLineImpl(group, 0);
        expectedHelpLines.add(testGroupHelpLine);

        final HelpLine testOption1HelpLine = new HelpLineImpl(group.findOption("testOption1"), 1);
        expectedHelpLines.add(testOption1HelpLine);

        final HelpLine testOption2HelpLine = new HelpLineImpl(group.findOption("testOption2"), 1);
        expectedHelpLines.add(testOption2HelpLine);

        final HelpLine testArgument1HelpLine = new HelpLineImpl(group.findOption("testArgument1"), 1);
        expectedHelpLines.add(testArgument1HelpLine);

        final HelpLine testArgument2HelpLine = new HelpLineImpl(group.findOption("testArgument2"), 1);
        expectedHelpLines.add(testArgument2HelpLine);

        final List<HelpLine> actualHelpLines = group.helpLines(0, Collections.emptySet(), null);

        assertEquals(expectedHelpLines.size(), actualHelpLines.size());
        for (int i = 0; i < expectedHelpLines.size(); i++) {
            assertEquals(expectedHelpLines.get(i).toString(), actualHelpLines.get(i).toString());
        }
    }

    private Option createTestOption(final String longName, final String description) {
        return new OptionBuilder()
                .withLongOpt(longName)
                .withDescription(description)
                .create();
    }

    private Argument createTestArgument(final String longName, final String description) {
        return new ArgumentBuilder()
                .withLongOpt(longName)
                .withDescription(description)
                .isReceiver()
                .create();
    }
}