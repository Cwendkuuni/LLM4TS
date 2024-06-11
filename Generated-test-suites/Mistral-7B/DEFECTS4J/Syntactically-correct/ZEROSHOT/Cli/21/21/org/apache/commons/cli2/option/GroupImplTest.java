package org.apache.commons.cli2.option;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class GroupImplTest {

    private static final String GROUP_NAME = "myGroup";
    private static final String GROUP_DESCRIPTION = "Description of the group";
    private static final int MIN_GROUP_OPTIONS = 2;
    private static final int MAX_GROUP_OPTIONS = 5;

    private static final List<Option> TEST_OPTIONS = new ArrayList<>();
    static {
        TEST_OPTIONS.add(OptionBuilder.withName("option1")
                .withDescription("Option 1")
                .withLongOpt("longOption1")
                .create());
        TEST_OPTIONS.add(OptionBuilder.withName("option2")
                .withDescription("Option 2")
                .withLongOpt("longOption2")
                .create());
        TEST_OPTIONS.add(OptionBuilder.withName("option3")
                .withDescription("Option 3")
                .withLongOpt("longOption3")
                .create());
        TEST_OPTIONS.add(OptionBuilder.withName("arg1")
                .withDescription("Argument 1")
                .isRequired(false)
                .createArgument());
        TEST_OPTIONS.add(OptionBuilder.withName("arg2")
                .withDescription("Argument 2")
                .isRequired(false)
                .createArgument());
    }

    private GroupImpl group;
    private WriteableCommandLine commandLine;

    @Parameterized.Parameters(name = "{index}: createGroup({0})")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {TEST_OPTIONS, GROUP_NAME, GROUP_DESCRIPTION, MIN_GROUP_OPTIONS, MAX_GROUP_OPTIONS, false},
                {TEST_OPTIONS, null, GROUP_DESCRIPTION, MIN_GROUP_OPTIONS, MAX_GROUP_OPTIONS, false},
                {TEST_OPTIONS, GROUP_NAME, null, MIN_GROUP_OPTIONS, MAX_GROUP_OPTIONS, false},
                {TEST_OPTIONS, GROUP_NAME, GROUP_DESCRIPTION, MIN_GROUP_OPTIONS, MAX_GROUP_OPTIONS, true}
        });
    }

    public GroupImplTest(List<Option> options, String name, String description,
                        int minimum, int maximum, boolean required) {
        this.group = new GroupImpl(options, name, description, minimum, maximum, required);
        this.commandLine = new MockWriteableCommandLine();
    }

    @Before
    public void setup() {
        // This test doesn't need any special setup
    }

    @Test
    public void testCanProcess() {
        // Test that the group can process the expected options
        for (Option option : TEST_OPTIONS) {
            assertTrue(group.canProcess(commandLine, option.getName()));
        }

        // Test that the group cannot process unknown options
        String unknownOption = "unknownOption";
        assertFalse(group.canProcess(commandLine, unknownOption));
    }

    @Test
    public void testGetName() {
        assertEquals(GROUP_NAME, group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals(GROUP_DESCRIPTION, group.getDescription());
    }

    @Test
    public void testProcess() {
        // Test that the group processes each option correctly
        group.process(commandLine, TEST_OPTIONS.iterator());

        for (Option option : TEST_OPTIONS) {
            assertTrue(commandLine.hasOption(option));
            option.assertProcessed();
        }

        // Test that the group processes anonymous arguments correctly
        for (Option arg : group.getAnonymous()) {
            arg.setValues(new String[]{"value"});
            arg.process(commandLine, TEST_OPTIONS.iterator());
            assertTrue(commandLine.hasOption(arg));
            arg.assertProcessed();
        }
    }

    @Test(expected = OptionException.class)
    public void testValidate() throws OptionException {
        // Test that the group validates when it has the minimum number of options
        group.validate(commandLine);

        // Test that the group throws an OptionException when it has too few options
        TEST_OPTIONS.remove(0);
        group.validate(commandLine);
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        Map<String, Option> optionMap = group.getOptionMap();
        assertEquals(TEST_OPTIONS.size(), optionMap.size());

        for (Map.Entry<String, Option> entry : optionMap.entrySet()) {
            Option opt = entry.getValue();
            String trigger = opt.getName();
            assertEquals(trigger, entry.getKey());
        }
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();
        assertEquals(TEST_OPTIONS.size(), options.size());

        for (int i = 0; i < options.size(); i++) {
            Option opt1 = options.get(i);
            Option opt2 = TEST_OPTIONS.get(i);
            assertEquals(opt1, opt2);
        }
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymous = group.getAnonymous();
        assertEquals(TEST_OPTIONS.size() / 2, anonymous.size());

        for (int i = 0; i < anonymous.size(); i++) {
            Option arg1 = anonymous.get(i);
            Option arg2 = TEST_OPTIONS.get(TEST_OPTIONS.size() - i - 1);
            assertEquals(arg1, arg2);
        }
    }

    @Test
    public void testFindOption() {
        Option foundOption = group.findOption("option1");
        assertNotNull(foundOption);
        assertEquals(TEST_OPTIONS.get(0), foundOption);

        assertNull(group.findOption("unknownOption"));
    }

    @Test
    public void testGetMinimum() {
        assertEquals(MIN_GROUP_OPTIONS, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(MAX_GROUP_OPTIONS, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testHelpLines() {
        List<String> helpLines = group.helpLines(1, Collections.emptySet(), null);
        assertEquals(1, helpLines.size());

        String helpLine = helpLines.get(0);
        assertTrue(helpLine.contains(GROUP_NAME));
        assertTrue(helpLine.contains("group"));
        assertTrue(helpLine.contains("options"));

        helpLines = group.helpLines(1, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null);
        assertEquals(1, helpLines.size());
        assertTrue(helpLine.contains(GROUP_NAME));

        helpLines = group.helpLines(1, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null);
        assertEquals(1, helpLines.size());
        assertTrue(helpLine.contains("(" + TEST_OPTIONS.size() + ")"));

        helpLines = group.helpLines(1, Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED), null);
        assertEquals(TEST_OPTIONS.size() + 1, helpLines.size());

        for (int i = 0; i < TEST_OPTIONS.size(); i++) {
            Option option = TEST_OPTIONS.get(i);
            String helpLine = group.helpLines(1, Collections.emptySet(), null).get(i + 1);
            assertTrue(helpLine.contains(option.getPreferredName()));
            assertTrue(helpLine.contains(option.getDescription()));
        }

        helpLines = group.helpLines(1, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null);
        for (int i = 0; i < TEST_OPTIONS.size(); i++) {
            Option option = TEST_OPTIONS.get(i);
            String helpLine = group.helpLines(1, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null).get(i + 1);
            assertTrue(helpLine.contains(option.getPreferredName()));
            assertTrue(helpLine.contains(option.getDescription()));
            assertTrue(helpLine.contains("(required)"));
        }

        helpLines = group.helpLines(1, Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED), null);
        for (int i = 0; i < TEST_OPTIONS.size(); i++) {
            Option option = TEST_OPTIONS.get(i);
            String helpLine = group.helpLines(1, Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED), null).get(i + 1);
            assertTrue(helpLine.contains(option.getPreferredName()));
            assertTrue(helpLine.contains(option.getDescription()));
            assertTrue(helpLine.contains("(required)"));
        }

        helpLines = group.helpLines(1, Collections.singleton(DisplaySetting.DISPLAY_GROUP_ARGUMENT), null);
        for (int i = TEST_OPTIONS.size() - 1; i >= 0; i--) {
            Option option = TEST_OPTIONS.get(i);
            if (option instanceof Argument) {
                String helpLine = group.helpLines(1, Collections.singleton(DisplaySetting.DISPLAY_GROUP_ARGUMENT), null).get(i + 1);
                assertTrue(helpLine.contains("{0}"));
                assertTrue(helpLine.contains(option.getDescription()));
            }
        }
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        String separator = " | ";

        group.appendUsage(buffer, Collections.emptySet(), null);
        String usage = buffer.toString();

        assertTrue(usage.contains(GROUP_NAME));
        assertTrue(usage.contains("group"));
        assertTrue(usage.contains("options"));

        buffer.setLength(0);
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null);
        usage = buffer.toString();

        assertTrue(usage.contains(GROUP_NAME));
        assertTrue(usage.contains("group"));
        assertTrue(usage.contains("(" + TEST_OPTIONS.size() + ")"));

        buffer.setLength(0);
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null, separator);
        usage = buffer.toString();

        assertTrue(usage.contains(GROUP_NAME));
        assertTrue(usage.contains("group"));
        assertTrue(usage.contains("(" + TEST_OPTIONS.size() + ")"));
        assertTrue(usage.contains(separator));

        buffer.setLength(0);
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED), null);
        usage = buffer.toString();

        for (int i = 0; i < TEST_OPTIONS.size(); i++) {
            Option option = TEST_OPTIONS.get(i);
            assertTrue(usage.contains(option.getPreferredName()));
            assertTrue(usage.contains(option.getDescription()));
        }

        buffer.setLength(0);
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null, separator);
        usage = buffer.toString();

        for (int i = 0; i < TEST_OPTIONS.size(); i++) {
            Option option = TEST_OPTIONS.get(i);
            assertTrue(usage.contains(option.getPreferredName()));
            assertTrue(usage.contains(option.getDescription()));
            assertTrue(usage.contains("(required)"));
            assertTrue(usage.contains(separator));
        }

        buffer.setLength(0);
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED), null, separator);
        usage = buffer.toString();

        for (int i = 0; i < TEST_OPTIONS.size(); i++) {
            Option option = TEST_OPTIONS.get(i);
            assertTrue(usage.contains(option.getPreferredName()));
            assertTrue(usage.contains(option.getDescription()));
            assertTrue(usage.contains("(required)"));
            assertTrue(usage.contains(separator));
        }

        buffer.setLength(0);
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_ARGUMENT), null);
        usage = buffer.toString();

        for (int i = TEST_OPTIONS.size() - 1; i >= 0; i--) {
            Option option = TEST_OPTIONS.get(i);
            if (option instanceof Argument) {
                assertTrue(usage.contains("{0}"));
                assertTrue(usage.contains(option.getDescription()));
            }
        }
    }
}